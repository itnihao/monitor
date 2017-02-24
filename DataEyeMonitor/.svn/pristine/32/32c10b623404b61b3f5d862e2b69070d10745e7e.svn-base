package com.dataeye.omp.module.monitor.server;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import clojure.lang.Obj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.common.PrivilegeControl.Scope;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.common.PageData;
import com.dataeye.omp.module.cmdb.device.DicValue;

/**
 * 服务器监控Controller
 * 
 * @author chenfanglin
 * @date 2016年1月6日 上午10:38:23
 */
@Controller
public class MachineModule {

	@Autowired
	private MachineService machineService;

	/**
	 * 查询所有主机  机房视图 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月6日 下午2:13:09
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/queryRoomServerList.do")
	public Object queryRoomServerList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.PAGEID, Keys.PAGESIZE, Keys.SEARCHTYPE);
		PageData<Machine> pageData = new PageData<Machine>();
		// 机房视图
		pageData = machineService.queryRoomServerList(parameter.getPageID(), parameter.getPageSize(),
				parameter.getSearchKey(), parameter.getOrderBy(), parameter.getOrder(), parameter.getIdcId());
		return pageData;
	}

	/**
	 * 业务视图 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月14日 上午9:59:14
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/queryBusinessServerList.do")
	public Object queryBusinessServerList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.PAGEID, Keys.PAGESIZE, Keys.SEARCHTYPE);
		PageData<Machine> pageData = new PageData<Machine>();
		// 业务视图
		pageData = machineService.queryBusinessServerList(parameter.getPageID(), parameter.getPageSize(),
				parameter.getSearchKey(), parameter.getOrderBy(), parameter.getOrder(), parameter.getBusiId(), parameter.getModuleId());
		return pageData;
	}

	/**
	 * 分组视图
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月14日 上午9:59:24
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/queryGroupServerList.do")
	public Object queryGroupServerList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		parameter.checkParameter(Keys.PAGEID, Keys.PAGESIZE, Keys.SEARCHTYPE);
		PageData<Machine> pageData = new PageData<Machine>();
		// 分组视图
		pageData = machineService.queryGroupServerList(parameter.getPageID(), parameter.getPageSize(),
				parameter.getSearchKey(), parameter.getOrderBy(), parameter.getOrder(), parameter.getGroupID());
		return pageData;
	}

	/**
	 * 根据业务查询模块
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月12日 下午3:26:47
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/queryModuleByBusiness.do")
	public Object queryModuleByBusiness(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		int busiId = parameter.getBusiId();
		parameter.checkParameter(Keys.BUSI_ID);
		List<DicValue> values = machineService.queryModuleByBusiness(busiId);
		return values;
	}

	/**
	 * 分组视图添加机器
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月13日 下午5:07:58
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/addMachineForGroup.do")
	public Object addMachineForGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		return machineService.addMachineForGroup(parameter.getGroupID(), parameter.getServerID());
	}


	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/monitor/getServerBasisData.do")
	public Object getServerBasisData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();
		return machineService.getServerBasisData(parameter.getServerID());
	}


}
