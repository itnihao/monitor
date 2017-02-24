package com.dataeye.omp.module.cmdb.business;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.common.PageData;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.exception.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务/模块 信息接口
 * @auther wendy
 * @since 2015/12/27 15:00
 */
@Controller
public class BusinessModule {

    @Autowired
    private BusinessDao businessDao;

    /**
     * 获取所有业务信息
     * @param req
     * @param rsq
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getBusinessList.do")
    public Object getBusinessList(HttpServletRequest req,
            HttpServletResponse rsq)throws  Exception{
        List<Business> busiList = businessDao.getBusinessList();

        List<Business> moduleList = businessDao.getModuleList();

        //为业务设置添加模块信息
        for(Business busi:busiList){
            List<Business> subModules = new ArrayList<Business>();
            for (Business module : moduleList) {
                if (busi.getId() == module.getPid()) {
                    subModules.add(module);
                }
            }

            busi.setModuleList(subModules);
            busi.setModuleNum(subModules.size());
        }

        return busiList;
    }

    /**
     * 添加业务
     * @param req
     * @param rsq
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/addBusiness.do")
    public Object addBusiness(HttpServletRequest req,
         HttpServletResponse rsq)throws  Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
        param.checkParameter(Keys.BUSINAME, Keys.OMPERSION);

       if(businessDao.checkBusinessExits(param.getBusiName())){
           ExceptionHandler.throwParameterException(StatusCode.EXISTS);
       }
       return businessDao.addBusiness(param.getBusiName(), param.getOmPerson());
    }

    /**
     * 添加模块
     * @param req
     * @param rsq
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/addModule.do")
    public Object addModule(HttpServletRequest req,
         HttpServletResponse rsq)throws  Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();

        param.checkParameter(Keys.MODULENAME, Keys.OMPERSION,Keys.BUSI_ID);

        //同一个业务下不能有相同的模块
        if (businessDao.checkModuleExits(param.getModuleName(),param.getBusiId())) {
            ExceptionHandler.throwParameterException(StatusCode.EXISTS);
        }

       return businessDao.addModule(param.getModuleName(),
               param.getOmPerson(), param.getBakPrincipal(),param.getBusiId());
    }


    /**
     * 修改模块
     * @param req
     * @param rsq
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/updateModule.do")
    public Object updateModule(HttpServletRequest req,
                            HttpServletResponse rsq)throws  Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();

        param.checkParameter(Keys.MODULENAME, Keys.OMPERSION,Keys.BUSI_ID);

        //同一个业务下不能有相同的模块
        if (businessDao.checkModuleExits(param.getModuleName(),
                param.getBusiId(),param.getModuleId())) {
            ExceptionHandler.throwParameterException(StatusCode.EXISTS);
        }

        return businessDao.updateModule(param.getModuleName(),
                param.getOmPerson(), param.getBakPrincipal(),
                param.getBusiId(),param.getModuleId());
    }

    /**
     * 删除业务或模块
     * @param req
     * @param rsq
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/deleteBusiness.do")
    public Object deleteBusiness(HttpServletRequest req,
          HttpServletResponse rsq)throws  Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
        param.checkParameter(Keys.BUSI_ID);
        businessDao.deleteBusiness(param.getBusiId());
        return null;
    }

    /**
     * 删除模块
     * @param req
     * @param rsq
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/deleteModule.do")
    public Object deleteModule(HttpServletRequest req,
                                     HttpServletResponse rsq)throws  Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();
        param.checkParameter(Keys.MODULE_ID);
        businessDao.deleteModule(param.getModuleId());
        return null;
    }

    /**
     * 根据业务ID获取模块
     * @param req
     * @param rsq
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getModuleListByBusiId.do")
    public Object getModuleListByBusiId(HttpServletRequest req,
           HttpServletResponse rsq)throws  Exception {
        DEContext ctx = (DEContext) req.getAttribute("CTX");
        DEParameter param = ctx.getDeParameter();

        param.checkParameter(Keys.BUSI_ID);

        businessDao.getModuleListByBusiId(param.getBusiId());

        return null;
    }

    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getBusinessSelectData.do")
    public Object getBusinessSelectData(HttpServletRequest req,
                                           HttpServletResponse rsq)throws  Exception {
      return  businessDao.getBusiDicList();

    }

    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getModuleSelectData.do")
    public Object getModuleSelectData(HttpServletRequest req,
                                        HttpServletResponse rsq)throws  Exception {
      return businessDao.getModuleDicList();
    }

}
