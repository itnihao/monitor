package com.dataeye.omp.module.checkModule;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.jobs.CheckReportInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wendy
 * @since 2016/8/3
 */
@Controller
public class CheckReportModule {

    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/report/check.do")
    public Object reportCheck(HttpServletRequest req,
                              HttpServletResponse rsp)
            throws Exception {
        new CheckReportInfo().dojob();
        return "succ";
    }
}
