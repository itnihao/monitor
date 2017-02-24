package com.dataeye.omp.module.alarm;

import com.dataeye.common.CachedObjects;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.constant.Constant.Separator;
import com.dataeye.omp.constant.Constant.Table;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.omp.module.cmdb.device.DicValue;
import com.dataeye.utils.DateUtils;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AlarmDAO {

	@Resource(name = "jdbcTemplateMonitor")
	private JdbcTemplate jdbcTemplateMonitor;

	/**
	 * 设置告警规则
	 *
	 * @param featrueID
	 * @param object
	 * @param servers
	 * @param busiId
	 * @param moduleId
	 * @param maxThreshold
	 * @param minThreshold
	 * @param maxMoM
	 * @param minMoM
	 * @param maxFrequency
	 * @param alarmType
	 * @param shieldStart
	 * @param shieldEnd
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月25日 下午7:42:03
	 */
	public void saveAlarmRule(int alarmRuleId, int featrueID, String object, String servers,
							  int busiId, int moduleId, int groupId, BigDecimal maxThreshold,
							  BigDecimal minThreshold, BigDecimal maxMoM,
							  BigDecimal minMoM, int maxFrequency, String alarmType,
							  String shieldStart, String shieldEnd, int alarmSectionType,
							  int alarmObjectType, int alarmLevel)
			throws ServerException {

		Rule rule = new Rule();
		rule.setAlarmSectionType(alarmSectionType);
		rule.setAlarmLevel(alarmLevel);
		switch (alarmSectionType) {
			case Constant.SHIELD_SECTION_THRESHOLD: {
				rule.setMaxThreshold(maxThreshold);
				rule.setMinThreshold(minThreshold);
				break;
			}

			case Constant.SHIELD_SECTION_MOM: {
				rule.setMaxMoM(maxMoM);
				rule.setMinMoM(minMoM);
				break;
			}


		}

		rule.setMaxFrequency(maxFrequency);
		List<Integer> alarmTypeList = new ArrayList<>();
		String[] type = alarmType.split(Separator.DEFAULT);
		for (int i = 0; i < type.length; i++) {
			alarmTypeList.add(Integer.parseInt(type[i]));
		}

		rule.setAlarmType(alarmTypeList);
		String ruleJson = rule.toJson();

		AlarmObject alarmObject = new AlarmObject();
		alarmObject.setAlarmObjectType(alarmObjectType);
		switch (alarmObjectType) {
			case Constant.ALARM_TYPE_DEVICE: {
				String[] server = servers.split(Separator.DEFAULT);
				List<Integer> sList = new ArrayList<>();
				for (int j = 0; j < server.length; j++) {
					sList.add(Integer.parseInt(server[j]));
				}
				alarmObject.setServers(sList);
				break;
			}

			case Constant.ALARM_TYPE_BUSINESS: {
				alarmObject.setBusinessID(busiId);
				alarmObject.setModuleID(moduleId);
				break;
			}

			case Constant.ALARM_TYPE_GROUP: {
				alarmObject.setGroupID(groupId);
				break;
			}
		}
		String objectJson = alarmObject.toJson();
		long startTime = 0, endTime = 0;
		if (StringUtil.isNotEmpty(shieldStart)) {
			startTime = DateUtils.getUnixTimestampFromyyyyMMdd(shieldStart);
		}
		if (StringUtil.isNotEmpty(shieldEnd)) {
			endTime = DateUtils.getUnixTimestampFromyyyyMMdd(shieldEnd);
		}
		try {
			String sql = "";
			if (alarmRuleId < 0) {
				sql = "insert into " + Table.DC_ALARM_RULE
						+ "(featureID,object,alarmObject,alarmRule," +
						"shieldStart,shieldEnd) values(?,?,?,?,?,?)";
				jdbcTemplateMonitor.insert(sql, featrueID, object,
						objectJson, ruleJson, startTime, endTime);
			} else {
				sql = "update " + Table.DC_ALARM_RULE + " set featureID = ?,"
						+ "object = ?, alarmObject = ?, alarmRule = ?, "
						+ "shieldStart = ?, shieldEnd = ? where id = ?";
				jdbcTemplateMonitor.update(sql, featrueID, object,
						objectJson, ruleJson, startTime, endTime, alarmRuleId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
	}

	/**
	 * 获取告警规则列表
	 * @return
	 * @throws ServerException
	 */
	public List<AlarmRule> getAlarmRuleList() throws ServerException {
		String sql = "select id, featureID, object, alarmObject, "
				+ "alarmRule, shieldStart, shieldEnd from  " + Table.DC_ALARM_RULE;
		final List<AlarmRule> alarmRules = new ArrayList<>();
		try {
				jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
					@Override
					public void processRow(ResultSet rs) throws SQLException {
						AlarmRule ar = new AlarmRule();
						ar.setId(rs.getInt("id"));
						ar.setFeatureID(rs.getInt("featureID"));
						ar.setObject(rs.getString("object"));

						String alarmObjectString = rs.getString("alarmObject");
						ar.setAlarmObjectString(alarmObjectString);

						ar.setAlarmObject(
								CachedObjects.GSON.fromJson(alarmObjectString, AlarmObject.class));

						String alarmRuleString = rs.getString("alarmRule");
						ar.setAlarmRuleString(alarmRuleString);
						ar.setRule(
								CachedObjects.GSON.fromJson(alarmRuleString, Rule.class));

						ar.setShieldStart(DateUtils.getDateString(rs.getLong("shieldStart"),"yyyy-MM-dd"));
						ar.setShieldEnd(DateUtils.getDateString(rs.getLong("shieldEnd"), "yyyy-MM-dd"));
						alarmRules.add(ar);

					}
				});
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
		return alarmRules;
	}

	/**
	 * 获取单条告警规则详情
	 * @param id
	 * @return
	 * @throws ServerException
	 */
	public AlarmRule getAlarmRuleById(int id) throws ServerException {
		String sql = "select id, featureID, object, alarmObject, "
				+ "alarmRule, shieldStart, shieldEnd from " + Table.DC_ALARM_RULE
				+ " where id = ?";

		final AlarmRule ar = new AlarmRule();

		try {
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					ar.setId(rs.getInt("id"));
					ar.setFeatureID(rs.getInt("featureID"));
					ar.setObject(rs.getString("object"));

					String alarmObjectString = rs.getString("alarmObject");
					ar.setAlarmObjectString(alarmObjectString);

					ar.setAlarmObject(
							CachedObjects.GSON.fromJson(alarmObjectString, AlarmObject.class));

					String alarmRuleString = rs.getString("alarmRule");
					ar.setAlarmRuleString(alarmRuleString);
					ar.setRule(
							CachedObjects.GSON.fromJson(alarmRuleString, Rule.class));

					ar.setShieldStart(DateUtils.getDateString(rs.getLong("shieldStart"), "yyyy-MM-dd"));
					ar.setShieldEnd(DateUtils.getDateString(rs.getLong("shieldEnd"), "yyyy-MM-dd"));
				}
			},id);

		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}

		return ar;
	}

	/**
	 * 删除告警规则 （伪删除）
	 * @param id
	 * @throws ServerException
	 */
	public void deleteAlarmRule(int id) throws ServerException {
		try {
			String sql = "update " + Table.DC_ALARM_RULE + " set delete_flag = 1 where id = ?";
			jdbcTemplateMonitor.update(sql, id);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}

	}



}
