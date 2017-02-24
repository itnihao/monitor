package com.dataeye.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;

import com.dataeye.common.PrivilegeControl.Scope;
import com.dataeye.exception.ClientException;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.constant.Constant.ServerCfg;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.utils.ApplicationContextUtil;
import com.dataeye.utils.LogUtils;
import com.dataeye.utils.ServerUtils;
import com.dataeye.utils.Tracker;
import com.dataeye.utils.ValidateUtils;

/**
 * <pre>
 * 权限处理器
 * @author Ivan          <br>
 * @date 2015年4月8日 下午8:26:49
 * <br>
 *
 */
public class PrivilegeHandler {
	/** interface of public scope */
	public static final Set<String> SCOPE_PUBLIC = new HashSet<String>();
	/** interface of after login scope */
	public static final Set<String> SCOPE_AFTER_LOGIN = new HashSet<String>();
	/** interface of after auth */
	public static final Set<String> SCOPE_AFTER_AUTH = new HashSet<String>();
	/** interface of fix ip */
	public static final Set<String> SCOPE_FIX_IP = new HashSet<String>();
	/** interface of write */
	public static final Set<String> WRITE = new HashSet<String>();
	/** 权限和接口的对应关系 */
	public static HashMap<String, Set<String>> PRIV_DESC_INTERFACES_MAP = new HashMap<String, Set<String>>();

	/**
	 * 
	 * <pre>
	 * 加载权限配置 
	 * @author Ivan<br>
	 * @date 2015年4月8日 下午8:27:49
	 * <br>
	 */
	public static void load() {
		String[] beans = ApplicationContextUtil.getAllBeans();
		for (String bean : beans) {// 依次遍历每个bean
			Object object = ApplicationContextUtil.getBean(bean);
			for (Method method : object.getClass().getSuperclass().getMethods()) {// 遍历这个bean里面所有的方法
				/** 尝试取 @see RequestMapping PrivilegeControl 这两个注解 */
				RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
				PrivilegeControl accessControl = method.getAnnotation(PrivilegeControl.class);
				if (ValidateUtils.isNotNull(requestMapping) && ValidateUtils.isNotNull(accessControl)) {// 如果都不是null
					// 获取请求url,解析出具体的接口
					String requestUri = requestMapping.value()[0];
					// requetUri就是具体的接口如/test/test.do,下面解析出test.do部分
					String doName = ServerUtils.getBasename(requestUri);
					if (ValidateUtils.isNotEmpty(doName)) {
						// 处理scope
						Scope scope = accessControl.scope();
						switch (scope) {
						case Public:
							SCOPE_PUBLIC.add(doName);
							break;
						case AfterLogin:
							SCOPE_AFTER_LOGIN.add(doName);
							break;
						case AfterAuth:
							SCOPE_AFTER_AUTH.add(doName);
							break;
						case FIXIP:
							SCOPE_FIX_IP.add(doName);
							break;
						default:
							break;
						}

						// 处理权限与接口的对应关系
						String[] privArr = accessControl.priv();
						for (String priv : privArr) {
							Set<String> set = PRIV_DESC_INTERFACES_MAP.get(priv);
							if (set == null) {
								HashSet<String> tmp = new HashSet<String>(1);
								tmp.add(doName);
								PRIV_DESC_INTERFACES_MAP.put(priv, tmp);
							} else {
								set.add(doName);
							}
						}

						// 如果是写操作的接口
						if (accessControl.write()) {
							WRITE.add(doName);
						}
					}
				}
			}
		}
		// output summary
		System.out.println("Public:" + CachedObjects.GSON.toJson(SCOPE_PUBLIC));
		System.out.println("AfterLogin:" + CachedObjects.GSON.toJson(SCOPE_AFTER_LOGIN));
		System.out.println("AfterAuth:" + CachedObjects.GSON.toJson(SCOPE_AFTER_AUTH));
		System.out.println("FixIP:" + CachedObjects.GSON.toJson(SCOPE_FIX_IP));
		System.out.println("Write:" + CachedObjects.GSON.toJson(WRITE));
		System.out.println("PrivDesc:" + CachedObjects.GSON.toJson(PRIV_DESC_INTERFACES_MAP));
	}

	/**
	 * 
	 * <pre>
	 * 权限检查
	 *  @param context  
	 *  @author Ivan<br>
	 * @throws ClientException 
	 *  @date 2015年4月13日 下午2:16:58
	 * <br>
	 */
	public static void check(DEContext context) throws ClientException {
		String ID = context.getID();
		String doName = context.getDeParameter().getDoName();// 当前请求的是哪个接口
		// 根据接口类型进行权限检查
		if (SCOPE_FIX_IP.contains(doName)) {
			// TODO:check ip
			LogUtils.logPriv(ID, doName, Scope.FIXIP.name(), "ok");
			return;
		}
		if (SCOPE_PUBLIC.contains(doName)) {
			LogUtils.logPriv(ID, doName, Scope.Public.name(), "ok");
			Tracker.add(ID, doName + " is public,ok");
			return;
		}
		// 除了上面两种类型的接口其他接口都需要有登录态，
		String userID = null;
		if (SCOPE_AFTER_LOGIN.contains(doName)) {
			// TODO:验证登录态
			LogUtils.logPriv(ID, doName, Scope.AfterLogin.name(), "ok");
			return;
		}

		// 如果是demo账号，检查接口 是否是写操作
		if (ServerCfg.DEMO_ACCOUNT.equals(userID) && WRITE.contains(doName)) {
			ExceptionHandler.throwPermissionException(StatusCode.NO_PRIV);
		}

		if (SCOPE_AFTER_AUTH.contains(doName)) {
			// TODO:验证用户的具体权限
			LogUtils.logPriv(ID, doName, Scope.AfterAuth.name(), "ok");
			return;
		}

		// TODO:服务异常，没有配置这个接口
	}
}
