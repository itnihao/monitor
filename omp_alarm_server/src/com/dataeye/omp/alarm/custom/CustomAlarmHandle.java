package com.dataeye.omp.alarm.custom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.dataeye.omp.alarm.kafka.produce.ProducerAdapter;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.common.CustomAlarmRule;
import com.dataeye.omp.common.ResourceHandler;
import com.dataeye.omp.utils.DateUtils;
import com.dataeye.omp.utils.ServerUtils;
import com.dataeye.util.log.DELogger;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;

/**
 * 自定义告警规则检测中心
 * @author chenfanglin
 * @date 2016年3月8日 下午12:08:21
 */
@Service
public class CustomAlarmHandle {
	
	private final static Logger logger = DELogger.getLogger("custom_Log");

	@Resource(name = "jdbcTemplateMonitor")
	private JdbcTemplate jdbcTemplateMonitor;

	/**
	 * 处理自定义告警
	 *
	 * @param alarmItem
	 * @param alarmObject
	 * @param subject
	 * @param content
	 * @author chenfanglin <br>
	 * @date 2016年3月8日 下午12:11:39
	 */
	public void handle(String alarmItem
			, String alarmObject
			, String subject
			, String content) {
		if (StringUtil.isEmpty(alarmObject)) {
			alarmObject = "";
		}
		// 1.获取告警规则
		CustomAlarmRule rule = getCustomAlarmRule(alarmItem);
		if (rule.getId() > 0) {
			// 2.更新上报时间
			updateReportTime(alarmItem, alarmObject);
			// 3.当前已经告警次数
			int frequency = getCurrentFrequency(alarmItem, alarmObject);
			if (rule.getMaxFrequency() > frequency) {
				long alarmTime = getLastAlarmTime(alarmItem, alarmObject);
				long currentTime = DateUtils.unixTimestamp();
				if (alarmTime == 0) {
					updateAlarmStatus(alarmItem, alarmObject, frequency);
					sendCustomAlarmMail(alarmItem, alarmObject, subject, content, rule.getAlarmType());
					recordSendAlarm(alarmItem, alarmObject);
				} else {
					// 计算下次应该告警的时间
					long nextAlarmTime = alarmTime + rule.getAlarmInterval() * 60;
					if (currentTime >= nextAlarmTime) {
						updateAlarmStatus(alarmItem, alarmObject, frequency);
						sendCustomAlarmMail(alarmItem, alarmObject, subject, content, rule.getAlarmType());
						recordSendAlarm(alarmItem, alarmObject);
					}
				}
			} else {
				logger.info(Constant.BASIC_TIME_FORMAT.format(new Date()) + "超过周期内最大告警次数！" + alarmItem + "," + alarmObject);
			}
		} else {
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】不存在告警规则，请设置：" + alarmItem);
		}
	}
	
	/**
	 * 记录已经发送的告警
	 * @param alarmItem
	 * @param alarmObject
	 * @author chenfanglin <br>
	 * @date 2016年3月8日 下午3:42:14
	 */
	private void recordSendAlarm(String alarmItem, String alarmObject) {
		long currentTime = DateUtils.unixTimestamp000();
		String sql = "insert into " + Constant.DC_ALARM_CUSTOMIZE_EVERY_DAY + "(alarmTime,alarmItem,alarmObject) values(?,?,?)";
		try {
			jdbcTemplateMonitor.insert(sql, currentTime,alarmItem,alarmObject);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】记录发送告警情况：" + e.getMessage());
		}
	}

