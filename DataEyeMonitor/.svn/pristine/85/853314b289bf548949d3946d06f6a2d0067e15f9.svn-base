package com.dataeye.omp.module.monitor.mysql;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.constant.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * @author wendy
 * @since 2016/3/29 17:23
 */
@Controller
public class MysqlMonitorModule {

    @Autowired
    private MysqlMonitorModuleDao dao;


    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/mysql/getMysqlInstanceList.do")
    public Object getMysqlInstanceList(HttpServletRequest req,
                                       HttpServletResponse rsp) throws Exception {
        System.out.println(req.getContextPath());
        String path = req.getContextPath();
        String uri = req.getRequestURL().toString();

        DEContext context = (DEContext) req.getAttribute("CTX");
        DEParameter param = context.getDeParameter();

        return dao.getMysqlInstanceList(param.getOrderBy(), param.getOrder(), param.getPageID(),
                param.getPageSize(), param.getSearchKey());
    }


    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/mysql/addMysqlAlarmRule.do")
    public Object addMysqlAlarmRule(HttpServletRequest req,
                                       HttpServletResponse rsp) throws Exception {
        DEContext context = (DEContext) req.getAttribute("CTX");
        DEParameter param = context.getDeParameter();

        int serverId = param.getServerID();
        int port = param.getPort();
        int main = param.getMainPrincipal();
        if (main == 0 || serverId == 0 || port == 0) {
            ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);
        }

        MysqlMonitorTO to = new MysqlMonitorTO();
        to.setServerID(serverId);
        to.setPort(port);
        to.setOpenFile(param.getOpenFile());
        to.setConnection(param.getConnection());
        to.setLock(param.getLock());
        to.setLockTime(param.getLockTime());
        to.setSlowSql(param.getSlowSql());
        to.setMaxFrequency(param.getMaxFrequency());

        to.setMainPrincipal(main);
        to.setBakPrincipal(param.getBakPrincipal() == null ? "" : param.getBakPrincipal());
        to.setAlarmType(param.getAlarmType() == null ? "" :param.getAlarmType());
        to.setAlarmInterval(param.getAlarmInterval());

        return dao.addMysqlRule(to);
    }

    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/mysql/getAlarmRuleByInstanceId.do")
    public Object getAlarmRuleByInstanceId(HttpServletRequest req,
                                    HttpServletResponse rsp) throws Exception {
        DEContext context = (DEContext) req.getAttribute("CTX");
        DEParameter param = context.getDeParameter();

        int serverId = param.getServerID();
        int port = param.getPort();
        if (serverId == 0 || port == 0) ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);
        return dao.loadByInstanceId(serverId, port);
    }

    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/mysql/updateMysqlAlarmRule.do")
    public Object updateMysqlAlarmRule(HttpServletRequest req,
                                           HttpServletResponse rsp) throws Exception {
        DEContext context = (DEContext) req.getAttribute("CTX");
        DEParameter param = context.getDeParameter();

        int serverId = param.getServerID();
        int port = param.getPort();
        if (serverId == 0 || port == 0) ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);

        MysqlMonitorTO to = new MysqlMonitorTO();
        to.setOpenFile(param.getOpenFile());
        to.setConnection(param.getConnection());
        to.setLock(param.getLock());
        to.setLockTime(param.getLockTime());
        to.setSlowSql(param.getSlowSql());
        to.setMaxFrequency(param.getMaxFrequency());
        to.setMainPrincipal(param.getMainPrincipal());
        to.setBakPrincipal(param.getBakPrincipal() == null ? "" : param.getBakPrincipal());
        to.setAlarmType(param.getAlarmType() == null ? "" : param.getAlarmType());
        to.setAlarmInterval(param.getAlarmInterval());
        to.setServerID(serverId);
        to.setPort(port);

        return dao.updateMysqlRule(to);
    }

}
