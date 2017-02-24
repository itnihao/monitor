package com.dataeye.omp.module.cmdb.device;

import com.google.gson.annotations.Expose;

/**
 * IP信息
 * @auther wendy
 * @since 2015/12/15 17:28
 */
public class DeviceIP {
    /**
     *设备ID
     */
    @Expose
    private long devId;

    /**
     * IP
     */
    @Expose
    private String ip;

    /**
     * 运营商ID
     */
    @Expose
    private long ispId;

    /**运营商名称*/
    @Expose
    private String ispName;

    /**
     * ip类型 0：内网IP， 1：外网IP
     */
    @Expose
    private long type;

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getDevId() {
        return devId;
    }

    public void setDevId(long devId) {
        this.devId = devId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getIspId() {
        return ispId;
    }

    public void setIspId(long ispId) {
        this.ispId = ispId;
    }

    public String getIspName() {
        return ispName;
    }

    public void setIspName(String ispName) {
        this.ispName = ispName;
    }
}