	/**
	 * 发送告警
	 * 
	 * @author chenfanglin <br>
	 * @date 2016年3月8日 下午3:27:40
	 */
	private void sendCustomAlarmMail(String alarmItem, String alarmObject, String subject, String content, String alarmType){
		String email = getAlarmAddress(alarmItem);
		String sub = ResourceHandler.getProperties("custom_subject") + subject;
		String cont = getAlarmContent(alarmItem,alarmObject,content);
		if (StringUtil.isEmpty(email) || StringUtil.isEmpty(subject) || StringUtil.isEmpty(content)) {
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "收件人，主题，内容为空：" + email + "," + subject + "," + content);
		} else {
			logger.info("subject:" + sub + " alarmType:" + alarmType);
			String[] types = alarmType.split(",");
			for (int i = 0;i < types.length;i++) {
				int type = Integer.parseInt(types[i]);
				logger.info("length:" + types.length + "i:" + i + " type:" + type);
				if (type == 2) {
					// 发送邮件
					logger.info("send alarm mail to kafka");
					ProducerAdapter.sendAlarmTOKafka(email, sub, cont);
				} else if (type == 0) {
					logger.info("send alarm  to app");
					// 发送APP告警
					ServerUtils.sendAppAlarm(email, sub, cont);
				} else if (type == 1) {
					// 发送短信告警
					ServerUtils.sendAlarmSms(email, content);
				}
			}
		}
	}
	
	/**
	 * 获取告警内容
	 * @param alarmItem
	 * @param alarmObject
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月8日 下午3:38:40
	 */
	private String getAlarmContent(String alarmItem, String alarmObject, String content) {
		StringBuffer sb = new StringBuffer();
		String sql = "select businessID from " + Constant.DC_ALARM_CUSTOMIZE_INFO + " where alarmItem = ? and status = 1";
		try {
			int businessID = jdbcTemplateMonitor.queryForInt(sql, alarmItem);
			String sql_ywname = "select name from " + Constant.BUSINESS + " where id = ?";
			String ywname = jdbcTemplateMonitor.queryForString(sql_ywname, businessID);
			sb.append(ResourceHandler.getProperties("ssyw")).append(ywname).append("\n")
			.append(ResourceHandler.getProperties("custom_alarm_item")).append(alarmItem).append("\n")
			.append(ResourceHandler.getProperties("custom_alarm_object")).append(alarmObject).append("\n")
			.append(content);
			logger.info(Constant.BASIC_TIME_FORMAT.format(new Date()) + "获取告警内容:" + sb.toString() + ">>" + alarmItem
					+ "," + alarmObject);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】获取告警内容：" + e.getMessage());
		}
		return sb.toString();
	}

	/**
	 * 获取告警收件人
	 * @param alarmItem
	 * @author chenfanglin <br>
	 * @date 2016年3月8日 下午3:30:29
	 */
	private String getAlarmAddress(String alarmItem){
		String sql_main = "select b.email from " + Constant.DC_ALARM_CUSTOMIZE_INFO + " a, " + Constant.EMPLOYEE + " b "
				+ "where a.mainEmployee = b.id and a.status = 1 and a.alarmItem = ?";
		String sql_back = "select others from " + Constant.DC_ALARM_CUSTOMIZE_INFO + " where alarmItem = ?";
		String sql_email = "select email from " + Constant.EMPLOYEE + " where id = ?";
		try {
			String mail_email = jdbcTemplateMonitor.queryForString(sql_main, alarmItem);
			String others = jdbcTemplateMonitor.queryForString(sql_back, alarmItem);
			String back_email = "";
			if (StringUtil.isNotEmpty(others)) {
				String[] s = others.split(Constant.SEPARATOR);
				for (int i = 0;i < s.length;i++) {
					String email = jdbcTemplateMonitor.queryForString(sql_email, s[i]);
					if (StringUtil.isEmpty(back_email)) {
						back_email = email;
					} else {
						back_email = back_email + Constant.SEPARATOR + email;
					}
				}
			}
			String omp = ResourceHandler.getProperties("alarm_omp");
			if (StringUtil.isNotEmpty(mail_email)) {
				mail_email = mail_email + Constant.SEPARATOR + omp;
			}
			if (StringUtil.isNotEmpty(back_email)) {
				mail_email = mail_email + Constant.SEPARATOR + back_email;
			}
			logger.info(Constant.BASIC_TIME_FORMAT.format(new Date()) + "获取邮件收件人, mail:" + mail_email + ">>" + alarmItem);
			return mail_email;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】获取邮件收件人：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 更新告警历史状态
	 * @param alarmItem
	 * @param alarmObject
	 * @param frequency
	 * @author chenfanglin <br>
	 * @date 2016年3月8日 下午3:23:29
	 */
	private void updateAlarmStatus(String alarmItem, String alarmObject,int frequency){
		long currentTime = DateUtils.unixTimestamp();
		String sql = "update " + Constant.DC_ALARM_CUSTOMIZE_CONTROL + " set alarmTime = ?,frequency = ?"
				+ " where alarmItem = ? and alarmObject = ?";
		try {
			frequency++;
			jdbcTemplateMonitor.update(sql, currentTime, frequency, alarmItem, alarmObject);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】更新告警历史状态：" + e.getMessage());
		}
	}
	
	/**
	 * 获取上次告警时间
	 * @param alarmItem
	 * @param alarmObject
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月8日 下午3:07:16
	 */
	private long getLastAlarmTime(String alarmItem, String alarmObject){
		String sql = "select alarmTime from " + Constant.DC_ALARM_CUSTOMIZE_CONTROL + " where alarmItem = ? and alarmObject = ?";
		try {
			long alarmTime = jdbcTemplateMonitor.queryForLong(sql, alarmItem, alarmObject);
			return alarmTime;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】异常已经恢复：" + e.getMessage());
		}
		return 0;
	}
	
	/**
	 * 获取当前已经告警次数
	 * @param alarmItem
	 * @param alarmObject
	 * @author chenfanglin <br>
	 * @date 2016年3月8日 下午2:42:08
	 */
	private int getCurrentFrequency(String alarmItem, String alarmObject){
		String sql = "select frequency from " + Constant.DC_ALARM_CUSTOMIZE_CONTROL + " where alarmItem = ? and alarmObject = ?";
		try {
			int frequency = jdbcTemplateMonitor.queryForInt(sql, alarmItem,alarmObject);
			return frequency;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】获取当前已经告警次数：" + e.getMessage());
		}
		return 0;
	}
	
	/**
	 * 更新上报时间
	 * @param alarmItem
	 * @param alarmObject
	 * @author chenfanglin <br>
	 * @date 2016年3月8日 下午2:36:28
	 */
	private void updateReportTime(String alarmItem,  String alarmObject) {
		String sql = "select count(*) from " + Constant.DC_ALARM_CUSTOMIZE_CONTROL + " where alarmItem = ? and alarmObject = ?";
		try {
			int count = jdbcTemplateMonitor.queryForInt(sql, alarmItem,alarmObject);
			long currentTime = DateUtils.unixTimestamp();
			if (count > 0) {
				// 存在，更新上报时间
				String sql_update = "update " + Constant.DC_ALARM_CUSTOMIZE_CONTROL
						+ " set reporteTime = ? where alarmItem = ? and alarmObject = ?";
				jdbcTemplateMonitor.update(sql_update, currentTime, alarmItem, alarmObject);
			} else {
				// 不存在，将上报数据插入数据库
				String sql_insert = "insert into " + Constant.DC_ALARM_CUSTOMIZE_CONTROL
						+ "(alarmItem,alarmObject,reporteTime) values(?,?,?)";
				jdbcTemplateMonitor.insert(sql_insert, alarmItem, alarmObject,currentTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】更新上报时间：" + e.getMessage());
		}

	}
	/**
	 * 获取告警规则
	 * @param alarmItem
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月8日 下午2:30:35
	 */
	private CustomAlarmRule getCustomAlarmRule(String alarmItem){
		final CustomAlarmRule rule = new CustomAlarmRule();
		String sql = "select a.id,a.customizeID,a.alarmInterval,a.maxFrequency,a.restoreType,a.restoreInterval,a.alarmType from "
		+ Constant.DC_ALARM_RULE_CUSTOMIZE + " a, " + Constant.DC_ALARM_CUSTOMIZE_INFO + " b where a.customizeID = b.id and b.alarmItem = ? and status = 1";
		try {
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				
				public void processRow(ResultSet rs) throws SQLException {
					long id = rs.getLong("id");
					long customizeID = rs.getLong("customizeID");
					int alarmInterval = rs.getInt("alarmInterval");
					int maxFrequency = rs.getInt("maxFrequency");
					int restoreType = rs.getInt("restoreType");
					int restoreInterval = rs.getInt("restoreInterval");
					String alarmType = rs.getString("alarmType");
					rule.setId(id);
					rule.setCustomizeID(customizeID);
					rule.setAlarmInterval(alarmInterval);
					rule.setMaxFrequency(maxFrequency);
					rule.setRestoreType(restoreType);
					rule.setRestoreInterval(restoreInterval);
					rule.setAlarmType(alarmType);
				}
			}, alarmItem);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】获取告警规则：" + ServerUtils.printStackTrace(e));
		}
		return rule;
	}
}
