package com.dataeye.omp.module.monitor.mysql;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEResponse;
import com.dataeye.omp.constant.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wendy
 * @since 2016/3/29 17:43
 */
@Controller
public class MysqlMonitorReportModule {

    @Autowired
    private MysqlMonitorReportService service;

    /**
     * 打开文件数
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/mysql/getOpenFileQuantity.do")
    public DEResponse getOpenFileQuantity(HttpServletRequest req,
                                       HttpServletResponse rsp) throws Exception {
        DEContext context = (DEContext) req.getAttribute("CTX");
        DEParameter param = context.getDeParameter();

        int serverId = param.getServerID();
        int port = param.getPort();
        String startDate = param.getStartdate();
        String endDate = param.getEnddate();

        if (serverId == 0 || port == 0 || startDate == null || endDate == null)
            ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);

        return service.switchToDaoMethods(serverId, port, startDate, endDate, 1);
    }


    /**
     * 连接数
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/mysql/getConnectionQuantity.do")
    public DEResponse getConnectionQuantity(HttpServletRequest req,
                                      HttpServletResponse rsp) throws Exception {
        DEContext context = (DEContext) req.getAttribute("CTX");
        DEParameter param = context.getDeParameter();

        int serverId = param.getServerID();
        int port = param.getPort();
        String startDate = param.getStartdate();
        String endDate = param.getEnddate();

        if (serverId == 0 || port == 0 || startDate == null || endDate == null)
            ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);

        return service.switchToDaoMethods(serverId, port, startDate, endDate, 2);
    }

    /**
     * 锁数量和锁时长
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/mysql/getLockQuantityAndLockTime.do")
    public DEResponse getLockQuantityAndLockTime(HttpServletRequest req,
                                        HttpServletResponse rsp) throws Exception {
        DEContext context = (DEContext) req.getAttribute("CTX");
        DEParameter param = context.getDeParameter();

        int serverId = param.getServerID();
        int port = param.getPort();
        String startDate = param.getStartdate();
        String endDate = param.getEnddate();

        if (serverId == 0 || port == 0 || startDate == null || endDate == null)
            ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);

        return service.switchToDaoMethods(serverId, port, startDate, endDate, 4);
    }

    /**
     * 慢sql
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/mysql/getSlowSqlQuantity.do")
    public DEResponse getSlowSqlQuantity(HttpServletRequest req,
                                             HttpServletResponse rsp) throws Exception {
        DEContext context = (DEContext) req.getAttribute("CTX");
        DEParameter param = context.getDeParameter();

        int serverId = param.getServerID();
        int port = param.getPort();
        String startDate = param.getStartdate();
        String endDate = param.getEnddate();

        if (serverId == 0 || port == 0 || startDate == null || endDate == null)
            ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);

        return service.switchToDaoMethods(serverId, port, startDate, endDate, 3);
    }


}
