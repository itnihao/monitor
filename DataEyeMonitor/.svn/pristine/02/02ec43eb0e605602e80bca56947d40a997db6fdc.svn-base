package com.dataeye.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dataeye.omp.constant.Constant.Separator;
import com.dataeye.omp.constant.Constant.Table;
import com.dataeye.utils.ServerUtils;
import com.dataeye.utils.ValidateUtils;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;

/**
 * 
 * <pre>
 * 获取数据库的配置
 * 1.先读取全局配置dataeye_global.dc_config的配置
 * 2.再读取每个数据库的特定配置databasex.dc_config
 * 如果有冲突就用2覆盖1
 * @author Ivan          <br>
 * @date 2015年4月3日 下午5:09:29 <br>
 * @version 1.0
 * <br>
 */
public class DcConfigs implements Job {
	/** 查询某个数据库表的最后更新时间,注意只支持MyIsam引擎表 */
	public static final String SQL_TABLE_LAST_UPDATETIME = "select unix_timestamp(UPDATE_TIME) from information_schema.TABLES where TABLE_SCHEMA=? and TABLE_NAME=? limit 1";
	/** 缓存dataeye_global.dc_config所有配置 */
	public static Map<String, List<String>> configMapGlobal = new HashMap<String, List<String>>();
	/** 缓存dataeye_oap.dc_config所有配置 */
	public static Map<String, List<String>> configMap = new HashMap<String, List<String>>();
	/** dataeye_global.dc_config最后更新时间 */
	public static Long lastUpdateTime_dataeye_global = 0L;
	/** dataeye_oap.dc_config最后更新时间 */
	public static Long lastUpdateTime_dataeye_oap = 0L;

	/**
	 * <pre>
	 * 定时扫描数据库配置表是否有改动，如果有改动就重新加载所有配置
	 * 1.dataeye_global.dc_config
	 * 2.dataeye_oap.dc_config
	 * 
	 * 
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// JdbcTemplate jdbcTemplate = ApplicationContextUtil.getBeanJdbcTemplateDataEyeGlobal();
		// long updateTime = getDcConfig(jdbcTemplate, Database.DATAEYE_GLOBAL, lastUpdateTime_dataeye_global,
		// configMapGlobal);
		// if (updateTime > 0) {
		// lastUpdateTime_dataeye_global = updateTime;
		// }
		// jdbcTemplate = ApplicationContextUtil.getBeanJdbcTemplateDataEyeOAP();
		// updateTime = getDcConfig(jdbcTemplate, Database.DATAEYE_OAP, lastUpdateTime_dataeye_oap, configMap);
		// if (updateTime > 0) {
		// lastUpdateTime_dataeye_oap = updateTime;
		// }
	}

	/**
	 * 
	 * <pre>
	 * 检查某个数据库是否有更新，如果有更新就加载，没有更新返回null
	 *  @param jdbcTemplate
	 *  @param database
	 *  @param lastUpdateTime
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月7日 上午11:06:30
	 * <br>
	 */
	private long getDcConfig(JdbcTemplate jdbcTemplate, String database, long lastUpdateTime,
			Map<String, List<String>> map) {
		try {
			final Map<String, List<String>> tmp = new HashMap<String, List<String>>();
			// 获取dc_config表的最后更新时间，和上次的更新时间比较，如果updateTime>lastUpdateTime就重新 加载配置
			long updateTime = jdbcTemplate.queryForLong(SQL_TABLE_LAST_UPDATETIME, database, Table.DC_CONFIG);
			if (updateTime > lastUpdateTime) {// 有更新
				String sql = "select VKey,Value from " + Table.DC_CONFIG;
				jdbcTemplate.query(sql, new RowCallbackHandler() {
					@Override
					public void processRow(ResultSet rs) throws SQLException {
						String key = rs.getString(1);
						String value = rs.getString(2);
						if (ValidateUtils.isNotEmpty(key, value)) {
							String[] valueArr = value.split(Separator.DEFAULT);
							List<String> valueList = new ArrayList<String>(valueArr.length);
							ServerUtils.putStringArr2List(valueArr, valueList);
							// 为了更加灵活的支持数据库的配置方式,可以支持将所有配置写到一行记录里面，也可以将配置写到多行配置里面
							List<String> listTmp = tmp.get(key);
							if (listTmp == null) {
								tmp.put(key, valueList);
							} else {
								listTmp.addAll(valueList);
								tmp.put(key, listTmp);
							}
						}
					}
				});
				lastUpdateTime = updateTime;
				System.out.println("reload success from " + database + ".dc_config=>" + CachedObjects.GSON.toJson(tmp));
				map.clear();
				map.putAll(tmp);
				return updateTime;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 
	 * <pre>
	 * 取出所有的配置（适用于多个配置值的配置）
	 * @param key
	 * @return  
	 * @author Ivan<br>
	 * @date 2014年10月25日 下午2:20:53
	 * <br>
	 */
	public static List<String> getConfigAll(String key) {
		return configMap.get(key);
	}

	/**
	 * 
	 * <pre>
	 * 取出第一个配置项,没有就返回null（适用于只有一个配置值的配置）
	 *  @param key
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2014年10月25日 下午2:21:06
	 * <br>
	 */
	public static String getConfigOne(String key) {
		List<String> list = configMap.get(key);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
 }
