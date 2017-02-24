package com.dataeye.jobs;


import com.dataeye.common.EmployeeInfo;
import com.dataeye.common.EmployeeResponse;
import com.dataeye.utils.ApplicationContextUtil;
import com.dataeye.utils.HttpClientUtil;
import com.dataeye.utils.ServerUtils;
import com.google.gson.Gson;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.print.DocPrintJob;

public class SyncEmployeeInfo implements Job {

    private static final String URL = "http://119.147.212.253:38081/company/employee";
    final JdbcTemplate jdbcTemplateMonitor = ApplicationContextUtil.getBeanJdbcTemplateMonitor();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        doJob();
    }

    public void doJob() {
        EmployeeResponse response = new Gson().fromJson(HttpClientUtil.get(URL), EmployeeResponse.class);
        if (response != null) {
            try {
                if (jdbcTemplateMonitor != null) {
                    List<EmployeeInfo> standardEmployees = response.getContent();
                    if (standardEmployees.size() > 0) {
                        System.out.println("========================================");
                        delOffStaffAddnewStaff(standardEmployees);
                        delStaffNotExits(standardEmployees);
                    }
                }
            } catch (Exception e) {
                ServerUtils.sendAlarmEmail("wendy@dataeye.com", "sync Employee", "sync Employee failed");
                e.printStackTrace();
            }
        } else {
            ServerUtils.sendAlarmEmail("wendy@dataeye.com", "sync Employee", "sync Employee failed,employee list is empty");
        }
    }

    private void delStaffNotExits(List<EmployeeInfo> standardEmployees) {
        List<String> currentEmployees = getCurrentStaff();
        String sql_delete = "delete from employee where email = ?";
        List<String> emails = new ArrayList<>();
        for (EmployeeInfo employeeInfo : standardEmployees) {
            emails.add(employeeInfo.getAlias());
        }

        for (String email : currentEmployees) {
            if (!emails.contains(email)) {
                System.out.println("delete staff not exits");
                jdbcTemplateMonitor.execute(sql_delete, email);
            }
        }
    }

    private void delOffStaffAddnewStaff(List<EmployeeInfo> standardEmployees) {
        String sql_check = "select count(1) from employee where email=?";
        String sql_delete = "delete from employee where email = ?";
        String sql_insert = "insert into employee (name,email,password,phone) values (?,?,?,?)";

        for (EmployeeInfo employee : standardEmployees) {
            int count = jdbcTemplateMonitor.queryForInt(sql_check, employee.getAlias());
            if (count > 0 && employee.getOpenType() == 2) {
                System.out.println("delete off staff");
                jdbcTemplateMonitor.execute(sql_delete, employee);
            }

            if (count < 0 && employee.getOpenType() == 1) {
                System.out.println("add new staff");
                jdbcTemplateMonitor.insert(sql_insert, employee.getName(), employee.getAlias(),
                        "e10adc3949ba59abbe56e057f20f883e", employee.getMobile());
            }
        }

    }

    public List<String> getCurrentStaff() {
        String sql = "select email from employee";
        final List<String> currentEmployees = new ArrayList<>();
        jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                currentEmployees.add(rs.getString(1));
            }
        });
        return currentEmployees;
    }
}
