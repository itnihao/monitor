package com.dataeye.monitor.task;

import com.dataeye.monitor.common.annocations.FourMinuteTask;
import com.dataeye.monitor.common.constants.Commands;
import com.dataeye.monitor.common.constants.Constant;
import com.dataeye.monitor.common.constants.ReportUrl;
import com.dataeye.monitor.utils.*;

import java.net.UnknownHostException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务器基础数据特性数据
 * Created by wendy on 2016/7/6.
 */
@FourMinuteTask
public class BasicServerFeature extends BaseTask {
    private static String ip = CommonUtils.getLocalIp();
    private static String hostName = CommonUtils.getLocalHostName();
    private static String netCardNum = ProcessUtil.execute(Commands.netCardNum).trim();
    private static String cpuNum = ProcessUtil.execute(Commands.cpuNum).trim();
    private static String cpuType = ProcessUtil.execute(Commands.cpuType).trim();
    private static String cpuPhysicalCores = ProcessUtil.execute(Commands.cpuPhysicalCores).trim();
    private static String cpuLogicCores = ProcessUtil.execute(Commands.cpuLogicCores).trim();
    private static String os = ProcessUtil.execute(Commands.osType).trim();
    private static String kernal = ProcessUtil.execute(Commands.kernal).trim();
    private String reportData;
    private DeviceBasic device = new DeviceBasic();

    @Override
    public void run() {
        try {
            collectData();
            generateData();
            report();
        } catch (Throwable e) {
            ExceptionHandler.error("basic server feature error", e);
        }
    }

    private void report() throws ServerException {
        if (StringUtils.isNotEmpty(reportData)) {
          //  batchReportHandler.put(ReportUrl.serverBasicUrl, reportData);
            HttpClientUtil.post(ReportUrl.serverBasicUrl, reportData);
        }
    }

    private void generateData() {
        reportData = Constant.GSON.toJson(device);
    }

    /**
     * {
     * "hostName": "dchbase1",
     * "ip":"192.168.1.173",
     * "netCardNum":"2",
     * "cpuNum":"4",
     * "cpuType":" Intel(R) Xeon(R) CPU E3-1220 V2 @ 3.10GHz",
     * "cpuPhysicalCores":"4",
     * "cpuLogicCores":"4",
     * "os":"CentOS release 6.6 (Final)",
     * "kernal":"2.6.32-279.el6.x86_64",
     * "memory":"32850460",
     * "diskNum":"2",
     * "diskSize":"4000797868032",
     * "diskDetails":[
     * {
     * "disk":"/dev/sdb",
     * "diskType":"HGST HUS724030ALA640",
     * "diskVolue":"3000592982016"
     * }
     * ],
     * "diskPartitions":[
     * {
     * "name": "/dev/sda2",
     * "partition": "/",
     * "partitionSize" : "105689415680"
     * }
     * ],
     * "dateTime":"2016-02-17 11:25:38"
     * }
     */
    private void collectData() throws UnknownHostException {
        reportData = null;
        device.setHostName(hostName);
        device.setIp(ip);
        device.setNetCardNum(netCardNum);
        device.setCpuNum(cpuNum);
        device.setCpuType(cpuType);
        device.setCpuPhysicalCores(cpuPhysicalCores);
        device.setCpuLogicCores(cpuLogicCores);
        device.setOs(os);
        device.setKernal(kernal);
        device.setMemory(getMemory());
        String diskNum = ProcessUtil.execute(Commands.diskNum).trim();
        device.setDiskNum(diskNum);
        String diskSize = ProcessUtil.execute(Commands.diskSize).trim();
        device.setDiskSize(diskSize);

        device.setDiskDetails(getDiskDetails());
        device.setDiskPartitions(getDiskPartitions());
        device.setDateTime(DateUtis.currentTime());
    }

    private List<Partition> getDiskPartitions() {
        String partitions = ProcessUtil.execute(Commands.PARTITION);
        List<Partition> list = new ArrayList<>();
        Partition partition = null;
        for (String line : partitions.split(Constant.LINE)) {
            line = StringUtils.replaceMultiBlankSpace(line);
            String[] arr = line.split(Constant.BLANKSPACE);
            long parttitionSize = Long.parseLong(arr[1]) * 1024;
            partition = new Partition(arr[0], arr[5], String.valueOf(parttitionSize));
            list.add(partition);
        }
        return list;
    }

