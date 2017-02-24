package com.dataeye.omp.module.monitor.server;

import com.google.gson.annotations.Expose;

/**
 * 分组
 * @author chenfanglin
 * @date 2016年1月13日 下午4:45:32
 */
public class Group {

	@Expose
	private Integer groupID;

	@Expose
	private Integer uid;

	@Expose
	private String groupName;

	/**
	 * @return the groupID
	 */
	public Integer getGroupID() {
		return groupID;
	}

	/**
	 * @param groupID
	 *            the groupID to set
	 */
	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
}
