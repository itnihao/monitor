package com.dataeye.omp.module.cmdb.process;

import com.dataeye.common.CachedObjects;
import com.dataeye.common.PrivilegeControl;
import com.dataeye.common.PrivilegeControl.Scope;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.module.cmdb.business.BusinessDao;
import com.dataeye.omp.module.cmdb.device.DeviceDao;
import com.dataeye.omp.module.cmdb.device.DicValue;
import com.dataeye.omp.module.cmdb.employee.EmployeeDao;
import com.google.gson.reflect.TypeToken;
import com.qq.jutil.string.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进程监控
 * @author wendy
 * @since  2016年3月3日 下午3:52:35
 */
@Controller
public class ProcessModule {

	@Autowired
	private ProcessService processService;
	@Autowired
	private ProcessDao processDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private BusinessDao busiDao;

	@Autowired
	private DeviceDao deviceDao;


	/**
	 * 查询所有的模块列表（含进程数）
	 * 参数：
	 * pageID 页码
	 * pageSize 每页显示条数
	 * orderBy 排序字段
	 * order 排序方式（升序、降序）
	 * searchKey 搜索关键字
	 * @param req   req
	 * @param rsp	rsp
	 * @return		Object
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/queryModuleList.do")
	public Object queryModuleList(HttpServletRequest req,
								  HttpServletResponse rsp) throws Exception {
		DEContext ctx = (DEContext) req.getAttribute("CTX");
		DEParameter param = ctx.getDeParameter();
		return processService.queryModuleList(param.getSearchKey(), param.getPageID(),
				param.getPageSize(), param.getOrder(), param.getOrderBy());
	}

	/**
	 * 查询进程列表
	 * 参数：
	 * moduleID 模块ID，等于0查全部
	 * pageID 页码
	 * pageSize 每页显示条数
	 * orderBy 排序字段
	 * order 排序方式（升序、降序）
	 * searchKey 搜索关键字
	 * @param req   请求
	 * @param rsp   响应
	 * @return   Object
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/queryProcessList.do")
	public Object queryProcessList(HttpServletRequest req,
								   HttpServletResponse rsp) throws Exception {
		DEContext ctx = (DEContext) req.getAttribute("CTX");
		DEParameter param = ctx.getDeParameter();
		return processService.queryProcessList(param.getSearchKey(), param.getPageID(),
				param.getPageSize(), param.getOrder(), param.getOrderBy());
	}


	/**
	 * 添加/修改进程，支持多台机器的添加符合条件的同一进程,
	 * 但只支持单个进程修改
	 * @param req req
	 * @param rsp rsp
	 * @return 		Object
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/addUpdateProcess.do")
	public Object addUpdateProcess(HttpServletRequest req,
								   HttpServletResponse rsp) throws Exception {
		DEContext ctx = (DEContext) req.getAttribute("CTX");
		DEParameter param = ctx.getDeParameter();
		param.checkParameter(Keys.BUSIID, Keys.MODULE_ID, Keys.SERVERS, Keys.PROCESS_NAME,
				Keys.DEPLOYPATH, Keys.MAIN_PRINCIPAL);

		//告警类型
		List<Integer> list = new ArrayList<>();
		String alarmType = param.getAlarmType();
		if (StringUtil.isNotEmpty(alarmType)) {
			String[] arr = alarmType.split(",");
			for (String s : arr) {
				list.add(Integer.parseInt(s));
			}
		}
		alarmType = CachedObjects.GSON.toJson(list);

		//机器列表
		String servers = param.getServers();
		String[] server = servers.split(Constant.Separator.DEFAULT);

		//进程名或者端口重复,不能添加的进程
		List<Integer> canNotAddList = new ArrayList<>();

		//已经添加过的进程
		List<Integer> existList = new ArrayList<>();

		//验证通过，且未添加的进程
		List<Integer> toBeAddList = new ArrayList<>();

		//添加进程
		if (param.getProcessID() < 0) {
			for (int i = 0; i < server.length; i++) {
				int serverId = Integer.parseInt(server[i]);
				if (processDao.checkPortAndNameExits(
						serverId, param.getPort(), param.getProcessName())) {
					existList.add(serverId);
					continue;
				}

				if (processDao.checkProcessPortExitsByServerId(
						param.getServerID(), param.getPort()) ||
						processDao.checkProcessNameExitsByServerId(
								param.getServerID(), param.getProcessName())) {
					canNotAddList.add(serverId);
					continue;
				}
				toBeAddList.add(serverId);
			}
			//修改
		} else {
			if (processDao.checkProcessPortExitsByServerId(
					param.getServerID(), param.getPort(), param.getProcessID())
					|| processDao.checkProcessNameExitsByServerId(
					param.getServerID(), param.getProcessName(), param.getProcessID())) {

				canNotAddList.add(Integer.parseInt(server[0]));
			} else {
				toBeAddList.add(Integer.parseInt(server[0]));
			}
		}

		if (toBeAddList.size() > 0) {
			processDao.addUpdateProcess(param.getProcessID(), param.getProcessName(),
					param.getBusiID(), param.getModuleID(), toBeAddList,
					param.getPort(), param.getDeployPath(), param.getConfigPath(),
					param.getLogPath(), param.getMainPrincipal(), param.getBakPrincipal(),
					param.getMonitorType(), param.getAlarmInterval(),
					param.getMaxFrequency(), alarmType);
		}

		Map<String, List<Integer>> map = new HashMap<>();
		map.put("canNotAddList", canNotAddList);
		map.put("existList", existList);
		map.put("AddedList", existList);
		return map;
	}

	/**
	 * 添加告警规则
	 * @param req  req
	 * @param rsp  rsp
	 * @return  Object
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/addProcessRule.do")
	public Object addProcessRule(HttpServletRequest req,
								 HttpServletResponse rsp) throws Exception {
		DEContext ctx = (DEContext) req.getAttribute("CTX");
		DEParameter param = ctx.getDeParameter();
		List<Integer> list = new ArrayList<>();
		String alarmType = param.getAlarmType();
		if (StringUtil.isNotEmpty(alarmType)) {
			String[] arr = alarmType.split(",");
			for (String s : arr) {
				list.add(Integer.parseInt(s));
			}

		}

		alarmType = CachedObjects.GSON.toJson(list);

		processDao.addProcessRule(param.getProcessID(), param.getMonitorType(),
				param.getAlarmInterval(), param.getMaxFrequency(), alarmType);
		return null;
	}

	/**
	 * 添加告警规则
	 * @param req req
	 * @param rsp rsp
	 * @return  Object
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/updateProcessRule.do")
	public Object updateProcessRule(HttpServletRequest req,
									HttpServletResponse rsp) throws Exception {
		DEContext ctx = (DEContext) req.getAttribute("CTX");
		DEParameter param = ctx.getDeParameter();
		List<Integer> list = new ArrayList<>();
		String alarmType = param.getAlarmType();
		if (StringUtil.isNotEmpty(alarmType)) {
			String[] arr = alarmType.split(",");
			for (String s : arr) {
				list.add(Integer.parseInt(s));
			}

		}

		alarmType = CachedObjects.GSON.toJson(list);
		processDao.updateProcessRule(param.getProcessID(), param.getMonitorType(),
				param.getAlarmInterval(), param.getMaxFrequency(), alarmType);
		return null;
	}

	/**
	 * 获取进程详细信息
	 * @param req   req
	 * @param rsp   rsp
	 * @return  Object
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/getProcessInfoByID.do")
	public Object getProcessInfoByID(HttpServletRequest req,
									 HttpServletResponse rsp) throws Exception {
		DEContext ctx = (DEContext) req.getAttribute("CTX");
		DEParameter param = ctx.getDeParameter();
		param.checkParameter(Keys.PROCESSID);
		Map<String, Object> map = processDao.getProcessInfoByID(param.getProcessID());
		int monitorType =(Integer) map.get("monitorType");
		List<Integer> list = new ArrayList<>();
		if (monitorType == 0) {
			list.add(0);
		}else if (monitorType == 1) {
			list.add(1);
		} else if (monitorType == 2) {
			list.add(0);
			list.add(1);
		}
		map.put("monitorType", list);
		String alarmType = (String)map.get("alarmType");
		List<Integer> alarmTypeList= CachedObjects.GSON.fromJson(alarmType,
				new TypeToken<List<Integer>>() {
				}.getType());
		map.put("alarmType", alarmTypeList);

		return map;
	}

	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/getInitPathAndPrincipal.do")
	public Object getInitPathAndPrincipal(HttpServletRequest req,
									 HttpServletResponse rsp) throws Exception {
		DEContext ctx = (DEContext) req.getAttribute("CTX");
		DEParameter param = ctx.getDeParameter();
		param.checkParameter(Keys.MODULE_ID);
		return processDao.getInitPathAndPrincipal(param.getModuleID());
	}



	/**
	 * 获取进程告警规则详细信息
	 * @param req   req
	 * @param rsp	rsp
	 * @return		Object
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/getProcessRuleByID.do")
	public Object getProcessRuleById(HttpServletRequest req,
									 HttpServletResponse rsp) throws Exception {
		DEContext ctx = (DEContext) req.getAttribute("CTX");
		DEParameter param = ctx.getDeParameter();
		param.checkParameter(Keys.PROCESSID);
		Map<String, Object> map = processDao.getProcessRuleByID(param.getProcessID());
		int monitorType =(Integer) map.get("monitorType");
		List<Integer> list = new ArrayList<>();
		if (monitorType == 0) {
			list.add(0);
		}else if (monitorType == 1) {
			list.add(1);
		} else if (monitorType == 2) {
			list.add(0);
			list.add(1);
		}
		map.put("monitorType", list);
		String alarmType = (String)map.get("alarmType");
		List<Integer> alarmTypeList= CachedObjects.GSON.fromJson(alarmType,
				new TypeToken<List<Integer>>() {
				}.getType());
		map.put("alarmType", alarmTypeList);

		return map;
	}

	/**
	 * 启动或暂停 告警
	 * @param req   req
	 * @param rsp	rsp
	 * @return		Object
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/switchMonitorStatus.do")
	public Object switchProcessRule(HttpServletRequest req,
					HttpServletResponse rsp) throws Exception {
		DEContext ctx = (DEContext) req.getAttribute("CTX");
		DEParameter param = ctx.getDeParameter();
		param.checkParameter(Keys.PROCESSID);
		processDao.changeProcessStatus(param.getProcessID());
		return null;
	}

	/**
	 * 获取模块信息下拉数据
	 * @param req	req
	 * @param rsp	rsp
	 * @return		Object
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/getModuleSelectData.do")
	public Object getModuleSelectData(HttpServletRequest req,
						HttpServletResponse rsp) throws Exception {
		List<DicValue> employeeList = employeeDao.getEmployeeSelectData();
		List<DicValue> businessList = busiDao.getBusiDicList();
		Map<String, Object> map = new HashMap<>();
		map.put("busiList", businessList);
		map.put("employeeList", employeeList);
		return map;
	}

	/**
	 * 获取进程下拉数据
	 * @param req	req
	 * @param rsp	rsp
	 * @return		Object
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/getProcessSelectData.do")
	public Object getProcessSelectData(HttpServletRequest req,
									  HttpServletResponse rsp) throws Exception {
		List<DicValue> employeeList = employeeDao.getEmployeeSelectData();
		List<DicValue> businessList = busiDao.getBusiDicList();
		List<DicValue> deviceList = deviceDao.getSelectDeviceList();
		List<DicValue> moduleList = busiDao.getModuleDicList();
		List<DicValue> moduleServerList = busiDao.getMonitorServerList();
		Map<String, Object> map = new HashMap<>();
		map.put("busiList", businessList);
		map.put("deviceList", deviceList);
		map.put("moduleList", moduleList);
		map.put("employeeList", employeeList);
		map.put("moduleServerList", moduleServerList);
		return map;
	}


	/**
	 *	获取模块详情
	 * @param req  	req
	 * @param rsp 	rsp
	 * @return		Object
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/getModuleDetailByID.do")
	public Object getModuleDetailByID(HttpServletRequest req,
									  HttpServletResponse rsp) throws Exception {
		DEContext ctx = (DEContext) req.getAttribute("CTX");
		DEParameter param = ctx.getDeParameter();
		return processDao.getModuleDetailById(param.getModuleID());
	}

	/**
	 *	根据进程ID删除进程
	 * @param req	req
	 * @param rsp	rsp
	 * @return		Object
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/process/deleteProcessById.do")
	public Object deleteProcessById(HttpServletRequest req,
									  HttpServletResponse rsp) throws Exception {
		DEContext ctx = (DEContext) req.getAttribute("CTX");
		DEParameter param = ctx.getDeParameter();
		param.checkParameter(Keys.PROCESSID);
		processDao.deleteProcessById(param.getProcessID());
		return null;
	}





}
