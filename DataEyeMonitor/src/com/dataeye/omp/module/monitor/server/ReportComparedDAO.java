package com.dataeye.omp.module.monitor.server;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.http.entity.mime.MIME;
import org.springframework.stereotype.Service;

import com.dataeye.common.CachedObjects;
import com.dataeye.common.ResourceHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.DEResponse;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.constant.Constant.ServerCfg;
import com.dataeye.utils.DateUtils;
import com.dataeye.utils.ServerUtils;
import com.google.gson.reflect.TypeToken;

/**
 * 对比图
 *
 * @author chenfanglin
 * @date 2016年1月20日 上午10:08:31
 */
@Service
public class ReportComparedDAO {

	/**
	 * 10 CPU 总使用率对比图 11 CPU 1分钟负载 12 CPU 5分钟负载 13 CPU 15分钟负载
	 *
	 * @param serverID
	 * @param startdate
	 * @param starttime
	 * @param tableName
	 * @param lang
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 上午10:54:07
	 */
	public DEResponse getComparedCpuUsageMinute(int serverID, String startdate, String starttime, String tableName,
												int featrueID, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + Constant.CPU
				+ ServerCfg.ROWKEY_SPLITER + startdate;
		String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + Constant.CPU
				+ ServerCfg.ROWKEY_SPLITER + starttime;

		NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
		NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);


		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();

			String value1 = ReportHbaseUtils.getOneMiniteValue(dataMap1, minute);
			String value2 = ReportHbaseUtils.getOneMiniteValue(dataMap2, minute);

			if (value1 != null ||value2 != null) {
				map.put("x", m);
			}

