package com.dataeye.omp.common;

import com.dataeye.utils.Tracker;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <pre>
 * DataEye Context
 * @author Ivan          <br>
 * @date 2015年3月31日 下午7:23:00
 * <br>
 *
 */
public class DEContext {
	/** 请求的ID */
	private String ID;
	/** request 对象 */
	private HttpServletRequest request;
	/** response 对象 */
	private HttpServletResponse response;
	/** session 对象 */
	private HttpSession session;
	/** 请求的类名 */
	private String className;
	/** 请求的方法名 */
	private String method;
	/** 参数处理的对象 */
	private DEParameter deParameter;
	/** 接口响应对象 */
	private DEResponse deResponse;

	public DEContext(ProceedingJoinPoint jp) {
		// 设置request/response/session对象
		this.request = (HttpServletRequest) jp.getArgs()[0];
		this.response = (HttpServletResponse) jp.getArgs()[1];
		this.session = this.request.getSession();
		// 设置类名和方法名
		this.className = jp.getTarget().getClass().getSimpleName();
		this.method = jp.getSignature().getName();
		// 设置DeResponse
		this.deResponse = new DEResponse();
		this.ID = this.deResponse.getID();
		this.deParameter = new DEParameter(this.request, this.deResponse.getID());
		// 把DEContext对象设置到Request属性中
		this.request.setAttribute("CTX", this);
		// 记录该ID的一个跟踪开始
		Tracker.add(ID, "START");
	}

	// *********************getters and setters******************************
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpSession getSession() {
		return session;
	}

	public String getClassName() {
		return className;
	}

	public String getMethod() {
		return method;
	}

	public DEParameter getDeParameter() {
		return deParameter;
	}

	public DEResponse getDeResponse() {
		return deResponse;
	}

	public String getLang() {
		return deParameter.getLang();
	}
}
