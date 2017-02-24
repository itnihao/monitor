package com.dataeye.omp.module.monitor.server;

import com.google.gson.annotations.Expose;

import java.math.BigInteger;

/**
 * 磁盘分区信息
 * @auther wendy
 * @since 2016/1/11 21:41
 */
    public class DiskPartition {

    /**名称*/
    @Expose
    private String name;

    /**分区*/
    @Expose
    private String partition;

    /**磁盘分区大小*/
    @Expose
    private BigInteger partitionSize;

    /**磁盘分区使用量*/
    @Expose
    private Long used;

    /**磁盘分组使用率*/
    @Expose
    private double usage;

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

    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
    }

    public double getUsage() {
        return usage;
    }

    public void setUsage(double usage) {
        this.usage = usage;
    }
}
