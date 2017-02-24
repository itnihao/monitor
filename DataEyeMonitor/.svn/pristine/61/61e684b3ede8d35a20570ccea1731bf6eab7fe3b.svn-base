package com.dataeye.omp.module.monitor.custom;

import com.dataeye.common.PrivilegeControl;
import com.dataeye.omp.common.DEContext;
import com.dataeye.omp.common.DEParameter;
import com.dataeye.omp.common.DEResponse;
import com.dataeye.omp.constant.Constant.ServerCfg;
import com.qq.jutil.string.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @auther wendy
 * @since 2016/3/15 20:37
 */
@Controller
public class CustomMonitorReportModule {


    @Autowired
    private CustomMonitorReportDAO dao;

    /**
     * 查询失败率  单天趋势
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/monitor/queryFailRate.do")
    public DEResponse queryFailRate(HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        DEContext context = (DEContext) request.getAttribute("CTX");
        DEParameter param = context.getDeParameter();

        String date = param.getCurrentDate();
        if (date == null || "".equals(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(new Date(System.currentTimeMillis()));
        }
        String monitorItem = param.getMonitorItem();
        if (monitorItem == null || "".equals(monitorItem)) return null;
        String caller = param.getCaller();
        String receiver = param.getReceiver();
        String ext1 = param.getExt1();
        String ext2 = param.getExt2();

        String rowKey = getRowkey(monitorItem, date, caller, receiver, ext1, ext2);
        return dao.getFailedRateToday(rowKey,date);
    }




    /**
     * 查询平均耗时  单天趋势
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
     @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
     @RequestMapping("/monitor/queryAverageCosts.do")
     public DEResponse queryAverageCosts(HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {
         DEContext context = (DEContext) request.getAttribute("CTX");
         DEParameter param = context.getDeParameter();

         String date = param.getCurrentDate();
         if (date == null || "".equals(date)) {
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             date = sdf.format(new Date(System.currentTimeMillis()));
         }
         String monitorItem = param.getMonitorItem();
         if (monitorItem == null || "".equals(monitorItem)) return null;
         String caller = param.getCaller();
         String receiver = param.getReceiver();
         String ext1 = param.getExt1();
         String ext2 = param.getExt2();

         String rowKey = getRowkey(monitorItem, date, caller, receiver, ext1, ext2);


         return dao.getAverageTimeCostToday(rowKey,date);
    }

    /**
     * 查询失败率  对比
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
     @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
     @RequestMapping("/monitor/queryFailRateContrast.do")
     public DEResponse queryFailRateContrast(HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
         DEContext context = (DEContext) request.getAttribute("CTX");
         DEParameter param = context.getDeParameter();
         String currentDate = param.getCurrentDate();
         if (currentDate == null || "".equals(currentDate)) {
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             currentDate = sdf.format(new Date(System.currentTimeMillis()));
         }
         String comparedDate = param.getComparedDate();
         String monitorItem = param.getMonitorItem();
         if (monitorItem == null || "".equals(monitorItem)) return null;
         String caller = param.getCaller();
         String receiver = param.getReceiver();
         String ext1 = param.getExt1();
         String ext2 = param.getExt2();
         String rowKey1 = getRowkey(monitorItem, currentDate, caller, receiver, ext1, ext2);
         String rowKey2 = getRowkey(monitorItem, comparedDate, caller, receiver, ext1, ext2);
         return dao.getFailedRateComparedDate(rowKey1, rowKey2, currentDate, comparedDate);
    }

    /**
     * 查询平均耗时  对比
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/monitor/queryAverageCostsContrast.do")
    public DEResponse queryAverageCostsContrast(HttpServletRequest request,
                                                HttpServletResponse response) throws Exception {
        DEContext context = (DEContext) request.getAttribute("CTX");
        DEParameter param = context.getDeParameter();

        String currentDate = param.getCurrentDate();
        if (currentDate == null || "".equals(currentDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = sdf.format(new Date(System.currentTimeMillis()));
        }
        String comparedDate = param.getComparedDate();
        String monitorItem = param.getMonitorItem();
        if (monitorItem == null || "".equals(monitorItem)) return null;
        String caller = param.getCaller();
        String receiver = param.getReceiver();
        String ext1 = param.getExt1();
        String ext2 = param.getExt2();

        String rowKey1 = getRowkey(monitorItem, currentDate, caller, receiver, ext1, ext2);
        String rowKey2 = getRowkey(monitorItem, comparedDate, caller, receiver, ext1, ext2);

        return dao.getAverageTimeCostCompared(rowKey1, rowKey2, currentDate, comparedDate);
    }


    /**
     * rowKey
     * @param monitorItem
     * @param date
     * @param caller
     * @param receiver
     * @param ext1
     * @param ext2
     * @return
     */
    private String getRowkey(String monitorItem, String date,
                             String caller, String receiver,
                             String ext1, String ext2) {
        String rowPrefix = monitorItem + ServerCfg.ROWKEY_SPLITER
                + date + ServerCfg.ROWKEY_SPLITER;

        //{item}#{time}#
        String rowKey = rowPrefix;

        //{item}#{time}###
        if (StringUtil.isEmpty(caller) && StringUtil.isEmpty(receiver)
                && StringUtil.isEmpty(ext1) && StringUtil.isEmpty(ext2)) {
            rowKey += ServerCfg.ROWKEY_SPLITER +
                    ServerCfg.ROWKEY_SPLITER + ServerCfg.ROWKEY_SPLITER;
        }

        //{item}#{time}#{caller}###
        if (StringUtil.isNotEmpty(caller) && StringUtil.isEmpty(receiver)
                && StringUtil.isEmpty(ext1) && StringUtil.isEmpty(ext2)) {
            rowKey += caller + ServerCfg.ROWKEY_SPLITER +
                    ServerCfg.ROWKEY_SPLITER + ServerCfg.ROWKEY_SPLITER;
        }

        //{item}#{time}##{receiver}##
        if (StringUtil.isEmpty(caller) && StringUtil.isNotEmpty(receiver)
                && StringUtil.isEmpty(ext1) && StringUtil.isEmpty(ext2)) {
            rowKey +=  ServerCfg.ROWKEY_SPLITER +receiver+
                    ServerCfg.ROWKEY_SPLITER + ServerCfg.ROWKEY_SPLITER;
        }

        //{item}#{time}###{ext1}#
        if (StringUtil.isEmpty(caller) && StringUtil.isEmpty(receiver)
                && StringUtil.isNotEmpty(ext1) && StringUtil.isEmpty(ext2)) {
            rowKey += ServerCfg.ROWKEY_SPLITER +
                    ServerCfg.ROWKEY_SPLITER + ext1 + ServerCfg.ROWKEY_SPLITER;
        }

        //{item}#{time}####{ext2}
        if (StringUtil.isEmpty(caller) && StringUtil.isEmpty(receiver)
                && StringUtil.isEmpty(ext1) && StringUtil.isNotEmpty(ext2)) {
            rowKey += ServerCfg.ROWKEY_SPLITER +
                    ServerCfg.ROWKEY_SPLITER + ServerCfg.ROWKEY_SPLITER + ext2;
        }

        //{item}#{time}#{caller}#{receiver}##
        if (StringUtil.isNotEmpty(caller) && StringUtil.isNotEmpty(receiver)
                && StringUtil.isEmpty(ext1) && StringUtil.isEmpty(ext2)) {
            rowKey += caller + ServerCfg.ROWKEY_SPLITER + receiver +
                    ServerCfg.ROWKEY_SPLITER + ServerCfg.ROWKEY_SPLITER;
        }

        //{item}#{time}#{caller}##{ext1}#
        if (StringUtil.isNotEmpty(caller) && StringUtil.isEmpty(receiver)
                && StringUtil.isNotEmpty(ext1) && StringUtil.isEmpty(ext2)) {
            rowKey += caller + ServerCfg.ROWKEY_SPLITER +
                    ServerCfg.ROWKEY_SPLITER +ext1+ ServerCfg.ROWKEY_SPLITER;
        }

        //{item}#{time}#{caller}###{ext2}
        if (StringUtil.isNotEmpty(caller) && StringUtil.isEmpty(receiver)
                && StringUtil.isEmpty(ext1) && StringUtil.isNotEmpty(ext2)) {
            rowKey += caller + ServerCfg.ROWKEY_SPLITER +
                    ServerCfg.ROWKEY_SPLITER + ServerCfg.ROWKEY_SPLITER + ext2;
        }

        //{item}#{time}##{receiver}#{ext1}#
        if (StringUtil.isEmpty(caller) && StringUtil.isNotEmpty(receiver)
                && StringUtil.isNotEmpty(ext1) && StringUtil.isEmpty(ext2)) {
            rowKey += ServerCfg.ROWKEY_SPLITER + receiver +
                    ServerCfg.ROWKEY_SPLITER + ext1 + ServerCfg.ROWKEY_SPLITER;
        }

        //{item}#{time}##{receiver}##{ext2}
        if (StringUtil.isEmpty(caller) && StringUtil.isNotEmpty(receiver)
                && StringUtil.isEmpty(ext1) && StringUtil.isNotEmpty(ext2)) {
            rowKey += ServerCfg.ROWKEY_SPLITER + receiver +
                    ServerCfg.ROWKEY_SPLITER + ServerCfg.ROWKEY_SPLITER + ext2;
        }

        //{item}#{time}###{ex1}#{ext2}
        if (StringUtil.isEmpty(caller) && StringUtil.isEmpty(receiver)
                && StringUtil.isNotEmpty(ext1) && StringUtil.isNotEmpty(ext2)) {
            rowKey += ServerCfg.ROWKEY_SPLITER + ServerCfg.ROWKEY_SPLITER
                    + ext1 + ServerCfg.ROWKEY_SPLITER + ext2;
        }

        //{item}#{time}#{caller}#{receiver}#{ex1}#
        if (StringUtil.isNotEmpty(caller) && StringUtil.isNotEmpty(receiver)
                && StringUtil.isNotEmpty(ext1) && StringUtil.isEmpty(ext2)) {
            rowKey += caller + ServerCfg.ROWKEY_SPLITER + receiver +
                    ServerCfg.ROWKEY_SPLITER + ext1 + ServerCfg.ROWKEY_SPLITER;
        }

        //{item}#{time}#{caller}#{receiver}##{ext2}
        if (StringUtil.isNotEmpty(caller) && StringUtil.isNotEmpty(receiver)
                && StringUtil.isEmpty(ext1) && StringUtil.isNotEmpty(ext2)) {
            rowKey += caller + ServerCfg.ROWKEY_SPLITER + receiver +
                    ServerCfg.ROWKEY_SPLITER + ServerCfg.ROWKEY_SPLITER + ext2;
        }

        //{item}#{time}#{caller}##{ext1}#{ext2}
        if (StringUtil.isNotEmpty(caller) && StringUtil.isEmpty(receiver)
                && StringUtil.isNotEmpty(ext1) && StringUtil.isNotEmpty(ext2)) {
            rowKey += caller + ServerCfg.ROWKEY_SPLITER +
                    ServerCfg.ROWKEY_SPLITER + ext1 + ServerCfg.ROWKEY_SPLITER + ext2;
        }

        //{item}#{time}##{receiver}#{ext1}#{ext2}
        if (StringUtil.isEmpty(caller) && StringUtil.isNotEmpty(receiver)
                && StringUtil.isNotEmpty(ext1) && StringUtil.isNotEmpty(ext2)) {
            rowKey += ServerCfg.ROWKEY_SPLITER + receiver +
                    ServerCfg.ROWKEY_SPLITER + ext1 + ServerCfg.ROWKEY_SPLITER + ext2;
        }

        //{item}#{time}#{caller}#{receiver}#{ext1}#{ext2}
        if (StringUtil.isNotEmpty(caller) && StringUtil.isNotEmpty(receiver)
                && StringUtil.isNotEmpty(ext1) && StringUtil.isNotEmpty(ext2)) {
            rowKey += caller + ServerCfg.ROWKEY_SPLITER + receiver +
                    ServerCfg.ROWKEY_SPLITER + ext1 + ServerCfg.ROWKEY_SPLITER + ext2;
        }
        return rowKey;
    }



}
