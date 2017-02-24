package com.dataeye.omp.common;

import java.util.List;
import java.util.Map;

import com.dataeye.common.CachedObjects;
import com.dataeye.common.HttpStatusCode;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.utils.ServerUtils;
import com.google.gson.annotations.Expose;

/**
 * <pre>
 * 每个接口处理完的响应对象
 * @author Ivan          <br>
 * @date 2015年3月31日 下午7:35:55
 * <br>
 *
 */
public class DEResponse {
	/** http 状态码 */
	private HttpStatusCode httpStatusCode;
	/** 标识请求的唯一ID */
	@Expose
	private String ID;
	/** 状态码 */
	@Expose
	private int statusCode;
	/** 响应的内容 */
	@Expose
	private Object content = "";
	
	/** 概述指标 */
	@Expose
	private List<KeyValuePair> glance;
	/** 指标标题 */
	@Expose
	private Map<String, String> name;
	
	public DEResponse() {
		this.ID = ServerUtils.getResponseID();
		this.statusCode = StatusCode.SUCCESS;
		this.httpStatusCode = HttpStatusCode.SUCCESS;
	}

	public String getID() {
		return ID;
	}

	public void setHttpStatusCode(HttpStatusCode httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public HttpStatusCode getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public Object getContent() {
		return content;
	}

	/**
	 * @return the glance
	 */
	public List<KeyValuePair> getGlance() {
		return glance;
	}

	/**
	 * @param glance the glance to set
	 */
	public void setGlance(List<KeyValuePair> glance) {
		this.glance = glance;
	}

	/**
	 * @return the name
	 */
	public Map<String, String> getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(Map<String, String> name) {
		this.name = name;
	}

	/**
	 * 
	 * <pre>
	 * 把当前对象转换为json格式
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月8日 下午3:10:58
	 * <br>
	 */
	public String toJson() {
		return CachedObjects.GSON_ONLY_EXPOSE.toJson(this);
	}
}
