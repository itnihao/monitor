package com.dataeye.omp.module.checkModule;


import com.dataeye.common.PrivilegeControl;
import com.dataeye.jobs.SyncEmployeeInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SyncEmployee {

    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/employee/syncEmployee.do")
    public Object syncEmployee(HttpServletRequest req, HttpServletResponse rsp)
            throws Exception {
        new SyncEmployeeInfo().doJob();
        return "success";
    }
}
