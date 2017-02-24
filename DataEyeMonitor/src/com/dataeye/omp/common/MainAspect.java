package com.dataeye.omp.common;

import com.dataeye.exception.CustomMessageException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import com.dataeye.common.CachedObjects;
import com.dataeye.common.HttpStatusCode;
import com.dataeye.common.PrivilegeHandler;
import com.dataeye.common.ResourceHandler;
import com.dataeye.exception.ClientException;
import com.dataeye.exception.ServerException;
import com.dataeye.utils.DateUtils;
import com.dataeye.utils.LogUtils;
import com.dataeye.utils.ServerUtils;
import com.dataeye.utils.Tracker;

/**
 * 
 * <pre>
 * 切面环绕每个接口方法逻辑,
 * @author Ivan          <br>
 * @date 2015年3月31日 下午7:09:47 <br>
 * @version 1.0
 * <br>
 */
@Aspect
@Service
public class MainAspect {
	/**
	 * 
	 * <pre>
	 * 给接口请求加切面
	 *  @param jp
	 *  @throws Throwable  
	 *  @author Ivan<br>
	 *  @date 2015年1月4日 下午6:02:26
	 * <br>
	 */
	@Around(value = "execution(public java.lang.Object com.dataeye.omp.module..*.*(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse) throws java.lang.Exception)")
	public void aroundMethod(ProceedingJoinPoint jp) throws Throwable {
		long startTime = DateUtils.winTimestamp();
		DEContext context = new DEContext(jp);
		DEResponse deResponse = context.getDeResponse();
		String lang = context.getLang();
		try {
			// 进行实际方法调用之前，先进行权限检查
			PrivilegeHandler.check(context);
			Object obj = jp.proceed();
			obj = obj == null ? CachedObjects.EMPTY_LIST : obj;
			deResponse.setContent(obj);
		} catch (ClientException clientException) {
			deResponse.setHttpStatusCode(HttpStatusCode.CLIENT_EXCEPTION);
			deResponse.setStatusCode(clientException.getStatusCode());
			deResponse.setContent(ResourceHandler.getProperties(deResponse.getStatusCode() + "", lang));
		} catch (ServerException serverException) {
			deResponse.setHttpStatusCode(HttpStatusCode.SERVER_EXCEPTION);
			deResponse.setStatusCode(serverException.getStatusCode());
			deResponse.setContent(ResourceHandler.getProperties(deResponse.getStatusCode() + "", lang));
		} catch (CustomMessageException ex) {
			deResponse.setHttpStatusCode(HttpStatusCode.SERVER_EXCEPTION);
			deResponse.setStatusCode(ex.getStatusCode());
			deResponse.setContent(ex.getMessage());
		}
		finally {// 将DEResponse对象返回
			String responseJson = deResponse.toJson();
			ServerUtils.writeToResponse(context.getResponse(), deResponse.getHttpStatusCode(), responseJson);
			DEParameter p = context.getDeParameter();
			LogUtils.logRequest(context.getID(), p.getDoName(), context.getClassName() + "." + context.getMethod(),
					p.getUrl(), responseJson, DateUtils.winTimestamp() - startTime);
			Tracker.Finish(context.getID());
		}
	}
	
	/**
	 * 图表 需要以另一种方式返回数据
	 * @param jp
	 * @throws Throwable
	 * @author chenfanglin <br>
	 * @date 2016年1月18日 下午7:38:07
	 */
	@SuppressWarnings("null")
	@Around(value = "execution(public com.dataeye.omp.common.DEResponse com.dataeye.omp.module.monitor..*.*(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse) throws java.lang.Exception)")
	public void aroundReportMethod(ProceedingJoinPoint jp) throws Throwable {
		long startTime = DateUtils.winTimestamp();
		DEContext context = new DEContext(jp);
		DEResponse deResponse = null;
		String lang = context.getLang();
		try {
			// 进行实际方法调用之前，先进行权限检查
			PrivilegeHandler.check(context);
			deResponse = (DEResponse) jp.proceed();
			deResponse = (DEResponse) (deResponse == null ? CachedObjects.EMPTY_LIST : deResponse);
		} catch (ClientException clientException) {
			deResponse.setHttpStatusCode(HttpStatusCode.CLIENT_EXCEPTION);
			deResponse.setStatusCode(clientException.getStatusCode());
			deResponse.setContent(ResourceHandler.getProperties(deResponse.getStatusCode() + "", lang));
		} catch (ServerException serverException) {
			deResponse.setHttpStatusCode(HttpStatusCode.SERVER_EXCEPTION);
			deResponse.setStatusCode(serverException.getStatusCode());
			deResponse.setContent(ResourceHandler.getProperties(deResponse.getStatusCode() + "", lang));
		} finally {// 将DEResponse对象返回
			String responseJson = deResponse.toJson();
			ServerUtils.writeToResponse(context.getResponse(), deResponse.getHttpStatusCode(), responseJson);
			DEParameter p = context.getDeParameter();
			LogUtils.logRequest(context.getID(), p.getDoName(), context.getClassName() + "." + context.getMethod(),
					p.getUrl(), responseJson, DateUtils.winTimestamp() - startTime);
			Tracker.Finish(context.getID());
		}
	}
}