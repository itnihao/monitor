package com.dataeye.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.dataeye.CookieDecoder;
import com.dataeye.omp.constant.Constant.CookieName;
import com.dataeye.omp.constant.Constant.ServerCfg;

/**
 * <pre>
 * 操作cookie的工具类
 * @author Ivan          <br>
 * @date 2015年4月1日 下午3:10:21
 * <br>
 *
 */
public class CookieUtils {
	/**
	 * 
	 * <pre>
	 * 从request中解析出所有的Cookie
	 *  @param request
	 *  @return  Map<String, Cookie>
	 *  @author Ivan<br>
	 *  @date 2015年4月1日 下午3:12:41
	 * <br>
	 */
	public static Map<String, Cookie> parseCookie(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		String cookieString = request.getHeader("Cookie");
		if (ValidateUtils.isNotEmpty(cookieString)) {
			CookieDecoder.decode(cookieString, cookieMap);
		}
		return cookieMap;
	}

	/**
	 * 
	 * <pre>
	 * 获取Cookie
	 *  @param cookieMap 通过parseCookie解析出来的map
	 *  @param name cookie名字
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月1日 下午3:24:28
	 * <br>
	 */
	public static String getCookie(Map<String, Cookie> cookieMap, String name) {
		if (ValidateUtils.isNotEmptyMap(cookieMap)) {
			Cookie cookie = cookieMap.get(name);
			if (cookie != null) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public static String getCookie(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = parseCookie(request);
		if (ValidateUtils.isNotEmptyMap(cookieMap)) {
			Cookie cookie = cookieMap.get(name);
			if (cookie != null) {
				return cookie.getValue();
			}
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 从cookie中取出语言
	 *  @param cookieMap
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月1日 下午4:21:05
	 * <br>
	 */
	public static String getLangFromCookie(Map<String, Cookie> cookieMap) {
		String lang = getCookie(cookieMap, CookieName.LANG);
		if (ValidateUtils.isValidLang(lang)) {
			return lang;
		}
		return ServerCfg.DEFAULT_LANG;
	}
}
