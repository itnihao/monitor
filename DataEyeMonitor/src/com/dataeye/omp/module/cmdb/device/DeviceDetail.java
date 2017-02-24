package com.dataeye.omp.module.cmdb.device;

/**
 * @auther wendy
 * @since 2016/1/26 8:47
 */
public class DeviceDetail {

    private String  diskDir;

    private String diskType;

    private String diskVolue;

    public String getDiskVolue() {
        return diskVolue;
    }

    public void setDiskVolue(String diskVolue) {
        this.diskVolue = diskVolue;
    }

    public String getDiskDir() {
        return diskDir;
    }

    public void setDiskDir(String diskDir) {
        this.diskDir = diskDir;
    }

    public String getDiskType() {
        return diskType;
    }

    public void setDiskType(String diskType) {
        this.diskType = diskType;
    }
}
