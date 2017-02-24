package com.dataeye.omp.report.bean;


import com.dataeye.omp.report.utils.Constant;
import java.math.BigInteger;
import java.util.List;

/**
 * 设备信息
 * @auther wendy
 * @since 2016/1/11 21:37
 */
public class Device {

    private int id;

    private String hostName;
    
    private String ip;

    private int netCardNum;

    private int cpuNum;

    private String cpuType;

    private int cpuPhysicalCores;

    private int cpuLogicCores;

    private String os;

    private String kernal;

    private BigInteger memory;

    private int diskNum;

    private BigInteger diskSize;

    private List<DiskDetails> diskDetails;

    private List<DiskPartition> diskPartitions;

    private String diskDetailsString;

    private String diskPartitionsString;

    private String dateTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getNetCardNum() {
        return netCardNum;
    }

    public void setNetCardNum(int netCardNum) {
        this.netCardNum = netCardNum;
    }

    public int getCpuNum() {
        return cpuNum;
    }

    public void setCpuNum(int cpuNum) {
        this.cpuNum = cpuNum;
    }

    public String getCpuType() {
        return cpuType;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    public int getCpuPhysicalCores() {
        return cpuPhysicalCores;
    }

    public void setCpuPhysicalCores(int cpuPhysicalCores) {
        this.cpuPhysicalCores = cpuPhysicalCores;
    }

    public int getCpuLogicCores() {
        return cpuLogicCores;
    }

    public void setCpuLogicCores(int cpuLogicCores) {
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

    public BigInteger getMemory() {
        return memory;
    }

    public void setMemory(BigInteger memory) {
        this.memory = memory;
    }

    public void setDiskSize(BigInteger diskSize) {
        this.diskSize = diskSize;
    }

    public int getDiskNum() {
        return diskNum;
    }

    public void setDiskNum(int diskNum) {
        this.diskNum = diskNum;
    }

    public List<DiskDetails> getDiskDetails() {
        return diskDetails;
    }

    public void setDiskDetails(List<DiskDetails> diskDetails) {
        this.diskDetails = diskDetails;
    }

    public List<DiskPartition> getDiskPartitions() {
        return diskPartitions;
    }

    public void setDiskPartitions(List<DiskPartition> diskPartitions) {
        this.diskPartitions = diskPartitions;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public BigInteger getDiskSize() {
        return diskSize;
    }

    public String getDiskDetailsString() {
        return diskDetailsString;
    }

    public void setDiskDetailsString(String diskDetailsString) {
        this.diskDetailsString = diskDetailsString;
    }

    public String getDiskPartitionsString() {
        return diskPartitionsString;
    }

    public void setDiskPartitionsString(String diskPartitionsString) {
        this.diskPartitionsString = diskPartitionsString;
    }

    /**
     * 将内存，磁盘总容量，单个磁盘容量，磁盘分区容量 字节转为MB
     */
    public void convertByte2KB(){
        BigInteger memory = this.getMemory().
                divide(new BigInteger(Constant.BYTE2KB));
        this.setMemory(memory);
        BigInteger diskSize = this.getDiskSize().
                divide(new BigInteger(Constant.BYTE2KB));
        this.setDiskSize(diskSize);

        for (DiskPartition diskPartition : this.getDiskPartitions()) {
            BigInteger partitionSize = diskPartition.getPartitionSize()
                    .divide(new BigInteger(Constant.BYTE2KB));
            diskPartition.setPartitionSize(partitionSize);
        }

        this.setDiskPartitionsString(
                Constant.gson.toJson(this.getDiskPartitions()));

        for (DiskDetails details : this.getDiskDetails()) {
            BigInteger diskVolue = details.getDiskVolue()
                    .divide(new BigInteger(Constant.BYTE2KB));
            details.setDiskVolue(diskVolue);
        }
        this.setDiskDetailsString(
                Constant.gson.toJson(this.getDiskDetails()));
    }

    /**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
    public String toString() {
        return "Device[hostName:" + this.hostName
                + " netCardNum:" + this.netCardNum
                + " cpuNum:" + this.cpuNum + " cpuType:" + this.cpuType
                + " cpuPyhsicalCores:" + getCpuPhysicalCores()
                + " cpuLogicCores:" + this.cpuLogicCores + " os:" + this.os
                + " kernal:" + this.kernal + " memory:" + this.memory
                + " diskNum" + this.diskNum + " diskSize:" + this.diskSize
                + " diskDetails:"+ this.diskDetails
                + " diskPartition:" + this.diskPartitions
                + " hbTime:" + this.dateTime + "]";
    }

    class DiskDetails {

        private String disk;

        private String diskType;

        private BigInteger diskVolue;

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

        public BigInteger getDiskVolue() {
            return diskVolue;
        }

        public void setDiskVolue(BigInteger diskVolue) {
            this.diskVolue = diskVolue;
        }

        public DiskDetails(String disk, String diskType, BigInteger diskVolue){
            this.disk = disk;
            this.diskType = diskType;
            this.diskVolue = diskVolue;
        }
    }

    class DiskPartition {

        private String name;

        private String partition;

        private BigInteger partitionSize;

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

        public BigInteger getPartitionSize() {
            return partitionSize;
        }

        public void setPartitionSize(BigInteger partitionSize) {
            this.partitionSize = partitionSize;
        }

        public DiskPartition(String name, String partition, BigInteger partitionSize) {
            this.name = name;
            this.partition = partition;
            this.partitionSize = partitionSize;
        }
    }
}
