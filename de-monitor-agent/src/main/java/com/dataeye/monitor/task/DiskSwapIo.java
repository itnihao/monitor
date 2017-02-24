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
 * @author wendy
 *         fid  object  desc
 *         31   diskioin    平均每秒把数据从硬盘读到物理内存的数据量
 *         32   diskioout   平均每秒把数据从物理内存写到硬盘的数据量
 *         33   swapioin    平均每秒把数据从磁盘交换区装入内存的数据量
 *         34   swapinout   平均每秒把数据从内存转储到磁盘交换区的数据量
 */
@FourMinuteTask
public class DiskSwapIo extends BaseTask {

    private Vmstat vmstat = new Vmstat();
    private String reportData;

    @Override
    public void run() {
        try {
            collectData();
            genarateData();
            report();
        } catch (Throwable e) {
            ExceptionHandler.error("DiskSwapIo error", e);
        }
    }

    private void report() throws ServerException {
        if (StringUtils.isNotEmpty(reportData)) {
            HttpClientUtil.post(ReportUrl.serverFeaturUrl, reportData);
            //batchReportHandler.put(ReportUrl.serverFeaturUrl, reportData);
        }
    }

    private void genarateData() {
        Feature feature = new Feature();
        feature.setFeature_list(new ArrayList<Feature>());
        Feature diskIoIn = new Feature(
                FID.pgpgin.getValue(),
                FID.pgpgin.getName(),
                new BigDecimal(vmstat.getDiskIoIn()));
        feature.addFeature(diskIoIn);

        Feature diskIoOut = new Feature(
                FID.pgpgout.getValue(),
                FID.pgpgout.getName(),
                new BigDecimal(vmstat.getDiskIoOut()));
        feature.addFeature(diskIoOut);

        Feature swapIoIn = new Feature(
                FID.pswpin.getValue(),
                FID.pswpin.getName(),
                new BigDecimal(vmstat.getSwapIoIn()));
        feature.addFeature(swapIoIn);

        Feature swapIoOut = new Feature(
                FID.pswpout.getValue(),
                FID.pswpout.getName(),
                new BigDecimal(vmstat.getSwapIoOut()));
        feature.addFeature(swapIoOut);
        reportData = Constant.GSON.toJson(feature);

    }

    /**
     * [deuser@mysql7 ~]$ cat /proc/vmstat
     * ...
     * pgpgin 9517964532
     * pgpgout 26319007539
     * pswpin 2190885
     * pswpout 2947624
     * ...
     */

    public void collectData()
            throws IOException {
        reportData = null;
        firstCollect();
        waitFourMinute();
        secondCollect();
        calculateAndSetUpdata();
    }

