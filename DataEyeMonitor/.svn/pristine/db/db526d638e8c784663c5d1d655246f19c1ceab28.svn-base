package com.dataeye.omp.module.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.common.PrivilegeControl.Scope;

/**
 * 通用接口
 * @author chenfanglin
 * @date 2016年3月3日 下午4:14:34
 */
@Controller
public class CommonModule {

	/**
	 * 查询所有的模块列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月3日 下午4:16:31
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/common/getModules.do")
	public Object getModules(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}
	
	/**
	 * 获取模块下的所有机器列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月3日 下午4:18:05
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/common/getMachineByModule.do")
	public Object getMachineByModule(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}
	
	/**
	 * 查询所有的业务列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月3日 下午5:25:37
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/common/getBusiness.do")
	public Object getBusiness(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}
}
