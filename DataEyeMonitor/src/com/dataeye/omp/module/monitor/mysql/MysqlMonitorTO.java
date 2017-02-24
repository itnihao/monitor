package com.dataeye.omp.module.monitor.mysql;


import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;

public class MysqlMonitorTO {

    //数据库id
    private int id;

    //mysql打开文件数阀值
    @Expose
    private int openFile;

    //mysql连接数阀值
    @Expose
    private int connection;

    //mysql锁数量阀值
    @Expose
    private int lock;

    //mysql锁时长阀值
    @Expose
    private int lockTime;

    //mysql慢sql数阀值
    @Expose
    private int slowSql;

    //mysql告警规则最大次数
    @Expose
    private int maxFrequency;

    //mysql告警主要负责人id
    @Expose
    private int mainPrincipal;

    //mysql告警其他负责人id  格式 ： 11,22
    @Expose
    private String bakPrincipal;

    //mysql告警类型 0 邮件，1短信，2 APP推送  格式 ：０,１,２
    @Expose
    private String alarmType;

    //mysql告警间隔
    @Expose
    private int alarmInterval;

    @Expose
    private String hostName;

    @Expose
    private String privateIp;

    @Expose
    private String busiName;

    @Expose
    private int serverID;

    @Expose
    private int port;

    @Expose
    private int isRuleExists;

    //创建时间
    private String createTime;

    //修改时间
    private String updateTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpenFile() {
        return openFile;
    }

    public void setOpenFile(int openFile) {
        this.openFile = openFile;
    }

    public int getConnection() {
        return connection;
    }

    public void setConnection(int connection) {
        this.connection = connection;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public int getLockTime() {
        return lockTime;
    }

    public void setLockTime(int lockTime) {
        this.lockTime = lockTime;
    }

    public int getSlowSql() {
        return slowSql;
    }

    public void setSlowSql(int slowSql) {
        this.slowSql = slowSql;
    }

    public int getMaxFrequency() {
        return maxFrequency;
    }

    public void setMaxFrequency(int maxFrequency) {
        this.maxFrequency = maxFrequency;
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

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public int getAlarmInterval() {
        return alarmInterval;
    }

    public void setAlarmInterval(int alarmInterval) {
        this.alarmInterval = alarmInterval;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    public String getBusiName() {
        return busiName;
    }

    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getIsRuleExists() {
        return isRuleExists;
    }

    public void setIsRuleExists(int isRuleExists) {
        this.isRuleExists = isRuleExists;
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }
}
