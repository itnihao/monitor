package com.dataeye.monitor.task;


import com.dataeye.monitor.common.annocations.OneMinuteTask;
import com.dataeye.monitor.common.constants.Commands;
import com.dataeye.monitor.common.constants.Constant;
import com.dataeye.monitor.common.constants.FID;
import com.dataeye.monitor.common.constants.ReportUrl;
import com.dataeye.monitor.utils.DateUtis;
import com.dataeye.monitor.utils.HttpClientUtil;
import com.dataeye.monitor.utils.ProcessUtil;
import com.dataeye.monitor.utils.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wendy on 2016/6/23.
 * 通过/proc/stat文件采集并计算CPU总使用率及单个核使用率。以cpu0为例，算法如下：
 * 1. cat /proc/stat | grep 'cpu0' 得到cpu0的信息
 * 2. cpuTotal1=user+nice+system+idle+iowait+irq+softirq
 * 3. cpuUsed1=user+nice+system+irq+softirq
 * 4. sleep 15秒
 * 5. 再次cat /proc/stat | grep ‘cpu0’得到cpu的信息
 * 6. cpuTotal2=user+nice+system+idle+iowait+irq+softirq
 * 7. cpuUsed2=user+nice+system+irq+softirq
 * 8. 得到cpu0 在15秒内的单核利用率：(cpuUsed2 – cpuUsed1) * 100 / (cpuTotal2 – cpuTotal1)
 * 9. 每分钟会采集4次（每次是15秒内的CPU使用率），把使用率最大的一次上报。
 * 相当于使用top –d 15命令，把user、nice、system、irq、softirq五项的使用率相加。
 */
@OneMinuteTask
public class CpuUsage extends BaseTask {

    //一分钟内每隔15秒收集一次，共收集次数
    private static final int  collectTimes = 4;

    private static final int USER = 0;
    private static final int NICE = 1;
    private static final int  SYS = 2;
    private static final int IDLE = 3;
    private static final int IOWAIT = 4;
    private static final int IRQ = 5;
    private static final int SOFTIRQ = 6;

    /**
     * 各个核心cpu每分钟最大利用率
     */
    private Map<String, Long> maxCpuUsageMinute =null;

    //上报结果
    private String reportData = null;

    @Override
    public void run() {
        try {
            collectOriginData();
            geneteReportData();
            report();
        } catch (Throwable e) {
            ExceptionHandler.error("cpu usage error", e);
        }
    }

    public void collectOriginData() throws IOException {
        maxCpuUsageMinute = new ConcurrentHashMap<>();
        reportData = null;
        for (int i = 0; i < collectTimes; i++) {
            calcCpuMaxUsagePer15s();
        }
    }

    public void geneteReportData() {
        String time = DateUtis.currentTime();
        Feature feature = new Feature();
        feature.setFeature_list(new ArrayList<Feature>());
        for (Map.Entry<String, Long> entry : maxCpuUsageMinute.entrySet()) {
            Feature f = new Feature();
            f.setFid(FID.cpu.getValue());
            f.setClient(Constant.IP);
            f.setValue(new BigDecimal(entry.getValue()));
            f.setObject(entry.getKey());
            f.setTime(time);
            feature.addFeature(f);
        }
        reportData = Constant.GSON.toJson(feature);
    }

    public void report() throws ServerException {
        if (StringUtils.isNotEmpty(reportData)) {
            HttpClientUtil.post(ReportUrl.serverFeaturUrl, reportData);
        }
    }

    public void calcCpuMaxUsagePer15s() throws IOException {
        Map<String, Cpu> cpuData = new HashMap<>();
        firstCollect(cpuData);
        waitFifteenSeconds();
        secondCollect(cpuData);
        calcCpuMaxUsage(cpuData);
    }


