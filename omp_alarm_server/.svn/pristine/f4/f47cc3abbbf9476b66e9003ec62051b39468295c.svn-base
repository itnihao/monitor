package com.dataeye.omp.jobs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.dataeye.util.log.DELogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.omp.alarm.server.AlarmServerUtils;
import com.dataeye.omp.common.AlarmMail;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.event.Launch;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import com.xunlei.spring.Config;

@Service
public class AlarmMailTask {
	
	@Resource(name = "jdbcTemplateMonitor")
	private JdbcTemplate jdbcTemplateMonitor;
	
	@Autowired
	private AlarmServerUtils alarmServerUtils;

	@Config
	private int mailSendTime = 30;

	private final static Logger logger = DELogger.getLogger("mail_log");
	
	public void alarmMailTask(){
		Launch.SCHEDULER.scheduleAtFixedRate(new Runnable() {
			public void run() {
				try {
					alarm();
				} catch (Throwable e) {
					logger.error("send email error!", e);
				}

			}
		}, mailSendTime, mailSendTime, TimeUnit.SECONDS);
	}

	private void alarm() {
		//1. 查询到所有的未读邮件
		String sql = "select id,addressee,subject,content from " + Constant.DC_ALARM_MAIL + " where status = 0";
		final Map<String, AlarmMail> mailMap = new HashMap<String, AlarmMail>();
		jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				long id = rs.getLong("id");
				String sql_update = "update " + Constant.DC_ALARM_MAIL + " set status = 1 where id = ?";
				jdbcTemplateMonitor.update(sql_update, id);
				String addressee = rs.getString("addressee");
				String subject = rs.getString("subject");
				String content = rs.getString("content");
				AlarmMail mail = new AlarmMail(addressee, subject, content);
				mail.setId(id);
				// 邮件合并
				AlarmMail mailFromMap = mailMap.get(addressee);
				if (mailFromMap != null) {
					// 同一次发送过程中有多个发送给同一人的邮件，把邮件进行合并，只发一封
					mailFromMap.incSize(1);
					mailFromMap.setSubject("告警合并【共" + mailFromMap.getSize() + "条】");
					mailFromMap.appendContent(mail.getContent());
					logger.info("告警合并【共" + mailFromMap.getSize() + "条】");
				} else {
					mailMap.put(addressee, mail);
				}
				logger.info("update mail info");

			}
		});
		for (Map.Entry<String, AlarmMail> mail : mailMap.entrySet()) {
			AlarmMail currentMail = mail.getValue();
			alarmServerUtils.sendAlarmMail(currentMail);
			logger.info("send mail");
		}
	}
	
}
