package com.dataeye.omp.module.monitor.server;

import com.dataeye.common.CachedObjects;
import com.dataeye.common.ResourceHandler;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.DEResponse;
import com.dataeye.omp.common.ValueItem;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.constant.Constant.ServerCfg;
import com.dataeye.omp.constant.Constant.Table;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.utils.DateUtils;
import com.dataeye.utils.HbaseProxyClient;
import com.dataeye.utils.ServerUtils;
import com.google.gson.reflect.TypeToken;
import com.xunlei.jdbc.JdbcTemplate;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 趋势图
 * @author chenfanglin
 * @date 2016年1月14日 上午11:33:58
 */
@Service
public class ReportDAO {

	@Resource(name = "jdbcTemplateMonitor")
	private JdbcTemplate jdbcTemplateMonitor;
	
	@Resource(name = "jdbcTemplateMonitorStat")
	private JdbcTemplate jdbcTemplateMonitorStat;

	/**
	 * CPU相关- CPU 总利用率 按分钟
	 * 
	 * @param serverID
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月14日 上午11:33:54
	 */
	public DEResponse getCpuTotalUsageMinute(int serverID, int featrueID, String date,
											 String tableName, String object,String lang)
			throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
				+ ServerCfg.ROWKEY_SPLITER + date;
		System.out.println("table:" + tableName + ",rowkey:" + rowKey);
		NavigableMap<byte[], byte[]> dataMap =
				ReportHbaseUtils.getRowFromHbase(tableName, rowKey);
		if (dataMap != null && dataMap.size() > 0) {
			int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			for (int minute = 0; minute < 1440; minute++) {
				long time = (start + 60 * minute) * 1000L;
				String m = sdf.format(new Date(time));
				Map<String, Object> map = new HashMap<String, Object>();
				String value = ReportHbaseUtils.getOneMiniteValue(dataMap, minute);
				if (value != null) {
					map.put("x", m);
					map.put("y0", Double.parseDouble(value) / 10000);
					list.add(map);
				}
			}
			// 初始化name
			Map<String, String> name = new HashMap<String, String>();
			// CPU总使用率
			name.put("y0", ResourceHandler.getProperties("cpu_total", lang));
			deResp.setContent(list);
			deResp.setName(name);
		}

