package com.dataeye.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.dataeye.omp.constant.Constant.SessionName;
import com.dataeye.omp.module.cmdb.user.User;

/**
 * 
 * @author Ivan <br>
 * @date 2015年8月11日 下午5:44:16 <br>
 *
 */
public class SessionUtils {
	/**
	 * 
	 * <pre>
	 * 从session中获取当前用户
	 * 
	 * @param request
	 * @return
	 * @author Ivan<br>
	 * @date 2015年8月11日 下午6:02:40 <br>
	 */
	public static User getCurrentUserFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object object = session.getAttribute(SessionName.CURRENT_USER);
		if (object != null && object instanceof User) {
			return (User) object;
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 把用户存入session
	 * 
	 * @param request
	 * @param user
	 * @author Ivan<br>
	 * @date 2015年8月12日 下午5:54:46 <br>
	 */
	public static void putUser2Session(HttpServletRequest request, User user) {
		HttpSession session = request.getSession();
		session.setAttribute(SessionName.CURRENT_USER, user);
	}
}
