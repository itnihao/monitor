package com.dataeye.omp.sso;

import java.util.List;

/**
 * 从统一登录服务取出的用户所有的app信息
 * 
 * @author ivantan
 *
 */
public class AppInfoSso {
	private String appID;
	private String appName;
	private int appType;
	private int gameType;
	private int gameType2;
	private int createrUID;
	private List<Integer> component;

	public AppInfoSso() {
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public int getGameType2() {
		return gameType2;
	}

	public void setGameType2(int gameType2) {
		this.gameType2 = gameType2;
	}

	public int getCreaterUID() {
		return createrUID;
	}

	public void setCreaterUID(int createrUID) {
		this.createrUID = createrUID;
	}

	public List<Integer> getComponent() {
		return component;
	}

	public void setComponent(List<Integer> component) {
		this.component = component;
	}

	@Override
	public String toString() {
		return "AppInfoSso [appID=" + appID + ", appName=" + appName + ", appType=" + appType + ", gameType=" + gameType
				+ ", gameType2=" + gameType2 + ", createrUID=" + createrUID + ", component=" + component + "]";
	}

}
