package com.dataeye.monitor.task;

import com.dataeye.monitor.common.annocations.OneMinuteTask;
import com.dataeye.monitor.common.constants.Commands;
import com.dataeye.monitor.common.constants.Constant;
import com.dataeye.monitor.common.constants.FID;
import com.dataeye.monitor.common.constants.ReportUrl;
import com.dataeye.monitor.utils.HttpClientUtil;
import com.dataeye.monitor.utils.ProcessUtil;
import com.dataeye.monitor.utils.StringUtils;
import com.xunlei.netty.httpserver.cmd.*;

import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.ArrayList;

/**
 * 统计CPU 1,5,10 分钟负载
 * Created by wendy on 2016/6/24.
 */
@OneMinuteTask
public class CpuLoadAverage extends BaseTask {
    private static final int one = 0;
    private static final int five = 1;
    private static final int fifteen = 2;
    private BigDecimal oneLoad;
    private BigDecimal fiveLoad;
    private BigDecimal fifteenLoad;
    private String reportData;

    @Override
    public void run() {
        try {
            collectData();
            generateData();
            report();
        } catch (Throwable e) {
            ExceptionHandler.error("cpu load average error", e);
        }
    }

    public void report() throws ServerException {
        if (StringUtils.isNotEmpty(reportData)) {
            HttpClientUtil.post(ReportUrl.serverFeaturUrl, reportData);
        }
    }

    /**
     * [deuser@mysql7 ~]$ cat /proc/loadavg
     * 0.28 0.27 0.27 2/1654 39991
     */
    public void collectData() {
        reportData = null;
        String line = ProcessUtil.execute(Commands.LOADAVERAGE);
        if (StringUtils.isNotEmpty(line)) {
            String[] arr = line.split(Constant.BLANKSPACE);
            int cpuNum = Runtime.getRuntime().availableProcessors();
            oneLoad = new BigDecimal(Double.parseDouble(arr[one]) * 100 / cpuNum)
                    .setScale(0, BigDecimal.ROUND_UP);
            fiveLoad = new BigDecimal(Double.parseDouble(arr[five]) * 100 / cpuNum)
                    .setScale(0, BigDecimal.ROUND_UP);
            fifteenLoad = new BigDecimal(Double.parseDouble(arr[fifteen]) * 100 / cpuNum)
                    .setScale(0, BigDecimal.ROUND_UP);

            logger.debug("cpuNum:"
                    + cpuNum
                    + " oneLoad:"
                    + oneLoad
                    + " fiveLoad:"
                    + fiveLoad
                    + " fifteenLoad:"
                    + fifteenLoad);

        }
    }

    public void generateData() {
        Feature feature = new Feature();
        feature.setFeature_list(new ArrayList<Feature>());
        Feature oneFeature = new Feature(
                FID.load_average_one.getValue(),
                FID.load_average_one.getName(),
                oneLoad);

        Feature fiveFeature = new Feature(
                FID.load_average_five.getValue(),
                FID.load_average_five.getName(),
                fiveLoad);

        Feature fifteenFeature = new Feature(
                FID.load_average_fifteen.getValue(),
                FID.load_average_fifteen.getName(),
                fifteenLoad);

        feature.addFeature(oneFeature);
        feature.addFeature(fiveFeature);
        feature.addFeature(fifteenFeature);
        reportData = Constant.GSON.toJson(feature);
    }
}
