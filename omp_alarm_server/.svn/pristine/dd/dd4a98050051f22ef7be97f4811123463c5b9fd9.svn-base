package com.dataeye.omp.detect.kafka.consume;

import java.io.*;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.dataeye.omp.alarm.kafka.produce.ProducerAdapter;
import com.dataeye.omp.common.AlarmBasicRule;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.common.Feature;
import com.dataeye.omp.common.FeatureMsg;
import com.dataeye.omp.common.ResourceHandler;
import com.dataeye.omp.dbproxy.mysql.MysqlUtil;
import com.dataeye.omp.utils.DateUtils;
import com.dataeye.omp.utils.ServerUtils;
import com.dataeye.util.log.DELogger;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;

/**
 * 告警规则检测 处理器
 * 
 * @author chenfanglin
 * @date 2016年1月26日 下午6:08:01
 */
@Service
public class DetectAlarmRuleHandler {

	private final static Logger kafka_Log = DELogger.getLogger("kafka_Log");
	
	@Resource(name = "jdbcTemplateMonitor")
	private JdbcTemplate jdbcTemplateMonitor;

	private static Map<String,DcAlarmControl>  dcAlarmControlMap;

	static{
		dcAlarmControlMap = loadControInfo();
	}


	private static Map<String,DcAlarmControl> loadControInfo(){
		final Map<String, DcAlarmControl> controlMap = new HashMap<>();
		String sql = "select id, featureID, object, serverID, reporteTime,alarmTime," +
				"startTime,restoreTime,frequency,status from " + Constant.DC_ALARM_HISTORY;
		JdbcTemplate jdbcTemplateMonitor =
				(JdbcTemplate) new ClassPathXmlApplicationContext("applicationContext.xml").getBean("jdbcTemplateMonitor");
		jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				DcAlarmControl dcAlarmControl = new DcAlarmControl();
				int serverId = rs.getInt("serverID");
				int fid = rs.getInt("featureID");
				String object = rs.getString("object");
				dcAlarmControl.setId(rs.getInt("id"));
				dcAlarmControl.setfId(fid);
				dcAlarmControl.setObject(object);
				dcAlarmControl.setServerId(serverId);
				dcAlarmControl.setReportTime(rs.getLong("reporteTime"));
				dcAlarmControl.setAlarmTime(rs.getLong("alarmTime"));
				dcAlarmControl.setStartTime(rs.getLong("startTime"));
				dcAlarmControl.setRestoreTime(rs.getLong("restoreTime"));
				dcAlarmControl.setFrequency(rs.getInt("frequency"));
				dcAlarmControl.setStatus(rs.getInt("status"));
				controlMap.put(serverId + "_" + fid + "_" + object, dcAlarmControl
				);
			}
		});
		return controlMap;
	}

	/**
	 * 处理上报信息
	 *
	 * @param stream
	 * @author chenfanglin <br>
	 * @date 2016年4月14日 下午7:37:58
	 */
	public void handle(KafkaStream<String, String> stream) {
		String message = null;
		try {
			for (MessageAndMetadata<String, String> kafkaMsg : stream) {
				kafka_Log.info("partition:" + kafkaMsg.partition() + " offset:" + kafkaMsg.offset());
				FeatureMsg featureMsg = FeatureMsg.parseJson(kafkaMsg.message());
				if (null != featureMsg) {
					List<Feature> list = featureMsg.getFeature_list();
					for (Feature feature : list) {
						if (isValidDate(feature.getTime()) > 0) {
							if (isAlarmFeature(feature.getFid())) {
								checkAlarmRule(feature);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(new File("/usr/local/htdocs/monitor/omp_alarm_server/logs/logs/exeception.log"));

				PrintWriter writer = new PrintWriter(outputStream);
				e.printStackTrace(writer);

				writer.println();
				writer.close();

			} catch (Exception e1) {

			}

			kafka_Log.error(Constant.BASIC_TIME_FORMAT.format(new Date()) +
					"获取kafka消息异常：" );
		}
	}
	
	/**
	 * 检测告警规则
	 * 
	 * @param feature
	 * @author chenfanglin <br>
	 * @throws Exception 
	 * @date 2016年1月26日 下午7:21:35
	 */
	private void checkAlarmRule(Feature feature) throws Exception {
		int featureID = feature.getFid();
		String object = feature.getObject();
		String ip = feature.getClient();
		BigDecimal value = feature.getValue();
		String time = feature.getTime();
		// 获取serverID
		int serverID = ServerUtils.getServerIDByIP(ip);
		List<AlarmBasicRule> list = new ArrayList<AlarmBasicRule>();
		if (Constant.FREEPARTITION == featureID && Constant.ROOT.equals(object)) {
			// 获取磁盘根分区告警规则
			list = getAlarmRuleFromMysql(Constant.ROOTPARTITION);
		} else if (Constant.FREEPARTITION == featureID && !Constant.ROOT.equals(object)) {
			// 获取磁盘分区告警规则
			list = getAlarmRuleFromMysql(Constant.DISKPARTITION);
		} else if (Constant.PHYSIC_MEMORY_USE_FEATURE == featureID) {
			// 获取物理内存 告警规则
			list = getAlarmRuleFromMysql(Constant.MEMORYPRIVATE);
		} else if (featureID == Constant.SVCTMTIMEMAX || featureID == Constant.AWAITTIMEMAX 
				|| featureID == Constant.AVGQUSZMAX || featureID == Constant.AVGRQSZ
				|| featureID == Constant.UTILMAX) {
			// IO
			list = getAlarmRuleFromMysql(featureID);
		} else {
			list = getAlarmRuleFromMysql(featureID, object);
		}
		coreProcess(list, featureID, object, serverID, value,time);
	}

	/**
	 * 特殊处理，获取磁盘根分区和其他分区的告警规则
	 * @param featureID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月10日 上午11:39:07
	 */
	private List<AlarmBasicRule> getAlarmRuleFromMysql(int featureID) {
		final List<AlarmBasicRule> list = new ArrayList<AlarmBasicRule>();
		try {
//			JdbcTemplate jdbcTemplateMonitor = MysqlUtil.getJdbcTemplateMonitor();
			String sql = "select id,featureID,object,servers,alarmObjectType,businessID,moduleID,groupID from " + Constant.DC_ALARM_RULE_BASIC + " where featureID = ? and status = 0";
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					AlarmBasicRule rule = new AlarmBasicRule();
					long id = rs.getLong("id");
					int featureID = rs.getInt("featureID");
					String object = rs.getString("object");
					String servers = rs.getString("servers");
					int alarmObjectType = rs.getInt("alarmObjectType");
					int businessID = rs.getInt("businessID");
					int moduleID = rs.getInt("moduleID");
					int groupID = rs.getInt("groupID");
					rule.setId(id);
					rule.setFeatureID(featureID);
					rule.setObject(object);
					rule.setServers(servers);
					rule.setAlarmObjectType(alarmObjectType);
					rule.setBusinessID(businessID);
					rule.setModuleID(moduleID);
					rule.setGroupID(groupID);
					list.add(rule);
				}
			}, featureID);
		} catch (Exception e) {
			e.printStackTrace();
			kafka_Log.error("【异常】获取告警规则：" + e.getMessage());
			//TODO
		}
		return list;
	}
	
	/**
	 * 获取告警规则
	 * 
	 * @param featureID
	 * @param object
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月27日 上午11:20:28
	 */
	private List<AlarmBasicRule> getAlarmRuleFromMysql(int featureID, String object) {
		final List<AlarmBasicRule> list = new ArrayList<AlarmBasicRule>();
		try {
			String sql = "select id,featureID,object,servers,alarmObjectType,businessID,moduleID,groupID from "
					+ Constant.DC_ALARM_RULE_BASIC + " where featureID = ? and object = ? and status = 0";
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					AlarmBasicRule rule = new AlarmBasicRule();
					long id = rs.getLong("id");
					int featureID = rs.getInt("featureID");
					String object = rs.getString("object");
					String servers = rs.getString("servers");
					int alarmObjectType = rs.getInt("alarmObjectType");
					int businessID = rs.getInt("businessID");
					int moduleID = rs.getInt("moduleID");
					int groupID = rs.getInt("groupID");
					rule.setId(id);
					rule.setFeatureID(featureID);
					rule.setObject(object);
					rule.setServers(servers);
					rule.setAlarmObjectType(alarmObjectType);
					rule.setBusinessID(businessID);
					rule.setModuleID(moduleID);
					rule.setGroupID(groupID);
					list.add(rule);
				}
			}, featureID, object);
		} catch (Exception e) {
			kafka_Log.error("【异常】获取告警规则：" + e.getMessage());
		}
		return list;
	}

	/**
	 * 检测并发送告警信息
	 * 
	 * @author chenfanglin <br>
	 * @throws Exception 
	 * @date 2016年1月27日 上午11:28:41
	 */
	private void coreProcess(List<AlarmBasicRule> listRule, int featureID,
			String object, int serverID, BigDecimal value,String time) throws Exception {
		long currentTime = DateUtils.unixTimestamp();

		//  检测上报的机器是否设置了告警规则
		long id = isExistAlarmRule(listRule, serverID);
		if (id != 0) {
			// 3. 检测当前时间是否在设置的屏蔽时间之内
			String sql_shield = "select count(1) from " + Constant.DC_ALARM_RULE_BASIC
					+ " where id = ? and shieldStart >= ? and shieldEnd <= ?";
			int total = jdbcTemplateMonitor.queryForInt(sql_shield, id, currentTime, currentTime);
			if (total == 0) {
				// 4.不在屏蔽时间之内，将上报数据和告警规则对比
				kafka_Log.info("上报值："+value+"||"+ featureID+"," + object + "," + serverID);
				AlarmBasicRule alarmRule = getAlarmRuleByID( id);
				if (alarmRule.getAlarmSectionType() == 0) {
					// 阀值
					checkRuleInfo(value, featureID, object, serverID, alarmRule,time);
				} else {
					// 环比
					checkRuleInfo(value, featureID, object, serverID, alarmRule,time);
				}
			}
		}
	}

	/**
	 * 查询告警规则
	 * @param id
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年3月4日 下午8:38:59
	 */
	private AlarmBasicRule getAlarmRuleByID(long id) throws ServerException{
		final AlarmBasicRule rule = new AlarmBasicRule();
		String sql = "select alarmSectionType,maxThreshold,minThreshold,maxMoM,minMoM,maxFrequency,alarmType from "
				+ Constant.DC_ALARM_RULE_BASIC + " where id = ?";

		try {
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				
				public void processRow(ResultSet rs) throws SQLException {
					int alarmSectionType = rs.getInt("alarmSectionType");
					BigDecimal maxThreshold = rs.getBigDecimal("maxThreshold");
					BigDecimal minThreshold = rs.getBigDecimal("minThreshold");
					BigDecimal maxMoM = rs.getBigDecimal("maxMoM");
					BigDecimal minMoM = rs.getBigDecimal("minMoM");
					int maxFrequency = rs.getInt("maxFrequency");
					String alarmType = rs.getString("alarmType");
					rule.setAlarmSectionType(alarmSectionType);
					rule.setMaxThreshold(maxThreshold);
					rule.setMinThreshold(minThreshold);
					rule.setMaxMoM(maxMoM);
					rule.setMinMoM(minMoM);
					rule.setMinThreshold(minThreshold);
					rule.setMaxFrequency(maxFrequency);
					rule.setAlarmType(alarmType);
				}
			}, id);
		} catch (Exception e) {
			e.printStackTrace();
			kafka_Log.error(Constant.BASIC_TIME_FORMAT.format(new Date())+"【异常】查询告警规则：" + e.getMessage());
		}
		return rule;
	}
	
	/**
	 * 比较上报值并且发送告警信息
	 * @param value
	 * @param featureID
	 * @param object
	 * @param serverID
	 * @param alarmRule
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年3月1日 下午5:26:18
	 */
	private synchronized void checkRuleInfo(BigDecimal value,int featureID,String object,
			int serverID,AlarmBasicRule alarmRule,String time) throws ServerException{
		String key = serverID + "_" + featureID + "_" + object;
		DcAlarmControl control = dcAlarmControlMap.get(key);

		if (null == control) {
			control = new DcAlarmControl(featureID, object, serverID);
			dcAlarmControlMap.put(key, control);
		}

		long currentTime = DateUtils.unixTimestamp();
		control.setReportTime(currentTime);

		if (CheckReportDataSuperUtils.checkReportValue(value, featureID, object, alarmRule,serverID)) {
			if (control.getId() <= 0) {
				sendBasicAlarmMail(alarmRule, value, featureID, object, serverID, time);
				control.setFrequency(1);
			} else {
				int frequency = control.getFrequency();
				if (alarmRule.getMaxFrequency() > frequency) {
					long alarmTime = control.getAlarmTime();
					if (alarmTime <= 0) {
						sendBasicAlarmMail(alarmRule, value, featureID, object, serverID, time);
						control.setAlarmTime(currentTime);
						control.setFrequency(control.getFrequency() + 1);
					} else {
						// 默认间隔三分钟发送一次
						long nextAlarmTime = alarmTime + Integer.parseInt(ResourceHandler.getProperties("next_alarm_time")) * 60;
						if (currentTime >= nextAlarmTime) {
							sendBasicAlarmMail(alarmRule, value, featureID, object, serverID, time);
							control.setAlarmTime(currentTime);
							control.setFrequency(control.getFrequency() + 1);
						}
					}
				} else {
					kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date()) + "超过周期内最大告警次数！" + featureID + "," + object + "," + serverID);
				}
			}
			control.setStartTime(currentTime);
			control.setStatus(1);
		} else {
			// 已经恢复
			control.setRestoreTime(currentTime);
			control.setStatus(0);
			control.setFrequency(0);
		}
		updateAlarmControlInfo(control);
	}

	private synchronized void updateAlarmControlInfo(DcAlarmControl control) {
		kafka_Log.info(control.toString());
		if (control.getId() <= 0) {
			//  不存在，将上报数据插入数据库
			String sql_insert = "insert into " + Constant.DC_ALARM_HISTORY
					+ "(featureID,object,serverID,reporteTime,alarmTime,startTime," +
					"restoreTime,frequency,status) values(?,?,?,?,?,?,?,?,?)";
			jdbcTemplateMonitor.insert(sql_insert, control.getfId(), control.getObject(),
					control.getServerId(), control.getReportTime(), control.getAlarmTime(),
					control.getStartTime(), control.getRestoreTime(), control.getFrequency(),
					control.getStatus());
		} else {
			// 存在，更新
			String sql_update = "update " + Constant.DC_ALARM_HISTORY
					+ " set reporteTime = ?,alarmTime = ?, startTime=? "
					+ "and restoreTime = ?, frequency = ?, status = ?"
					+ " where id = ?";

			jdbcTemplateMonitor.update(sql_update, control.getReportTime(),
					control.getAlarmTime(), control.getStartTime(),
					control.getRestoreTime(), control.getFrequency(), control.getStatus(),
					control.getId());
		}
	}

	private DcAlarmControl getAlarmControlInfo(int featureID, String object, int serverID) {
		String sql = "select id, featureID, object, serverID, reporteTime,alarmTime," +
				"startTime,restoreTime,frequency,status from " + Constant.DC_ALARM_HISTORY
				+ " where featureID = ? and object = ? and serverID = ?";

		final DcAlarmControl dcAlarmControl = new DcAlarmControl();
		MysqlUtil.getJdbcTemplateMonitor().query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				dcAlarmControl.setId(rs.getInt("id"));
				dcAlarmControl.setfId(rs.getInt("featureID"));
				dcAlarmControl.setObject(rs.getString("object"));
				dcAlarmControl.setServerId(rs.getInt("serverID"));
				dcAlarmControl.setReportTime(rs.getLong("reporteTime"));
				dcAlarmControl.setAlarmTime(rs.getLong("alarmTime"));
				dcAlarmControl.setStartTime(rs.getLong("startTime"));
				dcAlarmControl.setRestoreTime(rs.getLong("restoreTime"));
				dcAlarmControl.setFrequency(rs.getInt("frequency"));
				dcAlarmControl.setStatus(rs.getInt("status"));

			}
		}, featureID, object, serverID);
		return dcAlarmControl;
	}




	/**
	 * 发送告警邮件
	 * @param alarmRule
	 * @param value
	 * @param featureID
	 * @param object
	 * @param serverID
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年3月8日 下午6:02:18
	 */
	private void sendBasicAlarmMail(AlarmBasicRule alarmRule,BigDecimal value,int featureID,
			String object,int serverID,String time) throws ServerException{
		// 5.更新告警历史
		//updateAlarmHistory(featureID, object, serverID, frequency);
		// 6.发送告警信息
		String alarmType = alarmRule.getAlarmType();
		String[] types = alarmType.split(",");
		for (int i = 0;i < types.length;i++) {
			int type = Integer.parseInt(types[i]);
			kafka_Log.info("开始检查告警类型:" + type +"==" + featureID+"," + object + "," + serverID);
			// 获取当前机器的负责人和备份负责人email TODO 给机器所属业务模块下的人发送告警？
			String email = getEmailAddresser(serverID);
			String subject = getAlarmSubject(featureID,object);
			String content = getAlarmContent(featureID, object, serverID, value,time);
			if (type == 1) {
				// 6.1 发送邮件告警
				ProducerAdapter.sendAlarmTOKafka(email, subject, content);
			} else if (type == 0) {
				// 6.1 发送短信告警 TODO 给机器所属业务模块下的人发送告警？
				String phone = getPhoneAddresser(serverID);
				kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date())+"发送告警短信！");
				ServerUtils.sendAlarmSms(phone, content);
			} else if (type == 2) {
				ServerUtils.sendAppAlarm(email, subject, content);
				kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date())+"发送APP告警！");
			} else {
				//
				kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date())+"告警规则设置错误！" + featureID+","+ object+","+ serverID);
			}
		}
		// 7.记录已经发送的告警信息
		recordSendAlarm(featureID, object, serverID);
	}
	
	/**
	 * 检测是否设置了告警规则
	 * 
	 * @param listRule
	 * @param serverID
	 * @param serverID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月27日 下午5:28:31
	 */
	private long isExistAlarmRule(List<AlarmBasicRule> listRule, int serverID) {
		String sql_business = "select distinct svr_id from " + Constant.SERVER_BUSINESS + " where busi_id = ?";
		String sql_module = "select distinct svr_id from " + Constant.SERVER_BUSINESS + " where module_id = ?";
		String sql_group = "select distinct svr_id from " + Constant.SERVER_GROUP + " where group_id = ?";
		for (AlarmBasicRule rule : listRule) {
			long id = rule.getId();
			int alarmObjectType = rule.getAlarmObjectType();
			
			if (alarmObjectType == 0) {
				// 直接给机器设置的告警规则
				String servers = rule.getServers();
				String[] serverIDs = servers.split(Constant.SEPARATOR);
				kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date())+"直接给机器设置的告警规则,serverIDs:" + serverIDs);
				for (int i = 0;i < serverIDs.length;i++) {
					int serID = Integer.parseInt(serverIDs[i]);
					if (serverID == serID) {
						kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date())+"告警规则id:" + id);
						return id;
					}
				}
			} else if (alarmObjectType == 1) {
				try {
					// 给整个业务下的机器设置的告警规则
					if (rule.getBusinessID() > 0) {
						List<Integer> list = jdbcTemplateMonitor.queryForList(sql_business, Integer.class,
								rule.getBusinessID());
						if (list.contains(serverID)) {
							kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date())+"告警规则id:" + id);
							return id;
						}
					}
					// 给业务中某个模块下的机器设置的告警规则
					if (rule.getModuleID() > 0) {
						List<Integer> list = jdbcTemplateMonitor.queryForList(sql_module, Integer.class,
								rule.getModuleID());
						if (list.contains(serverID)) {
							kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date())+"告警规则id:" + id);
							return id;
						}
					}
				} catch (Exception e) {
					kafka_Log.error(Constant.BASIC_TIME_FORMAT.format(new Date())+"【异常】检测是否设置了告警规则：" + e.getMessage());
					// TODO 
				}
			} else {
				try {
					// 给整个分组下的机器设置的告警规则
					if (rule.getGroupID() > 0) {
						List<Integer> list = jdbcTemplateMonitor.queryForList(sql_group, Integer.class,
								rule.getGroupID());
						if (list.contains(serverID)) {
							kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date())+"告警规则id:" + id);
							return id;
						}
					}
				} catch (Exception e) {
					kafka_Log.error(Constant.BASIC_TIME_FORMAT.format(new Date())+"【异常】检测是否设置了告警规则：" + e.getMessage());
				}
			}
		}
		return 0;
	}

	/**
	 * 获取上次告警时间
	 * @param featureID
	 * @param object
	 * @param serverID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月8日 下午5:56:15
	 */
	private long getLastAlarmTime(int featureID, String object,
			int serverID){
		String sql = "select alarmTime from " + Constant.DC_ALARM_HISTORY + " where featureID = ? and object = ? and serverID = ?";
		try {
			long alarmTime = jdbcTemplateMonitor.queryForLong(sql, featureID, object,serverID);
			return alarmTime;
		} catch (Exception e) {
			e.printStackTrace();
			kafka_Log.error(Constant.BASIC_TIME_FORMAT.format(new Date()) + "【异常】获取上次告警时间：" + e.getMessage());
		}
		return 0;
	}
	
	/**
	 * 更新告警历史
	 * 
	 * @param featureID
	 * @param object
	 * @param serverID
	 * @author chenfanglin <br>
	 * @date 2016年1月27日 下午6:38:05
	 */
	private void updateAlarmHistory(int featureID, String object,
			int serverID, int frequency) {
		long currentTime = DateUtils.unixTimestamp();
		try {
			if (frequency == 0) {
				// 第一次发送告警信息
				frequency++;
				String sql = "update " + Constant.DC_ALARM_HISTORY
						+ " set startTime = ?,alarmTime = ?,frequency = ?,status = ? "
						+ "where featureID = ? and object = ? and serverID = ?";
				jdbcTemplateMonitor.update(sql, currentTime, currentTime, frequency, Constant.WARN, featureID, object,
						serverID);
			} else {
				// 之前发过告警信息
				frequency++;
				String sql = "update " + Constant.DC_ALARM_HISTORY + " set alarmTime = ?,frequency = ?,status = ? "
						+ "where featureID = ? and object = ? and serverID = ?";
				jdbcTemplateMonitor.update(sql, currentTime, frequency, Constant.WARN, featureID, object, serverID);
			}
		} catch (Exception e) {
			e.printStackTrace();
			kafka_Log.error(Constant.BASIC_TIME_FORMAT.format(new Date())+"【异常】 更新告警历史：" + e.getMessage());
			//TODO
		}
		
	}

	/**
	 * 异常已经恢复
	 * 
	 * @param featureID
	 * @param object
	 * @param serverID
	 * @author chenfanglin <br>
	 * @date 2016年1月27日 下午6:50:57
	 */
	private void restoreAlarm(int featureID, String object, int serverID) {
		long currentTime = DateUtils.unixTimestamp();
		try {
			String sql = "update " + Constant.DC_ALARM_HISTORY + " set restoreTime = ?,status = ?,frequency = ? "
					+ "where featureID = ? and object = ? and serverID = ?";
			// ?? 异常恢复，告警次数归零？
			jdbcTemplateMonitor.update(sql, currentTime, Constant.OK, 0, featureID, object, serverID);
		} catch (Exception e) {
			e.printStackTrace();
			kafka_Log.error(Constant.BASIC_TIME_FORMAT.format(new Date())+"【异常】异常已经恢复：" + e.getMessage());
		}
		
	}

	/**
	 * 记录已经发送的告警
	 * 
	 * @param object
	 * @param serverID
	 * @author chenfanglin <br>
	 * @date 2016年1月27日 下午7:23:50
	 */
	private void recordSendAlarm(int featureID, String object, int serverID) {
		long currentTime = DateUtils.unixTimestamp();
		try {
			String sql = "insert into " + Constant.DC_ALARM_SEND + "(sendTime,featureID,object,serverID) values(?,?,?,?)";
			jdbcTemplateMonitor.insert(sql, currentTime, featureID, object, serverID);
		} catch (Exception e) {
			e.printStackTrace();
			kafka_Log.error(Constant.BASIC_TIME_FORMAT.format(new Date())+"【异常】记录已经发送的告警：" + e.getMessage());
		}
		
	}
	
	/**
	 * 获取邮件收件人
	 * @param serverID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年2月23日 上午11:45:23
	 */
	private String getEmailAddresser(int serverID){
		String sql_admin = "select b.email from "+Constant.SERVER_LIST+" a,"+Constant.EMPLOYEE+" b where a.admin = b.id and a.id = ?";
		String sql_backup = "select email from "+Constant.EMPLOYEE+" where id in(select backup_admin from "+Constant.SERVER_LIST+" where id = ?)";
		String sql = "select email from "+Constant.EMPLOYEE+" where id in(select distinct om_person from "+Constant.BUSINESS+" "
				+ "where id in(select distinct module_id from "+Constant.SERVER_BUSINESS+" where svr_id = ?))";
		String sql_bak = "select email from "+Constant.EMPLOYEE+" where id = ?";
		String sql_bakPrincipal = "select distinct bakPrincipal from "+Constant.BUSINESS + " where id in(select distinct module_id from "
				+Constant.SERVER_BUSINESS+" where svr_id = ?)";
		try {
			String admin_email = jdbcTemplateMonitor.queryForString(sql_admin, serverID);
			List<String> list_email = jdbcTemplateMonitor.queryForList(sql_backup, String.class, serverID);
			List<String> emails = jdbcTemplateMonitor.queryForList(sql, String.class, serverID);
			String bakPrincipal = jdbcTemplateMonitor.queryForString(sql_bakPrincipal, serverID);
			StringBuffer sb = new StringBuffer();
			if (StringUtil.isNotEmpty(bakPrincipal)) {
				String[] bakStr = bakPrincipal.split(Constant.SEPARATOR);
				for (int i = 0;i < bakStr.length;i++) {
					String bakEmail = jdbcTemplateMonitor.queryForString(sql_bak, bakStr[i]);
					if (i == 0) {
						sb.append(bakEmail);
					} else {
						sb.append(Constant.SEPARATOR).append(bakEmail);
					}
				}
			}
			String backup_email = StringUtil.join(Constant.SEPARATOR, list_email);
			String yw_email = StringUtil.join(Constant.SEPARATOR, emails);
			if (StringUtil.isNotEmpty(backup_email)) {
				admin_email = admin_email + Constant.SEPARATOR + backup_email;
			}
			if (StringUtil.isNotEmpty(yw_email)) {
				admin_email = admin_email + Constant.SEPARATOR + yw_email;
			}
			if (StringUtil.isNotEmpty(sb.toString())) {
				admin_email = admin_email + Constant.SEPARATOR + sb.toString();
			}
			if (StringUtil.isNotEmpty(ResourceHandler.getProperties("alarm_omp"))) {
				admin_email = admin_email + Constant.SEPARATOR + ResourceHandler.getProperties("alarm_omp");
			}
			kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date())+"获取邮件收件人, admin_email:" + admin_email + "serverID=" + serverID);
			return admin_email;
		} catch (Exception e) {
			e.printStackTrace();
			kafka_Log.error(Constant.BASIC_TIME_FORMAT.format(new Date())+"【异常】获取邮件收件人：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 获取短信收件人
	 * @param serverID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年2月23日 上午11:52:16
	 */
	private String getPhoneAddresser(int serverID){
		String sql_phone = "select b.phone from "+Constant.SERVER_LIST+" a,"+Constant.EMPLOYEE+" b where a.admin = b.id and a.id = ?";
		String sql_backup = "select phone from "+Constant.EMPLOYEE+" where id in(select backup_admin from "+Constant.SERVER_LIST+" where id = ?)";
		String sql = "select phone from "+Constant.EMPLOYEE+" where id in(select distinct om_person from "+Constant.BUSINESS+" "
				+ "where id in(select distinct module_id from "+Constant.SERVER_BUSINESS+" where svr_id = ?))";
		try {
			String phone = jdbcTemplateMonitor.queryForString(sql_phone, serverID);
			
			List<String> list_phone = jdbcTemplateMonitor.queryForList(sql_backup, String.class, serverID);
			List<String> phones = jdbcTemplateMonitor.queryForList(sql, String.class, serverID);
			String backup_phone = StringUtil.join(Constant.SEPARATOR, list_phone);
			String yw_phone = StringUtil.join(Constant.SEPARATOR, phones);
			if (StringUtil.isNotEmpty(backup_phone)) {
				phone = phone + Constant.SEPARATOR + backup_phone;
			}
			if (StringUtil.isNotEmpty(yw_phone)) {
				phone = phone + Constant.SEPARATOR + yw_phone;
			}
			kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date())+"获取短信收件人, phone:" + phone+ "serverID=" + serverID);
			return phone;
		} catch (Exception e) {
			e.printStackTrace();
			kafka_Log.error(Constant.BASIC_TIME_FORMAT.format(new Date())+"【异常】获取邮件收件人：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 获取告警主题
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年2月24日 下午3:10:56
	 */
	private String getAlarmSubject(int featureID, String object){
		// 特殊处理 磁盘分区使用率和私有内存使用率
		if (featureID == Constant.FREEPARTITION) {
			if (Constant.ROOT.equals(object)) {
				featureID = Constant.ROOTPARTITION;
			} else {
				featureID = Constant.DISKPARTITION;
				object = Constant.DISK_USAGE;
			}
		} else if (featureID == Constant.MEMPRIVATE) {
			featureID = Constant.MEMORYPRIVATE;
			object = Constant.PRIMEM_USAGE;
		} else if (featureID == Constant.SVCTMTIMEMAX) {
			object = Constant.SVCTM;
		} else if (featureID == Constant.AWAITTIMEMAX) {
			object = Constant.AWAIT;
		} else if (featureID == Constant.AVGQUSZMAX) {
			object = Constant.AVEQ;
		} else if (featureID == Constant.AVGRQSZ) {
			object = Constant.AVGRQ_SZ;
		} else if (featureID == Constant.UTILMAX) {
			object = Constant.UTIL;
		}
		String sql = "select object_name from "+Constant.FEATURE_OBJECT+" where feature_id = ? and object = ?";
		try {
			String subject = jdbcTemplateMonitor.queryForString(sql, featureID, object);
			subject = ResourceHandler.getProperties("server_alarm") + subject;
			kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date())+"获取告警主题:"+subject+ "featureID=" + featureID + ",object="+object);
			return subject;
		} catch (Exception e) {
			e.printStackTrace();
			kafka_Log.error(Constant.BASIC_TIME_FORMAT.format(new Date())+"【异常】获取告警主题：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 获取告警内容
	 * @param featureID
	 * @param object
	 * @param serverID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年2月24日 下午3:52:10
	 */
	private String getAlarmContent(int featureID, String object,int serverID, BigDecimal value,String time){
		StringBuffer sb = new StringBuffer();
		try {
			String sql_host = "select host_name from " + Constant.SERVER_LIST + " where id = ?";
			String hostName = jdbcTemplateMonitor.queryForString(sql_host, serverID);
			String sql_nw_ip = "select group_concat(ip) from " + Constant.SERVER_IP_LIST + " where svr_id = ? and type = 0";
			String nw = jdbcTemplateMonitor.queryForString(sql_nw_ip, serverID);
			String sql_ww_ip = "select group_concat(ip) from " + Constant.SERVER_IP_LIST + " where svr_id = ? and type = 1";
			String ww = jdbcTemplateMonitor.queryForString(sql_ww_ip, serverID);
			String sql = "select object_name from "+Constant.FEATURE_OBJECT+" where feature_id = ? and object = ?";
			String formatValue = CheckReportDataSuperUtils.getReportValueByfeature(value, featureID, object, serverID);
			// 特殊处理 磁盘分区使用率和私有内存使用率
			if (featureID == Constant.FREEPARTITION) {
				if (Constant.ROOT.equals(object)) {
					featureID = Constant.ROOTPARTITION;
				} else {
					featureID = Constant.DISKPARTITION;
					object = Constant.DISK_USAGE;
				}
			} else if (featureID == Constant.MEMPRIVATE) {
				featureID = Constant.MEMORYPRIVATE;
				object = Constant.PRIMEM_USAGE;
			} else if (featureID == Constant.SVCTMTIMEMAX) {
				object = Constant.SVCTM;
			} else if (featureID == Constant.AWAITTIMEMAX) {
				object = Constant.AWAIT;
			} else if (featureID == Constant.AVGQUSZMAX) {
				object = Constant.AVEQ;
			} else if (featureID == Constant.AVGRQSZ) {
				object = Constant.AVGRQ_SZ;
			} else if (featureID == Constant.UTILMAX) {
				object = Constant.UTIL;
			}
			String object_name = jdbcTemplateMonitor.queryForString(sql, featureID,object);
			sb.append(ResourceHandler.getProperties("alarm_time")).append(time).append("\n")
			.append(ResourceHandler.getProperties("host_name")).append(hostName).append("\n")
			.append(ResourceHandler.getProperties("nw_value")).append(nw).append("\n")
			.append(ResourceHandler.getProperties("ww_value")).append(ww).append("\n")
			.append(object_name).append(Constant.COLON).append(formatValue);
			kafka_Log.info(Constant.BASIC_TIME_FORMAT.format(new Date())+"获取告警内容:"+sb.toString()+ "==" + featureID+"," + object + "," + serverID);
		} catch (Exception e) {
			e.printStackTrace();
			kafka_Log.error(Constant.BASIC_TIME_FORMAT.format(new Date())+"【异常】获取告警内容：" + e.getMessage());
		}
		return sb.toString();
	}

	/**
	 * 简单处理过期数据 过滤一小时前的数据
	 */
	public int isValidDate(String time) throws ParseException {

		Date rDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
		Calendar cal = Calendar.getInstance();

        /* HOUR_OF_DAY 指示一天中的小时 */
		cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - 1);
		return rDate.compareTo(cal.getTime());
	}

	public boolean isAlarmFeature(int fid) {
		int[] fids = new int[]{35, 36, 37, 38, 39, 50, 51, 52, 53, 54, 55, 56, 57, 10, 13, 27, 40, 60, 61};
		for (int i : fids) {
			if (i == fid) {
				return true;
			}
		}
		return false;
	}

}



