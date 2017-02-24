package com.dataeye.omp.module.alarm.basic;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.Constant.Separator;
import com.dataeye.omp.constant.Constant.Table;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.utils.DateUtils;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;

@Service
public class AlarmBasicDAO {

	@Resource(name = "jdbcTemplateMonitor")
	private JdbcTemplate jdbcTemplateMonitor;
	
	/**
	 * 查询服务器监控告警规则
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年3月4日 下午4:57:12
	 */
	public List<AlarmBasicRule> queryAlarmBasicRuleList() throws ServerException {
		final List<AlarmBasicRule> list = new ArrayList<AlarmBasicRule>();
		String sql = "select id,featureID,object,servers,businessID,moduleID,groupID,maxThreshold,minThreshold,maxMoM,minMoM,maxFrequency,"
				+ "shieldStart,shieldEnd,alarmType,status from " + Table.DC_ALARM_RULE_BASIC + " order by createTime desc";
		try {
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				
				public void processRow(ResultSet rs) throws SQLException {
					AlarmBasicRule rule = new AlarmBasicRule();
					long id = rs.getLong("id");
					int featureID = rs.getInt("featureID");
					String object = rs.getString("object");
					String servers = rs.getString("servers");
					int businessID = rs.getInt("businessID");
					int moduleID = rs.getInt("moduleID");
					int groupID = rs.getInt("groupID");
					BigDecimal maxThreshold = rs.getBigDecimal("maxThreshold");
					BigDecimal minThreshold = rs.getBigDecimal("minThreshold");
					BigDecimal maxMoM = rs.getBigDecimal("maxMoM");
					BigDecimal minMoM = rs.getBigDecimal("minMoM");
					int maxFrequency = rs.getInt("maxFrequency");
					long shieldStart = rs.getLong("shieldStart");
					long shieldEnd = rs.getLong("shieldEnd");
					String alarmType = rs.getString("alarmType");
					int status = rs.getInt("status");
					rule.setId(id);
					rule.setFeatureID(featureID);
					rule.setFeatureName(getFeatureName(featureID,object));
					rule.setObject(object);
					rule.setBusinessName(getBusinessOrModuleName(businessID));
					rule.setModuleName(getBusinessOrModuleName(moduleID));
					rule.setGroupName(getGroupName(groupID));
					rule.setMaxThreshold(maxThreshold);
					rule.setMinThreshold(minThreshold);
					rule.setMaxMoM(maxMoM);
					rule.setMinMoM(minMoM);
					rule.setMaxFrequency(maxFrequency);
					if (StringUtil.isNotEmpty(servers)) {
						rule.setServers(getServerName(servers));
					}
					if (shieldStart > 0 && shieldEnd > 0) {
						String start = DateUtils.getyyyyMMddFromTimestamp(shieldStart);
						String end = DateUtils.getyyyyMMddFromTimestamp(shieldEnd);
						rule.setShieldTime(start + Separator.TILDE + end);
					} else {
						rule.setShieldTime("");
					}
					rule.setAlarmType(alarmType);
					rule.setStatus(status);
					list.add(rule);
				}
			});
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
		return list;
	}
	/**
	 * 获取特性名称
	 * @param featureID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午2:12:23
	 */
	private String getFeatureName(int featureID,String object){
		String sql = "select object_name from " + Table.FEATURE_OBJECT + " where feature_id = ? and object = ?";
		return jdbcTemplateMonitor.queryForString(sql, featureID,object);
	}
	/**
	 * 获取机器名称
	 * @param servers
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月7日 下午2:08:58
	 */
	private String getServerName(String servers){
		String sql = "select host_name from " + Table.SERVER_LIST + " where id =?";
		String[] serverID = servers.split(",");
		List<String> list = new ArrayList<String>();
		for (int i = 0;i < serverID.length;i++) {
			String name = jdbcTemplateMonitor.queryForString(sql, serverID[i]);
			list.add(name);
		}
		String hostName = StringUtil.join(Separator.DEFAULT, list);
		return hostName;
	}
	/**
	 * 获取业务或模块名称
	 * @param id
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月4日 下午4:47:21
	 */
	private String getBusinessOrModuleName(int id){
		String sql = "select name from " + Table.BUSINESS + " where id = ?";
		String name = jdbcTemplateMonitor.queryForString(sql, id);
		return name;
	}
	
	/**
	 * 获取分组名称
	 * @param groupID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月4日 下午5:29:25
	 */
	private String getGroupName(int groupID){
		String sql = "select groupName from " + Table.SERVER_GROUP_OWNER + " where group_id = ?";
		String groupName = jdbcTemplateMonitor.queryForString(sql, groupID);
		return groupName;
	}
	
	/**
	 * 保存服务器监控告警规则
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年3月4日 下午5:06:20
	 */
	public void saveAlarmBasicRule(int alarmRuleID,int featureID,String object,int alarmObjectType,String servers,int businessID,int moduleID,
			int groupID,int alarmSectionType,BigDecimal maxThreshold,BigDecimal minThreshold,BigDecimal maxMoM,BigDecimal minMoM,
			int maxFrequency,String shieldStart,String shieldEnd,String alarmType) throws ServerException {
		long currentTime = System.currentTimeMillis()/1000;
		int start = 0;
		int end = 0;
		if (StringUtil.isNotEmpty(shieldStart) && StringUtil.isNotEmpty(shieldEnd)) {
			start = DateUtils.getUnixTimestampFromyyyyMMdd(shieldStart);
			end = DateUtils.getUnixTimestampFromyyyyMMdd(shieldEnd);
		}
		if (servers == null) {
			servers = "";
		}
			if (alarmRuleID > 0) {
				// 修改
				String sql = "update " + Table.DC_ALARM_RULE_BASIC + " set featureID = ?,object = ?,alarmObjectType = ?,servers = ?,businessID = ?,moduleID = ?,"
						+ "groupID = ?,alarmSectionType = ?,maxThreshold = ?,minThreshold = ?,maxMoM = ?,minMoM = ?,maxFrequency = ?,shieldStart = ?,"
						+ "shieldEnd = ?,alarmType = ?,updateTime = ? where id = ?";
				jdbcTemplateMonitor.update(sql, featureID,object,alarmObjectType,servers,businessID,moduleID,groupID,alarmSectionType,maxThreshold,
						minThreshold,maxMoM,minMoM,maxFrequency,start,end,alarmType,currentTime,alarmRuleID);
			} else {
				boolean insert = true;
				if (alarmObjectType == 0) {
					String sql_exist_server = "select count(*) from " + Table.DC_ALARM_RULE_BASIC + " where featureID = ? and object = ? and servers = ?";
					int count = jdbcTemplateMonitor.queryForInt(sql_exist_server, featureID,object,servers);
					if (count > 0) {
						insert = false;
					}
				} else if (alarmObjectType == 1) {
					String sql_exist_module = "select count(*) from " + Table.DC_ALARM_RULE_BASIC + " where featureID = ? and object = ? and businessID = ? and moduleID = ?";
					int count = jdbcTemplateMonitor.queryForInt(sql_exist_module, featureID,object,businessID,moduleID);
					if (count > 0) {
						insert = false;
					}
				} else if (alarmObjectType == 2) {
					String sql_exist_group = "select count(*) from " + Table.DC_ALARM_RULE_BASIC + " where featureID = ? and object = ? and groupID = ?";
					int count = jdbcTemplateMonitor.queryForInt(sql_exist_group, featureID,object,groupID);
					if (count > 0) {
						insert = false;
					}
				}
				if (insert) {
					// 添加
					String sql = "insert into " + Table.DC_ALARM_RULE_BASIC + "(featureID,object,alarmObjectType,servers,businessID,moduleID,groupID,"
							+ "alarmSectionType,maxThreshold,minThreshold,maxMoM,minMoM,maxFrequency,shieldStart,shieldEnd,alarmType,createTime)"
							+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					jdbcTemplateMonitor.insert(sql, featureID,object,alarmObjectType,servers,businessID,moduleID,groupID,alarmSectionType,maxThreshold,
							minThreshold,maxMoM,minMoM,maxFrequency,start,end,alarmType,currentTime);
				} else {
					ExceptionHandler.throwDatabaseException(StatusCode.RULEEXISTS, "存在相同的告警规则，不能重复添加。");
				}
				
			}

	}
	
	/**
	 * 启用禁用告警规则
	 * @param alarmRuleID
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年3月4日 下午6:10:16
	 */
	public void switchAlarmBasicRule(int alarmRuleID) throws ServerException {
		String sql = "select status from " + Table.DC_ALARM_RULE_BASIC + " where id = ?";
		try {
			int status = jdbcTemplateMonitor.queryForInt(sql, alarmRuleID);
			if (status == 0) {
				String sql_update = "update " + Table.DC_ALARM_RULE_BASIC + " set status = 1 where id = ?";
				jdbcTemplateMonitor.update(sql_update, alarmRuleID);
			} else {
				String sql_update = "update " + Table.DC_ALARM_RULE_BASIC + " set status = 0 where id = ?";
				jdbcTemplateMonitor.update(sql_update, alarmRuleID);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
	}
	
	/**
	 * 根据ID获取告警规则
	 * @param alarmRuleID
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年3月4日 下午7:43:06
	 */
	public AlarmBasicRule getAlarmBasicRuleByID(int alarmRuleID) throws ServerException {
		final AlarmBasicRule rule = new AlarmBasicRule();
		String sql = "select id,featureID,object,servers,alarmObjectType,businessID,moduleID,groupID,alarmSectionType,maxThreshold,minThreshold,maxMoM,minMoM,maxFrequency,"
				+ "shieldStart,shieldEnd,alarmType,status from " + Table.DC_ALARM_RULE_BASIC + " where id = ?";
		try {
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				
				public void processRow(ResultSet rs) throws SQLException {
					long id = rs.getLong("id");
					int featureID = rs.getInt("featureID");
					String object = rs.getString("object");
					String servers = rs.getString("servers");
					int alarmObjectType = rs.getInt("alarmObjectType");
					int businessID = rs.getInt("businessID");
					int moduleID = rs.getInt("moduleID");
					int groupID = rs.getInt("groupID");
					int alarmSectionType = rs.getInt("alarmSectionType");
					BigDecimal maxThreshold = rs.getBigDecimal("maxThreshold");
					BigDecimal minThreshold = rs.getBigDecimal("minThreshold");
					BigDecimal maxMoM = rs.getBigDecimal("maxMoM");
					BigDecimal minMoM = rs.getBigDecimal("minMoM");
					int maxFrequency = rs.getInt("maxFrequency");
					long shieldStart = rs.getLong("shieldStart");
					long shieldEnd = rs.getLong("shieldEnd");
					String alarmType = rs.getString("alarmType");
					int status = rs.getInt("status");
					rule.setId(id);
					rule.setFeatureID(featureID);
					rule.setObject(object);
					rule.setAlarmObjectType(alarmObjectType);
					rule.setBusinessID(businessID);
					rule.setModuleID(moduleID);
					rule.setGroupID(groupID);
					rule.setAlarmSectionType(alarmSectionType);
					rule.setMaxThreshold(maxThreshold);
					rule.setMinThreshold(minThreshold);
					rule.setMaxMoM(maxMoM);
					rule.setMinMoM(minMoM);
					rule.setMaxFrequency(maxFrequency);
					if (shieldStart > 0 && shieldEnd > 0) {
						String start = DateUtils.getyyyyMMddFromTimestamp(shieldStart);
						String end = DateUtils.getyyyyMMddFromTimestamp(shieldEnd);
						rule.setShieldStart(start);
						rule.setShieldEnd(end);
					} else {
						rule.setShieldStart("");
						rule.setShieldEnd("");
					}
					if (StringUtil.isNotEmpty(alarmType)) {
						rule.setAlarmTypes(strToList(alarmType));
					} else {
						rule.setAlarmTypes(Collections.EMPTY_LIST);
					}
					if (StringUtil.isNotEmpty(servers)) {
						rule.setServerIDs(strToList(servers));
					} else {
						rule.setServerIDs(Collections.EMPTY_LIST);
					}
					
					rule.setStatus(status);
				}
			}, alarmRuleID);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
		return rule;
	}
	
	private List<Integer> strToList(String str){
		List<Integer> list = new ArrayList<Integer>();
		String[] s = str.split(",");
		for (int i = 0;i < s.length;i++) {
			list.add(Integer.parseInt(s[i]));
		}
		return list;
	}
}