			if (value1 != null) {
				if (Constant.CPUUSAGE == featrueID) {
					map.put("y0", Double.parseDouble(value1) / 10000);
				} else {
					map.put("y0", Double.parseDouble(value1) / 100);
				}
			}
			if (value2 != null) {
				if (Constant.CPUUSAGE == featrueID) {
					map.put("y1", Double.parseDouble(value2) / 10000);
				}else{
					map.put("y1", Double.parseDouble(value2) / 100);
				}
			}
			if (!map.isEmpty()) {
				list.add(map);
			}

		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		if (Constant.CPUUSAGE == featrueID) {
			// CPU使用率
			name.put("y0", startdate + " " + ResourceHandler.getProperties("cpu_total", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("cpu_total", lang));
		} else if (Constant.ONEMIN == featrueID) {
			// CPU 1分钟负载
			name.put("y0", startdate + " " + ResourceHandler.getProperties("one_load", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("one_load", lang));
		} else if (Constant.FIVEMIN == featrueID) {
			// CPU 5分钟负载
			name.put("y0", startdate + " " + ResourceHandler.getProperties("five_load", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("five_load", lang));
		} else if (Constant.FIFTEENMIN == featrueID) {
			// CPU 15分钟负载
			name.put("y0", startdate + " " + ResourceHandler.getProperties("fiften_load", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("fiften_load", lang));
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 10 CPU 总使用率对比图 11 CPU 1分钟负载 12 CPU 5分钟负载 13 CPU 15分钟负载
	 *
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param tableName
	 * @param lang
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 上午10:54:11
	 */
	public DEResponse getComparedCpuUsageHour(int serverID, String startdate, String enddate, String starttime,
					String endtime, String tableName, int featrueID, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);

		int begin = DateUtils.getUnixTimestampFromyyyyMMdd(starttime);
		int finish = DateUtils.getUnixTimestampFromyyyyMMdd(endtime);
		// 初始化 比对时间段
		List<String> date_list = new ArrayList<String>();
		int time1= start;
		int time2 = begin;

		for (; time1 <= end & time2 <= finish;) {
			String current1 = DateUtils.getyyyyMMddFromTimestamp(time1);
			String current2 = DateUtils.getyyyyMMddFromTimestamp(time2);
			String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + Constant.CPU
					+ ServerCfg.ROWKEY_SPLITER + current1;
			String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + Constant.CPU
					+ ServerCfg.ROWKEY_SPLITER + current2;

			String current_x = DateUtils.getMMddFromTimestamp(time1);
			String compared_x = DateUtils.getMMddFromTimestamp(time2);
			NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
			NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);
			for (int hour = 1; hour <= 24; hour++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String paddingHour = String.format("%02d:00", hour);
				// x = 01-16 10:00#01-22 10:00
				String x = current_x + " " + paddingHour + ServerCfg.ROWKEY_SPLITER + compared_x + " " + paddingHour;
				map.put("x", x);

				Long value1 = ReportHbaseUtils.getOneHourValue(dataMap1, hour);
				Long value2 = ReportHbaseUtils.getOneHourValue(dataMap2, hour);

				if (value1 != null) {
					if (Constant.CPUUSAGE == featrueID) {
						map.put("y0", value1.doubleValue() / 10000);
					} else {
						map.put("y0", value1.doubleValue() / 100);
					}
				} else {
					map.put("y0", 0);
				}

				if (value2 != null) {
					if (Constant.CPUUSAGE == featrueID) {
						map.put("y1", value2.doubleValue() / 10000);
					} else {
						map.put("y1", value2.doubleValue() / 100);
					}
				} else {
					map.put("y1", 0);
				}

				list.add(map);
			}

			time1 += Constant.SECONDS_ONE_DAY;
			time2 += Constant.SECONDS_ONE_DAY;
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		if (Constant.CPUUSAGE == featrueID) {
			// CPU使用率
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("cpu_total", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("cpu_total", lang));
		} else if (Constant.ONEMIN == featrueID) {
			// CPU 1分钟负载
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("one_load", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("one_load", lang));
		} else if (Constant.FIVEMIN == featrueID) {
			// CPU 5分钟负载
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("five_load", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("five_load", lang));
		} else if (Constant.FIFTEENMIN == featrueID) {
			// CPU 15分钟负载
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("fiften_load", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("fiften_load", lang));
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 内存使用率 按分钟
	 *
	 * @param serverID
	 * @param startdate
	 * @param starttime
	 * @param tableName
	 * @param featrueID
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午5:00:29
	 */
	public DEResponse getComparedMemUsageMinute(int serverID, String startdate, String starttime, String tableName,
												int featrueID, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER
				+ Constant.MEM_USED + ServerCfg.ROWKEY_SPLITER + startdate;
		String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER
				+ Constant.MEM_USED + ServerCfg.ROWKEY_SPLITER + starttime;

		NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
		NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);

		// 获取机器总内存
		long memory = ReportHbaseUtils.getServerMemory(serverID);
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();

			String value1 = ReportHbaseUtils.getOneMiniteValue(dataMap1, minute);
			String value2 = ReportHbaseUtils.getOneMiniteValue(dataMap2, minute);

			if (value1 != null ||
					value2 != null) {
				map.put("x", m);
			}

			if (value1 != null) {
				double d = Double.parseDouble(value1) / (new Double(memory) * 1024);
				map.put("y0", ServerUtils.numberFormatDouble4RtnDouble(d));
			}

			if (value2 != null) {
				double d = Double.parseDouble(value2) / (new Double(memory) * 1024);
				map.put("y1", ServerUtils.numberFormatDouble4RtnDouble(d));
			}

			if (!map.isEmpty()) {
				list.add(map);
			}

		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// 内存使用率
		name.put("y0", startdate + " " + ResourceHandler.getProperties("mem_total", lang));
		name.put("y1", starttime + " " + ResourceHandler.getProperties("mem_total", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 内存使用率 按小时
	 *
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param tableName
	 * @param featrueID
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午5:00:32
	 */
	public DEResponse getComparedMemUsageHour(int serverID, String startdate, String enddate, String starttime,
											  String endtime, String tableName, int featrueID, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);
		int begin = DateUtils.getUnixTimestampFromyyyyMMdd(starttime);
		int finish = DateUtils.getUnixTimestampFromyyyyMMdd(endtime);

		// 获取机器总内存
		int memory = ReportHbaseUtils.getServerMemory(serverID);

		int time1 = start;
		int time2 = begin;
		for (; time1 <= end & time2 <= finish; ) {
			String current1 = DateUtils.getyyyyMMddFromTimestamp(time1);
			String current2 = DateUtils.getyyyyMMddFromTimestamp(time2);

			String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID +
					ServerCfg.ROWKEY_SPLITER + Constant.MEM_USED
					+ ServerCfg.ROWKEY_SPLITER + current1;

			String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID +
					ServerCfg.ROWKEY_SPLITER + Constant.MEM_USED
					+ ServerCfg.ROWKEY_SPLITER + current2;
			NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
			NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);

			String current_x = DateUtils.getMMddFromTimestamp(time1);
			String compared_x = DateUtils.getMMddFromTimestamp(time2);
			for (int hour = 1; hour <=24; hour++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String paddingHour = String.format("%02d:00", hour);

				// x = 01-16 10:00#01-22 10:00
				String x = current_x + " " + paddingHour + ServerCfg.ROWKEY_SPLITER
						+ compared_x + " " + paddingHour;
				Long value1 = ReportHbaseUtils.getOneHourValue(dataMap1, hour);
				Long value2 = ReportHbaseUtils.getOneHourValue(dataMap2, hour);

					map.put("x", x);

				if (value1 != null) {
					map.put("y0", value1.doubleValue() / (new Double(memory) * 1024));
				} else {
					map.put("y0", 0);
				}
				if (value2 != null) {
					map.put("y1", value2.doubleValue() / (new Double(memory) * 1024));
				} else {
					map.put("y1", 0);
				}
				list.add(map);
			}
			time1 += Constant.SECONDS_ONE_DAY;
			time2 += Constant.SECONDS_ONE_DAY;
		}

		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// 内存使用率
		name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("mem_total", lang));
		name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("mem_total", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 内存相关 21 mem_used 内存使用率 22 mem_pri Private内存 23 mem_vir Virtual内存 24 Private+IPCS 25 mem_swap_used SWAP内存使用量
	 *
	 * @param serverID
	 * @param startdate
	 * @param starttime
	 * @param tableName
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午5:30:33
	 */
	public DEResponse getComparedMemUsageAmountMinute(int serverID, String startdate, String starttime,
													  String tableName, int featrueID, String object, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
				+ ServerCfg.ROWKEY_SPLITER + startdate;
		String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
				+ ServerCfg.ROWKEY_SPLITER + starttime;

		NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
		NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();

			long mb = 1024 * 1024;

			String value1 = ReportHbaseUtils.getOneMiniteValue(dataMap1, minute);
			String value2 = ReportHbaseUtils.getOneMiniteValue(dataMap2, minute);
			if (value1 != null ||
					value2 != null) {
				map.put("x", m);
			}

			if (value1 != null) {
				map.put("y0", Long.parseLong(value1) / mb);
			}

			if (value2 != null) {
				map.put("y1", Long.parseLong(value2) / mb);
			}

			if (!map.isEmpty()) {
				list.add(map);
			}

		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		if (Constant.MENUSAGE == featrueID) {
			// 内存 使用量
			name.put("y0", startdate + " " + ResourceHandler.getProperties("mem_used", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("mem_used", lang));
		} else if (Constant.MEMPRIVATE == featrueID) {
			// Private内存
			name.put("y0", startdate + " " + ResourceHandler.getProperties("mem_pri", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("mem_pri", lang));
		} else if (Constant.MEMVIRTUAL == featrueID) {
			// Virtual内存
			name.put("y0", startdate + " " + ResourceHandler.getProperties("mem_vir", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("mem_vir", lang));
		} else if (Constant.MEMPRIVATEIPCS == featrueID) {
			// Private+IPCS
			name.put("y0", startdate + " " + ResourceHandler.getProperties("mem_pri_ipcs", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("mem_pri_ipcs", lang));
		} else if (Constant.MEMSWAPUSED == featrueID) {
			// SWAP内存使用量
			name.put("y0", startdate + " " + ResourceHandler.getProperties("swap_used", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("swap_used", lang));
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 内存相关 21 mem_used 内存使用率 22 mem_pri Private内存 23 mem_vir Virtual内存 24 Private+IPCS 25 mem_swap_used SWAP内存使用量
	 *
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param tableName
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 下午5:35:53
	 */
	public DEResponse getComparedMemUsageAmountHour(int serverID, String startdate, String enddate, String starttime,
													String endtime, String tableName, int featrueID, String object, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);

		int begin = DateUtils.getUnixTimestampFromyyyyMMdd(starttime);
		int finish = DateUtils.getUnixTimestampFromyyyyMMdd(endtime);

		int time1 = start;
		int time2 = begin;
		for (; time1 <= end && time2 <=finish;) {

			String current1 = DateUtils.getyyyyMMddFromTimestamp(time1);
			String current2 = DateUtils.getyyyyMMddFromTimestamp(time2);

			String current_x = DateUtils.getMMddFromTimestamp(time1);
			String compared_x = DateUtils.getMMddFromTimestamp(time2);

			String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER
					+ featrueID + ServerCfg.ROWKEY_SPLITER + object
					+ ServerCfg.ROWKEY_SPLITER + current1;

			String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER
					+ featrueID + ServerCfg.ROWKEY_SPLITER + object
					+ ServerCfg.ROWKEY_SPLITER + current2;

			NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
			NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);

			for (int hour = 1; hour <= 24; hour++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String paddingHour = String.format("%02d:00", hour);
				//String key = current + " " + String.format("%02d", hour);
				// x = 01-16 10:00#01-22 10:00
				String x = current_x + " " + paddingHour + ServerCfg.ROWKEY_SPLITER
						+ compared_x + " " + paddingHour;

				Long value1 = ReportHbaseUtils.getOneHourValue(dataMap1, hour);
				Long value2 = ReportHbaseUtils.getOneHourValue(dataMap2, hour);

					map.put("x", x);

				long mb = 1024 * 1024;
				if (value1 != null) {
					map.put("y0", value1 / mb);
				} else {
					map.put("y0", 0);
				}

				if (value2 != null) {
					map.put("y1", value2 / mb);
				} else {
					map.put("y1", 0);
				}
				list.add(map);

			}
			time1 += Constant.SECONDS_ONE_DAY;
			time2 += Constant.SECONDS_ONE_DAY;
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		if (Constant.MENUSAGE == featrueID) {
			// 内存 使用量
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("mem_used", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("mem_used", lang));
		} else if (Constant.MEMPRIVATE == featrueID) {
			// Private内存
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("mem_pri", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("mem_pri", lang));
		} else if (Constant.MEMVIRTUAL == featrueID) {
			// Virtual内存
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("mem_vir", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("mem_vir", lang));
		} else if (Constant.MEMPRIVATEIPCS == featrueID) {
			// Private+IPCS
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("mem_pri_ipcs", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("mem_pri_ipcs", lang));
		} else if (Constant.MEMSWAPUSED == featrueID) {
			// SWAP内存使用量
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("swap_used", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("swap_used", lang));
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 内存相关- SWAP内存使用率， 使用以下特性计算 SWAP使用大小/SWAP总大小
	 *
	 * @param serverID
	 * @param startdate
	 * @param starttime
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午5:46:37
	 */
	public DEResponse getComparedMemSwapUsageMinute(int serverID, String startdate, String starttime, String tableName,
													String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPUSED + ServerCfg.ROWKEY_SPLITER
				+ Constant.MEM_SWAP_USED + ServerCfg.ROWKEY_SPLITER + startdate;
		String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPUSED + ServerCfg.ROWKEY_SPLITER
				+ Constant.MEM_SWAP_USED + ServerCfg.ROWKEY_SPLITER + starttime;

		String rowKey_3 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPTOTAL + ServerCfg.ROWKEY_SPLITER
				+ Constant.MEM_SWAP_TOTAL + ServerCfg.ROWKEY_SPLITER + startdate;
		String rowKey_4 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPTOTAL + ServerCfg.ROWKEY_SPLITER
				+ Constant.MEM_SWAP_TOTAL + ServerCfg.ROWKEY_SPLITER + starttime;

		NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
		NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);
		NavigableMap<byte[], byte[]> dataMap3 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_3);
		NavigableMap<byte[], byte[]> dataMap4 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_4);

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();

			Long swapUsed1 = ReportHbaseUtils.getOneHourValue(dataMap1, minute);
			Long swapTotal1 = ReportHbaseUtils.getOneHourValue(dataMap2, minute);
			Long swapUsed2 = ReportHbaseUtils.getOneHourValue(dataMap3, minute);
			Long swapTotal2 = ReportHbaseUtils.getOneHourValue(dataMap4, minute);


			if ((swapUsed1 != null && swapTotal1 != null && swapTotal1 > 0) ||
					(swapUsed2 != null && swapTotal2 != null && swapTotal2 > 0)) {
				map.put("x", m);
			}
			if (swapUsed1 != null && swapTotal1 != null && swapTotal1 > 0) {
				map.put("y0", new BigDecimal(swapUsed1).divide(
						new BigDecimal(swapTotal1), 4, BigDecimal.ROUND_HALF_EVEN));
			}

			if (swapUsed2 != null && swapTotal2 != null && swapTotal2 > 0) {
				map.put("y1", new BigDecimal(swapUsed2).divide(
						new BigDecimal(swapTotal2), 4, BigDecimal.ROUND_HALF_EVEN));
			}

			if (!map.isEmpty()) {
				list.add(map);
			}

		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// SWAP内存使用率
		name.put("y0", startdate + " " + ResourceHandler.getProperties("swap_total", lang));
		name.put("y1", starttime + " " + ResourceHandler.getProperties("swap_total", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 内存相关- SWAP内存使用率， 使用以下特性计算 SWAP使用大小/SWAP总大小
	 *
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午5:46:42
	 */
	public DEResponse getComparedMemSwapUsageHour(int serverID, String startdate, String enddate, String starttime,
												  String endtime, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);

		int begin = DateUtils.getUnixTimestampFromyyyyMMdd(starttime);
		int finish = DateUtils.getUnixTimestampFromyyyyMMdd(endtime);

		int time1 = start;
		int time2 = begin;
		for (; time1 <= end & time2 <= finish; ) {

			String current1 = DateUtils.getyyyyMMddFromTimestamp(time1);
			String current2 = DateUtils.getyyyyMMddFromTimestamp(time2);

			String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPUSED + ServerCfg.ROWKEY_SPLITER + Constant.MEM_SWAP_USED
					+ ServerCfg.ROWKEY_SPLITER + current1;
			String rowKey_3 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPTOTAL + ServerCfg.ROWKEY_SPLITER + Constant.MEM_SWAP_TOTAL
					+ ServerCfg.ROWKEY_SPLITER + current1;

			String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPUSED
					+ ServerCfg.ROWKEY_SPLITER + Constant.MEM_SWAP_USED
					+ ServerCfg.ROWKEY_SPLITER + current2;
			String rowKey_4 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.MEMSWAPTOTAL
					+ ServerCfg.ROWKEY_SPLITER + Constant.MEM_SWAP_TOTAL
					+ ServerCfg.ROWKEY_SPLITER + current2;

			NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
			NavigableMap<byte[], byte[]> dataMap3 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_3);

			NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);
			NavigableMap<byte[], byte[]> dataMap4 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_4);
			

			String current_x = DateUtils.getMMddFromTimestamp(time1);
			String compared_x = DateUtils.getMMddFromTimestamp(time2);
			for (int hour = 1; hour <= 24; hour++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String paddingHour = String.format("%02d:00", hour);
				// x = 01-16 10:00#01-22 10:00
				String x = current_x + " " + paddingHour + ServerCfg.ROWKEY_SPLITER +
						compared_x + " " + paddingHour;
				map.put("x", x);

				Long swap_used = ReportHbaseUtils.getOneHourValue(dataMap1, hour);
				Long swap_total = ReportHbaseUtils.getOneHourValue(dataMap3, hour);

				if (swap_used != null && swap_total != null && swap_total > 0) {
					map.put("y0",
							new BigDecimal(swap_used).divide(new BigDecimal(swap_total), 4,
									BigDecimal.ROUND_HALF_EVEN));
				} else {
					map.put("y0", 0);
				}

				swap_used = ReportHbaseUtils.getOneHourValue(dataMap2, hour);
				swap_total = ReportHbaseUtils.getOneHourValue(dataMap4, hour);

				if (swap_used != null && swap_total != null && swap_total > 0) {
					map.put("y1",
							new BigDecimal(swap_used).divide(new BigDecimal(swap_total), 4,
									BigDecimal.ROUND_HALF_EVEN));
				} else {
					map.put("y1", 0);
				}
				list.add(map);
			}
			time1 += Constant.SECONDS_ONE_DAY;
			time2 += Constant.SECONDS_ONE_DAY;
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		// Swap内存使用率
		name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("swap_total", lang));
		name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("swap_total", lang));
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 网络相关
	 * @param serverID
	 * @param startdate
	 * @param starttime
	 * @param tableName
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午7:04:51
	 */
	public DEResponse getComparedNetworkUsageMinute(int serverID, String startdate, String starttime, String tableName,
													int featrueID, String object, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
				+ ServerCfg.ROWKEY_SPLITER + startdate;
		String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
				+ ServerCfg.ROWKEY_SPLITER + starttime;
		NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
		NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();

			String value1 = ReportHbaseUtils.getOneMiniteValue(dataMap1, minute);
			String value2 = ReportHbaseUtils.getOneMiniteValue(dataMap2, minute);
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
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		if (Constant.OUTFLOW == featrueID && Constant.EM2.equals(object)) {
			// 内网网卡出流量
			name.put("y0", startdate + " " + ResourceHandler.getProperties("intranet_out_flow", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("intranet_out_flow", lang));
		} else if (Constant.INFLOW == featrueID && Constant.EM2.equals(object)) {
			//内网网卡入流量
			name.put("y0", startdate + " " + ResourceHandler.getProperties("intranet_in_flow", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("intranet_in_flow", lang));
		} else if (Constant.OUTFLOW == featrueID && Constant.EM1.equals(object)) {
			// 外网网卡出流量
			name.put("y0", startdate + " " + ResourceHandler.getProperties("extranet_out_flow", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("extranet_out_flow", lang));
		} else if (Constant.INFLOW == featrueID && Constant.EM1.equals(object)) {
			// 外网网卡入流量
			name.put("y0", startdate + " " + ResourceHandler.getProperties("extranet_in_flow", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("extranet_in_flow", lang));
		} else if (Constant.OUTPACKAGEVOLUME == featrueID && Constant.EM2.equals(object)) {
			// 内网网卡出包量
			name.put("y0", startdate + " " + ResourceHandler.getProperties("intranet_out_package", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("intranet_out_package", lang));
		} else if (Constant.INPACKAGEVOLUME == featrueID && Constant.EM2.equals(object)) {
			// 内网网卡入包量
			name.put("y0", startdate + " " + ResourceHandler.getProperties("intranet_in_package", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("intranet_in_package", lang));
		} else if (Constant.OUTPACKAGEVOLUME == featrueID && Constant.EM1.equals(object)) {
			// 外网网卡出包量
			name.put("y0", startdate + " " + ResourceHandler.getProperties("extranet_out_package", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("extranet_out_package", lang));
		} else if (Constant.INPACKAGEVOLUME == featrueID && Constant.EM1.equals(object)) {
			// 外网网卡入包量
			name.put("y0", startdate + " " + ResourceHandler.getProperties("extranet_in_package", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("extranet_in_package", lang));
		} else if (Constant.TCPCONN == featrueID) {
			// TCP连接数 
			name.put("y0", startdate + " " + ResourceHandler.getProperties("tcp_open", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("tcp_open", lang));
		} else if (Constant.PASSIVETCPCONN == featrueID) {
			// 被动打开的TCP连接数
			name.put("y0", startdate + " " + ResourceHandler.getProperties("tcp_passive_open", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("tcp_passive_open", lang));
		} else if (Constant.UDPRECEIVEDATAGRAM == featrueID) {
			//UDP收数据报
			name.put("y0", startdate + " " + ResourceHandler.getProperties("udp_receive_datagram", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("udp_receive_datagram", lang));
		} else if (Constant.UDPSENDDATAGRAM == featrueID) {
			// UDP发数据报
			name.put("y0", startdate + " " + ResourceHandler.getProperties("udp_send_datagram", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("udp_send_datagram", lang));
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 网络相关
	 * 50 em2内网网卡出流量
	 * 51 em2内网网卡入流量
	 * 50 em1外网网卡出流量
	 * 51 em1 外网网卡入流量
	 * 52 em2内网网卡出包量
	 * 53 em2内网网卡入包量
	 * 52 em1外网网卡出包量
	 * 53 em1外网网卡入包量
	 * 54 被动打开TCP连接数
	 * 55TCP连接数
	 * 56UDP接收数据报
	 * 57UDP发送数据报
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param tableName
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午7:04:54
	 */
	public DEResponse getComparedNetworkUsageHour(int serverID, String startdate, String enddate, String starttime,
												  String endtime, String tableName, int featrueID, String object, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);

		int begin = DateUtils.getUnixTimestampFromyyyyMMdd(starttime);
		int finish = DateUtils.getUnixTimestampFromyyyyMMdd(endtime);

		int time1 = start;
		int time2 = begin;

		for (; time1<= end & time2 <= finish; ) {
			String current1 = DateUtils.getyyyyMMddFromTimestamp(time1);
			String current2 = DateUtils.getyyyyMMddFromTimestamp(time2);

			String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
					+ ServerCfg.ROWKEY_SPLITER + current1;

			String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
					+ ServerCfg.ROWKEY_SPLITER + current2;

			NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
			NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);

			String current_x = DateUtils.getMMddFromTimestamp(time1);
			String compared_x =  DateUtils.getMMddFromTimestamp(time2);
			for (int hour = 1; hour <= 24; hour++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String paddingHour = String.format("%02d:00", hour);
				// x = 01-16 10:00#01-22 10:00
				String x = current_x + " " + paddingHour + ServerCfg.ROWKEY_SPLITER +
						compared_x + " " + paddingHour;

				map.put("x", x);
				Long value1 = ReportHbaseUtils.getOneHourValue(dataMap1, hour);
				Long value2 = ReportHbaseUtils.getOneHourValue(dataMap2, hour);

				if (value1 != null) {
					map.put("y0", value1 / 100);
				} else {
					map.put("y0", 0);
				}

				if (value2 != null) {
					map.put("y1", value2 / 100);
				} else {
					map.put("y1", 0);
				}
				list.add(map);
			}

			time1 += Constant.SECONDS_ONE_DAY;
			time2 += Constant.SECONDS_ONE_DAY;
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		if (Constant.OUTFLOW == featrueID && Constant.EM2.equals(object)) {
			// 内网网卡出流量
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("intranet_out_flow", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("intranet_out_flow", lang));
		} else if (Constant.INFLOW == featrueID && Constant.EM2.equals(object)) {
			//内网网卡入流量
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("intranet_in_flow", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("intranet_in_flow", lang));
		} else if (Constant.OUTFLOW == featrueID && Constant.EM1.equals(object)) {
			// 外网网卡出流量
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("extranet_out_flow", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("extranet_out_flow", lang));
		} else if (Constant.INFLOW == featrueID && Constant.EM1.equals(object)) {
			// 外网网卡入流量
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("extranet_in_flow", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("extranet_in_flow", lang));
		} else if (Constant.OUTPACKAGEVOLUME == featrueID && Constant.EM2.equals(object)) {
			// 内网网卡出包量
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("intranet_out_package", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("intranet_out_package", lang));
		} else if (Constant.INPACKAGEVOLUME == featrueID && Constant.EM2.equals(object)) {
			// 内网网卡入包量
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("intranet_in_package", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("intranet_in_package", lang));
		} else if (Constant.OUTPACKAGEVOLUME == featrueID && Constant.EM1.equals(object)) {
			// 外网网卡出包量
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("extranet_out_package", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("extranet_out_package", lang));
		} else if (Constant.INPACKAGEVOLUME == featrueID && Constant.EM1.equals(object)) {
			// 外网网卡入包量
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("extranet_in_package", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("extranet_in_package", lang));
		} else if (Constant.TCPCONN == featrueID) {
			// TCP连接数 
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("tcp_open", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("tcp_open", lang));
		} else if (Constant.PASSIVETCPCONN == featrueID) {
			// 被动打开的TCP连接数
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("tcp_passive_open", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("tcp_passive_open", lang));
		} else if (Constant.UDPRECEIVEDATAGRAM == featrueID) {
			//UDP收数据报
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("udp_receive_datagram", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("udp_receive_datagram", lang));
		} else if (Constant.UDPSENDDATAGRAM == featrueID) {
			// UDP发数据报
			name.put("y0", startdate + "~" + enddate + " " + ResourceHandler.getProperties("udp_send_datagram", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("udp_send_datagram", lang));
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * IO相关-磁盘分区使用率
	 * @param serverID
	 * @param startdate
	 * @param starttime
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午7:39:43
	 */
	public DEResponse getComparedIOPartitionUsageMinute(int serverID, String startdate, String starttime,
														String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String diskPartition = ReportHbaseUtils.getDiskPartition(serverID);
		// json字符串转对象集合
		List<DiskPartition> listPartition = CachedObjects.GSON.fromJson(diskPartition,
				new TypeToken<List<DiskPartition>>() {}.getType());

		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		int i = 0;
		int j = 1;

		Map<String, NavigableMap<byte[], byte[]>> totalMap = new HashMap<String, NavigableMap<byte[], byte[]>>();
		for (DiskPartition disk : listPartition) {
			String y1 = "y" + i;
			String y2 = "y" + j;
			name.put(y1, startdate + " " + disk.getPartition());
			name.put(y2, starttime + " " + disk.getPartition());
			String partition = disk.getPartition();
			String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.FREEPARTITION
					+ ServerCfg.ROWKEY_SPLITER
					+ partition + ServerCfg.ROWKEY_SPLITER + startdate;
			String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.FREEPARTITION
					+ ServerCfg.ROWKEY_SPLITER
					+ partition + ServerCfg.ROWKEY_SPLITER + starttime;

			NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
			NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);
			String key1 = y1 + ServerCfg.ROWKEY_SPLITER + disk.getPartitionSize();
			String key2 = y2 + ServerCfg.ROWKEY_SPLITER + disk.getPartitionSize();

			totalMap.put(key1, dataMap1);
			totalMap.put(key2, dataMap2);

			i = i + 2;
			j = j + 2;
		}

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
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

				String value = ReportHbaseUtils.getOneMiniteValue(entry.getValue(), minute);
				if (value != null && total > 0) {
					map.put("x", m);
					Long free = Long.parseLong(value);
					double d = (total - free) / total.doubleValue();
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
	 * IO相关-磁盘分区使用率
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午7:40:07
	 */
	public DEResponse getComparedIOPartitionUsageHour(int serverID, String startdate,
													  String enddate, String starttime,
													  String endtime, String tableName,
													  String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String diskPartition = ReportHbaseUtils.getDiskPartition(serverID);
		// json字符串转对象集合
		List<DiskPartition> listPartition = CachedObjects.GSON.fromJson(diskPartition,
				new TypeToken<List<DiskPartition>>() {}.getType());

		Map<String, String> name = new HashMap<String, String>();

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);

		int begin = DateUtils.getUnixTimestampFromyyyyMMdd(starttime);
		int finish = DateUtils.getUnixTimestampFromyyyyMMdd(endtime);

		int time1 = start;
		int time2 = begin;
		for (; time1 <= end & time2 <=finish; ) {
			int i = 0;
			int j = 1;
			String current1 = DateUtils.getyyyyMMddFromTimestamp(time1);
			String current2 = DateUtils.getyyyyMMddFromTimestamp(time2);
			Map<String, NavigableMap<byte[], byte[]>> totalMap =
					new HashMap<String, NavigableMap<byte[], byte[]>>();

			for (DiskPartition disk : listPartition) {
				String y1 = "y" + i;
				String y2 = "y" + j;
				name.put(y1, disk.getPartition());
				name.put(y2, disk.getPartition());
				String partition = disk.getPartition();

				String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.FREEPARTITION
						+ ServerCfg.ROWKEY_SPLITER + partition
						+ ServerCfg.ROWKEY_SPLITER + current1;

				String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.FREEPARTITION
						+ ServerCfg.ROWKEY_SPLITER+ partition
						+ ServerCfg.ROWKEY_SPLITER + current2;

				NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
				NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);
				String key1 = y1 + ServerCfg.ROWKEY_SPLITER + disk.getPartitionSize();
				String key2 = y2 + ServerCfg.ROWKEY_SPLITER + disk.getPartitionSize();

				totalMap.put(key1, dataMap1);
				totalMap.put(key2, dataMap2);

				i = i + 2;
				j = j + 2;
			}

			String current_x = DateUtils.getMMddFromTimestamp(time1);
			String compared_x = DateUtils.getMMddFromTimestamp(time2);

			for (int hour = 1; hour <= 24; hour++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String paddingHour = String.format("%02d:00", hour);
				// x = 01-16 10:00#01-22 10:00
				String x = current_x + " " + paddingHour + ServerCfg.ROWKEY_SPLITER
						+ compared_x + " " + paddingHour;
				map.put("x", x);

				for (Map.Entry<String, NavigableMap<byte[],byte[]>> entry : totalMap.entrySet()) {
					String[] keys = entry.getKey().split(ServerCfg.ROWKEY_SPLITER);

					String y = keys[0];
					// 总大小
					Long partitionSize = Long.parseLong(keys[1]);
					Long total = partitionSize * 1024;
					Long free = ReportHbaseUtils.getOneHourValue(entry.getValue(), hour);

					if (free != null && total > 0) {
						double value = (total - free) / total.doubleValue();
						map.put(y, ServerUtils.numberFormatDouble4RtnDouble(value));
					} else {
						map.put(y, 0);
					}
				}

				list.add(map);
			}
			time1 += Constant.SECONDS_ONE_DAY;
			time2 += Constant.SECONDS_ONE_DAY;
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 磁盘分区使用量
	 * @param serverID
	 * @param startdate
	 * @param starttime
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午7:40:04
	 */
	public DEResponse getComparedIOPartitionAmountMinute(int serverID, String startdate, String starttime,
														 String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String diskPartition = ReportHbaseUtils.getDiskPartition(serverID);
		// json字符串转对象集合
		List<DiskPartition> listPartition = CachedObjects.GSON.fromJson(diskPartition,
				new TypeToken<List<DiskPartition>>() {}.getType());

		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		int i = 0;
		int j = 1;
		Map<String, NavigableMap<byte[], byte[]>> totalMap = new HashMap<String, NavigableMap<byte[], byte[]>>();
		for (DiskPartition disk : listPartition) {
			String y0 = "y" + i;
			String y1 = "y" + j;
			name.put(y0, startdate + " " + disk.getPartition());
			name.put(y1, starttime + " " + disk.getPartition());
			String partition = disk.getPartition();
			String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.FREEPARTITION
					+ ServerCfg.ROWKEY_SPLITER
					+ partition + ServerCfg.ROWKEY_SPLITER + startdate;
			String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + Constant.FREEPARTITION
					+ ServerCfg.ROWKEY_SPLITER
					+ partition + ServerCfg.ROWKEY_SPLITER + starttime;

			NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
			NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);

			String key1 = y0 + ServerCfg.ROWKEY_SPLITER + disk.getPartitionSize();
			String key2 = y1 + ServerCfg.ROWKEY_SPLITER + disk.getPartitionSize();
			totalMap.put(key1, dataMap1);
			totalMap.put(key2, dataMap2);

			i = i + 2;
			j = j + 2;
		}

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();

			for (Map.Entry<String,NavigableMap<byte[],byte[]>> entry: totalMap.entrySet()) {
				String[] keys = entry.getKey().split(ServerCfg.ROWKEY_SPLITER);
				String y = keys[0];
				// 总大小
				Long partitionSize = Long.parseLong(keys[1]);
				Long total = partitionSize.longValue() * 1024;

				String value = ReportHbaseUtils.getOneMiniteValue(entry.getValue(), minute);

				if (value != null && total > 0) {
					map.put("x", m);
					Long free = Long.parseLong(value);
					long used = total - free;
					long gb = 1024 * 1024 * 1024;
					BigDecimal result = new BigDecimal(used / gb)
							.setScale(2, RoundingMode.UP);
					map.put(y, result.doubleValue());
				}
			}
			list.add(map);
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * 磁盘分区使用量
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param tableName
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午7:40:00
	 */
	public DEResponse getComparedIOPartitionAmountHour(int serverID, String startdate, String enddate,
													   String starttime, String endtime, String tableName, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String diskPartition = ReportHbaseUtils.getDiskPartition(serverID);
		// json字符串转对象集合
		List<DiskPartition> listPartition = CachedObjects.GSON.fromJson(diskPartition,
				new TypeToken<List<DiskPartition>>() {}.getType());
		Map<String, Map<String, Double>> data_map_1 = new HashMap<String, Map<String, Double>>();
		Map<String, DiskPartition> y_map_1 = new HashMap<String, DiskPartition>();

		Map<String, Map<String, Double>> data_map_2 = new HashMap<String, Map<String, Double>>();
		Map<String, DiskPartition> y_map_2 = new HashMap<String, DiskPartition>();

		Map<String, String> name = new HashMap<String, String>();

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);

		int begin = DateUtils.getUnixTimestampFromyyyyMMdd(starttime);
		int finish = DateUtils.getUnixTimestampFromyyyyMMdd(endtime);

		// 初始化 比对时间段
		List<String> date_list = new ArrayList<String>();
		try {
			for (int time = begin; time <= finish; time += Constant.SECONDS_ONE_DAY) {
				String current = DateUtils.getyyyyMMddFromTimestamp(time);
				int index = 0;
				for (int i = 0;i < listPartition.size();i++) {
					String y = "y" + index;
					y_map_1.put(y, listPartition.get(i));
					String partition = listPartition.get(i).getPartition();
					name.put(y, partition);
					String rowKey = serverID + ServerCfg.ROWKEY_SPLITER + Constant.FREEPARTITION + ServerCfg.ROWKEY_SPLITER + partition
							+ ServerCfg.ROWKEY_SPLITER + current;
					Map<String, Double> valueMap = new HashMap<String, Double>();
					ReportHbaseUtils.getValueFromHbaseHour(current, tableName, rowKey, valueMap);
					String key = current + ServerCfg.ROWKEY_SPLITER + y;
					data_map_1.put(key, valueMap);
					index = index + 2;
				}
				date_list.add(current);
			}

			int i = 0;
			// 一一对应两个时间段
			Map<String, String> date_map = new HashMap<String, String>();
			for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
				String current = DateUtils.getyyyyMMddFromTimestamp(time);
				int index = 1;
				for (int j = 0;j < listPartition.size();j++) {
					String y = "y" + index;
					y_map_2.put(y, listPartition.get(j));
					String partition = listPartition.get(j).getPartition();
					name.put(y, partition);
					String rowKey = serverID + ServerCfg.ROWKEY_SPLITER + Constant.FREEPARTITION + ServerCfg.ROWKEY_SPLITER + partition
							+ ServerCfg.ROWKEY_SPLITER + current;
					Map<String, Double> valueMap = new HashMap<String, Double>();
					ReportHbaseUtils.getValueFromHbaseHour(current, tableName, rowKey, valueMap);
					String key = current + ServerCfg.ROWKEY_SPLITER + y;
					data_map_2.put(key, valueMap);
					index = index + 2;
				}
				date_map.put(current, date_list.get(i));
				i++;
			}

			for (int time = start; time <= end; time += Constant.SECONDS_ONE_DAY) {
				String current = DateUtils.getyyyyMMddFromTimestamp(time);
				String compared = date_map.get(current);

				String current_x = DateUtils.getMMddFromTimestamp(time);
				String compared_x = compared.substring(5, 10);
				for (int hour = 1; hour <=24; hour++) {
					Map<String, Object> map = new HashMap<String, Object>();
					String paddingHour = String.format("%02d:00", hour);
					String key = current + " " + String.format("%02d", hour);
					// x = 01-16 10:00#01-22 10:00
					String x = current_x + " " + paddingHour + ServerCfg.ROWKEY_SPLITER + compared_x + " " + paddingHour;
					map.put("x", x);

					for (Map.Entry<String, DiskPartition> diskMap : y_map_2.entrySet()) {
						String y = diskMap.getKey();
						DiskPartition disk = diskMap.getValue();
						// 总大小
						BigInteger partitionSize = disk.getPartitionSize();
						long total = partitionSize.longValue() * 1024;
						String diskKey = current + ServerCfg.ROWKEY_SPLITER + y;
						Map<String, Double> value_free = data_map_2.get(diskKey);
						Double free = value_free.get(key);
						if (free != null && total > 0) {
							long value = (long) (total - free);
							double gb = 1024 * 1024 * 1024;
							BigDecimal result = new BigDecimal(value / gb)
									.setScale(2, RoundingMode.UP);
							map.put(y, result.doubleValue());
						} else {
							map.put(y, 0);
						}
					}
					key = compared + " " + String.format("%02d", hour);
					for (Map.Entry<String, DiskPartition> diskMap : y_map_1.entrySet()) {
						String y = diskMap.getKey();
						DiskPartition disk = diskMap.getValue();
						// 总大小
						BigInteger partitionSize = disk.getPartitionSize();
						long total = partitionSize.longValue() * 1024;
						String diskKey = compared + ServerCfg.ROWKEY_SPLITER + y;
						Map<String, Double> value_free = data_map_1.get(diskKey);
						Double free = value_free.get(key);
						if (free != null && total > 0) {
							long value = (long) (total - free);
							double gb = 1024 * 1024 * 1024;
							BigDecimal result = new BigDecimal(value / gb)
									.setScale(2, RoundingMode.UP);
							map.put(y, result.doubleValue());
						} else {
							map.put(y, 0);
						}
					}
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * IO使用情况
	 * featrueID=31&object=DISK IO IN
	 *featrueID=32&object=DISK IO OUT
	 *featrueID=33&object=SWAP SI
	 *featrueID=34&object=SWAP SO
	 *featrueID=35&object=’Svctm_time_max’
	 *featrueID=36&object=’Await_time_max’
	 *featrueID=37&object=’avgqu_sz_max’
	 *featrueID=38&object=’avgrq_sz’
	 *featrueID=39&object=’util_max’
	 * @param serverID
	 * @param startdate
	 * @param starttime
	 * @param tableName
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午7:39:52
	 */
	public DEResponse getComparedIOUsageMinute(int serverID, String startdate,
											   String starttime, String tableName,
											   int featrueID, String object, String lang)throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
				+ ServerCfg.ROWKEY_SPLITER + startdate;
		String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
				+ ServerCfg.ROWKEY_SPLITER + starttime;

		NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
		NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int minute = 0; minute < 1440; minute++) {
			long time = (start + 60 * minute) * 1000L;
			String m = sdf.format(new Date(time));
			Map<String, Object> map = new HashMap<String, Object>();

			String value1 = ReportHbaseUtils.getOneMiniteValue(dataMap1, minute);
			String value2 = ReportHbaseUtils.getOneMiniteValue(dataMap2, minute);

			if (value1 != null ||
					value2 != null) {
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
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		if (Constant.DISKIOREAD == featrueID) {
			// 磁盘IO读
			name.put("y0", startdate + " " + ResourceHandler.getProperties("io_r", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("io_r", lang));
		} else if (Constant.DISKIOWRITE == featrueID) {
			// 磁盘IO写
			name.put("y0", startdate + " " + ResourceHandler.getProperties("io_w", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("io_w", lang));
		} else if (Constant.SWAPSI == featrueID) {
			//  SWAP SI
			name.put("y0", startdate + " " + ResourceHandler.getProperties("io_swap_i", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("io_swap_i", lang));
		} else if (Constant.SWAPSO == featrueID) {
			//  SWAP SO
			name.put("y0", startdate + " " + ResourceHandler.getProperties("io_swap_o", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("io_swap_o", lang));
		} else if (Constant.SVCTMTIMEMAX == featrueID) {
			// Svctm_time
			name.put("y0", startdate + " " + ResourceHandler.getProperties("io_svctm_time", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("io_svctm_time", lang));
		} else if (Constant.AWAITTIMEMAX == featrueID) {
			// Await_max
			name.put("y0", startdate + " " + ResourceHandler.getProperties("io_await_max", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("io_await_max", lang));
		} else if (Constant.AVGQUSZMAX == featrueID) {
			// avgqu_sz
			name.put("y0", startdate + " " + ResourceHandler.getProperties("io_avgqu_sz", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("io_avgqu_sz", lang));
		} else if (Constant.AVGRQSZ == featrueID) {
			// avgrq_sz
			name.put("y0", startdate + " " + ResourceHandler.getProperties("io_avgrq_sz", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("io_avgrq_sz", lang));
		} else if (Constant.UTILMAX == featrueID) {
			// util
			name.put("y0", startdate + " " + ResourceHandler.getProperties("io_util", lang));
			name.put("y1", starttime + " " + ResourceHandler.getProperties("io_util", lang));
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

	/**
	 * IO使用情况
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param tableName
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月20日 下午7:39:55
	 */
	public DEResponse getComparedIOUsageHour(int serverID, String startdate, String enddate, String starttime,
											 String endtime, String tableName, int featrueID,
											 String object, String lang) throws ServerException {
		DEResponse deResp = new DEResponse();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		int start = DateUtils.getUnixTimestampFromyyyyMMdd(startdate);
		int end = DateUtils.getUnixTimestampFromyyyyMMdd(enddate);

		int begin = DateUtils.getUnixTimestampFromyyyyMMdd(starttime);
		int finish = DateUtils.getUnixTimestampFromyyyyMMdd(endtime);

		int time1 = start;
		int time2 = begin;
		for (; time1 <= end & time2 <= finish; ) {

			String current1 = DateUtils.getyyyyMMddFromTimestamp(time1);
			String current2 = DateUtils.getyyyyMMddFromTimestamp(time2);

			String rowKey_1 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
					+ ServerCfg.ROWKEY_SPLITER + current1;

			String rowKey_2 = serverID + ServerCfg.ROWKEY_SPLITER + featrueID + ServerCfg.ROWKEY_SPLITER + object
					+ ServerCfg.ROWKEY_SPLITER + current2;

			NavigableMap<byte[], byte[]> dataMap1 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_1);
			NavigableMap<byte[], byte[]> dataMap2 = ReportHbaseUtils.getRowFromHbase(tableName, rowKey_2);

			String current_x = DateUtils.getMMddFromTimestamp(time1);
			String compared_x = DateUtils.getMMddFromTimestamp(time2);
			for (int hour = 1; hour <= 24; hour++) {
				Map<String, Object> map = new HashMap<String, Object>();
				String paddingHour = String.format("%02d:00", hour);
				// x = 01-16 10:00#01-22 10:00
				String x = current_x + " " + paddingHour + ServerCfg.ROWKEY_SPLITER + compared_x + " " + paddingHour;
				map.put("x", x);

				Long value1 = ReportHbaseUtils.getOneHourValue(dataMap1, hour);
				Long value2 = ReportHbaseUtils.getOneHourValue(dataMap2, hour);

				if (value1 != null) {
					map.put("y0", value1 / 100);
				} else {
					map.put("y0", 0);
				}

				if (value2 != null) {
					map.put("y1", value2 / 100);
				} else {
					map.put("y1", 0);
				}
				list.add(map);
			}

			time1 += Constant.SECONDS_ONE_DAY;
			time2 += Constant.SECONDS_ONE_DAY;
		}
		// 初始化name
		Map<String, String> name = new HashMap<String, String>();
		if (Constant.DISKIOREAD == featrueID) {
			// 磁盘IO读
			name.put("y0", ResourceHandler.getProperties("io_r", lang));
			name.put("y1", ResourceHandler.getProperties("io_r", lang));
		} else if (Constant.DISKIOWRITE == featrueID) {
			// 磁盘IO写
			name.put("y0", ResourceHandler.getProperties("io_w", lang));
			name.put("y1", starttime + "~" + endtime + " " + ResourceHandler.getProperties("io_w", lang));
		} else if (Constant.SWAPSI == featrueID) {
			//  SWAP SI
			name.put("y0",ResourceHandler.getProperties("io_swap_i", lang));
			name.put("y1", ResourceHandler.getProperties("io_swap_i", lang));
		} else if (Constant.SWAPSO == featrueID) {
			//  SWAP SO
			name.put("y0", ResourceHandler.getProperties("io_swap_o", lang));
			name.put("y1", ResourceHandler.getProperties("io_swap_o", lang));
		} else if (Constant.SVCTMTIMEMAX == featrueID) {
			// Svctm_time
			name.put("y0",  ResourceHandler.getProperties("io_svctm_time", lang));
			name.put("y1", ResourceHandler.getProperties("io_svctm_time", lang));
		} else if (Constant.AWAITTIMEMAX == featrueID) {
			// Await_max
			name.put("y0",  ResourceHandler.getProperties("io_await_max", lang));
			name.put("y1",  ResourceHandler.getProperties("io_await_max", lang));
		} else if (Constant.AVGQUSZMAX == featrueID) {
			// avgqu_sz
			name.put("y0",  ResourceHandler.getProperties("io_avgqu_sz", lang));
			name.put("y1",  ResourceHandler.getProperties("io_avgqu_sz", lang));
		} else if (Constant.AVGRQSZ == featrueID) {
			// avgrq_sz
			name.put("y0", ResourceHandler.getProperties("io_avgrq_sz", lang));
			name.put("y1",  ResourceHandler.getProperties("io_avgrq_sz", lang));
		} else if (Constant.UTILMAX == featrueID) {
			// util
			name.put("y0",  ResourceHandler.getProperties("io_util", lang));
			name.put("y1", ResourceHandler.getProperties("io_util", lang));
		}
		deResp.setContent(list);
		deResp.setName(name);
		return deResp;
	}

}
