package com.dataeye.omp.common;

import com.google.gson.annotations.Expose;

/**
 * 告警历史
 * @author chenfanglin
 * @date 2016年1月23日 下午4:25:32
 */
public class AlarmHistory {

	@Expose
	private long id;
	
	// 特性id
	@Expose
	private Integer featureID;
	
	// 特性对象
	@Expose
	private String object;
	
	//机器ID
	@Expose
	private Integer serverID; 

	// 最近上报时间
	@Expose
	private long reporteTime;
	
	// 最近发送告警时间
	@Expose
	private long alarmTime;
	
	// 第一次发送告警时间
	@Expose
	private long startTime;
	
	// 恢复时间
	@Expose
	private long restoreTime;
	
	// 告警次数
	@Expose
	private int frequency;
	
	// 状态 0正常 1不正常
	@Expose
	private int status;
	/**
	 * @return the serverID
	 */
	public Integer getServerID() {
		return serverID;
	}

	/**
	 * @param serverID the serverID to set
	 */
	public void setServerID(Integer serverID) {
		this.serverID = serverID;
	}

	/**
	 * @return the reporteTime
	 */
	public long getReporteTime() {
		return reporteTime;
	}

	/**
	 * @param reporteTime the reporteTime to set
	 */
	public void setReporteTime(long reporteTime) {
		this.reporteTime = reporteTime;
	}

	/**
	 * @return the alarmTime
	 */
	public long getAlarmTime() {
		return alarmTime;
	}

	/**
	 * @param alarmTime the alarmTime to set
	 */
	public void setAlarmTime(long alarmTime) {
		this.alarmTime = alarmTime;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the restoreTime
	 */
	public long getRestoreTime() {
		return restoreTime;
	}

	/**
	 * @param restoreTime the restoreTime to set
	 */
	public void setRestoreTime(long restoreTime) {
		this.restoreTime = restoreTime;
	}

	/**
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the featureID
	 */
	public Integer getFeatureID() {
		return featureID;
	}

	/**
	 * @param featureID the featureID to set
	 */
	public void setFeatureID(Integer featureID) {
		this.featureID = featureID;
	}

	/**
	 * @return the object
	 */
	public String getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(String object) {
		this.object = object;
	}
	
}
