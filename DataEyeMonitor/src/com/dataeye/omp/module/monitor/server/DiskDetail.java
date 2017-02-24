package com.dataeye.omp.module.monitor.server;

import com.google.gson.annotations.Expose;

/**
 * @auther wendy
 * @since 2016/3/17 18:42
 */
public class DiskDetail {

    /**磁盘名称*/
    @Expose
    private String disk;

    /**磁盘类型*/
    @Expose
    private String diskType;

    /**磁盘容量*/
    @Expose
    private String diskVolue;

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
