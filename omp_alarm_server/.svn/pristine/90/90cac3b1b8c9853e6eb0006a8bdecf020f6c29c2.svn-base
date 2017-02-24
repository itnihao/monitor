package com.dataeye.omp.detect.kafka.consume;

/**
 * 服务特性告警控制类
 * Created by wendy on 2016/4/29.
 */
public class DcAlarmControl {
    private int id;

    private int fId;

    private String object;

    private int serverId;

    private long reportTime;

    private long alarmTime;

    private long startTime;

    private long restoreTime;

    private int frequency;

    private int status;

    public DcAlarmControl(int fid, String object, int serverId) {
        this.fId=fid;
        this.object=object;
        this.serverId = serverId;
    }

    public DcAlarmControl(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getfId() {
        return fId;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public long getReportTime() {
        return reportTime;
    }

    public void setReportTime(long reportTime) {
        this.reportTime = reportTime;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getRestoreTime() {
        return restoreTime;
    }

    public void setRestoreTime(long restoreTime) {
        this.restoreTime = restoreTime;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[id:"+this.getId() +" serverID:"+this.getServerId()
                +" featureId"+this.getfId()+" reportTime:"+this.reportTime
                +" restoreTime:"+this.getRestoreTime()
                + " alarmTime "+this.alarmTime +" frequency:"+this.frequency
                +" status:"+this.status+"]";
    }
}