    /**
     * [deuser@mysql7 ~]$ cat /proc/stat | grep cpu
     * cpu  1354537432 321 832681329 21923971865 2051212658 14658 41478713 0 0
     * cpu0 375297832 39 157586983 2029809990 653981759 14658 35777327 0 0
     * cpu1 118954992 50 79294561 2419846926 667656091 0 1144084 0 0
     * cpu2 345372699 73 173885805 2471557983 275459078 0 1153429 0 0
     * cpu3 141954442 87 86525857 2810595826 217701094 0 771098 0 0
     * cpu4 169764300 19 95937056 2925480546 97617533 0 688256 0 0
     * cpu5 63850391 32 43564581 3119350946 61447860 0 867694 0 0
     * cpu6 84891039 7 62419376 3104882187 41021298 0 437936 0 0
     * cpu7 54451733 10 133467107 3042447457 36327942 0 638886 0 0
     */
    private void firstCollect(Map<String, Cpu> cpuData)
            throws IOException {
        Cpu cpu;
        String result = ProcessUtil.execute(Commands.CPU);
        for (String line : result.split(Constant.LINE)) {
            String cpuName = line.substring(0, line.indexOf(Constant.BLANKSPACE)).trim();
            String value = line.substring(line.indexOf(Constant.BLANKSPACE)).trim();
            String[] arrString = value.split(Constant.BLANKSPACE);
            long[] arr = StringUtils.strArr2longArr(arrString);
            long cpuTotal = arr[USER] + arr[NICE] + arr[SYS] + arr[IDLE] + arr[IOWAIT]
                    + arr[IRQ] + arr[SOFTIRQ];
            long cpuUsed = arr[USER] + arr[NICE] + arr[SYS] + arr[IRQ] + arr[SOFTIRQ];
            cpu = new Cpu();
            cpu.setUsed(cpuUsed);
            cpu.setTotal(cpuTotal);
            cpu.setName(cpuName);
            cpuData.put(cpuName, cpu);
        }
    }

    private void secondCollect(Map<String, Cpu> cpuData)
            throws IOException {
        Cpu cpu;
        String result = ProcessUtil.execute(Commands.CPU);
        for (String line : result.split(Constant.LINE)) {
            String key = line.substring(0, line.indexOf(Constant.BLANKSPACE)).trim();
            String value = line.substring(line.indexOf(Constant.BLANKSPACE)).trim();
            long[] arr = StringUtils.strArr2longArr(value.split(Constant.BLANKSPACE));
            long cpuTotal = arr[USER] + arr[NICE] + arr[SYS] + arr[IDLE] + arr[IOWAIT]
                    + arr[IRQ] + arr[SOFTIRQ];
            long cpuUsed = arr[USER] + arr[NICE] + arr[SYS] + arr[IRQ] + arr[SOFTIRQ];
            cpu = cpuData.get(key);
            cpu.setDeltaused(cpuUsed);
            cpu.setDeltatotal(cpuTotal);
        }
    }


    private void waitFifteenSeconds() {
        try {
            Thread.sleep(Constant.FIFTEENSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void calcCpuMaxUsage(Map<String,Cpu> cpuData) {
        for (Map.Entry<String, Cpu> entry : cpuData.entrySet()) {
            Cpu cpu = entry.getValue();
            Long used = cpu.getUsed();
            Long total = cpu.getTotal();
            Long deltaUsed = cpu.getDeltaused();
            Long deltaTotal = cpu.getDeltatotal();
            Long usage = (deltaUsed - used) * 100 * 100 / (deltaTotal - total);
            cpu.setUsage(usage);
            logger.debug(cpu.getName()
                    + " cpuUsed:"
                    + cpu.getUsed()
                    + " cpuTotal:"
                    + cpu.getTotal()
                    + " cpuDeltaUsed:"
                    + cpu.getDeltaused()
                    + " cpuDeltatotal:"
                    + cpu.getDeltatotal()
                    + " usage:"
                    + cpu.getUsage());

            Long maxUsage = maxCpuUsageMinute.get(cpu.getName());
            if (maxUsage == null || maxUsage < usage) {
                maxCpuUsageMinute.put(cpu.getName(), usage);
            }
        }
    }


    class Cpu {
        private String name;

        //cpu使用量
        private long used;

        //cpu总量
        private long total;

        //15秒后的cpu使用量
        private long deltaused;

        //15秒后的cpu总量
        private long deltatotal;

        private double usage;

        public long getUsed() {
            return used;
        }

        public void setUsed(long used) {
            this.used = used;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public long getDeltaused() {
            return deltaused;
        }

        public void setDeltaused(long deltaused) {
            this.deltaused = deltaused;
        }

        public long getDeltatotal() {
            return deltatotal;
        }

        public void setDeltatotal(long deltatotal) {
            this.deltatotal = deltatotal;
        }

        public String getName() {return name;}

        public void setName(String name) {this.name = name;}

        public double getUsage() {return usage;}

        public void setUsage(double usage) {this.usage = usage;}
    }

}
