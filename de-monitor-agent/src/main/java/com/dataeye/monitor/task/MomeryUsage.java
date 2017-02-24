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
 * 统计机器内存指标 应用程序内存使用量，内存使用量，所有进程实际内存使用量，
 * 所有进程虚拟内存使用量，共享内存大小，交换空间内存大小，
 * 物理内存使用量
 * Created by wendy on 2016/6/27.
 */
@FourMinuteTask
public class MomeryUsage extends BaseTask {
    private Memory memInfo = null;
    private String reportData;

    @Override
    public void run() {
        try {
            collectData();
            generateData();
            report();
        } catch (Throwable e) {
            ExceptionHandler.error("MomeryUsage error", e);
        }
    }

    private void report() throws ServerException {
        if (StringUtils.isNotEmpty(reportData)) {
            HttpClientUtil.post(ReportUrl.serverFeaturUrl, reportData);
            //batchReportHandler.put(ReportUrl.serverFeaturUrl, reportData);
        }
    }

    /**
     * [deuser@mysql7 ~]$ cat /proc/meminfo
     * MemTotal:       65920828 kB
     * MemFree:          764980 kB
     * Buffers:          392148 kB
     * Cached:         53030572 kB
     * SwapCached:         8796 kB
     * ...
     * SwapTotal:      67108856 kB
     * SwapFree:       66318048 kB
     * ...
     */
    public void collectData()
            throws IOException {
        reportData = null;
        memInfo = new Memory();
        String result = ProcessUtil.execute(Commands.MEM_INFO);

        for (String line : result.split(Constant.LINE)) {
            line = StringUtils.replaceMultiBlankSpace(line);
            String[] arr = line.split(Constant.BLANKSPACE);
            long value = Long.parseLong(arr[1]);
            if (line.startsWith("MemTotal"))
                memInfo.setMemTotal(value * 1024);
            if (line.startsWith("MemFree"))
                memInfo.setMemFree(value * 1024);
            if (line.startsWith("Buffers"))
                memInfo.setMemBuffers(value * 1024);
            if (line.startsWith("Cached"))
                memInfo.setMemCached(value * 1024);
            if (line.startsWith("SwapTotal"))
                memInfo.setSwapTotal(value * 1024);
            if (line.startsWith("SwapFree"))
                memInfo.setSwapFree(value * 1024);
        }

        String privateMem = ProcessUtil.execute(Commands.PRIVATE_MEM);
        if (StringUtils.isNotEmpty(privateMem)) {
            memInfo.setPrivateMem(Long.parseLong(privateMem.trim()) * 1024);
        }

        String virtualMem = ProcessUtil.execute(Commands.VIRTUAL_MEM);
        if (StringUtils.isNotEmpty(virtualMem)) {
            memInfo.setVirtualMem(Long.parseLong(virtualMem.trim()) * 1024);
        }

        String ipcs = ProcessUtil.execute(Commands.IPCS);
        if (StringUtils.isNotEmpty(ipcs)) {
            memInfo.setIpcs(Long.parseLong(ipcs.trim()));
        }
        calculate();
    }


    private void calculate() {
        long memProc = memInfo.getMemTotal() - memInfo.getMemFree()
                - memInfo.getMemBuffers() - memInfo.getMemCached();

        long memUsed = memInfo.getMemTotal() - memInfo.getMemFree();

        long privateIpcs = memInfo.getPrivateMem() + memInfo.getIpcs();

        long swapUsed = memInfo.getSwapTotal() - memInfo.getSwapFree();

        memInfo.setMemProc(memProc);
        memInfo.setMemUsed(memUsed);
        memInfo.setPrivateIpcs(privateIpcs);
        memInfo.setSwapUsed(swapUsed);

        logger.debug("memProc:" + memProc + " memUsed:" + memUsed +
                " MemPri:" + memInfo.getPrivateMem() + " memVir:" + memInfo.getVirtualMem()
                + " memPriIcps:" + memInfo.getPrivateIpcs() + " swapUsed:" + memInfo.getSwapUsed()
                + " swapTotal:" + memInfo.getSwapTotal());
    }