		return deResp;
	}


	/**
	 * CPU相关- CPU 总利用率 按小时
	 * 
	 * @param serverID
	 * @param featrueID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月14日 下午4:05:37
	 */
	public DEResponse getCpuTotalUsageHour(int serverID, int featrueID, String startdate, String enddate,
			String tableName, String object, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	//	Map<String, Double> valueMap = new HashMap<String, Double>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
					+ ServerCfg.ROWKEY_SPLITER + current;
			//ReportHbaseUtils.getValueFromHbaseHour(current, tableName, rowKey, valueMap);
			NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey);
			for (int hour = 1; hour <= 24; hour++) {
				String paddingHour = String.format("%02d:00", hour);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", current + " " + paddingHour);
				Long value = ReportHbaseUtils.getOneHourValue(dataMap, hour);
				if (value == null) {
					map.put("y0", 0);
				} else {
					map.put("y0", value.doubleValue() / 10000);
				}
				list.add(map);
			}
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// CPU总使用率
		name.put("y0", ResourceHandler.getProperties("cpu_total", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * CPU 1/5/15分钟负载 按分钟
	 * 
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param lang
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 上午11:32:02
	 */
	public DEResponse getCpuEveryMinUsageMinute(int serverID, String date, String tableName, String lang)
			throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_11 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.ONEMIN + ServerCfg.ROWKEY_SPLITER
				+ Constant.CPU + ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_12 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.FIVEMIN + ServerCfg.ROWKEY_SPLITER
				+ Constant.CPU + ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_13 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.FIFTEENMIN + ServerCfg.ROWKEY_SPLITER
				+ Constant.CPU + ServerCfg.ROWKEY_SPLITER + date;

		NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_11);
		NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_12);
		NavigableMap<byte[], byte[]> dataMap3 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_13);
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();
			String value1 = ReportHbaseUtils.getOneMiniteValue(dataMap1, minute);
			String value2 = ReportHbaseUtils.getOneMiniteValue(dataMap2, minute);
			String value3 = ReportHbaseUtils.getOneMiniteValue(dataMap3, minute);

			if (value1 != null || value2 != null || value3 != null) {
				map.put("x", m);
			}

			if (value1 != null) {
				double value = Double.parseDouble(value1) / 100;
				map.put("y0", ServerUtils.numberFormatDouble4RtnDouble(value));
			}
			if (value2 != null) {
				double value = Double.parseDouble(value2) / 100;
				map.put("y1", ServerUtils.numberFormatDouble4RtnDouble(value));
			}
			if (value3 != null) {
				double value = Double.parseDouble(value3) / 100;
				map.put("y2", ServerUtils.numberFormatDouble4RtnDouble(value));
			}
			if (!map.isEmpty()) {
				list.add(map);
			}

		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// CPU 1/5/15分钟负载
		name.put("y0", ResourceHandler.getProperties("one_load", lang));
		name.put("y1", ResourceHandler.getProperties("five_load", lang));
		name.put("y2", ResourceHandler.getProperties("fiften_load", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * CPU 1/5/15分钟负载 按小时
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param lang
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 上午11:32:37
	 */
	public DEResponse getCpuEveryMinUsageHour(int serverID, String startdate, String enddate, String tableName,
			String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_11 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.ONEMIN + ServerCfg.ROWKEY_SPLITER
					+ Constant.CPU + ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_12 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.FIVEMIN + ServerCfg.ROWKEY_SPLITER
					+ Constant.CPU + ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_13 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.FIFTEENMIN + ServerCfg.ROWKEY_SPLITER
					+ Constant.CPU + ServerCfg.ROWKEY_SPLITER + current;
			NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_11);
			NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_12);
			NavigableMap<byte[], byte[]> dataMap3 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_13);

			for (int hour = 1; hour <= 24; hour++) {
				String paddingHour = String.format("%02d:00", hour);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", current + " " + paddingHour);
				Long value1 = ReportHbaseUtils.getOneHourValue(dataMap1, hour);
				Long value2 = ReportHbaseUtils.getOneHourValue(dataMap2, hour);
				Long value3 = ReportHbaseUtils.getOneHourValue(dataMap3, hour);

				if (value1 == null) {
					map.put("y0", 0);
				} else {
					double value = value1.doubleValue() / 100;
					map.put("y0", ServerUtils.numberFormatDouble4RtnDouble(value));
				}
				if (value2 == null) {
					map.put("y1", 0);
				} else {
					double value = value2.doubleValue() / 100;
					map.put("y1", ServerUtils.numberFormatDouble4RtnDouble(value));
				}
				if (value3 == null) {
					map.put("y2", 0);
				} else {
					double value = value3.doubleValue() / 100;
					map.put("y2", ServerUtils.numberFormatDouble4RtnDouble(value));
				}
				list.add(map);
			}
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// CPU 1/5/15分钟负载
		name.put("y0", ResourceHandler.getProperties("one_load", lang));
		name.put("y1", ResourceHandler.getProperties("five_load", lang));
		name.put("y2", ResourceHandler.getProperties("fiften_load", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 各核心CPU使用率 按分钟
	 *
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param lang
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午3:33:19
	 */
	public DEResponse getCpuEveryCoreUsageMinute(int serverID,
				String date, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// 获取CPU物理核数
		int amount = getCpuCoreAmount(serverID);
		System.out.println("cpu amout: " + amount);
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Map<String, NavigableMap<byte[], byte[]>> totalMap = new HashMap<>();

		for (int i = 0; i < amount; i++) {
			String y = "y" + i;
			String cpu = "cpu" + i;
			name.put(y, ResourceHandler.getProperties(cpu, lang));
			String rowKey = serverID + ServerCfg.ROWKEY_SPLITER + Constant.CPUUSAGE + ServerCfg.ROWKEY_SPLITER
					+ cpu + ServerCfg.ROWKEY_SPLITER + date;
			NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey);
			totalMap.put(cpu, dataMap);
		}

		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<>();
			for (Map.Entry<String, NavigableMap<byte[], byte[]>> entry : totalMap.entrySet()) {
				String key = entry.getKey();
				String y = "y" + key.substring(3);
				String value = ReportHbaseUtils.getOneMiniteValue(entry.getValue(), minute);
				if (value != null) {
					map.put("x", m);
					double d = Double.parseDouble(value) / 10000;
					map.put(y, ServerUtils.numberFormatDouble4RtnDouble(d));
				}
			}
			if (!map.isEmpty()) {
				list.add(map);
			}

		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 各核心CPU使用率 按小时
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param lang
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午3:33:54
	 */
	public DEResponse getCpuEveryCoreUsageHour(int serverID, String startdate, String enddate, String tableName,
			String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// 获取CPU物理核数
		int amount = getCpuCoreAmount(serverID);
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);

		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			Map<String, NavigableMap<byte[], byte[]>> totalMap = new HashMap<>();
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			for (int i = 0; i < amount; i++) {
				String y = "y" + i;
				String cpu = Constant.CPU + i;
				name.put(y, ResourceHandler.getProperties(cpu, lang));
				String rowKey = serverID + ServerCfg.ROWKEY_SPLITER + Constant.CPUUSAGE + ServerCfg.ROWKEY_SPLITER
						+ Constant.CPU + i + ServerCfg.ROWKEY_SPLITER + current;
				NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey);
				totalMap.put(cpu, dataMap);
			}

			for (int hour = 1; hour <= 24; hour++) {
				Map<String, Object> map = new HashMap<>();
				for (Map.Entry<String, NavigableMap<byte[], byte[]>> entry : totalMap.entrySet()) {
					String key = entry.getKey();
					String y = "y" + key.substring(3);
					String paddingHour = String.format("%02d:00", hour);
					map.put("x", current + " " + paddingHour);
					Long value = ReportHbaseUtils.getOneHourValue(entry.getValue(), hour);
					if (value == null) {
						map.put(y, 0);
					} else {
						double d = value.doubleValue() / 10000;
						map.put(y, ServerUtils.numberFormatDouble4RtnDouble(d));
					}
					list.add(map);
				}
			}

		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 获取CPU物理核数
	 * 
	 * @param serverID
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午3:37:00
	 */
	public int getCpuCoreAmount(int serverID) throws ServerException {
		String sql = "select cpu_logic_cores from server_list where id = ?";
		try {
			int amount = jdbcTemplateMonitorStat.queryForInt(sql, serverID);
			return amount;
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.HBASE_ERROR, e);
		}
		return 0;
	}

	/**
	 * 内存相关- 内存总使用率 按分钟
	 * 
	 * @param serverID
	 * @param featrueID
	 * @param date
	 * @param tableName
	 * @param object
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午3:05:43
	 */
	public DEResponse getMemTotalUsageMinute(int serverID, int featrueID, String date, String tableName, String object,
			String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
				+ ServerCfg.ROWKEY_SPLITER + date;
		System.out.println("table:" + tableName + ",rowkey:" + rowKey);

		NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey);
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		long memoryTotal = ReportHbaseUtils.getServerMemory(serverID);
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();
			String value = ReportHbaseUtils.getOneMiniteValue(dataMap, minute);
			if (value != null) {
				map.put("x", m);
				double v= Double.parseDouble(value);

				BigDecimal d = new BigDecimal(v / (memoryTotal * 1024)).setScale(4, BigDecimal.ROUND_UP);
				map.put("y0", d.doubleValue());
				list.add(map);
			}
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// 内存总使用率
		name.put("y0", ResourceHandler.getProperties("mem_total", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 内存相关- 内存总使用率
	 * 
	 * @param serverID
	 * @param featrueID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param object
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午3:05:39
	 */
	public DEResponse getMemTotalUsageHour(int serverID, int featrueID, String startdate, String enddate,
			String tableName, String object, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		int memoryTotal = ReportHbaseUtils.getServerMemory(serverID);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
					+ ServerCfg.ROWKEY_SPLITER + current;
			NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey);
			for (int hour = 1; hour <=24; hour++) {
				String paddingHour = String.format("%02d:00", hour);
				Map<String, Object> map = new HashMap<String, Object>();
				Long value = ReportHbaseUtils.getOneHourValue(dataMap, hour);
				if (value != null){
					map.put("x", current + " " + paddingHour);

					double v= value.doubleValue();
					BigDecimal d = new BigDecimal(v / (memoryTotal * 1024)).setScale(4, BigDecimal.ROUND_UP);

					map.put("y0", d.doubleValue());
					list.add(map);
				}
			}
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// 内存总使用率
		name.put("y0", ResourceHandler.getProperties("mem_total", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 内存使用情况 按分钟 21 内存使用量 22 所有进程的private内存占用总和 23 所有进程的virtual内存占用总和 24所有进程private内存+ipcs内存的总和
	 * 
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月14日 下午7:43:09
	 */
	public DEResponse getMemUseStateMinute(int serverID, String date, String tableName, String lang)
			throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_21 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MENUSAGE + ServerCfg.ROWKEY_SPLITER
				+ Constant.MEM_USED + ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_22 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMPRIVATE + ServerCfg.ROWKEY_SPLITER
				+ Constant.MEM_PRI + ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_23 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMVIRTUAL + ServerCfg.ROWKEY_SPLITER
				+ Constant.MEM_VIR + ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_24 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMPRIVATEIPCS + ServerCfg.ROWKEY_SPLITER
				+ Constant.MPRI_IPCS + ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_27 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.PHYSIC_MEMORY_USE_FEATURE + ServerCfg.ROWKEY_SPLITER
				+ Constant.PHYSIC_MEMORY_USE_OBJECT + ServerCfg.ROWKEY_SPLITER + date;

		NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_21);
		NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_22);
		NavigableMap<byte[], byte[]> dataMap3 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_23);
		NavigableMap<byte[], byte[]> dataMap4 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_24);
		NavigableMap<byte[], byte[]> dataMap5 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_27);

		System.out.println("rowkey21:" + rowKey_21 + " rowkey22:" + rowKey_22 + " rowkey23:" + rowKey_23
				+ " rowkey24:" + rowKey_24 + " rowkey27:" + rowKey_27);

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();

			String value1 = ReportHbaseUtils.getOneMiniteValue(dataMap1, minute);
			String value2 = ReportHbaseUtils.getOneMiniteValue(dataMap2, minute);
			String value3 = ReportHbaseUtils.getOneMiniteValue(dataMap3, minute);
			String value4 = ReportHbaseUtils.getOneMiniteValue(dataMap4, minute);
			String value5 = ReportHbaseUtils.getOneMiniteValue(dataMap5, minute);

			if (value1 != null || value2 != null || value3 != null || value4 != null || value5 != null) {
				map.put("x", m);
			}

			long mb = 1024 * 1024;
			if (value1 != null) {
				map.put("y0", Long.parseLong(value1)/ mb);
			}
			if (value2 != null) {
				map.put("y1", Long.parseLong(value2)/mb);
			}
			if (value3 != null) {
				map.put("y2", Long.parseLong(value3)/mb);
			}
			if (value4 != null) {
				map.put("y3", Long.parseLong(value4)/mb);
			}
			if (value5 != null) {
				map.put("y4", Long.parseLong(value5)/mb);
			}

			if(!map.isEmpty()){
				list.add(map);
			}

		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("mem_used", lang));
		name.put("y1", ResourceHandler.getProperties("mem_pri", lang));
		name.put("y2", ResourceHandler.getProperties("mem_vir", lang));
		name.put("y3", ResourceHandler.getProperties("mem_pri_ipcs", lang));
		name.put("y4", ResourceHandler.getProperties("mem_res_total", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 内存使用情况 按小时
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月14日 下午7:51:27
	 */
	public DEResponse getMemUseStateHour(int serverID, String startdate, String enddate, String tableName, String lang)
			throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_21 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MENUSAGE + ServerCfg.ROWKEY_SPLITER
					+ Constant.MEM_USED + ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_22 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMPRIVATE + ServerCfg.ROWKEY_SPLITER
					+ Constant.MEM_PRI + ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_23 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMVIRTUAL + ServerCfg.ROWKEY_SPLITER
					+ Constant.MEM_VIR + ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_24 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMPRIVATEIPCS + ServerCfg.ROWKEY_SPLITER
					+ Constant.MPRI_IPCS + ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_27 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.PHYSIC_MEMORY_USE_FEATURE + ServerCfg.ROWKEY_SPLITER
					+ Constant.PHYSIC_MEMORY_USE_OBJECT + ServerCfg.ROWKEY_SPLITER + current;

			NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_21);
			NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_22);
			NavigableMap<byte[], byte[]> dataMap3 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_23);
			NavigableMap<byte[], byte[]> dataMap4 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_24);
			NavigableMap<byte[], byte[]> dataMap5 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_27);

			for (int hour = 1; hour <=24; hour++) {
				String paddingHour = String.format("%02d:00", hour);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", current + " " + paddingHour);
				long mb = 1024 * 1024;

				Long value1 = ReportHbaseUtils.getOneHourValue(dataMap1, hour);
				Long value2 = ReportHbaseUtils.getOneHourValue(dataMap2, hour);
				Long value3 = ReportHbaseUtils.getOneHourValue(dataMap3, hour);
				Long value4 = ReportHbaseUtils.getOneHourValue(dataMap4, hour);
				Long value5 = ReportHbaseUtils.getOneHourValue(dataMap5, hour);

				if (value1 != null) {
					map.put("y0", value1/mb);
				} else {
					map.put("y0", 0);
				}
				if (value2 != null) {
					map.put("y1", value2/mb);
				} else {
					map.put("y1", 0);
				}
				if (value3 != null) {
					map.put("y2", value3/mb);
				} else {
					map.put("y2", 0);
				}
				if (value4 != null) {
					map.put("y3", value4/mb);
				} else {
					map.put("y3", 0);
				}
				if (value5 != null) {
					map.put("y4", value5/mb);
				} else {
					map.put("y4", 0);
				}
				list.add(map);
			}
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("mem_used", lang));
		name.put("y1", ResourceHandler.getProperties("mem_pri", lang));
		name.put("y2", ResourceHandler.getProperties("mem_vir", lang));
		name.put("y3", ResourceHandler.getProperties("mem_pri_ipcs", lang));
		name.put("y4", ResourceHandler.getProperties("mem_res_total", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * SWAP内存使用率 WAP使用大小/SWAP总大小 按分钟
	 * 
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月15日 下午3:43:03
	 */
	public DEResponse getSwapMemTotalUsageMinute(int serverID, String date, String tableName, String lang)
			throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_swap = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPUSED + ServerCfg.ROWKEY_SPLITER
				+ Constant.MEM_SWAP_USED + ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_swap_total = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPTOTAL
				+ ServerCfg.ROWKEY_SPLITER + Constant.MEM_SWAP_TOTAL + ServerCfg.ROWKEY_SPLITER + date;
		NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_swap);
		NavigableMap<byte[], byte[]> dataMapTotal = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_swap_total);
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();
			String swapUsed = ReportHbaseUtils.getOneMiniteValue(dataMap, minute);
			String swapUsedTotal = ReportHbaseUtils.getOneMiniteValue(dataMapTotal, minute);
			if (swapUsed != null && swapUsedTotal != null) {
				if (Long.parseLong(swapUsedTotal) > 0) {
					map.put("x", m);
					map.put("y0", new BigDecimal(swapUsed).divide(
							new BigDecimal(swapUsedTotal), 4, BigDecimal.ROUND_HALF_EVEN));
					list.add(map);
				}
			}
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// 内存总使用率
		name.put("y0", ResourceHandler.getProperties("swap_total", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * SWAP内存使用率 WAP使用大小/SWAP总大小 按小时
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午3:43:48
	 */
	public DEResponse getSwapMemTotalUsageHour(int serverID, String startdate, String enddate, String tableName,
			String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_swap = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPUSED + ServerCfg.ROWKEY_SPLITER
					+ Constant.MEM_SWAP_USED + ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_swap_total = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPTOTAL
					+ ServerCfg.ROWKEY_SPLITER + Constant.MEM_SWAP_TOTAL + ServerCfg.ROWKEY_SPLITER + current;

			NavigableMap<byte[], byte[]> dataMapUsed = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_swap);
			NavigableMap<byte[], byte[]> dataMapTotal = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_swap_total);

			for (int hour = 1; hour <=24; hour++) {
				String paddingHour = String.format("%02d:00", hour);
				String key = current + " " + String.format("%02d", hour);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", current + " " + paddingHour);
				Long swapUsed = ReportHbaseUtils.getOneHourValue(dataMapUsed, hour);
				Long swapTotal = ReportHbaseUtils.getOneHourValue(dataMapTotal, hour);
				if (swapUsed != null && swapTotal != null && swapTotal > 0) {
					map.put("y0", new BigDecimal(swapUsed).divide(
									new BigDecimal(swapTotal), 4, BigDecimal.ROUND_HALF_EVEN));
				} else {
					map.put("y0", 0);
				}
				list.add(map);
			}
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// 内存总使用率
		name.put("y0", ResourceHandler.getProperties("swap_total", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * Swap 内存使用量 按分钟
	 * 
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午5:00:59
	 */
	public DEResponse getSwapMemUseStateMinute(int serverID, String date, String tableName, String lang)
			throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_swap = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPUSED + ServerCfg.ROWKEY_SPLITER
				+ Constant.MEM_SWAP_USED + ServerCfg.ROWKEY_SPLITER + date;
		NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_swap);
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();
			String swapUsed = ReportHbaseUtils.getOneMiniteValue(dataMap, minute);
			long mb = 1024 * 1024;
			if (swapUsed != null) {
				map.put("x", m);
				map.put("y0", Long.parseLong(swapUsed) / mb);
				list.add(map);
			}
		}

		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// 内存总使用率
		name.put("y0", ResourceHandler.getProperties("swap_used", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * Swap 内存使用量 按小时
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午5:01:03
	 */
	public DEResponse getSwapMemUseStateHour(int serverID, String startdate, String enddate, String tableName,
			String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_swap = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPUSED + ServerCfg.ROWKEY_SPLITER
					+ Constant.MEM_SWAP_USED + ServerCfg.ROWKEY_SPLITER + current;
			NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_swap);
			for (int hour = 1; hour <=24; hour++) {
				String paddingHour = String.format("%02d:00", hour);
				Map<String, Object> map = new HashMap<String, Object>();
				Long swap_used = ReportHbaseUtils.getOneHourValue(dataMap, hour);
				long mb = 1024 * 1024;
				if (swap_used != null) {
					map.put("x", current + " " + paddingHour);
					map.put("y0", swap_used / mb);
					list.add(map);
				}
			}
		}

		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// 内存总使用率
		name.put("y0", ResourceHandler.getProperties("swap_used", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 打开的Tcp连接数 按分钟
	 * 
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月18日 下午3:43:34
	 */
	public DEResponse getTcpConnMinute(int serverID, String date, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_swap = serverID + ServerCfg.ROWKEY_SPLITER + Constant.TCPCONN + ServerCfg.ROWKEY_SPLITER
				+ Constant.CURRESTAB + ServerCfg.ROWKEY_SPLITER + date;
		setValueOneRowKeyMinute(tableName, date, rowKey_swap, list);
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("tcp_open", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 打开的Tcp连接数 按小时
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月18日 下午3:43:24
	 */
	public DEResponse getTcpConnHour(int serverID, String startdate, String enddate, String tableName, String lang)
			throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);

		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_swap = serverID + ServerCfg.ROWKEY_SPLITER + Constant.TCPCONN + ServerCfg.ROWKEY_SPLITER
					+ Constant.CURRESTAB + ServerCfg.ROWKEY_SPLITER + current;
			setValueOneRowKeyHour(current, tableName, rowKey_swap, list);
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("tcp_open", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 被动打开的Tcp连接数 按分钟
	 * 
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月18日 下午3:43:17
	 */
	public DEResponse getPassiveTcpConnMinute(int serverID, String date, String tableName, String lang)
			throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_swap = serverID + ServerCfg.ROWKEY_SPLITER + Constant.PASSIVETCPCONN + ServerCfg.ROWKEY_SPLITER
				+ Constant.PASSIVEOPENS + ServerCfg.ROWKEY_SPLITER + date;
		setValueOneRowKeyMinute(tableName, date, rowKey_swap, list);
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("tcp_passive_open", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 被动打开的Tcp连接数 按小时
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月18日 下午3:43:10
	 */
	public DEResponse getPassiveTcpConnHour(int serverID, String startdate, String enddate, String tableName,
			String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_swap = serverID + ServerCfg.ROWKEY_SPLITER + Constant.PASSIVETCPCONN
					+ ServerCfg.ROWKEY_SPLITER + Constant.PASSIVEOPENS + ServerCfg.ROWKEY_SPLITER + current;
			setValueOneRowKeyHour(current, tableName, rowKey_swap, list);
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("tcp_passive_open", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 内网出入流量 按分钟
	 * 
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午5:10:30
	 */
	public DEResponse getIntranetNICFlowMinute(int serverID, String date, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_out = serverID + ServerCfg.ROWKEY_SPLITER + Constant.OUTFLOW + ServerCfg.ROWKEY_SPLITER
				+ Constant.EM2 + ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_in = serverID + ServerCfg.ROWKEY_SPLITER + Constant.INFLOW + ServerCfg.ROWKEY_SPLITER
				+ Constant.EM2 + ServerCfg.ROWKEY_SPLITER + date;

		NavigableMap<byte[], byte[]> dataMap_out = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_out);
		NavigableMap<byte[], byte[]> dataMap_in = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_in);

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();
			String value1 = ReportHbaseUtils.getOneMiniteValue(dataMap_out, minute);
			String value2 = ReportHbaseUtils.getOneMiniteValue(dataMap_in, minute);

			if (value1 != null || value2 != null) {
				map.put("x", m);
			}
			if (value1 != null) {
				map.put("y0", new BigDecimal(value1).divide(
						new BigDecimal(1024 * 1024), 2, BigDecimal.ROUND_UP));
			}

			if (value2 != null) {
				map.put("y1", new BigDecimal(value2).divide(
						new BigDecimal(1024 * 1024), 2, BigDecimal.ROUND_UP));
			}

			if (!map.isEmpty()) {
				list.add(map);
			}
		}

		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("intranet_out_flow", lang));
		name.put("y1", ResourceHandler.getProperties("intranet_in_flow", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}



	/**
	 * 一个rowKey，获取hbase数据，存放到list中
	 * @param tableName
	 * @param date
	 * @param rowKey
	 * @param list
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月21日 上午10:55:01
	 */
	private void setValueOneRowKeyMinute(String tableName, String date, String rowKey,List<Map<String, Object>> list) throws ServerException {
		NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey);
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();
			String value = ReportHbaseUtils.getOneMiniteValue(dataMap, minute);
			if (value != null) {
				map.put("x", m);
				map.put("y0", Long.parseLong(value));
				list.add(map);
			}
		}
	}
	
	/**
	 * 一个rowKey，获取hbase数据，存放到list中
	 * @param current
	 * @param tableName
	 * @param rowKey
	 * @param list
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月21日 上午10:58:15
	 */
	private void setValueOneRowKeyHour(String current, String tableName, String rowKey
			,List<Map<String, Object>> list) throws ServerException {
		NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey);
		for (int hour = 1; hour <= 24; hour++) {
			String paddingHour = String.format("%02d:00", hour);
			Map<String, Object> map = new HashMap<String, Object>();
			Long swap_used = ReportHbaseUtils.getOneHourValue(dataMap, hour);
			if (swap_used != null) {
				map.put("x", current + " " + paddingHour);
				map.put("y0",swap_used);
				list.add(map);
			}

		}
	}
	
	/**
	 * 获取hbase数据，存放到list中
	 * @param tableName
	 * @param date
	 * @param rowKey_out
	 * @param rowKey_in
	 * @param list
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月21日 上午10:34:09
	 */
	private void setValueMinute(String tableName, String date, String rowKey_out,String rowKey_in,List<Map<String, Object>> list) throws ServerException {

		NavigableMap<byte[], byte[]> dataMap_out = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_out);
		NavigableMap<byte[], byte[]> dataMap_in = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_in);

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();
			String value1 = ReportHbaseUtils.getOneMiniteValue(dataMap_out, minute);
			String value2 = ReportHbaseUtils.getOneMiniteValue(dataMap_in, minute);

			if (value1 != null || value2 != null) {
				map.put("x", m);
			}
			if (value1 != null) {
				map.put("y0", Long.parseLong(value1));
			}

			if (value2 != null) {
				map.put("y1", Long.parseLong(value2));
			}

			if (!map.isEmpty()) {
				list.add(map);
			}
		}
	}
	
	/**
	 * 获取hbase数据，存放到list中
	 * @param current
	 * @param tableName
	 * @param rowKey_out
	 * @param rowKey_in
	 * @param list
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月21日 上午10:38:01
	 */
	private void setValueHour(String current, String tableName, String rowKey_out, String rowKey_in,
							  List<Map<String, Object>> list) throws ServerException {
//		ReportHbaseUtils.getValueFromHbaseHour(current, tableName, rowKey_out, valueMap_out);
//		ReportHbaseUtils.getValueFromHbaseHour(current, tableName, rowKey_in, valueMap_in);
		NavigableMap<byte[], byte[]> dataMapOut = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_out);
		NavigableMap<byte[], byte[]> dataMapIn = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_in);
		for (int hour = 1; hour <= 24; hour++) {
			String paddingHour = String.format("%02d:00", hour);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("x", current + " " + paddingHour);
			Long valueOut = ReportHbaseUtils.getOneHourValue(dataMapOut, hour);
			Long valueIn = ReportHbaseUtils.getOneHourValue(dataMapIn, hour);
			if (valueOut != null) {
				map.put("y0",valueOut);
			} else {
				map.put("y0", 0);
			}
			if (valueIn!= null) {
				map.put("y1", valueIn);
			} else {
				map.put("y1", 0);
			}
			list.add(map);
		}
	}
	
	/**
	 * 内网出入流量 按小时
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午5:10:51
	 */
	public DEResponse getIntranetNICFlowHour(int serverID, String startdate, String enddate, String tableName,
			String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_out = serverID + ServerCfg.ROWKEY_SPLITER + Constant.OUTFLOW
					+ ServerCfg.ROWKEY_SPLITER + Constant.EM2 + ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_in = serverID + ServerCfg.ROWKEY_SPLITER + Constant.INFLOW
					+ ServerCfg.ROWKEY_SPLITER + Constant.EM2 + ServerCfg.ROWKEY_SPLITER + current;
			NavigableMap<byte[], byte[]> dataMapOut = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_out);
			NavigableMap<byte[], byte[]> dataMapIn = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_in);
			for (int hour = 1; hour <= 24; hour++) {
				String paddingHour = String.format("%02d:00", hour);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", current + " " + paddingHour);
				Long valueOut = ReportHbaseUtils.getOneHourValue(dataMapOut, hour);
				Long valueIn = ReportHbaseUtils.getOneHourValue(dataMapIn, hour);
				if (valueOut != null) {
					map.put("y0", new BigDecimal(valueOut)
							.divide(new BigDecimal(1024 * 1024), 2, BigDecimal.ROUND_UP));
				} else {
					map.put("y0", 0);
				}
				if (valueIn!= null) {
					map.put("y1", new BigDecimal(valueIn)
							.divide(new BigDecimal(1024 * 1024), 2, BigDecimal.ROUND_UP));
				} else {
					map.put("y1", 0);
				}
				list.add(map);
			}


		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("intranet_out_flow", lang));
		name.put("y1", ResourceHandler.getProperties("intranet_in_flow", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 内网出入包量 按分钟
	 * 
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午5:11:05
	 */
	public DEResponse getIntranetNICPackageVolumeMinute(int serverID, String date, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_out = serverID + ServerCfg.ROWKEY_SPLITER + Constant.OUTPACKAGEVOLUME + ServerCfg.ROWKEY_SPLITER
				+ Constant.EM2 + ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_in = serverID + ServerCfg.ROWKEY_SPLITER + Constant.INPACKAGEVOLUME + ServerCfg.ROWKEY_SPLITER
				+ Constant.EM2 + ServerCfg.ROWKEY_SPLITER + date;
		setValueMinute(tableName, date, rowKey_out, rowKey_in, list);
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("intranet_out_package", lang));
		name.put("y1", ResourceHandler.getProperties("intranet_in_package", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 内网出入包量 按小时
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午5:11:34
	 */
	public DEResponse getIntranetNICPackageVolumeHour(int serverID, String startdate, String enddate, String tableName,
			String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_out = serverID + ServerCfg.ROWKEY_SPLITER + Constant.OUTPACKAGEVOLUME
					+ ServerCfg.ROWKEY_SPLITER + Constant.EM2 + ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_in = serverID + ServerCfg.ROWKEY_SPLITER + Constant.INPACKAGEVOLUME
					+ ServerCfg.ROWKEY_SPLITER + Constant.EM2 + ServerCfg.ROWKEY_SPLITER + current;
			setValueHour(current, tableName, rowKey_out, rowKey_in, list);
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("intranet_out_package", lang));
		name.put("y1", ResourceHandler.getProperties("intranet_in_package", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 外网出入流量 按分钟
	 * 
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午5:11:47
	 */
	public DEResponse getExtranetNICFlowMinute(int serverID, String date, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_out = serverID + ServerCfg.ROWKEY_SPLITER + Constant.OUTFLOW + ServerCfg.ROWKEY_SPLITER
				+ Constant.EM1 + ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_in = serverID + ServerCfg.ROWKEY_SPLITER + Constant.INFLOW + ServerCfg.ROWKEY_SPLITER
				+ Constant.EM1 + ServerCfg.ROWKEY_SPLITER + date;
		NavigableMap<byte[], byte[]> dataMap_out = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_out);
		NavigableMap<byte[], byte[]> dataMap_in = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_in);

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();
			String value1 = ReportHbaseUtils.getOneMiniteValue(dataMap_out, minute);
			String value2 = ReportHbaseUtils.getOneMiniteValue(dataMap_in, minute);

			if (value1 != null || value2 != null) {
				map.put("x", m);
			}
			if (value1 != null) {
				map.put("y0", new BigDecimal(value1)
						.divide(new BigDecimal(1024 * 1024), 2, BigDecimal.ROUND_UP));
			}

			if (value2 != null) {
				map.put("y1", new BigDecimal(value2)
						.divide(new BigDecimal(1024 * 1024), 2, BigDecimal.ROUND_UP));
			}

			if (!map.isEmpty()) {
				list.add(map);
			}
		}

		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("extranet_out_flow", lang));
		name.put("y1", ResourceHandler.getProperties("extranet_in_flow", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 外网出入流量 按小时
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午5:12:00
	 */
	public DEResponse getExtranetNICFlowHour(int serverID, String startdate, String enddate, String tableName,
			String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_out = serverID + ServerCfg.ROWKEY_SPLITER + Constant.OUTFLOW
					+ ServerCfg.ROWKEY_SPLITER + Constant.EM1 + ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_in = serverID + ServerCfg.ROWKEY_SPLITER + Constant.INFLOW
					+ ServerCfg.ROWKEY_SPLITER + Constant.EM1 + ServerCfg.ROWKEY_SPLITER + current;

			NavigableMap<byte[], byte[]> dataMapOut = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_out);
			NavigableMap<byte[], byte[]> dataMapIn = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_in);
			for (int hour = 1; hour <= 24; hour++) {
				String paddingHour = String.format("%02d:00", hour);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", current + " " + paddingHour);
				Long valueOut = ReportHbaseUtils.getOneHourValue(dataMapOut, hour);
				Long valueIn = ReportHbaseUtils.getOneHourValue(dataMapIn, hour);
				if (valueOut != null) {
					map.put("y0", new BigDecimal(valueOut)
							.divide(new BigDecimal(1024 * 1024), 2, BigDecimal.ROUND_UP));
				} else {
					map.put("y0", 0);
				}
				if (valueIn!= null) {
					map.put("y1", new BigDecimal(valueIn)
							.divide(new BigDecimal(1024 * 1024), 2, BigDecimal.ROUND_UP));
				} else {
					map.put("y1", 0);
				}
				list.add(map);
			}
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("extranet_out_flow", lang));
		name.put("y1", ResourceHandler.getProperties("extranet_in_flow", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 外网出入包量 按分钟
	 * 
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午5:12:10
	 */
	public DEResponse getExtranetNICPackageVolumeMinute(int serverID, String date, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_out = serverID + ServerCfg.ROWKEY_SPLITER + Constant.OUTPACKAGEVOLUME + ServerCfg.ROWKEY_SPLITER
				+ Constant.EM1 + ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_in = serverID + ServerCfg.ROWKEY_SPLITER + Constant.INPACKAGEVOLUME + ServerCfg.ROWKEY_SPLITER
				+ Constant.EM1 + ServerCfg.ROWKEY_SPLITER + date;
		
		setValueMinute(tableName, date, rowKey_out, rowKey_in, list);
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("extranet_out_package", lang));
		name.put("y1", ResourceHandler.getProperties("extranet_in_package", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 外网出入包量 按小时
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午5:12:25
	 */
	public DEResponse getExtranetNICPackageVolumeHour(int serverID, String startdate, String enddate, String tableName,
			String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_out = serverID + ServerCfg.ROWKEY_SPLITER + Constant.OUTPACKAGEVOLUME
					+ ServerCfg.ROWKEY_SPLITER + Constant.EM1 + ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_in = serverID + ServerCfg.ROWKEY_SPLITER + Constant.INPACKAGEVOLUME
					+ ServerCfg.ROWKEY_SPLITER + Constant.EM1 + ServerCfg.ROWKEY_SPLITER + current;
			setValueHour(current, tableName, rowKey_out, rowKey_in, list);
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("extranet_out_flow", lang));
		name.put("y1", ResourceHandler.getProperties("extranet_in_flow", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * UDP收发数据报 按分钟
	 * 
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午5:12:36
	 */
	public DEResponse getUDPDatagramMinute(int serverID, String date, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_receive = serverID + ServerCfg.ROWKEY_SPLITER + Constant.UDPRECEIVEDATAGRAM + ServerCfg.ROWKEY_SPLITER
				+ Constant.INDATAGRAMS + ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_send = serverID + ServerCfg.ROWKEY_SPLITER + Constant.UDPSENDDATAGRAM + ServerCfg.ROWKEY_SPLITER
				+ Constant.OUTDATAGRAMS + ServerCfg.ROWKEY_SPLITER + date;
		setValueMinute(tableName, date, rowKey_receive, rowKey_send, list);
		
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("udp_receive_datagram", lang));
		name.put("y1", ResourceHandler.getProperties("udp_send_datagram", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * UDP收发数据报 按小时
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午5:12:50
	 */
	public DEResponse getUDPDatagramHour(int serverID, String startdate, String enddate, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_receive = serverID + ServerCfg.ROWKEY_SPLITER + Constant.UDPRECEIVEDATAGRAM
					+ ServerCfg.ROWKEY_SPLITER + Constant.INDATAGRAMS + ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_send = serverID + ServerCfg.ROWKEY_SPLITER + Constant.UDPSENDDATAGRAM
					+ ServerCfg.ROWKEY_SPLITER + Constant.OUTDATAGRAMS + ServerCfg.ROWKEY_SPLITER + current;
			setValueHour(current, tableName, rowKey_receive, rowKey_send, list);
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("udp_receive_datagram", lang));
		name.put("y1", ResourceHandler.getProperties("udp_send_datagram", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 磁盘IO相关 - 磁盘分区使用率 按分钟
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午7:01:33
	 */
	public DEResponse getIOTotalUsageMinute(int serverID, String date,
											String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String diskPartition = ReportHbaseUtils.getDiskPartition(serverID);
		// json字符串转对象集合
		List<DiskPartition> listPartition = CachedObjects.GSON.fromJson(diskPartition,
				new TypeToken<List<DiskPartition>>() {
				}.getType());

		Map<String, NavigableMap<byte[], byte[]>> totalMap =
				new HashMap<String, NavigableMap<byte[], byte[]>>();
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		for (int j = 0; j < listPartition.size(); j++) {
			DiskPartition disk = listPartition.get(j);
			String y = "y" + j;
			name.put(y, disk.getPartition());
			String partition = disk.getPartition();
			String rowKey_root_free = serverID + ServerCfg.ROWKEY_SPLITER +
					Constant.FREEPARTITION + ServerCfg.ROWKEY_SPLITER + partition
					+ ServerCfg.ROWKEY_SPLITER + date;

			NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_root_free);
			totalMap.put(y + ServerCfg.ROWKEY_SPLITER + disk.getPartitionSize(), dataMap);
		}

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();

			for (Map.Entry<String, NavigableMap<byte[], byte[]>> entry : totalMap.entrySet()) {
				String[] keys = entry.getKey().split(ServerCfg.ROWKEY_SPLITER);
				String y = keys[0];
				// 总大小
				Long partitionSize = Long.parseLong(keys[1]);
				Long total = partitionSize * 1024;
				String valueFree = ReportHbaseUtils.getOneMiniteValue(entry.getValue(), minute);
				if (valueFree != null) {
					map.put("x", m);
					Long free = Long.parseLong(valueFree);
					double value = (total - free) / total.doubleValue();
					map.put(y, ServerUtils.numberFormatDouble4RtnDouble(value));
					list.add(map);
				}
			}
		}
		// IO使用率
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 磁盘IO相关 - 磁盘分区使用率 按小时
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月19日 下午7:01:50
	 */
	public DEResponse getIOTotalUsageHour(int serverID, String startdate,
				String enddate, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String diskPartition = ReportHbaseUtils.getDiskPartition(serverID);
		// json字符串转对象集合
		List<DiskPartition> listPartition = CachedObjects.GSON.fromJson(diskPartition,
				new TypeToken<List<DiskPartition>>() {}.getType());

		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);

		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			Map<String, NavigableMap<byte[], byte[]>> totalMap =
					new HashMap<String, NavigableMap<byte[], byte[]>>();
			for (int i = 0;i < listPartition.size();i++) {
				String y = "y" + i;
				DiskPartition disk = listPartition.get(i);
				String partition = disk.getPartition();
				name.put(y, partition);
				String rowKey_free = serverID + ServerCfg.ROWKEY_SPLITER +
						Constant.FREEPARTITION + ServerCfg.ROWKEY_SPLITER + partition
						+ ServerCfg.ROWKEY_SPLITER + current;

				NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_free);
				String key = y + ServerCfg.ROWKEY_SPLITER  + disk.getPartitionSize();
				totalMap.put(key, dataMap);
			}

			for (int hour = 1; hour <=24; hour++) {
				String paddingHour = String.format("%02d:00", hour);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", current + " " + paddingHour);
				for (Map.Entry<String, NavigableMap<byte[],byte[]>> entry : totalMap.entrySet()) {
					String[] keys = entry.getKey().split(ServerCfg.ROWKEY_SPLITER);
					String y = keys[0];
					// 总大小
					Long partitionSize = Long.parseLong(keys[1]);
					Long total = partitionSize.longValue() * 1024;
					Long valueFree = ReportHbaseUtils.getOneHourValue(entry.getValue(), hour);
					if (valueFree != null) {
						double value = (total - valueFree) / total.doubleValue();
						map.put(y, ServerUtils.numberFormatDouble4RtnDouble(value));
						list.add(map);
						map.put(y, value);
					} else {
						map.put(y, 0);
					}
					list.add(map);
				}
			}
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 *  磁盘IO相关 - 磁盘IO使用量 按分钟
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午7:02:01
	 */
	public DEResponse getIOUseStateMinute(int serverID, String date, String tableName, String lang)
			throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String diskPartition = ReportHbaseUtils.getDiskPartition(serverID);
		// json字符串转对象集合
		List<DiskPartition> listPartition = CachedObjects.GSON.fromJson(diskPartition,
				new TypeToken<List<DiskPartition>>() {}.getType());
		Map<String, NavigableMap<byte[], byte[]>> totalMap = new HashMap<String, NavigableMap<byte[], byte[]>>();

		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		for (int j = 0; j < listPartition.size(); j++) {
			DiskPartition disk = listPartition.get(j);
			String y = "y" + j;
			String partition = disk.getPartition();
			name.put(y, partition);
			String rowKey_root_free = serverID + ServerCfg.ROWKEY_SPLITER +
					Constant.FREEPARTITION + ServerCfg.ROWKEY_SPLITER + partition
					+ ServerCfg.ROWKEY_SPLITER + date;

			NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_root_free);
			String key = y + ServerCfg.ROWKEY_SPLITER + disk.getPartitionSize();
			totalMap.put(key, dataMap);
		}
		
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();
			for (Map.Entry<String, NavigableMap<byte[], byte[]>> entry : totalMap.entrySet()) {
				String[] keys = entry.getKey().split(ServerCfg.ROWKEY_SPLITER);
				String y = keys[0];
				// 总大小
				Long partitionSize = Long.parseLong(keys[1]);
				long total = partitionSize.longValue() * 1024;

				String valueFree = ReportHbaseUtils.getOneMiniteValue(entry.getValue(), minute);
				if (valueFree != null) {
					map.put("x", m);
					long free = Long.parseLong(valueFree);
					long value = total - free;
					double gb = 1024 * 1024 * 1024;
					BigDecimal result = new BigDecimal(value / gb)
							.setScale(2, RoundingMode.UP);
					map.put(y, result.doubleValue());
					list.add(map);
				}
			}
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}


	public DEResponse getFeatureFrom35To39ByHour(int serverID, String startDate,
												 String endDate, int featureId, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String diskDetail = ReportHbaseUtils.getDiskDetail(serverID);
		// json字符串转对象集合
		List<DiskDetail> listDetail = CachedObjects.GSON.fromJson(diskDetail,
				new TypeToken<List<DiskDetail>>() {}.getType());

		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startDate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(endDate);

		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			Map<String, NavigableMap<byte[], byte[]>> totalMap =
					new HashMap<String, NavigableMap<byte[], byte[]>>();
			for (int i = 0;i < listDetail.size();i++) {
				String y = "y" + i;
				DiskDetail disk = listDetail.get(i);
				String diskName = disk.getDisk();
				String[] names = diskName.split("/");
				String n = names[names.length - 1];
				name.put(y, n);
				String rowKey = serverID + ServerCfg.ROWKEY_SPLITER +
						featureId + ServerCfg.ROWKEY_SPLITER + n
						+ ServerCfg.ROWKEY_SPLITER + current;

				NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey);
				String key = y + ServerCfg.ROWKEY_SPLITER  + n;
				totalMap.put(key, dataMap);
			}

			for (int hour = 1; hour <=24; hour++) {
				String paddingHour = String.format("%02d:00", hour);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", current + " " + paddingHour);
				for (Map.Entry<String, NavigableMap<byte[],byte[]>> entry : totalMap.entrySet()) {
					String[] keys = entry.getKey().split(ServerCfg.ROWKEY_SPLITER);
					String y = keys[0];

					Long value= ReportHbaseUtils.getOneHourValue(entry.getValue(), hour);
					map.put(y, value);
					list.add(map);
				}
			}
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}


	public DEResponse getFeatureFrom35To39ByMinute(int serverID, String date, int featureId, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String diskDetail = ReportHbaseUtils.getDiskDetail(serverID);
		// json字符串转对象集合
		List<DiskDetail> listPartition = CachedObjects.GSON.fromJson(diskDetail,
				new TypeToken<List<DiskDetail>>() {}.getType());
		Map<String, NavigableMap<byte[], byte[]>> totalMap = new HashMap<String, NavigableMap<byte[], byte[]>>();

		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		for (int j = 0; j < listPartition.size(); j++) {
			DiskDetail disk = listPartition.get(j);
			String y = "y" + j;
			String diskName = disk.getDisk();
			String[] names = diskName.split("/");
			String n = names[names.length - 1];
			name.put(y, n);
			String rowKey = serverID + ServerCfg.ROWKEY_SPLITER +
					featureId + ServerCfg.ROWKEY_SPLITER + n
					+ ServerCfg.ROWKEY_SPLITER + date;

			NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey);
			String key = y + ServerCfg.ROWKEY_SPLITER + n;
			totalMap.put(key, dataMap);
		}

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();
			for (Map.Entry<String, NavigableMap<byte[], byte[]>> entry : totalMap.entrySet()) {
				String[] keys = entry.getKey().split(ServerCfg.ROWKEY_SPLITER);
				String y = keys[0];

				String value = ReportHbaseUtils.getOneMiniteValue(entry.getValue(), minute);
				if (value != null) {
					map.put("x", m);
					map.put(y, value);
					list.add(map);
				}
				
			}
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 *  磁盘IO相关 - 磁盘IO使用量 按小时
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午7:02:04
	 */
	public DEResponse getIOUseStateHour(int serverID, String startdate, String enddate,
										String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		String diskPartition = ReportHbaseUtils.getDiskPartition(serverID);
		// json字符串转对象集合
		List<DiskPartition> listPartition = CachedObjects.GSON.fromJson(diskPartition,
				new TypeToken<List<DiskPartition>>() {}.getType());
		
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			Map<String, NavigableMap<byte[], byte[]>> totalMap =
					new HashMap<String, NavigableMap<byte[], byte[]>>();

			for (int i = 0;i < listPartition.size();i++) {
				String y = "y" + i;
				String partition = listPartition.get(i).getPartition();
				name.put(y, partition);
				String rowKey_free = serverID + ServerCfg.ROWKEY_SPLITER +
						Constant.FREEPARTITION + ServerCfg.ROWKEY_SPLITER + partition
						+ ServerCfg.ROWKEY_SPLITER + current;

				String key = y + ServerCfg.ROWKEY_SPLITER + listPartition.get(i).getPartitionSize();
				NavigableMap<byte[], byte[]> dataMap = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_free);
				totalMap.put(key, dataMap);
			}

			for (int hour = 1; hour <=24; hour++) {
				String paddingHour = String.format("%02d:00", hour);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("x", current + " " + paddingHour);
				for (Map.Entry<String, NavigableMap<byte[], byte[]>> entry : totalMap.entrySet()) {
					String[] keys = entry.getKey().split(ServerCfg.ROWKEY_SPLITER);
					String y = keys[0];
					// 总大小
					Long partitionSize = Long.parseLong(keys[1]);
					long total = partitionSize.longValue() * 1024;
					Long valueFree = ReportHbaseUtils.getOneHourValue(entry.getValue(), hour);
					if (valueFree != null) {
						Long value = total - valueFree;
						long gb = 1024 * 1024 * 1024;
						BigDecimal result = new BigDecimal(value.doubleValue() / gb)
								.setScale(2, RoundingMode.UP);
						map.put(y, result.doubleValue());
					} else {
						map.put(y, 0);
					}
					list.add(map);
				}
			}
		}

		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 磁盘IO相关 - 磁盘IO读写 按分钟
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午7:02:08
	 */
	public DEResponse getDiskIOReadWriteMinute(int serverID, String date, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_r = serverID + ServerCfg.ROWKEY_SPLITER + Constant.DISKIOREAD + ServerCfg.ROWKEY_SPLITER + Constant.PGPGIN
				+ ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_w = serverID + ServerCfg.ROWKEY_SPLITER + Constant.DISKIOWRITE + ServerCfg.ROWKEY_SPLITER + Constant.PGPGOUT
				+ ServerCfg.ROWKEY_SPLITER + date;
		setValueMinute(tableName, date, rowKey_r, rowKey_w, list);
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// IO使用率
		name.put("y0", ResourceHandler.getProperties("io_r", lang));
		name.put("y1", ResourceHandler.getProperties("io_w", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 磁盘IO相关 - 磁盘IO读写 按小时
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午7:02:12
	 */
	public DEResponse getDiskIOReadWriteHour(int serverID, String startdate, String enddate, String tableName,
			String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Double> valueMap_r = new HashMap<String, Double>();
		Map<String, Double> valueMap_w = new HashMap<String, Double>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_r = serverID + ServerCfg.ROWKEY_SPLITER + Constant.DISKIOREAD + ServerCfg.ROWKEY_SPLITER + Constant.PGPGIN
					+ ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_w = serverID + ServerCfg.ROWKEY_SPLITER + Constant.DISKIOWRITE + ServerCfg.ROWKEY_SPLITER + Constant.PGPGOUT
					+ ServerCfg.ROWKEY_SPLITER + current;
			setValueHour(current, tableName, rowKey_r, rowKey_w, list);
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("io_r", lang));
		name.put("y1", ResourceHandler.getProperties("io_w", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 *  磁盘IO相关 - SWAP 使用情况 按分钟
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午7:02:16
	 */
	public DEResponse getIOSwapUseStateMinute(int serverID, String date, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_r = serverID + ServerCfg.ROWKEY_SPLITER + Constant.SWAPSI + ServerCfg.ROWKEY_SPLITER + Constant.PSWPIN
				+ ServerCfg.ROWKEY_SPLITER + date;
		String rowKey_w = serverID + ServerCfg.ROWKEY_SPLITER + Constant.SWAPSO + ServerCfg.ROWKEY_SPLITER + Constant.PSWPOUT
				+ ServerCfg.ROWKEY_SPLITER + date;
		setValueMinute(tableName, date, rowKey_r, rowKey_w, list);
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// IO使用率
		name.put("y0", ResourceHandler.getProperties("io_swap_i", lang));
		name.put("y1", ResourceHandler.getProperties("io_swap_o", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 *  磁盘IO相关 - SWAP 使用情况 按小时
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午7:02:19
	 */
	public DEResponse getIOSwapUseStateHour(int serverID, String startdate, String enddate, String tableName,
			String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Double> valueMap_r = new HashMap<String, Double>();
		Map<String, Double> valueMap_w = new HashMap<String, Double>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_r = serverID + ServerCfg.ROWKEY_SPLITER + Constant.SWAPSI + ServerCfg.ROWKEY_SPLITER + Constant.PSWPIN
					+ ServerCfg.ROWKEY_SPLITER + current;
			String rowKey_w = serverID + ServerCfg.ROWKEY_SPLITER + Constant.SWAPSO + ServerCfg.ROWKEY_SPLITER + Constant.PSWPOUT
					+ ServerCfg.ROWKEY_SPLITER + current;
			setValueHour(current, tableName, rowKey_r, rowKey_w, list);
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		name.put("y0", ResourceHandler.getProperties("io_swap_i", lang));
		name.put("y1", ResourceHandler.getProperties("io_swap_o", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 35	Svctm_time_max
	 * 36	Await_time_max
	 * 37	avgqu_sz_max
	 * 38	avgrq_sz
	 * 39	util_max
	 * @param serverID
	 * @param date
	 * @param tableName
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月21日 上午11:16:35
	 */
	public DEResponse getIOUseTrendMinute(int serverID, String date, String tableName, int featrueID,
			String object, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_swap = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER
				+ object + ServerCfg.ROWKEY_SPLITER + date;
		setValueOneRowKeyMinute(tableName, date, rowKey_swap, list);
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		if (Constant.SVCTMTIMEMAX == featrueID) {
			// Svctm_time
			name.put("y0", ResourceHandler.getProperties("io_svctm_time", lang));
		} else if (Constant.AWAITTIMEMAX == featrueID) {
			// Await_max
			name.put("y0", ResourceHandler.getProperties("io_await_max", lang));
		} else if (Constant.AVGQUSZMAX == featrueID) {
			// avgqu_sz
			name.put("y0", ResourceHandler.getProperties("io_avgqu_sz", lang));
		} else if (Constant.AVGRQSZ == featrueID) {
			// avgrq_sz
			name.put("y0", ResourceHandler.getProperties("io_avgrq_sz", lang));
		} else if (Constant.UTILMAX == featrueID) {
			// util
			name.put("y0", ResourceHandler.getProperties("io_util", lang));
		} 
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 35	Svctm_time_max
	 * 36	Await_time_max
	 * 37	avgqu_sz_max
	 * 38	avgrq_sz
	 * 39	util_max
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param tableName
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月21日 上午11:23:14
	 */
	public DEResponse getIOUseTrendHour(int serverID, String startdate, String enddate, String tableName,
			int featrueID, String object, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Double> valueMap_tcp = new HashMap<String, Double>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
			String current = DateUtils.getyyyyMMddFromTimestamp(time);
			String rowKey_swap = serverID + ServerCfg.ROWKEY_SPLITER + featrueID
					+ ServerCfg.ROWKEY_SPLITER + object + ServerCfg.ROWKEY_SPLITER + current;
			setValueOneRowKeyHour(current, tableName, rowKey_swap, list);
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		if (Constant.SVCTMTIMEMAX == featrueID) {
			// Svctm_time
			name.put("y0", ResourceHandler.getProperties("io_svctm_time", lang));
		} else if (Constant.AWAITTIMEMAX == featrueID) {
			// Await_max
			name.put("y0", ResourceHandler.getProperties("io_await_max", lang));
		} else if (Constant.AVGQUSZMAX == featrueID) {
			// avgqu_sz
			name.put("y0", ResourceHandler.getProperties("io_avgqu_sz", lang));
		} else if (Constant.AVGRQSZ == featrueID) {
			// avgrq_sz
			name.put("y0", ResourceHandler.getProperties("io_avgrq_sz", lang));
		} else if (Constant.UTILMAX == featrueID) {
			// util
			name.put("y0", ResourceHandler.getProperties("io_util", lang));
		} 
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 硬件状态
	 * @param serverID
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月25日 上午10:52:26
	 */
	public List<ValueItem> getHardWareStatus(Integer serverID, String lang) throws ServerException {
		List<ValueItem> list = new ArrayList<ValueItem>();
		String dbKey = serverID.toString();
		// cpu状态
		String column_cpu = Constant.CPUSTATUS + ServerCfg.ROWKEY_SPLITER + Constant.HARD_PROC;
		// 内存状态
		String column_mem = Constant.MEMORYSTATUS + ServerCfg.ROWKEY_SPLITER + Constant.HARD_MEM;
		// 机器温度状态
		String column_temps = Constant.TEMPSSTATUS + ServerCfg.ROWKEY_SPLITER + Constant.HARD_TEMPS;
		// 物理硬盘状态
		String column_disk = Constant.DISKSTATUS + ServerCfg.ROWKEY_SPLITER + Constant.HARD_DISK;
		// 74	电源状态
		String column_pwr = Constant.PWRSUPPLIESSTATUS + ServerCfg.ROWKEY_SPLITER + Constant.HARD_PWR;
		// 75	系统面板CMOS电池
		String column_batt = Constant.BATTERIESSTATUS + ServerCfg.ROWKEY_SPLITER + Constant.HARD_BATT;
		// 网卡状态
		String column_nics = Constant.NICSSTATUS + ServerCfg.ROWKEY_SPLITER + Constant.HARD_NICS;
		// 风扇状态
		String column_fans = Constant.FANSSTATUS + ServerCfg.ROWKEY_SPLITER + Constant.HARD_FANS;
		try {
			Result hbaseResult = HbaseProxyClient.getOneRecord(Table.OMP_SERVER_FEATURE_STAT, dbKey);
			if (hbaseResult != null && !hbaseResult.isEmpty()) {
				getHbaseValue(hbaseResult,column_cpu,list,ResourceHandler.getProperties("cpu_status", lang));
				getHbaseValue(hbaseResult,column_mem,list,ResourceHandler.getProperties("mem_status", lang));
				getHbaseValue(hbaseResult,column_temps,list,ResourceHandler.getProperties("temps_status", lang));
				getHbaseValue(hbaseResult,column_disk,list,ResourceHandler.getProperties("disk_status", lang));
				getHbaseValue(hbaseResult,column_pwr,list,ResourceHandler.getProperties("pwr_status", lang));
				getHbaseValue(hbaseResult,column_batt,list,ResourceHandler.getProperties("batt_status", lang));
				getHbaseValue(hbaseResult,column_nics,list,ResourceHandler.getProperties("nics_status", lang));
				getHbaseValue(hbaseResult,column_fans,list,ResourceHandler.getProperties("fans_status", lang));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.HBASE_ERROR, e);
		}
		return list; 
	}

	/**
	 * 从hbase中获取数据
	 * @param hbaseResult
	 * @param column
	 * @param list
	 * @param val
	 * @author chenfanglin <br>
	 * @date 2016年1月25日 上午11:23:08
	 */
	private void getHbaseValue(Result hbaseResult,String column,List<ValueItem> list, String val){
		byte[] cpu = hbaseResult.getValue(Bytes.toBytes("stat"), Bytes.toBytes(column));
		if (cpu != null) {
			ValueItem valueItem = new ValueItem();
			int value = Integer.parseInt(Bytes.toString(cpu).split("_")[0]);
			valueItem.setValue(value);
			valueItem.setItem(val);
			list.add(valueItem);
		}
	}


	public static void main(String[] args) {
		long value=616388935684314124L;
		long valu1 = 65920828L * 1024;

		System.out.println(value / 1024 / 8);
//
//		String s1 = "61638893568";
//		String s2 = "65920828";
//
//		BigDecimal b = new BigDecimal(s1).divide(new BigDecimal(s2),
//				2, BigDecimal.ROUND_UP);
//		System.out.println(b.doubleValue());
//		System.out.println(new BigDecimal(0).longValue());
	}

}
