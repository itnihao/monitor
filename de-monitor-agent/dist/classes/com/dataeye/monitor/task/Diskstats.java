package com.dataeye.monitor.task;

import com.dataeye.monitor.common.annocations.FourMinuteTask;
import com.dataeye.monitor.common.constants.Commands;
import com.dataeye.monitor.common.constants.Constant;
import com.dataeye.monitor.common.constants.FID;
import com.dataeye.monitor.common.constants.ReportUrl;
import com.dataeye.monitor.utils.HttpClientUtil;
import com.dataeye.monitor.utils.ProcessUtil;
import com.dataeye.monitor.utils.StringUtils;

import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@FourMinuteTask
public class Diskstats extends BaseTask {
    private static final int name = 2;
    private static final int rio = 3;
    private static final int rmerge = 4;
    private static final int rsect = 5;
    private static final int ruse = 6;
    private static final int wio = 7;
    private static final int wmerge = 8;
    private static final int wsect = 9;
    private static final int wuse = 10;
    private static final int running = 11;
    private static final int use = 12;
    private static final int aveq = 13;

    private String reportData;
    List<Iostat240> iostats=null;
    private Map<String, Diskstat> temp = null;

    @Override
    public void run() {
        try {
            collectData();
            generateData();
            report();
        } catch (Throwable e) {
            ExceptionHandler.error("Diskstats error", e);
        }
    }

    private void generateData() {
        Feature feature = new Feature();
        feature.setFeature_list(new ArrayList<Feature>());

        for (Iostat240 iostat : iostats) {
            Feature svctmTimeMax = new Feature(
                    FID.svctm.getValue(),
                    iostat.getDiskName(),
                    iostat.getSvctmTimeMax());
            feature.addFeature(svctmTimeMax);

            Feature awaitTimeMax = new Feature(
                    FID.await.getValue(),
                    iostat.getDiskName(),
                    iostat.getAwaitTimeMax());
            feature.addFeature(awaitTimeMax);

            Feature avgquSzMax = new Feature(
                    FID.avgqu_sz_max.getValue(),
                    iostat.getDiskName(),
                    iostat.getAvgquSzMax());
            feature.addFeature(avgquSzMax);

            Feature avgrqSz = new Feature(
                    FID.avgrq_sz.getValue(),
                    iostat.getDiskName(),
                    iostat.getAvgrqSz());
            feature.addFeature(avgrqSz);

            Feature utilMax = new Feature(
                    FID.util_max.getValue(),
                    iostat.getDiskName(),
                    iostat.getUtilMax());
            feature.addFeature(utilMax);
        }
        reportData = Constant.GSON.toJson(feature);
    }

    private void report() throws ServerException {
        if (StringUtils.isNotEmpty(reportData)) {
            HttpClientUtil.post(ReportUrl.serverFeaturUrl, reportData);
           // batchReportHandler.put(ReportUrl.serverFeaturUrl, reportData);
        }
    }

    public void collectData() {
        reportData = null;
        temp = new ConcurrentHashMap<>();
        firstCollect();
        waitFourMinute();
        secondCollect();
        calculateDiskStat();
    }

    private void calculateDiskStat() {
        iostats = new ArrayList<>();
        for (Map.Entry<String, Diskstat>
                entry : temp.entrySet()) {
            Diskstat diskStat = entry.getValue();
            BigDecimal svctmTimeMax = calsvctmTimeMax(diskStat);
            BigDecimal awaitTimeMax = calawaitTimeMax(diskStat);
            BigDecimal avgrqSz = calAvgrqSz(diskStat);

            long aveq = diskStat.getAveq2() - diskStat.getAveq();
            BigDecimal avgquSzMax = new BigDecimal(aveq * 100 / 240 / 1000);

            long use = diskStat.getUse2() - diskStat.getUse();
            BigDecimal utilMax = new BigDecimal(use * 100 / 240 / 1000);

            Iostat240 iostat = new Iostat240(diskStat.getName(), svctmTimeMax
                    , awaitTimeMax, avgquSzMax, avgrqSz, utilMax);
            iostats.add(iostat);

        }
    }

    private void secondCollect() {
        String result = ProcessUtil.execute(Commands.DISKSTATS);
        Diskstat diskStat = null;
        for (String line : result.split(Constant.LINE)) {
            line = StringUtils.replaceMultiBlankSpace(line);
            String[] arr = line.split(Constant.BLANKSPACE);
            diskStat = temp.get(arr[name]);
            diskStat.setRio2(Long.parseLong(arr[rio]));
            diskStat.setRmerge2(Long.parseLong(arr[rmerge]));
            diskStat.setRsect2(Long.parseLong(arr[rsect]));
            diskStat.setRuse2(Long.parseLong(arr[ruse]));
            diskStat.setWio2(Long.parseLong(arr[wio]));
            diskStat.setWmerge2(Long.parseLong(arr[wmerge]));
            diskStat.setWsect2(Long.parseLong(arr[wsect]));
            diskStat.setWuse2(Long.parseLong(arr[wuse]));
            diskStat.setRunning2(Long.parseLong(arr[running]));
            diskStat.setUse2(Long.parseLong(arr[use]));
            diskStat.setAveq2(Long.parseLong(arr[aveq]));
        }
    }

