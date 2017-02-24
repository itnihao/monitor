package com.dataeye.monitor.task;

import com.dataeye.monitor.common.constants.Constant;
import com.dataeye.monitor.utils.DateUtis;
import com.google.gson.annotations.Expose;

import java.math.BigDecimal;
import java.util.List;

/**
 * 机器上报特性
 * Created by wendy on 2016/6/24.
 */
public class Feature {

    @Expose
    private List<Feature> feature_list;

    private Integer fid;

    private String object;

    private String client;

    private BigDecimal value;

    private String time;


    public Feature(Integer fid
            , String object
            , BigDecimal value) {
        this.fid = fid;
        this.object = object;
        this.value = value;
        this.setClient(Constant.IP);
        this.setTime(DateUtis.currentTime());
    }

    public Feature() {
    }


    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
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

    public List<Feature> getFeature_list() {
        return feature_list;
    }

    public void setFeature_list(List<Feature> feature_list) {
        this.feature_list = feature_list;
    }

    public void addFeature(Feature feature) {
        this.getFeature_list().add(feature);
    }
}
