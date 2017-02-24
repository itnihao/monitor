package com.dataeye.jobs;

import com.dataeye.utils.ApplicationContextUtil;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportMachineInfo implements Job {
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		final JdbcTemplate jdbcTemplateMonitor = ApplicationContextUtil.getBeanJdbcTemplateMonitor();
		JdbcTemplate jdbcTemplateMonitorStat = ApplicationContextUtil.getBeanJdbcTemplateMonitorStat();

		final String sql_insert = "insert into server_list(dev_id, host_name, net_card_num,"
				+ "cpu_num, cpu_type, cpu_physical_cores, cpu_logic_cores,"
				+ "os, kernel, memory, disk_num, disk_size, disk_details,"
				+ "disk_partition) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		String sql = "select dev_id, host_name, net_card_num,"
				+ "cpu_num, cpu_type, cpu_physical_cores, cpu_logic_cores,"
				+ "os, kernel, memory, disk_num, disk_size, disk_details,"
				+ "disk_partition from server_list";
		jdbcTemplateMonitorStat.query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				jdbcTemplateMonitor.insert(sql_insert, rs.getInt("dev_id"),rs.getString("host_name"),rs.getInt("net_card_num"),
						rs.getInt("cpu_num"),rs.getString("cpu_type"),rs.getInt("cpu_physical_cores"),rs.getInt("cpu_logic_cores"),
						rs.getString("os"),rs.getString("kernel"),rs.getInt("memory"),rs.getInt("disk_num"),rs.getInt("disk_size"),
						rs.getString("disk_details"),rs.getString("disk_partition"));
			}
		});
		
	}

}
