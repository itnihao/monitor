package com.dataeye.omp.module.monitor.custom;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEParameter.Keys;
import com.dataeye.omp.constant.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther wendy
 * @since 2016/3/15 20:27
 */
@Controller
public class CustomMonitorModule {

    @Autowired
    private CustomMonitorDAO customMonitorDAO;
    /**
     * 添加自定义监控项
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/monitor/addCustomMonitor.do")
    public Object addCustomMonitor(HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        DEContext context = (DEContext) request.getAttribute("CTX");
        DEParameter param = context.getDeParameter();
        param.checkParameter(Keys.MONITORITEM, Keys.BUSI_ID);

        boolean b = customMonitorDAO.isExists(param.getMonitorItem());
        if (b) ExceptionHandler.throwParameterException(StatusCode.EXISTS);
        CustomMonitor monitor = new CustomMonitor();
        monitor.setMonitorItem(param.getMonitorItem());
        monitor.setBusiness(param.getBusiId());
        monitor.setMainPrincipal(param.getMainPrincipal());
        monitor.setBakPrincipal(param.getBakPrincipal() == null ? "" : param.getBakPrincipal());
        return customMonitorDAO.add(monitor);
    }

    /**
     * 修改自定义监控项
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/monitor/updateCustomMonitor.do")
    public Object updateCustomMonitor(HttpServletRequest request, HttpServletResponse response) throws Exception {
        DEContext context = (DEContext) request.getAttribute("CTX");
        DEParameter param = context.getDeParameter();
        param.checkParameter(Keys.MONITORITEM, Keys.BUSI_ID);

        boolean b = customMonitorDAO.isExists(param.getMonitorItem(), param.getCustomMonitorId());
        if (b) ExceptionHandler.throwParameterException(StatusCode.EXISTS);

        CustomMonitor monitor = new CustomMonitor();
        monitor.setId(param.getCustomMonitorId());
        monitor.setMonitorItem(param.getMonitorItem());
        monitor.setBusiness(param.getBusiId());
        monitor.setMainPrincipal(param.getMainPrincipal());
        monitor.setBakPrincipal(param.getBakPrincipal() == null ? "" : param.getBakPrincipal());
        return customMonitorDAO.update(monitor);
    }

    /**
     * 根据id查询自定义监控项
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/monitor/getCustomMonitorById.do")
    public Object getCustomMonitorById(HttpServletRequest request, HttpServletResponse response) throws Exception{
        DEContext context = (DEContext) request.getAttribute("CTX");
        DEParameter param = context.getDeParameter();
        return customMonitorDAO.loadById(param.getCustomMonitorId());
    }

    /**
     * 分页条件查询自定义监控项
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/monitor/getCustomMonitorForList.do")
    public Object getCustomMonitorForList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        DEContext context = (DEContext) request.getAttribute("CTX");
        DEParameter param = context.getDeParameter();
        return customMonitorDAO.getByPage(param.getPageID(),param.getPageSize(),
                param.getSearchKey(),param.getOrderBy(),param.getOrder());
    }

    /**
     * 获取自定义监控对象选项下拉项
     * 自定义监控选项类型 1-->调用端  2-->接收端   3-->扩展属性1   4-->扩展属性2
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/monitor/getSelectorByMonitorItem.do")
    public Object getSelectorByMonitorItem(HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        DEContext context = (DEContext) request.getAttribute("CTX");
        DEParameter param = context.getDeParameter();

        int type = param.getMonitorSelectorType();

        String monitorItem = param.getMonitorItem();
        String caller = param.getCaller();
        String receiver = param.getReceiver();
        String ext1 = param.getExt1();
        String ext2 = param.getExt2();
        String searchKey = param.getSearchKey();
        return customMonitorDAO.getMonitorSelectItem(type, searchKey,
                monitorItem, caller, receiver, ext1, ext2);


//        String monitorItem = param.getMonitorItem();
//        String caller = param.getCaller() == null ? "[^#]*" : param.getCaller();
//        String receiver = param.getReceiver() == null ? "[^#]*" : param.getReceiver();
//        String ext1 = param.getExt1() == null ? "[^#]*" : param.getExt1();
//        String ext2 = param.getExt2() == null ? "" : param.getExt2();


//        List<Map<String, String>> list = new ArrayList<>();
//
//        if (monitorItem != null && !"".equals(monitorItem)) {
//            if (type == 1) {
//                //获取调用端数据
//                list = customMonitorDAO.getSelectorByMonitorItem(monitorItem);
//            }
//
//            if (type == 2) {
//                //获取接受端数据
//                list = customMonitorDAO.getSelectorByMonitorItem(monitorItem, caller);
//            }
//
//            if (type == 3) {
//                //获取扩展属性1数据
//                list = customMonitorDAO.getSelectorByMonitorItem(monitorItem, caller, receiver);
//            }
//
//            if (type == 4) {
//                //获取扩展属性2数据
//                list = customMonitorDAO.getSelectorByMonitorItem(monitorItem, caller, receiver, ext1);
//            }
//        }
//
//        return list;
    }


    /**
     * 获取自定义监控项下拉列表
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/monitor/getMonitorItemList.do")
    public Object getMonitorItemList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        DEContext context = (DEContext) request.getAttribute("CTX");
        DEParameter param = context.getDeParameter();

        String key = param.getSearchKey();

        return customMonitorDAO.getMonitorItemByFuzzy(key);
    }




}
