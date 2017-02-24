package com.dataeye.omp.module.cmdb.process;

import com.google.gson.annotations.Expose;

/**
 * @author wendy
 * @since 2016/3/5 17:29
 */
public class ProcessInfo {

    /**进程ID*/
    @Expose
    private int id;

    /**进程名称*/
    @Expose
    private String processName;

    /**端口*/
    @Expose
    private int port;

    /**业务ID*/
    private int busiID;

    /**业务名称*/
    @Expose
    private String busiName;

    /**模块ID*/
    private int moduleID;

    /**模块名称*/
    @Expose
    private String moduleName;

    /**服务器ID*/
    private int serveID;

    /**服务器名称*/
    @Expose
    private String serverName;

    /**内网IP*/
    @Expose
    private String privateIp;

    /**部署路径*/
    private String deployPath;

    /**配置文件路径*/
    private String configPath;

    /**日志路径*/
    private String logsPath;

    /**主要负责人*/
    private int mainPrincipal;

    /**备份负责人ID,多个*/
    private String bakPrincipal;

    /**进程状态*/
    @Expose
    private int processStatus;

    /**添加修改标志位 0为添加  1 为修改*/
    @Expose
    private int addEditFlag;

    /**监控状态  0暂停， 1启动*/
    @Expose
    private int monitorStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public int getServeID() {
        return serveID;
    }

    public void setServeID(int serveID) {
        this.serveID = serveID;
    }

    public String getDeployPath() {
        return deployPath;
    }

    public void setDeployPath(String deployPath) {
        this.deployPath = deployPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getLogsPath() {
        return logsPath;
    }

    public void setLogsPath(String logsPath) {
        this.logsPath = logsPath;
    }

    public int getMainPrincipal() {
        return mainPrincipal;
    }

    public void setMainPrincipal(int mainPrincipal) {
        this.mainPrincipal = mainPrincipal;
    }

    public String getBakPrincipal() {
        return bakPrincipal;
    }

    public void setBakPrincipal(String bakPrincipal) {
        this.bakPrincipal = bakPrincipal;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public int getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(int monitorStatus) {
        this.monitorStatus = monitorStatus;
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

    public int getAddEditFlag() {
        return addEditFlag;
    }

    public void setAddEditFlag(int addEditFlag) {
        this.addEditFlag = addEditFlag;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }
}
