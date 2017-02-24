package com.dataeye.omp.module.cmdb.process;

/**
 * 进程告警规则
 * @author wendy
 * @since 2016/3/7 15:51
 */
public class ProcessRule {
    private int id;

    private int processID;

    private int monitorType;

    private int alarmInterval;

    private int maxFrequency;

    private String alarmType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public int getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(int monitorType) {
        this.monitorType = monitorType;
    }

    public int getAlarmInterval() {
        return alarmInterval;
    }

    public void setAlarmInterval(int alarmInterval) {
        this.alarmInterval = alarmInterval;
    }

    public int getMaxFrequency() {
        return maxFrequency;
    }

    public void setMaxFrequency(int maxFrequency) {
        this.maxFrequency = maxFrequency;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }
}
