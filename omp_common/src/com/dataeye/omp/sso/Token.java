package com.dataeye.omp.sso;

/**
 * Token
 * @author chenfanglin
 * @date 2016年4月19日 下午6:02:12
 */
public class Token {
	/** 用户标识 */
	private String UID;
	/** 生成的token */
	private String token;
	/** 更新时间 */
	private int updateTime;
	/** 过期时间 */
	private int expireTime;

	public Token() {
	}

	public Token(String UID, String token, int updateTime, int expireTime) {
		this.UID = UID;
		this.token = token;
		this.updateTime = updateTime;
		this.expireTime = expireTime;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(int updateTime) {
		this.updateTime = updateTime;
	}

	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

}
