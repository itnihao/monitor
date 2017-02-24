package com.dataeye.omp.common;
import java.math.BigInteger;
import java.util.List;

/**
 * 设备信息
 * @auther wendy
 * @since 2016/1/11 21:37
 */
public class Device {

    //设备ID
    private int id;

    //业务ID
    private int busiID;

    //模块ID
    private int moduleID;

    //分组ID
    private int groupID;

    //机器名
    private String hostName;

    //内网Ip
    private String privateIp;

    //内存大小
    private BigInteger memory;

    //磁盘信息
    private List<DiskDetails> diskDetails;

    //分区信息
    private List<DiskPartition> diskPartitions;

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


    public int getBusiID() {
        return busiID;
    }

    public void setBusiID(int busiID) {
        this.busiID = busiID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    public BigInteger getMemory() {
        return memory;
    }

    public void setMemory(BigInteger memory) {
        this.memory = memory;
    }

    @Override
    public String toString() {
        return "Device[id:" + id + " hostName:" + this.hostName +
                " ip:" + this.privateIp;
    }
}