    private void firstCollect() {
        String result = ProcessUtil.execute(Commands.DISKSTATS);
        Diskstat diskStat = null;
        for (String line : result.split(Constant.LINE)) {
            line = StringUtils.replaceMultiBlankSpace(line);
            String[] arr = line.split(Constant.BLANKSPACE);
            diskStat = new Diskstat(arr[name],
                    Long.parseLong(arr[rio]),
                    Long.parseLong(arr[rmerge]),
                    Long.parseLong(arr[rsect]),
                    Long.parseLong(arr[ruse]),
                    Long.parseLong(arr[wio]),
                    Long.parseLong(arr[wmerge]),
                    Long.parseLong(arr[wsect]),
                    Long.parseLong(arr[wuse]),
                    Long.parseLong(arr[running]),
                    Long.parseLong(arr[use]),
                    Long.parseLong(arr[aveq]));
            temp.put(arr[name], diskStat);
        }
    }

    private void waitFourMinute() {
        try {
            Thread.sleep(240 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private BigDecimal calAvgrqSz(Diskstat diskStat) {
        long use = diskStat.getRsect2() + diskStat.getWsect2()
                - diskStat.getRsect() - diskStat.getWsect();

        long total = diskStat.getRio2() + diskStat.getWio2()
                - diskStat.getRio() - diskStat.getWio();

        BigDecimal avgrqSz =  new BigDecimal(0);
        if (total > 0) {
            avgrqSz = new BigDecimal(use).divide(new BigDecimal(total),
                    0, BigDecimal.ROUND_UP);
        }
        return avgrqSz;
    }

    private BigDecimal calawaitTimeMax(Diskstat diskStat) {
        long use = diskStat.getRuse2() + diskStat.getWuse2()
                - diskStat.getRuse() - diskStat.getWuse();

        long total = diskStat.getRio2() + diskStat.getWio2()
                - diskStat.getRio() - diskStat.getWio();

        BigDecimal awaitTimeMax = new BigDecimal(0);

        if (total > 0) {
            awaitTimeMax = new BigDecimal(use).divide(new BigDecimal(total),
                    2, BigDecimal.ROUND_UP).multiply(new BigDecimal(100));
        }
        return awaitTimeMax;
    }

    private BigDecimal calsvctmTimeMax(Diskstat diskStat) {
        long use = diskStat.getUse2() - diskStat.getUse();
        long total = diskStat.getRio2() + diskStat.getWio2()
                - diskStat.getRio() - diskStat.getWio();

        BigDecimal svctmTimeMax = new BigDecimal(0);
        if (total > 0) {
            svctmTimeMax = new BigDecimal(use).divide(new BigDecimal(total),
                    2, BigDecimal.ROUND_UP).multiply(new BigDecimal(100));
        }
        return svctmTimeMax;
    }

    class Iostat240 {
        private String diskName;
        private BigDecimal svctmTimeMax;
        private BigDecimal awaitTimeMax;
        private BigDecimal avgquSzMax;
        private BigDecimal avgrqSz;
        private BigDecimal utilMax;

        public Iostat240(String diskName
                , BigDecimal svctmTimeMax
                , BigDecimal awaitTimeMax
                , BigDecimal avgquSzMax
                , BigDecimal avgrqSz
                , BigDecimal utilMax) {
            this.diskName = diskName;
            this.svctmTimeMax = svctmTimeMax;
            this.awaitTimeMax = awaitTimeMax;
            this.avgquSzMax = avgquSzMax;
            this.avgrqSz = avgrqSz;
            this.utilMax = utilMax;
        }

        @Override
        public String toString() {
            return "Iostat240{" +
                    "diskName='" + diskName + '\'' +
                    ", svctmTimeMax=" + svctmTimeMax +
                    ", awaitTimeMax=" + awaitTimeMax +
                    ", avgquSzMax=" + avgquSzMax +
                    ", avgrqSz=" + avgrqSz +
                    ", utilMax=" + utilMax +
                    '}';
        }

        public String getDiskName() {
            return diskName;
        }

        public BigDecimal getSvctmTimeMax() {
            return svctmTimeMax;
        }

        public BigDecimal getAwaitTimeMax() {
            return awaitTimeMax;
        }

        public BigDecimal getAvgquSzMax() {
            return avgquSzMax;
        }

        public BigDecimal getAvgrqSz() {
            return avgrqSz;
        }

        public BigDecimal getUtilMax() {
            return utilMax;
        }
    }


    /**
     * major minor  name rio rmerge rsect   ruse    wio      wmerge     wsect      wuse   running     use     aveq
     * 8       0 sda 115466966 3326839 19036911610 2834561392 1235238665 5215793150 52796005256 4242532633 0 2166845126 2966154562
     */
    class Diskstat {
        private String name;
        private long rio;
        private long rmerge;
        private long rsect;
        private long ruse;
        private long wio;
        private long wmerge;
        private long wsect;
        private long wuse;
        private long running;
        private long use;
        private long aveq;

        private long rio2;
        private long rmerge2;
        private long rsect2;
        private long ruse2;
        private long wio2;
        private long wmerge2;
        private long wsect2;
        private long wuse2;
        private long running2;
        private long use2;
        private long aveq2;

        public Diskstat(String name, long rio, long rmerge
                , long rsect, long ruse, long wio, long wmerge
                , long wsect, long wuse, long running
                , long use, long aveq) {
            this.name = name;
            this.rio = rio;
            this.rmerge = rmerge;
            this.rsect = rsect;
            this.ruse = ruse;
            this.wio = wio;
            this.wmerge = wmerge;
            this.wsect = wsect;
            this.wuse = wuse;
            this.running = running;
            this.use = use;
            this.aveq = aveq;
        }

        @Override
        public String toString() {
            return "Diskstat{" +
                    "name='" + name + '\'' +
                    ", rio=" + rio +
                    ", rmerge=" + rmerge +
                    ", rsect=" + rsect +
                    ", ruse=" + ruse +
                    ", wio=" + wio +
                    ", wmerge=" + wmerge +
                    ", wsect=" + wsect +
                    ", wuse=" + wuse +
                    ", running=" + running +
                    ", use=" + use +
                    ", aveq=" + aveq +
                    ", rio2=" + rio2 +
                    ", rmerge2=" + rmerge2 +
                    ", rsect2=" + rsect2 +
                    ", ruse2=" + ruse2 +
                    ", wio2=" + wio2 +
                    ", wmerge2=" + wmerge2 +
                    ", wsect2=" + wsect2 +
                    ", wuse2=" + wuse2 +
                    ", running2=" + running2 +
                    ", use2=" + use2 +
                    ", aveq2=" + aveq2 +
                    '}';
        }

        public long getRio2() {
            return rio2;
        }

        public void setRio2(long rio2) {
            this.rio2 = rio2;
        }

        public long getRmerge2() {
            return rmerge2;
        }

        public void setRmerge2(long rmerge2) {
            this.rmerge2 = rmerge2;
        }

        public long getRsect2() {
            return rsect2;
        }

        public void setRsect2(long rsect2) {
            this.rsect2 = rsect2;
        }

        public long getRuse2() {
            return ruse2;
        }

        public void setRuse2(long ruse2) {
            this.ruse2 = ruse2;
        }

        public long getWio2() {
            return wio2;
        }

        public void setWio2(long wio2) {
            this.wio2 = wio2;
        }

        public long getWmerge2() {
            return wmerge2;
        }

        public void setWmerge2(long wmerge2) {
            this.wmerge2 = wmerge2;
        }

        public long getWsect2() {
            return wsect2;
        }

        public void setWsect2(long wsect2) {
            this.wsect2 = wsect2;
        }

        public long getWuse2() {
            return wuse2;
        }

        public void setWuse2(long wuse2) {
            this.wuse2 = wuse2;
        }

        public long getRunning2() {
            return running2;
        }

        public void setRunning2(long running2) {
            this.running2 = running2;
        }

        public long getUse2() {
            return use2;
        }

        public void setUse2(long use2) {
            this.use2 = use2;
        }

        public long getAveq2() {
            return aveq2;
        }

        public void setAveq2(long aveq2) {
            this.aveq2 = aveq2;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getRio() {
            return rio;
        }

        public long getRmerge() {
            return rmerge;
        }

        public long getRsect() {
            return rsect;
        }

        public long getRuse() {
            return ruse;
        }

        public long getWio() {
            return wio;
        }

        public long getWmerge() {
            return wmerge;
        }

        public long getWsect() {
            return wsect;
        }

        public long getWuse() {
            return wuse;
        }

        public long getRunning() {
            return running;
        }

        public long getUse() {
            return use;
        }

        public long getAveq() {
            return aveq;
        }
    }
}