    private void generateData() {

        Feature feature = new Feature();
        feature.setFeature_list(new ArrayList<Feature>());
        Feature memProcFeature = new Feature(
                FID.mem_proc.getValue(),
                FID.mem_proc.getName(),
                new BigDecimal(memInfo.getMemProc()));

        feature.addFeature(memProcFeature);

        Feature memUsedFeature = new Feature(
                FID.mem_used.getValue(),
                FID.mem_used.getName(),
                new BigDecimal(memInfo.getMemUsed()));
        feature.addFeature(memUsedFeature);

        Feature memPrivateFeature = new Feature(
                FID.mem_pri.getValue(),
                FID.mem_pri.getName(),
                new BigDecimal(memInfo.getPrivateMem()));

        feature.addFeature(memPrivateFeature);

        Feature memVirtualFeature = new Feature(
                FID.mem_vir.getValue(),
                FID.mem_vir.getName(),
                new BigDecimal(memInfo.getVirtualMem()));
        feature.addFeature(memVirtualFeature);

        Feature memPrivateIpcsFeature = new Feature(
                FID.mpri_ipcs.getValue(),
                FID.mpri_ipcs.getName(),
                new BigDecimal(memInfo.getPrivateIpcs()));

        feature.addFeature(memPrivateIpcsFeature);

        Feature swapUsedFeature = new Feature(
                FID.mem_swap_used.getValue(),
                FID.mem_swap_used.getName(),
                new BigDecimal(memInfo.getSwapUsed()));
        feature.addFeature(swapUsedFeature);

        Feature swapTotalFeature = new Feature(
                FID.mem_swap_total.getValue(),
                FID.mem_swap_total.getName(),
                new BigDecimal(memInfo.getSwapTotal()));

        feature.addFeature(swapTotalFeature);

        reportData = Constant.GSON.toJson(feature);
    }


    class Memory {
        private long memTotal;
        private long memFree;
        private long memBuffers;
        private long memCached;
        private long privateMem;
        private long virtualMem;
        private long ipcs;
        private long swapTotal;
        private long swapFree;
        private long memProc;
        private long memUsed;
        private long privateIpcs;
        private long swapUsed;

        public long getMemTotal() {
            return memTotal;
        }

        public void setMemTotal(long memTotal) {
            this.memTotal = memTotal;
        }

        public long getMemFree() {
            return memFree;
        }

        public void setMemFree(long memFree) {
            this.memFree = memFree;
        }

        public long getMemBuffers() {
            return memBuffers;
        }

        public void setMemBuffers(long memBuffers) {
            this.memBuffers = memBuffers;
        }

        public long getMemCached() {
            return memCached;
        }

        public void setMemCached(long memCached) {
            this.memCached = memCached;
        }

        public long getPrivateMem() {
            return privateMem;
        }

        public void setPrivateMem(long privateMem) {
            this.privateMem = privateMem;
        }

        public long getVirtualMem() {
            return virtualMem;
        }

        public void setVirtualMem(long virtualMem) {
            this.virtualMem = virtualMem;
        }

        public long getIpcs() {
            return ipcs;
        }

        public void setIpcs(long ipcs) {
            this.ipcs = ipcs;
        }

        public long getSwapTotal() {
            return swapTotal;
        }

        public void setSwapTotal(long swapTotal) {
            this.swapTotal = swapTotal;
        }

        public long getSwapFree() {
            return swapFree;
        }

        public void setSwapFree(long swapFree) {
            this.swapFree = swapFree;
        }

        public long getMemProc() {
            return memProc;
        }

        public void setMemProc(long memProc) {
            this.memProc = memProc;
        }

        public long getMemUsed() {
            return memUsed;
        }

        public void setMemUsed(long memUsed) {
            this.memUsed = memUsed;
        }

        public long getPrivateIpcs() {
            return privateIpcs;
        }

        public void setPrivateIpcs(long privateIpcs) {
            this.privateIpcs = privateIpcs;
        }

        public long getSwapUsed() {
            return swapUsed;
        }

        public void setSwapUsed(long swapUsed) {
            this.swapUsed = swapUsed;
        }
    }


}