    private List<DiskDetail> getDiskDetails() {
        List<DiskDetail> list = new ArrayList<DiskDetail>();
        String diskDetail = ProcessUtil.execute(Commands.DiskDetail);
        DiskDetail detail = null;
        for (String line : diskDetail.split(Constant.LINE)) {
            line = StringUtils.replaceMultiBlankSpace(line);
            String[] arr = line.split(Constant.BLANKSPACE);
            detail = new DiskDetail(arr[0], "", arr[3]);
            list.add(detail);
        }
        return list;
    }

    public String getMemory() {
        String ret = ProcessUtil.execute(Commands.memory).trim();
        long memory = 0;
        if (StringUtils.isNotEmpty(ret)) {
            memory = Long.parseLong(ret) * 1024;
        }
        return String.valueOf(memory);
    }


    class DiskDetail {
        private String disk;
        private String diskType;
        private String diskVolue;

        public DiskDetail(String disk, String diskType, String diskVolue) {
            this.disk = disk;
            this.diskType = diskType;
            this.diskVolue = diskVolue;
        }

        public String getDisk() {
            return disk;
        }

        public void setDisk(String disk) {
            this.disk = disk;
        }

        public String getDiskType() {
            return diskType;
        }

        public void setDiskType(String diskType) {
            this.diskType = diskType;
        }

        public String getDiskVolue() {
            return diskVolue;
        }

        public void setDiskVolue(String diskVolue) {
            this.diskVolue = diskVolue;
        }
    }

    class Partition {
        private String name;
        private String partition;
        private String partitionSize;

        public Partition(String name, String partition, String partitionSize) {
            this.name = name;
            this.partition = partition;
            this.partitionSize = partitionSize;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPartition() {
            return partition;
        }

        public void setPartition(String partition) {
            this.partition = partition;
        }

        public String getPartitionSize() {
            return partitionSize;
        }

        public void setPartitionSize(String partitionSize) {
            this.partitionSize = partitionSize;
        }
    }


    class DeviceBasic {
        private String hostName;
        private String ip;
        private String netCardNum;
        private String cpuNum;
        private String cpuType;
        private String cpuPhysicalCores;
        private String cpuLogicCores;
        private String os;
        private String kernal;
        private String memory;
        private String diskNum;
        private String diskSize;
        private List<DiskDetail> diskDetails;
        private List<Partition> diskPartitions;
        private String dateTime;

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getNetCardNum() {
            return netCardNum;
        }

        public void setNetCardNum(String netCardNum) {
            this.netCardNum = netCardNum;
        }

        public String getCpuNum() {
            return cpuNum;
        }

        public void setCpuNum(String cpuNum) {
            this.cpuNum = cpuNum;
        }

        public String getCpuType() {
            return cpuType;
        }

        public void setCpuType(String cpuType) {
            this.cpuType = cpuType;
        }

        public String getCpuPhysicalCores() {
            return cpuPhysicalCores;
        }

        public void setCpuPhysicalCores(String cpuPhysicalCores) {
            this.cpuPhysicalCores = cpuPhysicalCores;
        }

        public String getCpuLogicCores() {
            return cpuLogicCores;
        }

        public void setCpuLogicCores(String cpuLogicCores) {
            this.cpuLogicCores = cpuLogicCores;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getKernal() {
            return kernal;
        }

        public void setKernal(String kernal) {
            this.kernal = kernal;
        }

        public String getMemory() {
            return memory;
        }

        public void setMemory(String memory) {
            this.memory = memory;
        }

        public String getDiskNum() {
            return diskNum;
        }

        public void setDiskNum(String diskNum) {
            this.diskNum = diskNum;
        }

        public String getDiskSize() {
            return diskSize;
        }

        public void setDiskSize(String diskSize) {
            this.diskSize = diskSize;
        }

        public List<DiskDetail> getDiskDetails() {
            return diskDetails;
        }

        public void setDiskDetails(List<DiskDetail> diskDetails) {
            this.diskDetails = diskDetails;
        }

        public List<Partition> getDiskPartitions() {
            return diskPartitions;
        }

        public void setDiskPartitions(List<Partition> diskPartitions) {
            this.diskPartitions = diskPartitions;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }
    }

}

