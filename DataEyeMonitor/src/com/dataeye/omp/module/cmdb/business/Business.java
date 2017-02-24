package com.dataeye.omp.module.cmdb.business;

import com.google.gson.annotations.Expose;

import java.sql.Date;
import java.util.List;

/**
 * 业务/模块信息
 * @auther wendy
 * @since 2015/12/27 15:01
 */
public class Business {

    @Expose
    private int id;

    @Expose
    //业务或模块名称
    private String name;

    @Expose
    //父级业务ID
    private int pid;

    @Expose
    //运维负责人
    private String omPerson;

    @Expose
    //运维负责人名称
    private String omPersonName;

    @Expose
    //创建时间
    private Date time;

    @Expose
    //模块数
    private int moduleNum;

    @Expose
    //业务对应的模块列表
    private List<Business> moduleList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getOmPerson() {
        return omPerson;
    }

    public void setOmPerson(String omPerson) {
        this.omPerson = omPerson;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<Business> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<Business> moduleList) {
        this.moduleList = moduleList;
    }

    public int getModuleNum() {
        return moduleNum;
    }

    public void setModuleNum(int moduleNum) {
        this.moduleNum = moduleNum;
    }

    public String getOmPersonName() {
        return omPersonName;
    }

    public void setOmPersonName(String omPersonName) {
        this.omPersonName = omPersonName;
    }
}
