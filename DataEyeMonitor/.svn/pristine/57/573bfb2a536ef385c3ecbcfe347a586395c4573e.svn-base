package com.dataeye.omp.module.monitor.server;

import java.util.List;

import com.dataeye.omp.common.KeyValue;
import com.google.gson.annotations.Expose;

/**
 * 服务器
 * 
 * @author chenfanglin
 * @date 2016年1月6日 上午10:19:17
 */
public class Machine {

	// 主机ID
	@Expose
	private Integer serverID;
	
	// 主机名
	@Expose
	private String hostName;
	
	// ip
	@Expose
	private List<KeyValue<String,Integer>> ip;

	// 机房
	@Expose
	private String machineRoom;

	// 业务
	@Expose
	private String business;

	// cpu使用率
	@Expose
	private String cpuUsage;

	// 5分钟负载
	@Expose
	private double fiveLoad;

	// 状态
	@Expose
	private int status;

	@Expose
	private int diskNum;

	public int getDiskNum() {
		return diskNum;
	}

	public void setDiskNum(int diskNum) {
		this.diskNum = diskNum;
	}

	/**
	 * @return the serverID
	 */
	public Integer getServerID() {
		return serverID;
	}

	/**
	 * @param serverID
	 *            the serverID to set
	 */
	public void setServerID(Integer serverID) {
		this.serverID = serverID;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the ip
	 */
	public List<KeyValue<String, Integer>> getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(List<KeyValue<String, Integer>> ip) {
		this.ip = ip;
	}

	/**
	 * @return the machineRoom
	 */
	public String getMachineRoom() {
		return machineRoom;
	}

	/**
	 * @param machineRoom
	 *            the machineRoom to set
	 */
	public void setMachineRoom(String machineRoom) {
		this.machineRoom = machineRoom;
	}

	/**
	 * @return the business
	 */
	public String getBusiness() {
		return business;
	}

	/**
	 * @param business
	 *            the business to set
	 */
	public void setBusiness(String business) {
		this.business = business;
	}

	/**
	 * @return the cpuUsage
	 */
	public String getCpuUsage() {
		return cpuUsage;
	}

	/**
	 * @param cpuUsage
	 *            the cpuUsage to set
	 */
	public void setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public double getFiveLoad() {
		return fiveLoad;
	}

	public void setFiveLoad(double fiveLoad) {
		this.fiveLoad = fiveLoad;
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
}
