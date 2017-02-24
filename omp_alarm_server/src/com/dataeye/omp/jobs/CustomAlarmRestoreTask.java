package com.dataeye.omp.jobs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.omp.alarm.server.AlarmServerUtils;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.event.Launch;
import com.dataeye.omp.utils.DateUtils;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import com.xunlei.spring.Config;

/**
 * 自定义告警 恢复任务
 * 
 * @author chenfanglin
 * @date 2016年3月8日 下午4:38:36
 */
@Service
public class CustomAlarmRestoreTask {

	@Resource(name = "jdbcTemplateMonitor")
	private JdbcTemplate jdbcTemplateMonitor;

	@Autowired
	private AlarmServerUtils alarmServerUtils;

	@Config
	private int mailSendTime = 30;

	public void customAlarmRestoreTask() {
		Launch.SCHEDULER.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				final long currentTime = DateUtils.unixTimestamp();
				// 获取 告警类型是 ：间隔多久恢复 的所有告警规则
				String sql = "select a.alarmItem,b.restoreInterval,b.maxFrequency from " + Constant.DC_ALARM_CUSTOMIZE_INFO + " a, " + Constant.DC_ALARM_RULE_CUSTOMIZE + " b "
						+ "where a.id = b.customizeID and b.restoreType = 0 and a.status = 1";
				final String sql_control = "select id,alarmTime from " + Constant.DC_ALARM_CUSTOMIZE_CONTROL + " where alarmItem = ? and frequency = ?";
				final String sql_restore = "update " + Constant.DC_ALARM_CUSTOMIZE_CONTROL + " set frequency = 0,restoreTime = ? where id = ?";
				jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
					public void processRow(ResultSet rs) throws SQLException {
						String alarmItem = rs.getString("alarmItem");
						final int restoreInterval = rs.getInt("restoreInterval");
						int maxFrequency = rs.getInt("maxFrequency");
						jdbcTemplateMonitor.query(sql_control, new RowCallbackHandler() {
							public void processRow(ResultSet rs) throws SQLException {
								long id = rs.getLong("id");
								long alarmTime = rs.getLong("alarmTime");
								//计算可以恢复时间
								long restoreTime = alarmTime + restoreInterval*60;
								if (currentTime >= restoreTime) {
									// 自动恢复
									jdbcTemplateMonitor.update(sql_restore, currentTime,id);
								}
							}
						}, alarmItem,maxFrequency);
					}
				});
			}
		}, mailSendTime, mailSendTime, TimeUnit.SECONDS);
	}
	
}
