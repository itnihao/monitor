package com.dataeye.omp.module.cmdb.employee;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataeye.common.PrivilegeControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.common.PageData;

/**
 * 员工管理模块
 * @auther wendy
 * @since 2015/12/27 13:48
 */
@Controller
public class EmployeeModule {

    @Autowired
    private EmployeeDao employeeDao;

    /**
     * 查询所有员工信息
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getEmployeeList.do")
    public Object getAllEmployeeList(HttpServletRequest req,
         HttpServletResponse rsp)throws Exception{
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
        param.checkParameter(Keys.PAGEID,Keys.PAGESIZE);
        PageData pageData =
                employeeDao.getAllEmployeeList(param.getSearchKey(),
                        param.getPageID(),param.getPageSize());
        return pageData;
    }

    /**
     * 根据部门查询员工信息
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    public Object getEmployeeListByDepId(HttpServletRequest req,
            HttpServletResponse rsp)throws Exception{
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
        param.checkParameter(Keys.DEPTID,
                Keys.SEARCHKEY,Keys.PAGEID,Keys.PAGESIZE);
        PageData pageData =
                employeeDao.getEmployeeListByDepId(param.getDeptId(),
                        param.getSearchKey(),param.getPageID(),param.getPageSize());
        return pageData;
    }

    /**
     *根据小组查询员工信息
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    public Object getEmployeeListByTeamId(HttpServletRequest req,
                 HttpServletResponse rsp)throws Exception{
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
        param.checkParameter(Keys.TEAMID,
                Keys.SEARCHKEY,Keys.PAGEID,Keys.PAGESIZE);
        PageData pageData =
                employeeDao.getEmployeeListByTeamId(param.getTeamId(),
                        param.getSearchKey(),param.getPageID(),param.getPageSize());
        return pageData;
    }



    /**
     * 获取员工下拉信息
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getEmployeeSelectData.do")
    public Object getSelectEmployeeData(HttpServletRequest req,
                                          HttpServletResponse rsp)throws Exception{
        return employeeDao.getEmployeeSelectData();
    }

}