    private void waitFourMinute() {
        try {
            Thread.sleep(240 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void secondCollect() throws IOException {
        String result = ProcessUtil.execute(Commands.VMSTAT);
        for (String line : result.split(Constant.LINE)) {
            line = StringUtils.replaceMultiBlankSpace(line);
            long value = Long.parseLong(line.split(Constant.BLANKSPACE)[1]);
            if (line.contains("pgpgin"))
                vmstat.setDeltapgpgin(value);
            if (line.contains("pgpgout"))
                vmstat.setDeltapgpgout(value);
            if (line.contains("pswpin"))
                vmstat.setDeltapswpin(value);
            if (line.contains("pswpout"))
                vmstat.setDeltapswpout(value);
        }

        logger.debug("deltapgpgin: "
                + vmstat.getDeltapgpgin()
                + " deltapgpgout:"
                + vmstat.getDeltapgpgout()
                + " deltapswpin: "
                + vmstat.getDeltapswpin()
                + " deltapswpout:"
                + vmstat.getDeltapswpout());
    }

    private void calculateAndSetUpdata() {

        long diskIn = (vmstat.getDeltapgpgin() - vmstat.getPgpgin()) / 240;
        long diskOut = (vmstat.getDeltapgpgout() - vmstat.getPgpgout()) / 240;
        long swapIn = (vmstat.getDeltapswpin() - vmstat.getPswpin()) / 240;
        long swapOut = (vmstat.getDeltapswpout() - vmstat.getPswpout()) / 240;

        vmstat.setDiskIoIn(diskIn);
        vmstat.setDiskIoOut(diskOut);
        vmstat.setSwapIoIn(swapIn);
        vmstat.setSwapIoOut(swapOut);

        logger.debug("diskIn: "
                + diskIn
                + " diskOut: "
                + diskOut
                + " swapIn: "
                + swapIn
                + " swapOut:"
                + swapOut);
    }

    private void firstCollect()
            throws IOException {
        String result = ProcessUtil.execute(Commands.VMSTAT);
        for (String line : result.split(Constant.LINE)) {
            line = StringUtils.replaceMultiBlankSpace(line);
            long value = Long.parseLong(line.split(Constant.BLANKSPACE)[1]);

            if (line.contains("pgpgin"))
                vmstat.setPgpgin(value);
            if (line.contains("pgpgout"))
                vmstat.setPgpgout(value);
            if (line.contains("pswpin"))
                vmstat.setPswpin(value);
            if (line.contains("pswpout"))
                vmstat.setPswpout(value);
        }
        logger.debug("pgpgin: "
                + vmstat.getPgpgin()
                + " pgpgout:"
                + vmstat.getPgpgout()
                + " pswpin: "
                + vmstat.getPswpin()
                + " pswpout:"
                + vmstat.getPswpout());
    }


    class Vmstat {
        private long pgpgin;
        private long pgpgout;
        private long pswpin;
        private long pswpout;
        private long deltapgpgin;
        private long deltapgpgout;
        private long deltapswpin;
        private long deltapswpout;

        private long diskIoIn;

        private long diskIoOut;

        private long swapIoIn;

        private long swapIoOut;

        public long getPgpgin() {
            return pgpgin;
        }

        public void setPgpgin(long pgpgin) {
            this.pgpgin = pgpgin;
        }

        public long getPgpgout() {
            return pgpgout;
        }

        public void setPgpgout(long pgpgout) {
            this.pgpgout = pgpgout;
        }

        public long getPswpin() {
            return pswpin;
        }

        public void setPswpin(long pswpin) {
            this.pswpin = pswpin;
        }

        public long getPswpout() {
            return pswpout;
        }

        public void setPswpout(long pswpout) {
            this.pswpout = pswpout;
        }

        public long getDeltapgpgin() {
            return deltapgpgin;
        }

        public void setDeltapgpgin(long deltapgpgin) {
            this.deltapgpgin = deltapgpgin;
        }

        public long getDeltapgpgout() {
            return deltapgpgout;
        }

        public void setDeltapgpgout(long deltapgpgout) {
            this.deltapgpgout = deltapgpgout;
        }

        public long getDeltapswpin() {
            return deltapswpin;
        }

        public void setDeltapswpin(long deltapswpin) {
            this.deltapswpin = deltapswpin;
        }

        public long getDeltapswpout() {
            return deltapswpout;
        }

        public void setDeltapswpout(long deltapswpout) {
            this.deltapswpout = deltapswpout;
        }

        public long getDiskIoIn() {
            return diskIoIn;
        }

        public void setDiskIoIn(long diskIoIn) {
            this.diskIoIn = diskIoIn;
        }

        public long getDiskIoOut() {
            return diskIoOut;
        }

        public void setDiskIoOut(long diskIoOut) {
            this.diskIoOut = diskIoOut;
        }

        public long getSwapIoIn() {
            return swapIoIn;
        }

        public void setSwapIoIn(long swapIoIn) {
            this.swapIoIn = swapIoIn;
        }

        public long getSwapIoOut() {
            return swapIoOut;
        }

        public void setSwapIoOut(long swapIoOut) {
            this.swapIoOut = swapIoOut;
        }
    }

}
