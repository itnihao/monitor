package com.dataeye.omp.module.alarm;

import com.dataeye.common.CachedObjects;
import com.google.gson.annotations.Expose;

import java.math.BigDecimal;
import java.util.List;

/**
 * 告警对象
 * 
 * @author chenfanglin
 * @date 2016年1月23日 下午4:22:22
 */
public class AlarmObject {

	/** 机器id （一次可设置多个）*/
	@Expose
	private List<Integer> servers;

	/** 业务id */
	@Expose
	private int businessID;

	/**  模块id  */
	@Expose
	private int moduleID;

	/**  分组id  */
	@Expose
	private int groupID;

	/**告警对象类型 */
	@Expose
	private int alarmObjectType;

	public List<Integer> getServers() {
		return servers;
	}

	public void setServers(List<Integer> servers) {
		this.servers = servers;
	}

	public int getBusinessID() {
		return businessID;
	}

	public void setBusinessID(int businessID) {
		this.businessID = businessID;
	}

	public int getModuleID() {
		return moduleID;
	}

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
		return CachedObjects.GSON_ONLY_EXPOSE.toJson(this);
	}


	public int getAlarmObjectType() {
		return alarmObjectType;
	}

	public void setAlarmObjectType(int alarmObjectType) {
		this.alarmObjectType = alarmObjectType;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
}
