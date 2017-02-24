package com.dataeye.omp.module.cmdb.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dataeye.common.HttpStatusCode;
import com.dataeye.exception.ClientException;
import com.dataeye.utils.HttpClientUtil;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.common.PrivilegeControl.Scope;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.constant.Constant.SessionName;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.omp.module.cmdb.employee.Employee;
import com.dataeye.utils.MD5Coding;
import com.qq.jutil.string.StringUtil;

@Controller
public class UserModule {

	@Autowired
	private UserDAO userDAO;

	private static final String SUCC = "succ";
	private static final String FAIL = "fail";

	private static final String skipIp = "14.127.86.246,14.127.85.34";

	/**
	 * 登陆
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月21日 下午6:00:20
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/login.do")
	public Object userLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.EMAIL,Keys.PASSWORD);
		String password = MD5Coding.encode2HexStr(parameter.getPassword().getBytes());
		Employee employee = userDAO.getEmployeeByEmail(parameter.getEmail());


		String verifyUrl = "http://119.147.212.253:38083/common/securityCode/verify";

		verifyUrl += "?uid=" + parameter.getEmail() + "&app=" + parameter.getApp() + "&code=" + parameter.getVerifyCode();

		String host = getLocalIp(request);
		String resut = FAIL;
//		if (host.contains(skipIp)) {
//			resut = SUCC;
//		} else {
			resut = HttpClientUtil.get(verifyUrl);
		//}

		if (SUCC.equals(resut)) {
			if (StringUtil.isNotEmpty(employee.getPassword()) &&
					employee.getPassword().equals(password.toLowerCase())) {
				// 成功登陆
				HttpSession session = request.getSession();

				session.setAttribute(SessionName.CURRENT_USER, employee);
			} else {
				// 用户名或密码错误
				//context.getDeResponse().setStatusCode(StatusCode.PASSWORD_ERROR);
				throw new ClientException(StatusCode.PASSWORD_ERROR);
			}
		} else {
			//context.getDeResponse().setStatusCode(StatusCode.VERIFYCODE_ERROR);
			//验证码错误
			throw new ClientException(StatusCode.VERIFYCODE_ERROR);
		}
		return employee;
	}


	public static String getLocalIp(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		String forwarded = request.getHeader("X-Forwarded-For");
		String realIp = request.getHeader("X-Real-IP");

		System.out.println("remoteAddr:" + remoteAddr + " forwarded:" + forwarded + " realIp:" + realIp);

		String ip = null;
		if (realIp == null) {
			if (forwarded == null) {
				ip = remoteAddr;
			} else {
				ip = remoteAddr + "/" + forwarded.split(",")[0];
			}
		} else {
			if (realIp.equals(forwarded)) {
				ip = realIp;
			} else {
				if(forwarded != null){
					forwarded = forwarded.split(",")[0];
				}
				ip = realIp + "/" + forwarded;
			}
		}
		return ip;
	}

	/**
	 * 注销
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月21日 下午6:00:20
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/logout.do")
	public Object loginOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 成功登陆
		HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect("/pages/login.jsp");
		return null;
	}


	public static void main(String[] args) {
		System.out.println(MD5Coding.encode2HexStr("testUser1".getBytes()));
	}
}
