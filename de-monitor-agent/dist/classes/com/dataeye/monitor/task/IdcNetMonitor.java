package com.dataeye.monitor.task;

import com.dataeye.monitor.common.annocations.OneMinuteTask;
import com.dataeye.monitor.common.constants.Commands;
import com.dataeye.monitor.common.constants.Constant;
import com.dataeye.monitor.common.constants.FID;
import com.dataeye.monitor.common.constants.ReportUrl;
import com.dataeye.monitor.utils.HttpClientUtil;
import com.dataeye.monitor.utils.ProcessUtil;
import com.dataeye.monitor.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author wendy
 * @since 2016/9/13
 */
@OneMinuteTask
public class IdcNetMonitor extends BaseTask {

    //电信交换机
    private static final String ethernetA = "192.168.1.252";
    //联通交换机
    private static final String ethernetB = "192.168.1.253";

    private String reportData;
    private IdcNet idcNetA;
    private IdcNet idcNetB;

    @Override
    public void run() {
        if (Constant.IP.equals(ReportUrl.idcNetCollectDataHost)) {
            collectData();
            generateData();
            report();
        }
    }

    public void collectData() {
        firstCollect();
        waitOneMinute();
        secondCollect();
        calculateData();
    }


    private void generateData() {
        Feature parent = new Feature();
        parent.setFeature_list(new ArrayList<Feature>());

        Feature feature = new Feature(FID.idcNetIn.getValue(),
                idcNetA.getName(), new BigDecimal(idcNetA.getNetIn()));
        parent.addFeature(feature);

        feature = new Feature(FID.idcNetOut.getValue(), idcNetA.getName(),
                new BigDecimal(idcNetA.getNetOut()));
        parent.addFeature(feature);

        feature = new Feature(FID.idcNetIn.getValue(), idcNetB.getName(),
                new BigDecimal(idcNetB.getNetIn()));
        parent.addFeature(feature);

        feature = new Feature(FID.idcNetOut.getValue(), idcNetB.getName(),
                new BigDecimal(idcNetB.getNetOut()));
        parent.addFeature(feature);

        if (idcNetA.getNetIn() < 0 || idcNetA.getNetOut() < 0
                || idcNetB.getNetIn() < 0 || idcNetB.getNetOut() < 0) {
            reportData = null;
            return;
        }
        reportData = Constant.GSON.toJson(parent);
    }

    private void report() {
        if (StringUtils.isNotEmpty(reportData)) {
           // batchReportHandler.put(ReportUrl.serverFeaturUrl, reportData);
            HttpClientUtil.post(ReportUrl.serverFeaturUrl, reportData);
            HttpClientUtil.post(ReportUrl.testUrl, reportData);
        }
    }

    private void calculateData() {
        long netInA = (idcNetA.getDeltaNetIn() - idcNetA.getNetIn()) / 60 * 8;
        long netOutA = (idcNetA.getDeltaNetOut() - idcNetA.getNetOut()) / 60 * 8;

        idcNetA.setNetIn(netInA);
        idcNetA.setNetOut(netOutA);
        long netInB = (idcNetB.getDeltaNetIn() - idcNetB.getNetIn()) / 60 * 8;
        long netOutB = (idcNetB.getDeltaNetOut() - idcNetB.getNetOut()) / 60 * 8;
        idcNetB.setNetIn(netInB);
        idcNetB.setNetOut(netOutB);

    }

    private void firstCollect() {
        idcNetA = new IdcNet();
        idcNetB = new IdcNet();
        String line = ProcessUtil.execute(Commands.idcNetIn.replace("{}", ethernetA));
        idcNetA.setName("ethernetA");
        idcNetA.setNetIn(Long.parseLong(line.trim()));
        line = ProcessUtil.execute(Commands.idcNetOut.replace("{}", ethernetA));
        idcNetA.setNetOut(Long.parseLong(line.trim()));

        line = ProcessUtil.execute(Commands.idcNetIn.replace("{}", ethernetB));
        idcNetB.setName("ethernetB");
        idcNetB.setNetIn(Long.parseLong(line.trim()));
        line = ProcessUtil.execute(Commands.idcNetOut.replace("{}", ethernetB));
        idcNetB.setNetOut(Long.parseLong(line.trim()));
    }

    private void secondCollect() {
        String line = ProcessUtil.execute(Commands.idcNetIn.replace("{}", ethernetA));
        idcNetA.setDeltaNetIn(Long.parseLong(line.trim()));
        line = ProcessUtil.execute(Commands.idcNetOut.replace("{}", ethernetA));
        idcNetA.setDeltaNetOut(Long.parseLong(line.trim()));

        line = ProcessUtil.execute(Commands.idcNetIn.replace("{}", ethernetB));
        idcNetB.setDeltaNetIn(Long.parseLong(line.trim()));
        line = ProcessUtil.execute(Commands.idcNetOut.replace("{}", ethernetB));
        idcNetB.setDeltaNetOut(Long.parseLong(line.trim()));
    }

    private void waitOneMinute() {
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class IdcNet {
        private String name;
        private long netIn;
        private long netOut;

        private long deltaNetIn;
        private long deltaNetOut;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getNetIn() {
            return netIn;
        }

        public void setNetIn(long netIn) {
            this.netIn = netIn;
        }

        public long getNetOut() {
            return netOut;
        }

        public void setNetOut(long netOut) {
            this.netOut = netOut;
        }

        public long getDeltaNetIn() {
            return deltaNetIn;
        }

        public void setDeltaNetIn(long deltaNetIn) {
            this.deltaNetIn = deltaNetIn;
        }

        public long getDeltaNetOut() {
            return deltaNetOut;
        }

        public void setDeltaNetOut(long deltaNetOut) {
            this.deltaNetOut = deltaNetOut;
        }
    }

}
