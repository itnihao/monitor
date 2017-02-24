package com.dataeye.omp.common;

import java.math.BigDecimal;

import com.google.gson.Gson;

/**
 * 特性对象
 * @author chenfanglin
 * @date 2016年1月26日 下午7:03:53
 */
public class Feature {
	private final static Gson gson = new Gson();
	/**
	 * 特性id
	 */
	private int fid;

	/**
	 * 监控的对象
	 */
	private String object;

	/**
	 * 所在的server内网IP
	 */
	private String client;

	/**
	 * 值
	 */
	private BigDecimal value;

	/**
	 * 时间戳
	 */
	private String time;

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String toJson() {
		return gson.toJson(this);
	}
}
