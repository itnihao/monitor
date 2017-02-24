package com.dataeye.omp.module.cmdb.idcmanage;


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

@Controller
public class IdcManageModule {

    @Autowired
    private IdcManageDao dao;

    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/idc/getIdcServerDetailInfo.do")
    public Object getIdcServerDetailInfo(HttpServletRequest req,
                                         HttpServletResponse rsp) throws Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();


        return dao.get();
    }

    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/idc/updateServerSortFlag.do")
    public Object updateServerSortFlag(HttpServletRequest req,
                                         HttpServletResponse rsp) throws Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();


        int flag =  dao.updateSortFlag(param.getServerID(), param.getServerSortFlag());

        switch (flag) {

            case 0 :
                ExceptionHandler.throwCustomMessageException(StatusCode.SERVER_STATUS_ERROR, "服务器内部异常"); break;

            case -1 :
                ExceptionHandler.throwCustomMessageException(StatusCode.PARAMETER_ERROR, "输入的sortFlag参数有误"); break;

            case -2 :
                ExceptionHandler.throwCustomMessageException(StatusCode.SERVER_STATUS_ERROR, "不能上移,已经是第一个"); break;

            case -3 :
                ExceptionHandler.throwCustomMessageException(StatusCode.SERVER_STATUS_ERROR, "不能下移,已经是最后一个");break;

            case 2 :break;

        }

        return flag;
    }
}
