package com.dataeye.omp.module.alarm.customize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.constant.StatusCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.common.PrivilegeControl.Scope;
import com.dataeye.exception.AbstractDataEyeException;
import com.dataeye.exception.ExceptionHandler;

/**
 * 自定义告警
 * @author chenfanglin
 * @date 2016年3月3日 下午5:14:24
 */
@Controller
public class CustomizeAlarmModule {

	@Autowired
	private CustomizeAlarmDAO customizeAlarmDAO;

	/**
	 * 查询告警项列表
	 * 参数：
	 * pageID 页码
	 * pageSize 每页显示条数
	 * orderBy 排序字段
	 * order 排序方式（升序、降序）
	 * searchKey 搜索关键字
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月3日 下午5:18:21
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/customize/queryCustomizeAlarmList.do")
	public Object queryCustomizeAlarmList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();

		parameter.checkParameter(Keys.PAGEID, Keys.PAGESIZE);

		return customizeAlarmDAO.queryByQualifiers(parameter.getPageID(), parameter.getPageSize(), parameter.getSearchKey(),
				parameter.getOrderBy(), parameter.getOrder());
	}

	/**
	 * 根据告警项id查询记录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
     */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/customize/getCustomizeAlarmById.do")
	public Object getCustomizeAlarmById(HttpServletRequest request, HttpServletResponse response) throws Exception{
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();


		return customizeAlarmDAO.getById(parameter.getCustomizeAlarmId());
	}
	
	/**
	 * 添加自定义告警项
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月3日 下午5:20:39
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/customize/addCustomizeAlarm.do")
	public Object addCustomizeAlarm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();

		parameter.checkParameter(Keys.ALARM_ITEM, Keys.MAIN_EMPLOYEE);

		int i = customizeAlarmDAO.getRecordByAlarmItem(parameter.getAlarmItem());
		if (i > 0) {
			ExceptionHandler.throwDatabaseException(StatusCode.EXISTS, "alarmItem (告警项) 不能重复");
		}
		CustomizeAlarm alarm = new CustomizeAlarm();
		alarm.setBusinessID(parameter.getBusiId());
		alarm.setRemark(parameter.getRemark() == null ? "" : parameter.getRemark());
		alarm.setAlarmItem(parameter.getAlarmItem());
		alarm.setStatus(parameter.getStatus());
		alarm.setMainEmployee(parameter.getMainEmployee());
		alarm.setOthers(parameter.getOthers() == null ? "" : parameter.getOthers());

		CustomizeAlarmRule rule = new CustomizeAlarmRule();
		rule.setAlarmInterval(parameter.getAlarmInterval());
		rule.setAlarmType(parameter.getAlarmType() == null ? "" : parameter.getAlarmType());
		rule.setMaxFrequency(parameter.getMaxFrequency());
		rule.setRestoreInterval(parameter.getRestoreInterval());
		rule.setRestoreType(parameter.getRestoreType());
		return customizeAlarmDAO.add(alarm, rule);
	}
	
	/**
	 * 修改自定义告警项
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月3日 下午5:21:56
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/customize/updateCustomizeAlarm.do")
	public Object updateCustomizeAlarm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();

		parameter.checkParameter(Keys.CUSTOMIZE_ALARM_ID);

		CustomizeAlarm alarm = new CustomizeAlarm();
		alarm.setId(parameter.getCustomizeAlarmId());
		alarm.setBusinessID(parameter.getBusiId());
		alarm.setRemark(parameter.getRemark());
		alarm.setAlarmItem(parameter.getAlarmItem());
		alarm.setStatus(parameter.getStatus());
		alarm.setMainEmployee(parameter.getMainEmployee());
		alarm.setOthers(parameter.getOthers());

		CustomizeAlarmRule rule = new CustomizeAlarmRule();
		rule.setAlarmInterval(parameter.getAlarmInterval());
		rule.setAlarmType(parameter.getAlarmType());
		rule.setMaxFrequency(parameter.getMaxFrequency());
		rule.setRestoreInterval(parameter.getRestoreInterval());
		rule.setRestoreType(parameter.getRestoreType());

		return customizeAlarmDAO.update(alarm, rule);
	}
	
	/**
	 * 启动或者暂停 告警
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月3日 下午5:22:12
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/customize/switchCustomizeAlarm.do")
	public Object switchCustomizeAlarm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter parameter = context.getDeParameter();

		parameter.checkParameter(Keys.CUSTOMIZE_ALARM_ID, Keys.STATUS);
		return customizeAlarmDAO.switchByStatus(parameter.getCustomizeAlarmId(), parameter.getStatus());
	}
}
