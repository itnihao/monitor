package com.dataeye.omp.process.kafka.consume;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.dataeye.omp.alarm.kafka.produce.ProducerAdapter;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.common.ProcessAlarmRule;
import com.dataeye.omp.common.ProcessInfo;
import com.dataeye.omp.common.ProcessList;
import com.dataeye.omp.common.ResourceHandler;
import com.dataeye.omp.utils.DateUtils;
import com.dataeye.omp.utils.ServerUtils;
import com.dataeye.util.log.DELogger;
import com.google.gson.reflect.TypeToken;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;

/**
 * 进程监控状态检测
 * 
 * @author chenfanglin
 * @date 2016年3月7日 下午2:33:26
 */
@Service
public class ProcessAlarmHandle {

	private final static Logger logger = DELogger.getLogger("process_Log");

	@Resource(name = "jdbcTemplateMonitor")
	private JdbcTemplate jdbcTemplateMonitor;

	/**
	 * 处理上报进程状态信息
	 * 
	 * @param message
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午2:39:03
	 */
	public void handle(KafkaStream<String, String> stream) {
		try {
			for (MessageAndMetadata<String, String> kafkaMsg : stream) {
				logger.info(kafkaMsg.message());
				if (StringUtil.isNotEmpty(kafkaMsg.message())) {
					ProcessList processList = ProcessList.parseJson(kafkaMsg.message());
					List<ProcessInfo> list = processList.getProcess_list();
					for (ProcessInfo info : list) {
						String processName = info.getName();
						String ip = info.getIp();
						int port = info.getPort();
						int portStatus = info.getPortStatus();
						int processStatus = info.getProcessStatus();
						logger.info("上报消息：" + processName + "," + ip + "," + port + "," + portStatus + "," + processStatus);
						// 1.获取serverID
						int serverID = ServerUtils.getServerIDByIP(ip);
						// 2. 获取告警规则
						ProcessAlarmRule alarmRule = getProcessAlarmRule(processName, port, serverID);
						// 3. 触发告警规则，进入检测逻辑
						checkProcessAlarmRule(alarmRule, processName, port, serverID, processStatus, portStatus);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "获取kafka消息异常：" + e.getMessage());
		}
	}

	/**
	 * 检测告警规则
	 * 
	 * @param alarmRule
	 * @param processName
	 * @param port
	 * @param status
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午3:28:18
	 */
	private void checkProcessAlarmRule(ProcessAlarmRule alarmRule, String processName, int port, int serverID,
			int processStatus, int portStatus) {
		// 1.更新上报时间
		updateReportTime(processName, port, serverID);
		if (alarmRule.getMonitorType() == 2) {
			if (processStatus == 1 || portStatus == 1) {
				// 异常
				coreProcess(alarmRule, processName, port, serverID, processStatus, portStatus);
			} else {
				// 发送异常恢复通知邮件
//				sendProcessRestoreMail(alarmRule, processName, port, serverID, processStatus, portStatus);
				// 正常
				restoreAlarm(processName, port, serverID);
			}
		} else if (alarmRule.getMonitorType() == 1) {
			if (portStatus == 1) {
				coreProcess(alarmRule, processName, port, serverID, processStatus, portStatus);
			} else {
				// 发送异常恢复通知邮件
//				sendProcessRestoreMail(alarmRule, processName, port, serverID, processStatus, portStatus);
				// 正常
				restoreAlarm(processName, port, serverID);
			}
		} else if (alarmRule.getMonitorType() == 0) {
			if (processStatus == 1) {
				coreProcess(alarmRule, processName, port, serverID, processStatus, portStatus);
			} else {
				// 发送异常恢复通知邮件
//				sendProcessRestoreMail(alarmRule, processName, port, serverID, processStatus, portStatus);
				// 正常
				restoreAlarm(processName, port, serverID);
			}
		}

	}

	/**
	 * 核心处理
	 * 
	 * @param alarmRule
	 * @param processName
	 * @param port
	 * @param serverID
	 * @param processStatus
	 * @param portStatus
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午6:40:35
	 */
	private void coreProcess(ProcessAlarmRule alarmRule, String processName, int port, int serverID, int processStatus,
			int portStatus) {
		// 2.获取当前已经告警次数
		int frequency = getCurrentFrequency(processName, port, serverID);
		if (alarmRule.getMaxFrequency() > frequency) {
			// 2.1当前linux时间戳
			long currentTime = DateUtils.unixTimestamp();
			// 2.2获取上次发送告警的时间
			long alarmTime = getLastAlarmTime(processName, port, serverID);
			if (alarmTime == 0) {
				// 第一次发送告警，直接发送
				// 更新告警历史状态
				updateAlarmStatus(processName, port, serverID, frequency);
				// 发送告警邮件
				sendProcessAlarmMail(alarmRule, processName, port, serverID, processStatus, portStatus);
				recordSendAlarm(processName, portStatus, serverID);
			} else {
				// 2.3计算出下次应该发送告警的时间
				long nextAlarmTime = alarmTime + alarmRule.getAlarmInterval() * 60;
				if (currentTime >= nextAlarmTime) {
					// 更新告警历史状态
					updateAlarmStatus(processName, port, serverID, frequency);
					// 3.间隔时间到，发送告警
					sendProcessAlarmMail(alarmRule, processName, port, serverID, processStatus, portStatus);
					// 4.记录发送告警，用作后续统计
					recordSendAlarm(processName, portStatus, serverID);
				}
			}

		} else {
			logger.info(Constant.BASIC_TIME_FORMAT.format(new Date()) + "超过周期内最大告警次数！" + processName + "," + port + ","
					+ serverID);
		}
	}

	/**
	 * 记录发送告警情况
	 * @param processName
	 * @param port
	 * @param serverID
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午6:56:49
	 */
	private void recordSendAlarm(String processName, int port, int serverID){
		long currentTime = DateUtils.unixTimestamp000();
		String sql = "insert into " + Constant.DC_PROCESS_ALARM_EVERY_DAY + "(alarmTime,processName,port,serverID) values(?,?,?,?)";
		try {
			jdbcTemplateMonitor.insert(sql, currentTime,processName,port,serverID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】记录发送告警情况：" + ServerUtils.printStackTrace(e));
		}
		
	}
	/**
	 * 发送恢复通知邮件
	 * 
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午6:44:23
	 */
//	private void sendProcessRestoreMail(ProcessAlarmRule alarmRule, String processName, int port, int serverID,
//			int processStatus, int portStatus){
//		String email = getAlarmAddress(processName, port, serverID);
//		String subject = ResourceHandler.getProperties("process_restore");
//		String content = getAlarmContent(alarmRule, processName, port, serverID, processStatus, portStatus);
//		ProducerAdapter.sendAlarmTOKafka(email, subject, content);
//	}
	
	/**
	 * 发送告警邮件
	 * 
	 * @param processName
	 * @param port
	 * @param serverID
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午4:46:12
	 */
	private void sendProcessAlarmMail(ProcessAlarmRule alarmRule, String processName, int port, int serverID,
			int processStatus, int portStatus) {
		String email = getAlarmAddress(processName, port, serverID);
		String subject = ResourceHandler.getProperties("process_alarm");
		String content = getAlarmContent(alarmRule, processName, port, serverID, processStatus, portStatus);
		
		String alarmType = alarmRule.getAlarmType();
		List<Integer> list = Constant.GSON.fromJson(alarmType, new TypeToken<List<Integer>>(){}.getType());
		if (list.size() > 0) {
			for (int type : list) {
				if (type == 1) {
					// 告警邮件
					ProducerAdapter.sendAlarmTOKafka(email, subject, content);
				} else if (type == 0) {
					// 告警短信
					ServerUtils.sendAlarmSms(email, content);
				} else if (type == 2) {
					// APP告警
					ServerUtils.sendAppAlarm(email, subject, content);
				}
			}
		} else {
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "告警类型没有选择。");
		}
		
	}

	/**
	 * 获取告警内容
	 * 
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午4:57:05
	 */
	private String getAlarmContent(ProcessAlarmRule alarmRule, String processName, int port, int serverID,
			int processStatus, int portStatus) {
		StringBuffer sb = new StringBuffer();
		try {
			String sql_mk = "select moduleID from " + Constant.DC_PROCESS_INFO + " where processName = ? and port = ? and serverID = ?";
			int moduleID = jdbcTemplateMonitor.queryForInt(sql_mk, processName,port,serverID);
			String sql_yw = "select businessID from " + Constant.DC_PROCESS_INFO + " where processName = ? and port = ? and serverID = ?";
			int ywID = jdbcTemplateMonitor.queryForInt(sql_yw, processName,port,serverID);
			String sql_name = "select name from " + Constant.BUSINESS + " where id = ?";
			// 模块名称
			String moduleName = jdbcTemplateMonitor.queryForString(sql_name, moduleID);
			// 业务名称
			String ywName = jdbcTemplateMonitor.queryForString(sql_name, ywID);
			String sql_host = "select host_name from " + Constant.SERVER_LIST + " where id = ?";
			// 主机名
			String hostName = jdbcTemplateMonitor.queryForString(sql_host, serverID);
			String sql_nw_ip = "select group_concat(ip) from " + Constant.SERVER_IP_LIST
					+ " where svr_id = ? and type = 0";
			// 内网
			String nw = jdbcTemplateMonitor.queryForString(sql_nw_ip, serverID);
			String sql_ww_ip = "select group_concat(ip) from " + Constant.SERVER_IP_LIST
					+ " where svr_id = ? and type = 1";
			// 外网
			String ww = jdbcTemplateMonitor.queryForString(sql_ww_ip, serverID);

			sb.append(ResourceHandler.getProperties("ssyw")).append(ywName).append("\n")
					.append(ResourceHandler.getProperties("ssmk")).append(moduleName).append("\n")
					.append(ResourceHandler.getProperties("host_name")).append(hostName).append("\n")
					.append(ResourceHandler.getProperties("nw_value")).append(nw).append("\n")
					.append(ResourceHandler.getProperties("ww_value")).append(ww == null ? "" : ww).append("\n")
					.append(ResourceHandler.getProperties("process_name")).append(processName).append("\n")
					.append(ResourceHandler.getProperties("port_value")).append(port).append("\n");
			if (alarmRule.getMonitorType() == 0) {
				sb.append(ResourceHandler.getProperties("process_status"))
						.append(processStatus == 1 ? ResourceHandler.getProperties("process_fail") : ResourceHandler
								.getProperties("process_ok")).append("\n");
			} else if (alarmRule.getMonitorType() == 1) {
				sb.append(ResourceHandler.getProperties("port_status"))
						.append(portStatus == 1 ? ResourceHandler.getProperties("port_fail") : ResourceHandler
								.getProperties("port_ok")).append("\n");
			} else {
				sb.append(ResourceHandler.getProperties("process_status"))
						.append(processStatus == 1 ? ResourceHandler.getProperties("process_fail") : ResourceHandler
								.getProperties("process_ok"))
						.append("\n")
						.append(ResourceHandler.getProperties("port_status"))
						.append(portStatus == 1 ? ResourceHandler.getProperties("port_fail") : ResourceHandler
								.getProperties("port_ok")).append("\n");
			}
			logger.info(Constant.BASIC_TIME_FORMAT.format(new Date()) + "获取告警内容:" + sb.toString() + "==" + processName
					+ "," + port + "," + serverID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】获取告警内容：" + e.getMessage());
		}
		return sb.toString();
	}

	/**
	 * 获取告警收件人
	 * 
	 * @param processName
	 * @param port
	 * @param serverID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午4:26:23
	 */
	private String getAlarmAddress(String processName, int port, int serverID) {
		String sql_main = "select b.email from " + Constant.DC_PROCESS_INFO + " a," + Constant.EMPLOYEE
				+ " b where a.mainPrincipal = b.id " + "and a.processName = ? and a.port = ? and a.serverID = ?";
		String sql_others = "select bakPrincipal from " + Constant.DC_PROCESS_INFO
				+ " where processName = ? and port = ? and serverID = ?";
		String sql_name = "select email from "+Constant.EMPLOYEE+" where id = ?";
		try {
			// 获取主要负责人
			String mail = jdbcTemplateMonitor.queryForString(sql_main, processName, port, serverID);
			String bak_id = jdbcTemplateMonitor.queryForString(sql_others, processName,port,serverID);
			List<String> list = new ArrayList<String>();
			if (StringUtil.isNotEmpty(bak_id)) {
				String[] ids = bak_id.split(",");
				for (int i = 0;i < ids.length;i++) {
					String name = jdbcTemplateMonitor.queryForString(sql_name, ids[i]);
					list.add(name);
				}
			}
			String bak_email = StringUtil.join(Constant.SEPARATOR, list);
			if (StringUtil.isNotEmpty(bak_email)) {
				mail = mail + Constant.SEPARATOR + bak_email;
			}
			String omp = ResourceHandler.getProperties("alarm_omp");
			if (StringUtil.isNotEmpty(mail)) {
				mail = mail + Constant.SEPARATOR + omp;
			}
			logger.info(Constant.BASIC_TIME_FORMAT.format(new Date()) + "获取邮件收件人, mail:" + mail + ">>" + processName
					+ "," + port + "," + serverID);
			return mail;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】获取邮件收件人：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 更新告警历史状态
	 * 
	 * @param processName
	 * @param port
	 * @param serverID
	 * @param frequency
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午4:18:10
	 */
	private void updateAlarmStatus(String processName, int port, int serverID, int frequency) {
		long currentTime = DateUtils.unixTimestamp();
		String sql = "update " + Constant.DC_PROCESS_ALARM_CONTROL + " set alarmTime = ?,frequency = ?,status = 1 "
				+ "where processName = ? and port = ? and serverID = ?";
		try {
			frequency++;
			jdbcTemplateMonitor.update(sql, currentTime, frequency, processName, port, serverID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】更新告警历史状态：" + e.getMessage());
		}
	}

	/**
	 * 获取上次发送告警时间
	 * 
	 * @param processName
	 * @param port
	 * @param serverID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午4:07:35
	 */
	private long getLastAlarmTime(String processName, int port, int serverID) {
		String sql = "select alarmTime from " + Constant.DC_PROCESS_ALARM_CONTROL
				+ " where processName = ? and port = ? and serverID = ?";
		try {
			long alarmTime = jdbcTemplateMonitor.queryForLong(sql, processName, port, serverID);
			return alarmTime;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】异常已经恢复：" + e.getMessage());
		}
		return 0;
	}

	/**
	 * 异常已经恢复
	 * 
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午4:00:25
	 */
	private void restoreAlarm(String processName, int port, int serverID) {
		long currentTime = DateUtils.unixTimestamp();
		String sql = "update " + Constant.DC_PROCESS_ALARM_CONTROL + " set restoreTime = ?, frequency = 0, status = 0 "
				+ "where processName = ? and port = ? and serverID = ?";
		try {
			jdbcTemplateMonitor.update(sql, currentTime, processName, port, serverID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】异常已经恢复：" + e.getMessage());
		}
	}

	/**
	 * 获取当前已经告警次数
	 * 
	 * @param processName
	 * @param port
	 * @param serverID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午3:47:16
	 */
	private int getCurrentFrequency(String processName, int port, int serverID) {
		String sql = "select frequency from " + Constant.DC_PROCESS_ALARM_CONTROL
				+ " where processName = ? and port = ? and serverID = ?";
		try {
			int frequency = jdbcTemplateMonitor.queryForInt(sql, processName, port, serverID);
			return frequency;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】获取当前已经告警次数：" + e.getMessage());
		}
		return 0;
	}

	/**
	 * 更新上报时间
	 * 
	 * @param processName
	 * @param port
	 * @param serverID
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午3:36:16
	 */
	private void updateReportTime(String processName, int port, int serverID) {
		String sql = "select count(*) from " + Constant.DC_PROCESS_ALARM_CONTROL
				+ " where processName = ? and port = ? and serverID = ?";
		try {
			int count = jdbcTemplateMonitor.queryForInt(sql, processName, port, serverID);
			long currentTime = DateUtils.unixTimestamp();
			if (count > 0) {
				// 存在，更新上报时间
				String sql_update = "update " + Constant.DC_PROCESS_ALARM_CONTROL
						+ " set reporteTime = ? where processName = ? and port = ? and serverID = ?";
				jdbcTemplateMonitor.update(sql_update, currentTime, processName, port, serverID);
			} else {
				// 不存在，将上报数据插入数据库
				String sql_insert = "insert into " + Constant.DC_PROCESS_ALARM_CONTROL
						+ "(processName,port,serverID,reporteTime) values(?,?,?,?)";
				jdbcTemplateMonitor.insert(sql_insert, processName, port, serverID, currentTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】更新上报时间：" + e.getMessage());
		}

	}

	/**
	 * 获取告警规则
	 * 
	 * @param processName
	 * @param port
	 * @param serverID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午3:15:15
	 */
	private ProcessAlarmRule getProcessAlarmRule(String processName, int port, int serverID) {
		final ProcessAlarmRule rule = new ProcessAlarmRule();
		String sql = "select a.id processID,b.monitorType,b.id,b.alarmInterval,b.maxFrequency,b.alarmType from "
				+ Constant.DC_PROCESS_INFO + " a, " + Constant.DC_PROCESS_ALARM_RULE + " b "
				+ "where a.id = b.processID and a.processName = ? and a.port = ? and a.serverID = ? and a.status = 1";
		try {
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {

				public void processRow(ResultSet rs) throws SQLException {
					long processID = rs.getLong("processID");
					int monitorType = rs.getInt("monitorType");
					long id = rs.getLong("id");
					int alarmInterval = rs.getInt("alarmInterval");
					int maxFrequency = rs.getInt("maxFrequency");
					String alarmType = rs.getString("alarmType");
					rule.setId(id);
					rule.setProcessID(processID);
					rule.setMonitorType(monitorType);
					rule.setAlarmInterval(alarmInterval);
					rule.setMaxFrequency(maxFrequency);
					rule.setAlarmType(alarmType);
				}
			}, processName, port, serverID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】查询告警规则：" + e.getMessage());
		}
		return rule;
	}
}
