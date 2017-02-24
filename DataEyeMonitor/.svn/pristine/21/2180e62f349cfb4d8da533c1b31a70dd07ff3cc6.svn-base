package com.dataeye.omp.module.alarm.customize;

/**
 * Created by mr on 2016/3/5.
 */

import com.google.gson.annotations.Expose;

/**
 * 自定义告警规则
 */
public class CustomizeAlarmRule {

    //告警规则id
    @Expose
    private long id;

    //自定义告警项id
    @Expose
    private long customizeAlarmId;

    //告警间隔 单位 min
    @Expose
    private int alarmInterval;

    //最大告警次数
    @Expose
    private int maxFrequency;

    //告警恢复类型  0--恢复间隔时间一到自动恢复   1--收到上报恢复信息恢复
    @Expose
    private int restoreType;

    //恢复间隔时间
    @Expose
    private int restoreInterval;

    //告警类型  0--邮件，1--短信，2--APP推送
    @Expose
    private String alarmType;

    //告警规则创建时间
    @Expose
    private long createTime;

    //告警规则更新时间
    @Expose
    private long updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomizeAlarmId() {
        return customizeAlarmId;
    }

    public void setCustomizeAlarmId(long customizeAlarmId) {
        this.customizeAlarmId = customizeAlarmId;
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

    public int getRestoreType() {
        return restoreType;
    }

    public void setRestoreType(int restoreType) {
        this.restoreType = restoreType;
    }

    public int getRestoreInterval() {
        return restoreInterval;
    }

    public void setRestoreInterval(int restoreInterval) {
        this.restoreInterval = restoreInterval;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
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
