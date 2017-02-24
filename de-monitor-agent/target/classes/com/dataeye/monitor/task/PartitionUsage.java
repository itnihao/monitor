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

/**
 * Created by wendy on 2016/6/27.
 * <p/>
 * 统计磁盘信息和磁盘io信息
 * fid  object  desc
 * 40   diskusage   分区可用空间大小
 * 41   disktotalUsage  分区总可用空间大小
 */
@FourMinuteTask
public class PartitionUsage extends BaseTask {

    private static final int partTotal = 1;
    private static final int partUsed = 2;
    private static final int partName = 5;

    private List<Partition> partitions = null;
    private String reportData;

    @Override
    public void run() {
        try {
            collectData();
            generateData();
            report();
        } catch (Throwable e) {
            ExceptionHandler.error("PartitionUsage error", e);
        }
    }

    private void report() throws ServerException {
        if (StringUtils.isNotEmpty(reportData)) {
            HttpClientUtil.post(ReportUrl.serverFeaturUrl, reportData);
           // batchReportHandler.put(ReportUrl.serverFeaturUrl, reportData);
        }
    }

    private void generateData() {
        Feature feature = new Feature();
        feature.setFeature_list(new ArrayList<Feature>());
        for (Partition partition : partitions) {
            Feature diskusedFeature = new Feature(
                    FID.partition_free.getValue(),
                    partition.getName(),
                    new BigDecimal(partition.getFree())
            );
            feature.addFeature(diskusedFeature);

            Feature diskTotalFeature = new Feature(
                    FID.partition_total.getValue(),
                    partition.getName(),
                    new BigDecimal(partition.getTotal())
            );
            feature.addFeature(diskTotalFeature);
        }
        reportData = Constant.GSON.toJson(feature);
    }

    public List<Partition> collectData() {
        reportData = null;
        partitions = new ArrayList<>();

        String result = ProcessUtil.execute(Commands.PARTITION);
        Partition disk;
        for (String line : result.split(Constant.LINE)) {
            line = StringUtils.replaceMultiBlankSpace(line);
            String[] arr = line.split(Constant.BLANKSPACE);
            Double sysReserveSpace = Long.parseLong(arr[partTotal]) * 0.05;
            double used = (sysReserveSpace.longValue() + Long.parseLong(arr[partUsed])) * 1024;
            long total = Long.parseLong(arr[partTotal]) * 1024;
            double free = total - used;
            disk = new Partition(arr[partName], free, total);

            partitions.add(disk);
            logger.debug("partName: " + disk.getName() + " free: " + free + " total: " + disk.getTotal());
        }
        return partitions;
    }


    class Partition {
        private String name;

        private double free;

        private long total;

        public Partition(String name, double free, long total) {
            this.name = name;
            this.free = free;
            this.total = total;
        }

        public String getName() {
            return name;
        }

        public double getFree() {
            return free;
        }

        public long getTotal() {
            return total;
        }
    }


}
