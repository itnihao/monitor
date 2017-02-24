package com.dataeye.monitor.task;

import com.dataeye.monitor.common.annocations.FourMinuteTask;
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

/**
 * 统计每个网卡出入流量，出入包量，
 * 统计tcp连接数,被动打开tcp链接数
 * 统计udp收发数据报
 * 统计 tcp TIME_WAIT 连接数
 * Created by wendy on 2016/6/29.
 */
@FourMinuteTask
public class TcpUdpUsage extends BaseTask {
    private static final int udpIn = 1;
    private static final int udpOut = 4;
    private static final int tcpConn = 9;
    private static final int tcpPassiveOpens = 6;

    private String reportData;

    private TcpUdp tcpUdp = new TcpUdp();

    @Override
    public void run() {
        try {
            collectData();
            generateData();
            report();
        } catch (Throwable e) {
            ExceptionHandler.error("TcpUdpUsage error", e);
        }
    }

    private void collectData() throws IOException {
        reportData = null;
        firstCollect();
        wailFourMinute();
        secondCollect();
        calculateAndResetData();
    }

    private void wailFourMinute() {
        try {
            Thread.sleep(240 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void calculateAndResetData() {
        long tcpPassiveOpens = (tcpUdp.getDeltaTcpPassiveOpens()
                - tcpUdp.getTcpPassiveOpens()) / 240;

        long udpIn = (tcpUdp.getDeltaUdpIn() - tcpUdp.getUdpIn()) / 240;
        long udpOut = (tcpUdp.getDeltaUdpOut() - tcpUdp.getUdpOut()) / 240;

        tcpUdp.setTcpPassiveOpens(tcpPassiveOpens);
        tcpUdp.setUdpIn(udpIn);
        tcpUdp.setUdpOut(udpOut);
    }

    private void secondCollect() throws IOException {
        String result = ProcessUtil.execute(Commands.NET_SNMP);
        for (String line : result.split(Constant.LINE)) {
            line = StringUtils.replaceMultiBlankSpace(line);
            if (isTcpData(line)) setTcpDeltaData(line, tcpUdp);
            if (isUdpData(line)) setUdpDeltaData(line, tcpUdp);
        }
    }

    private void setUdpDeltaData(String line, TcpUdp tcpUdp) {
        String[] udpData = line.split(Constant.BLANKSPACE);
        tcpUdp.setDeltaUdpIn(Long.parseLong(udpData[udpIn]));
        tcpUdp.setDeltaUdpOut(Long.parseLong(udpData[udpOut]));
    }

    private void setTcpDeltaData(String line, TcpUdp tcpUdp) {
        String[] tcpData = line.split(Constant.BLANKSPACE);
        tcpUdp.setDeltaTcpConn(Long.parseLong(tcpData[tcpConn]));
        tcpUdp.setDeltaTcpPassiveOpens(Long.parseLong(tcpData[tcpPassiveOpens]));

    }

    private void report() throws ServerException {
        if (StringUtils.isNotEmpty(reportData)) {
            HttpClientUtil.post(ReportUrl.serverFeaturUrl, reportData);
            //batchReportHandler.put(ReportUrl.serverFeaturUrl, reportData);
        }
    }

    private void generateData() {
        Feature feature = new Feature();
        feature.setFeature_list(new ArrayList<Feature>());
        Feature tcpConnFeature = new Feature(FID.tcp_conn.getValue(),
                FID.tcp_conn.getName(), new BigDecimal(tcpUdp.getTcpConn()));
        feature.addFeature(tcpConnFeature);

        Feature tcpPassiveFeature = new Feature(FID.tcppassiveopens.getValue(),
                FID.tcppassiveopens.getName(), new BigDecimal(tcpUdp.getTcpPassiveOpens()));
        feature.addFeature(tcpPassiveFeature);

        Feature udpIn = new Feature(FID.udp_in.getValue(), FID.udp_in.getName()
                , new BigDecimal(tcpUdp.getUdpIn()));
        feature.addFeature(udpIn);

        Feature udpOut = new Feature(FID.udp_out.getValue(), FID.udp_out.getName()
                , new BigDecimal(tcpUdp.getUdpOut()));
        feature.addFeature(udpOut);

        reportData = Constant.GSON.toJson(feature);
    }

    /**
     * [deuser@mysql7 ~]$ cat /proc/net/snmp
     * Ip: Forwarding DefaultTTL InReceives InHdrErrors InAddrErrors ForwDatagrams InUnknownProtos InDiscards InDelivers OutRequests OutDiscards OutNoRoutes ReasmTimeout ReasmReqds ReasmOKs ReasmFails FragOKs FragFails FragCreates
     * Ip: 2 64 35252305787 0 1044 0 0 0 33350004694 29773169187 1697 125 1 1 0 1 0 0 0
     * Icmp: InMsgs InErrors InDestUnreachs InTimeExcds InParmProbs InSrcQuenchs InRedirects InEchos InEchoReps InTimestamps InTimestampReps InAddrMasks InAddrMaskReps OutMsgs OutErrors OutDestUnreachs OutTimeExcds OutParmProbs OutSrcQuenchs OutRedirects OutEchos OutEchoReps OutTimestamps OutTimestampReps OutAddrMasks OutAddrMaskReps
     * Icmp: 805182 12721 22114 861 0 3 16 363002 419156 16 0 0 0 977928 0 193584 0 0 0 0 421333 362995 0 16 0 0
     * IcmpMsg: InType0 InType3 InType4 InType5 InType8 InType11 InType13 OutType0 OutType3 OutType8 OutType14
     * IcmpMsg: 419156 22114 3 16 363002 861 16 362995 193584 421333 16
     * Tcp: RtoAlgorithm RtoMin RtoMax MaxConn ActiveOpens PassiveOpens AttemptFails EstabResets CurrEstab InSegs OutSegs RetransSegs InErrs OutRsts
     * Tcp: 1 200 120000 -1 117618204 206665581 10909024 1544451 357 33349084592 29768447453 3677081 5331 27073529
     * Udp: InDatagrams NoPorts InErrors OutDatagrams RcvbufErrors SndbufErrors
     * Udp: 69299 43445 94 71895 0 0
     * UdpLite: InDatagrams NoPorts InErrors OutDatagrams RcvbufErrors SndbufErrors
     * UdpLite: 0 0 0 0 0 0
     */
    public void firstCollect() throws IOException {
        String result = ProcessUtil.execute(Commands.NET_SNMP);
        for (String line : result.split(Constant.LINE)) {
            line = StringUtils.replaceMultiBlankSpace(line);
            if (isTcpData(line)) setTcpData(line, tcpUdp);
            if (isUdpData(line)) setUdpData(line, tcpUdp);
        }
    }

    private boolean isTcpData(String line) {
        return line.contains("Tcp") && !line.contains("PassiveOpens");

    }

    private void setTcpData(String line, TcpUdp tcpUdp) {
        String[] tcpData = line.split(Constant.BLANKSPACE);
        tcpUdp.setTcpConn(Long.parseLong(tcpData[tcpConn]));
        tcpUdp.setTcpPassiveOpens(Long.parseLong(tcpData[tcpPassiveOpens]));
    }

    private boolean isUdpData(String line) {
        return line.contains("Udp") && !line.contains("InDatagrams");
    }

    private void setUdpData(String line, TcpUdp tcpUdp) {
        String[] udpData = line.split(Constant.BLANKSPACE);
        tcpUdp.setUdpIn(Long.parseLong(udpData[udpIn]));
        tcpUdp.setUdpOut(Long.parseLong(udpData[udpOut]));
    }


    class TcpUdp {
        private long tcpConn;

        private long tcpPassiveOpens;

        private long udpIn;

        private long udpOut;

        private long deltaTcpConn;

        private long deltaTcpPassiveOpens;

        private long deltaUdpIn;

        private long deltaUdpOut;

        public long getTcpConn() {
            return tcpConn;
        }

        public void setTcpConn(long tcpConn) {
            this.tcpConn = tcpConn;
        }

        public long getTcpPassiveOpens() {
            return tcpPassiveOpens;
        }

        public void setTcpPassiveOpens(long tcpPassiveOpens) {
            this.tcpPassiveOpens = tcpPassiveOpens;
        }

        public long getUdpIn() {
            return udpIn;
        }

        public void setUdpIn(long udpIn) {
            this.udpIn = udpIn;
        }

        public long getUdpOut() {
            return udpOut;
        }

        public void setUdpOut(long udpOut) {
            this.udpOut = udpOut;
        }

        public long getDeltaTcpConn() {
            return deltaTcpConn;
        }

        public void setDeltaTcpConn(long deltaTcpConn) {
            this.deltaTcpConn = deltaTcpConn;
        }

        public long getDeltaTcpPassiveOpens() {
            return deltaTcpPassiveOpens;
        }

        public void setDeltaTcpPassiveOpens(long deltaTcpPassiveOpens) {
            this.deltaTcpPassiveOpens = deltaTcpPassiveOpens;
        }

        public long getDeltaUdpIn() {
            return deltaUdpIn;
        }

        public void setDeltaUdpIn(long deltaUdpIn) {
            this.deltaUdpIn = deltaUdpIn;
        }

        public long getDeltaUdpOut() {
            return deltaUdpOut;
        }

        public void setDeltaUdpOut(long deltaUdpOut) {
            this.deltaUdpOut = deltaUdpOut;
        }
    }

}
