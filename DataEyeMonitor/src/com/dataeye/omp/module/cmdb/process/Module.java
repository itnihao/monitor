package com.dataeye.omp.module.cmdb.process;

import com.google.gson.annotations.Expose;

/**
 * @auther wendy
 * @since 2016/3/5 17:30
 */
public class Module {

    /**业务ID*/
    @Expose
    private int busiID;

    /**业务名称*/
    @Expose
    private String busiName;

    /**模块ID*/
    @Expose
    private int moduleID;

    /**模块名称*/
    @Expose
    private String moduleName;

    /**进程数量*/
    @Expose
    private int processNum;

    /**主要负责人*/
    @Expose
    private String mainPrincipal;


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

    public String getBusiName() {
        return busiName;
    }

    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }



    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getProcessNum() {
        return processNum;
    }

    public void setProcessNum(int processNum) {
        this.processNum = processNum;
    }

    public String getMainPrincipal() {
        return mainPrincipal;
    }

    public void setMainPrincipal(String mainPrincipal) {
        this.mainPrincipal = mainPrincipal;
    }
}
