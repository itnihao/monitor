package com.dataeye.omp.module.alarm.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.common.PageData;

/**
 * 服务器监控告警规则
 * 
 * @author chenfanglin
 * @date 2016年3月4日 下午4:07:24
 */
@Controller
public class AlarmBasicModule {

	@Autowired
	private AlarmBasicService alarmBasicService;
	
	@Autowired
	private AlarmBasicDAO alarmBasicDAO;

	/**
	 * 查询服务器监控告警规则
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年3月4日 下午4:57:12
	 */
	@PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/basic/queryAlarmBasicRuleList.do")
	public Object queryAlarmBasicRuleList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter param = context.getDeParameter();
		param.checkParameter(Keys.PAGEID, Keys.PAGESIZE);
		PageData<AlarmBasicRule> pageData = new PageData<AlarmBasicRule>();
		pageData = alarmBasicService.queryAlarmBasicRuleList(param.getPageID(), param.getPageSize(),
				param.getSearchKey(), param.getOrderBy(), param.getOrder());
		return pageData;
	}
	
	/**
	 * 保存服务器监控告警规则
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月4日 下午5:05:18
	 */
	@PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/basic/saveAlarmBasicRule.do")
	public Object addAlarmBasicRule(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter param = context.getDeParameter();
		alarmBasicDAO.saveAlarmBasicRule(param.getAlarmRuleId(),param.getFeatrueID(), param.getObject(), param.getAlarmObjectType(), param.getServers(), 
				param.getBusiId(), param.getModuleId(), param.getGroupID(), param.getAlarmSectionType(), param.getMaxThreshold(), 
				param.getMinThreshold(), param.getMaxMoM(), param.getMinMoM(), param.getMaxFrequency(), param.getShieldStart(),
				param.getShieldEnd(), param.getAlarmType());
		return null;
	}
	
	/**
	 * 启用禁用告警规则
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月4日 下午5:41:01
	 */
	@PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/basic/switchAlarmBasicRule.do")
	public Object switchAlarmBasicRule(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter param = context.getDeParameter();
		alarmBasicDAO.switchAlarmBasicRule(param.getAlarmRuleId());
		return null;
	}
	
	/**
	 * 根据ID获取告警规则
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月4日 下午5:54:06
	 */
	@PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/basic/getAlarmBasicRuleByID.do")
	public Object getAlarmBasicRuleByID(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DEContext context = (DEContext) request.getAttribute("CTX");
		DEParameter param = context.getDeParameter();
		AlarmBasicRule rule = alarmBasicDAO.getAlarmBasicRuleByID(param.getAlarmRuleId());
		return rule;
	}
}
