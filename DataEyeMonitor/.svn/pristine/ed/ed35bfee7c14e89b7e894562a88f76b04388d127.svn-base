package com.dataeye.omp.module.cmdb.summary;

import com.dataeye.common.PrivilegeControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @auther wendy
 * @since 2015/12/29 15:48
 */
@Controller
public class SummaryModule {

    @Autowired
    private SummaryDao summaryDao;

    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getSummaryTopData.do")
    public Object getSummaryTopData(HttpServletRequest req,
       HttpServletResponse rsq)throws Exception{
        List<Summary> summaryList=summaryDao.getDeviceSummaryTopData();
        return summaryList;
    }

    @PrivilegeControl(scope = PrivilegeControl.Scope.AfterLogin, write = false)
    @RequestMapping("/getSummaryTableData.do")
    public Object getSummaryTableData(HttpServletRequest req,
       HttpServletResponse rsq)throws Exception{
        List<Summary> summaryList=summaryDao.getBusiSummaryData();
        return summaryList;
    }
}
