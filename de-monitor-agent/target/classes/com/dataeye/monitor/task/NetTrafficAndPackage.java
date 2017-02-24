package com.dataeye.monitor.task;

import com.dataeye.monitor.common.annocations.OneMinuteTask;
import com.dataeye.monitor.common.constants.Commands;
import com.dataeye.monitor.common.constants.Constant;
import com.dataeye.monitor.common.constants.FID;
import com.dataeye.monitor.common.constants.ReportUrl;
import com.dataeye.monitor.utils.HttpClientUtil;
import com.dataeye.monitor.utils.ProcessUtil;
import com.dataeye.monitor.utils.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网卡出入流量和出入包
 * Created by wendy on 2016/7/5.
 */
@OneMinuteTask
public class NetTrafficAndPackage extends BaseTask{
    private static final int byteIn = 0;
    private static final int packIn = 1;
    private static final int byteOut = 8;
    private static final int packOut = 9;

    private String reportData;
    private Map<String, TrafficPackage> trafficPackageData;

    @Override
    public void run() {
        try {
            collectData();
            generateData();
            report();
        } catch (Throwable e) {
            ExceptionHandler.error("NetTrafficAndPackage error", e);
        }
    }

    private void report() throws ServerException {
        if (StringUtils.isNotEmpty(reportData)) {
            HttpClientUtil.post(ReportUrl.serverFeaturUrl, reportData);
            //batchReportHandler.put(ReportUrl.serverFeaturUrl, reportData);
        }
    }

    private void generateData() {
        Feature parent = new Feature();
        parent.setFeature_list(new ArrayList<Feature>());
        for (Map.Entry<String, TrafficPackage> entry : trafficPackageData.entrySet()) {
            TrafficPackage tp = entry.getValue();
            Feature byteInFeature = new Feature(FID.net_byte_in.getValue(),
                    tp.getName(), new BigDecimal(tp.getByteIn()));
            parent.addFeature(byteInFeature);

            Feature byteOutFeature = new Feature(FID.net_byte_out.getValue(),
                    tp.getName(), new BigDecimal(tp.getByteOut()));
            parent.addFeature(byteOutFeature);

            Feature packInFeature = new Feature(FID.net_pack_in.getValue(),
                    tp.getName(), new BigDecimal(tp.getPackIn()));
            parent.addFeature(packInFeature);

            Feature packOutFeature = new Feature(FID.net_pack_out.getValue(),
                    tp.getName(), new BigDecimal(tp.getPackOut()));
            parent.addFeature(packOutFeature);
        }
        reportData = Constant.GSON.toJson(parent);
    }


    public String collectTcpTimeWait() {
        String ret = ProcessUtil.execute(Commands.TIME_WAIT);
        return ret.trim();
    }

    public String collectPingData() {
        String ret = ProcessUtil.execute(Commands.PING);
        return ret.trim();
    }

    public void collectData() throws IOException {
        trafficPackageData = new ConcurrentHashMap<>();
        reportData = null;
        firstCollect();
        waitOneMinute();
        secondCollect();
        calculteAndResetData();
    }

