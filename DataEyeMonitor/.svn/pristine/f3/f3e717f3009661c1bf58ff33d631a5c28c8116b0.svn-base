package com.dataeye.omp.module.cmdb.user;

import java.util.HashMap;
import java.util.Map;

import com.dataeye.omp.sso.AppInfoSso;

/**
 * 放在Session中的User对象
 * 
 * @author Ivan <br>
 * @date 2015年8月11日 下午5:45:43 <br>
 *
 */
public class User {
	private int uid;
	private String userName;
	private String token;
	/** 缓存当前用户的appid-AppInfoSso */
	private Map<String, AppInfoSso> appMap = new HashMap<String, AppInfoSso>();

	public User(int uid, String userName) {
		this.uid = uid;
		this.userName = userName;
	}

	public int getUid() {
		return uid;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public Map<String, AppInfoSso> getAppMap() {
		return appMap;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setAppMap(Map<String, AppInfoSso> appMap) {
		this.appMap = appMap;
	}
	
	
}
