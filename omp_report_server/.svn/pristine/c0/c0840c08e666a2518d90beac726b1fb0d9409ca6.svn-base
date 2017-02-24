package com.dataeye.omp.report.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 参数类
 * @auther wendy
 * @since 2016/1/8 17:21
 */
public class Feature {
    /**
     * 特性id
     */
    private int fid;

    /**
     * 监控的对象
     */
    private String object;

    /**
     * 所在的server内网IP
     */
    private String client;

    /**
     * 值
     */
    private BigDecimal value;

    /**
     * 时间戳
     */
    private String time;

    private List<Feature> feature_list;

    public List<Feature> getFeature_list() {
        return feature_list;
    }

    public void setFeature_list(List<Feature> feature_list) {
        this.feature_list = feature_list;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "[fid:" + fid + " object:" + object + " client:" +
                client + " time:" + time + " value:" + value+ "]";
    }
}