    private void waitOneMinute() {
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void calculteAndResetData() {
        for (Map.Entry<String, TrafficPackage>
                entry : trafficPackageData.entrySet()) {
            TrafficPackage tp = entry.getValue();
            long byteIn = (tp.getDeltaByteIn() - tp.getByteIn()) * 8 / 60;
            long byteOut = (tp.getDeltaByteOut() - tp.getByteOut()) * 8 / 60;
            long packIn = (tp.getDeltaPackIn() - tp.getPackIn()) / 60;
            long packOut = (tp.getDeltaPackout() - tp.getPackOut()) / 60;
            tp.setByteIn(byteIn);
            tp.setByteOut(byteOut);
            tp.setPackIn(packIn);
            tp.setPackOut(packOut);
        }
    }

    /**
     * [deuser@mysql7 ~]$ cat /proc/net/dev
     * Inter-|   Receive                                                |  Transmit
     * face |bytes    packets errs drop fifo frame compressed multicast|bytes    packets errs drop fifo colls carrier compressed
     * lo:1605148937801 6287846480    0    0    0     0          0         0 1605148937801 6287846480    0    0    0     0       0          0
     * em1:60601773977 594675670    0    0    0   432          0 539754689 5146491255 9731641    0    0    0     0       0          0
     * em2:1235492453962 6932959402    0  544    0 162532          0 539782629 1492317755543 5588645031    0   57    0     0       0          0
     * em3:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
     * em4:       0       0    0    0    0     0          0         0        0       0    0    0    0     0       0          0
     */
    private void firstCollect() throws IOException {

        String result = ProcessUtil.execute(Commands.NET_DEV);
        TrafficPackage netWork;
        for (String line : result.split(Constant.LINE)) {
            line = StringUtils.replaceMultiBlankSpace(line);
            if (isAvaliableNetWork(line)) {
                String netWorkName = line.substring(0, line.indexOf(Constant.COLON));
                if (StringUtils.isNotEmpty(netWorkName)) {
                    netWorkName = netWorkName.trim();
                }

                line = line.substring(line.indexOf(Constant.COLON) + 1).trim();
                String[] netRcvTrans = line.split(Constant.BLANKSPACE);

                netWork = new TrafficPackage();
                netWork.setName(netWorkName);
                netWork.setByteIn(Long.parseLong(netRcvTrans[byteIn]));
                netWork.setByteOut(Long.parseLong(netRcvTrans[byteOut]));
                netWork.setPackIn(Long.parseLong(netRcvTrans[packIn]));
                netWork.setPackOut(Long.parseLong(netRcvTrans[packOut]));
                trafficPackageData.put(netWork.getName(), netWork);
            }
        }
    }


    private void secondCollect() throws IOException {
        String result = ProcessUtil.execute(Commands.NET_DEV);
        TrafficPackage netWork = null;
        for (String line : result.split(Constant.LINE)) {
            line = StringUtils.replaceMultiBlankSpace(line);
            if (isAvaliableNetWork(line)) {
                String netWorkName = line.substring(0, line.indexOf(Constant.COLON));
                if (StringUtils.isNotEmpty(netWorkName)) {
                    netWorkName = netWorkName.trim();
                }

                line = line.substring(line.indexOf(Constant.COLON) + 1).trim();
                String[] netRcvTrans = line.split(Constant.BLANKSPACE);

                netWork = trafficPackageData.get(netWorkName);
                long netByteIn = Long.parseLong(netRcvTrans[byteIn]);
                netWork.setDeltaByteIn(netByteIn);

                long netByteOut = Long.parseLong(netRcvTrans[byteOut]);
                netWork.setDeltaByteOut(netByteOut);

                long netPackIn = Long.parseLong(netRcvTrans[packIn]);
                netWork.setDeltaPackIn(netPackIn);

                long netPackOut = Long.parseLong(netRcvTrans[packOut]);
                netWork.setDeltaPackout(netPackOut);

                logger.debug(netWork.toString());
            }
        }


    }

    private static Set<String> networkNames = null;
    private boolean isAvaliableNetWork(String line) {
        if (null == networkNames) {
            networkNames = new HashSet<>();
            String result = ProcessUtil.execute(Commands.NETWORK_NAME);
            for (String s : result.split(Constant.LINE)) {
                networkNames.add(s);
            }
        }

        for (String networkName : networkNames) {
            if(line.contains(networkName)) return true;
        }
        return false;
    }




    class TrafficPackage {
        private String name;

        private Long ByteIn;

        private Long ByteOut;

        private Long PackIn;

        private Long PackOut;

        private Long deltaByteIn;

        private Long deltaByteOut;

        private Long deltaPackIn;

        private Long deltaPackout;

        @Override
        public String toString() {
            return "TrafficPackage{" +
                    "name='" + name + '\'' +
                    ", ByteIn=" + ByteIn +
                    ", ByteOut=" + ByteOut +
                    ", PackIn=" + PackIn +
                    ", PackOut=" + PackOut +
                    ", deltaByteIn=" + deltaByteIn +
                    ", deltaByteOut=" + deltaByteOut +
                    ", deltaPackIn=" + deltaPackIn +
                    ", deltaPackout=" + deltaPackout +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getByteIn() {
            return ByteIn;
        }

        public void setByteIn(Long byteIn) {
            ByteIn = byteIn;
        }

        public Long getByteOut() {
            return ByteOut;
        }

        public void setByteOut(Long byteOut) {
            ByteOut = byteOut;
        }

        public Long getPackIn() {
            return PackIn;
        }

        public void setPackIn(Long packIn) {
            PackIn = packIn;
        }

        public Long getPackOut() {
            return PackOut;
        }

        public void setPackOut(Long packOut) {
            PackOut = packOut;
        }

        public Long getDeltaByteIn() {
            return deltaByteIn;
        }

        public void setDeltaByteIn(Long deltaByteIn) {
            this.deltaByteIn = deltaByteIn;
        }

        public Long getDeltaByteOut() {
            return deltaByteOut;
        }

        public void setDeltaByteOut(Long deltaByteOut) {
            this.deltaByteOut = deltaByteOut;
        }

        public Long getDeltaPackIn() {
            return deltaPackIn;
        }

        public void setDeltaPackIn(Long deltaPackIn) {
            this.deltaPackIn = deltaPackIn;
        }

        public Long getDeltaPackout() {
            return deltaPackout;
        }

        public void setDeltaPackout(Long deltaPackout) {
            this.deltaPackout = deltaPackout;
        }
    }
}
