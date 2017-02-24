package com.dataeye.omp.module.monitor.custom;


import com.google.gson.annotations.Expose;
import com.kenai.jaffl.annotations.In;

import java.util.List;
import java.util.Map;

/**
 * 自定义监控项
 */
public class CustomMonitor {

    //自定义监控项id
    @Expose
    private int id;

    //自定义监控项(唯一)
    @Expose
    private String monitorItem;

    //主要负责人id
    @Expose
    private int mainPrincipal;

    //主要负责人姓名
    @Expose
    private String mainPrincipalName;

    //备份负责人id(1,2,3)
    @Expose
    private String bakPrincipal;

    //备份负责人姓名[{"id":"1","name":"james"}]
    @Expose
    private List<Map<String, String>> bakPrincipalNames;

    //所属业务id
    @Expose
    private int business;

    //所属业务名字
    @Expose
    private String businessName;

    //创建时间
    @Expose
    private long createTime;

    //更新时间
    @Expose
    private long updateTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonitorItem() {
        return monitorItem;
    }

    public void setMonitorItem(String monitorItem) {
        this.monitorItem = monitorItem;
    }

    public int getMainPrincipal() {
        return mainPrincipal;
    }

    public void setMainPrincipal(int mainPrincipal) {
        this.mainPrincipal = mainPrincipal;
    }

    public String getMainPrincipalName() {
        return mainPrincipalName;
    }

    public void setMainPrincipalName(String mainPrincipalName) {
        this.mainPrincipalName = mainPrincipalName;
    }

    public String getBakPrincipal() {
        return bakPrincipal;
    }

    public void setBakPrincipal(String bakPrincipal) {
        this.bakPrincipal = bakPrincipal;
    }

    public List<Map<String, String>> getBakPrincipalNames() {
        return bakPrincipalNames;
    }

    public void setBakPrincipalNames(List<Map<String, String>> bakPrincipalNames) {
        this.bakPrincipalNames = bakPrincipalNames;
    }

    public int getBusiness() {
        return business;
    }

    public void setBusiness(int business) {
        this.business = business;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
