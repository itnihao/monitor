package com.dataeye.omp.module.monitor.server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.common.PrivilegeControl.Scope;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.constant.Constant.SessionName;
import com.dataeye.omp.module.cmdb.employee.Employee;

/**
 * 分组管理
 * @author chenfanglin
 * @date 2016年1月11日 上午11:16:28
 */
@Controller
public class GroupModule {

	@Autowired
	private GroupDAO groupDAO;
	
	/**
	 * 查询分组
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月11日 上午11:17:16
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/queryGroup.do")
	public Object queryGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Employee employee = (Employee) session.getAttribute(SessionName.CURRENT_USER);
		List<Group> groups = new ArrayList<Group>();
		if (employee == null) {
//			// 重新登录
//			response.sendRedirect("/pages/login.jsp");
		} else {
			groups = groupDAO.queryGroup(employee.getId());
		}
		return groups;
	}
	
	/**
	 * 删除分组
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月13日 下午4:59:02
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/deleteGroup.do")
	public Object deleteGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.GROUPID);
		groupDAO.deleteGroup(parameter.getGroupID());
		return null;
	}
	
	/**
	 * 添加分组
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月13日 下午4:59:09
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/addGroup.do")
	public Object addGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		HttpSession session = request.getSession();
		Employee employee = (Employee) session.getAttribute(SessionName.CURRENT_USER);
		if (employee == null) {
//			// 重新登录
//			response.sendRedirect("/pages/login.jsp");
		} else {
			return groupDAO.addGroup(parameter.getGroupName(), employee.getId());
		}
		return 0;
	}
}
