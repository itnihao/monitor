package com.dataeye.omp.module.alarm;

import com.dataeye.common.CachedObjects;
import com.dataeye.common.PrivilegeControl;
import com.dataeye.common.PrivilegeControl.Scope;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.module.cmdb.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 告警平台 控制器
 * @author chenfanglin
 * @date 2016年1月25日 下午2:38:51
 */
@Controller
public class AlarmModule {

	@Autowired
	private AlarmDAO alarmDAO;

	@Autowired
	private AlarmService alarmService;

	/**
	 * 设置告警规则
	 * @param req
	 * @param rsp
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年1月25日 下午6:00:56
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/saveAlarmRule.do")
	public Object saveAlarmRule(HttpServletRequest req, HttpServletResponse rsp)
			throws Exception {
		DEContext context = (DEContext) req.getAttribute("CTX");
		DEParameter param = context.getDeParameter();
		param.checkParameter(Keys.FEATRUEID, Keys.OBJECT);
		alarmDAO.saveAlarmRule(param.getAlarmRuleId(),
				param.getFeatrueID(), param.getObject(), param.getServers(),
				param.getBusiId(), param.getModuleId(), param.getGroupID(),
				param.getMaxThreshold(), param.getMinThreshold(),
				param.getMaxMoM(), param.getMinMoM(), param.getMaxFrequency(),
				param.getAlarmType(), param.getShieldStart(),
				param.getShieldEnd(), param.getAlarmSectionType(),
				param.getAlarmObjectType(), param.getAlarmLevel());

		return null;
	}

	/**
	 * 获取告警规则列表
	 * @param req
	 * @param rsp
	 * @return
	 * @throws Exception
     */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/getAlarmRuleList.do")
	public Object getAlarmRuleList(HttpServletRequest req, HttpServletResponse rsp)
			throws Exception {
		DEContext ctx = (DEContext) req.getAttribute("CTX");
		DEParameter param = ctx.getDeParameter();
		param.checkParameter(Keys.PAGEID, Keys.PAGESIZE);
		return alarmService.getAlarmRuleList(param.getSearchKey(),
				param.getPageID(), param.getPageSize(),
				param.getOrderBy(), param.getOrder());
	}

	/**
	 * 获取告警规则详情
	 * @param req
	 * @param rsp
	 * @return
	 * @throws Exception
     */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/getAlarmRuleDetail.do")
	public Object getAlarmRuleDetail(HttpServletRequest req, HttpServletResponse rsp)
			throws Exception {
		DEContext context = (DEContext) req.getAttribute("CTX");
		DEParameter param = context.getDeParameter();
		AlarmRule alarmRule = alarmDAO.getAlarmRuleById(param.getAlarmRuleId());
		return alarmRule;
	}

	/**
	 * 删除告警规则
	 * @param req
	 * @param rsp
	 * @return
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/deleteAlarmRuleById.do")
	public Object deleteAlarmRuleById(HttpServletRequest req, HttpServletResponse rsp)
			throws Exception {
		DEContext context = (DEContext) req.getAttribute("CTX");
		DEParameter param = context.getDeParameter();
		alarmDAO.deleteAlarmRule(param.getAlarmRuleId());
		return null;
	}

	/**
	 * 删除告警规则
	 * @param req
	 * @param rsp
	 * @return
	 * @throws Exception
	 */
	@PrivilegeControl(scope = Scope.AfterLogin, write = false)
	@RequestMapping("/alarm/getAlarmSelectData.do")
	public Object getAlarmSelectData(HttpServletRequest req, HttpServletResponse rsp)
			throws Exception {
		Employee employee = (Employee) req.getSession().
				getAttribute(Constant.SessionName.CURRENT_USER);
		if (employee == null) {
			return null;
		}
		return alarmService.getAlarmSelectData(employee.getId());
	}



}
