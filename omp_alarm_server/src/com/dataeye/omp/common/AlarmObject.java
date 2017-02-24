package com.dataeye.omp.common;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

/**
 * 告警对象
 * 
 * @author chenfanglin
 * @date 2016年1月23日 下午4:22:22
 */
public class AlarmObject {
	private final static Gson gson = new Gson();
	// 机器id
	@Expose
	private List<Integer> servers;

	// 业务id
	@Expose
	private int businessID;

	// 模块id
	@Expose
	private int moduleID;
	
	@Expose
	private int groupID;
	
	@Expose
	private int alarmObjectType;


	/**
	 * @return the servers
	 */
	public List<Integer> getServers() {
		return servers;
	}

	/**
	 * @param servers the servers to set
	 */
	public void setServers(List<Integer> servers) {
		this.servers = servers;
	}

	/**
	 * @return the businessID
	 */
	public int getBusinessID() {
		return businessID;
	}

	/**
	 * @param businessID
	 *            the businessID to set
	 */
	public void setBusinessID(int businessID) {
		this.businessID = businessID;
	}

	/**
	 * @return the moduleID
	 */
	public int getModuleID() {
		return moduleID;
	}

	/**
	 * @param moduleID
	 *            the moduleID to set
	 */
	public void setModuleID(int moduleID) {
		this.moduleID = moduleID;
	}
	/**
	 * 把当前对象转换为json格式
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月25日 下午7:45:34
	 */
	public String toJson() {
		return gson.toJson(this);
	}
	
	/**
	 * @return the alarmObjectType
	 */
	public int getAlarmObjectType() {
		return alarmObjectType;
	}

	/**
	 * @return the groupID
	 */
	public int getGroupID() {
		return groupID;
	}

	/**
	 * @param groupID the groupID to set
	 */
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	/**
	 * @param alarmObjectType the alarmObjectType to set
	 */
	public void setAlarmObjectType(int alarmObjectType) {
		this.alarmObjectType = alarmObjectType;
	}

	public static AlarmObject parseJson(String json){
		return gson.fromJson(json, AlarmObject.class);
	}
}
