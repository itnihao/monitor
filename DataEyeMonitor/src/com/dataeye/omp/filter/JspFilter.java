package com.dataeye.omp.filter;

import com.dataeye.common.HttpStatusCode;
import com.dataeye.omp.common.DEResponse;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.omp.module.cmdb.employee.Employee;
import com.dataeye.utils.ServerUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当用户访问jsp的时候对用户登录态进行验证
 *
 * @author Ivan <br>
 * @date 2015年8月11日 下午4:45:05 <br>
 */
public class JspFilter extends OncePerRequestFilter {
	/**
	 * <pre>
	 * 获取当前访问的Jsp文件名
	 *
	 * @param request
	 * @return
	 * @author Ivan<br>
	 * @date 2015年8月11日 下午5:18:06 <br>
	 */
	private String getJspName(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		return requestUri.substring(requestUri.lastIndexOf("/") + 1);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String currentpage = getJspName(request);
			Employee employee = (Employee) request.getSession().getAttribute(Constant.SessionName.CURRENT_USER);

			if ("login.jsp".equals(currentpage)) {
				filterChain.doFilter(request, response);
				return;
			} else {
				if (employee != null) {
					filterChain.doFilter(request, response);
					return;
				} else {
					response.sendRedirect("/pages/login.jsp");
					return;
				}

			}
		}catch (Exception e) {
			e.printStackTrace();
			DEResponse deResponse = new DEResponse();
			deResponse.setHttpStatusCode(HttpStatusCode.SERVER_EXCEPTION);
			deResponse.setStatusCode(StatusCode.SERVER_STATUS_ERROR);
			deResponse.setContent(e.getMessage());
			ServerUtils.writeToResponse(response, HttpStatusCode.SERVER_EXCEPTION, deResponse.toJson());
		}
	}
}