package com.dataeye.omp.common;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * 返回K-V键对
 * @author xieco
 * @date 2015-7-14下午5:17:54
 */
public class KeyValuePair implements Serializable{
	@Expose
	private String k;
	@Expose
	private Object V;
	
	public KeyValuePair() {
		super();
	}
	public KeyValuePair(String k, Object v) {
		super();
		this.k = k;
		V = v;
	}
	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}
	public Object getV() {
		return V;
	}
	public void setV(Object v) {
		V = v;
	}
}