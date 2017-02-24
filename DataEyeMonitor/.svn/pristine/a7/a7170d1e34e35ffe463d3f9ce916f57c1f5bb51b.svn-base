package com.dataeye.omp.module.monitor.server;

import java.io.IOException;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.utils.ApplicationContextUtil;
import com.dataeye.utils.HbaseProxyClient;
import com.xunlei.jdbc.JdbcTemplate;

/**
 * 
 * @author chenfanglin
 * @date 2016年1月20日 上午11:09:04
 */
public class ReportHbaseUtils {

	/**
	 * 从hbase 中获取一行数据
	 * @param tableName
	 * @param rowKey
	 * @return
	 * @throws ServerException
	 */
	public static NavigableMap<byte[], byte[]> getRowFromHbase(String tableName, String rowKey)
			throws ServerException {
		NavigableMap<byte[], byte[]> resultMap = null;
		try {
			Result result = HbaseProxyClient.getOneRecord(tableName, rowKey);
			if (result != null && !result.isEmpty()) {
				resultMap = result.getFamilyMap(Bytes.toBytes("stat"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.HBASE_ERROR, e);
		}
		return resultMap;
	}


	/**
	 *  一分钟值
	 * @param dataMap
	 * @param minute
	 * @return
	 */
	public static String getOneMiniteValue(NavigableMap<byte[],byte[]> dataMap, int minute) {
		byte[] min = Bytes.toBytes(String.valueOf(minute));
		if (dataMap != null) {
			byte[] b = dataMap.get(min);
			if (b != null) {
				String value = Bytes.toString(b).split("_")[0];
				return value;
			}
		}

		return null;
	}

	/**
	 * 一小时值
	 * @param dataMap
	 * @param hour
	 * @return
	 */
	public static Long getOneHourValue(NavigableMap<byte[],byte[]> dataMap, int hour) {
		byte[] b = Bytes.toBytes(String.format("%02d", hour));
		if (dataMap != null) {
			byte[] value = dataMap.get(b);
			if (value != null) {
				return Bytes.toLong(value);
			}
		}
		return null;
	}



	/**
	 * 从hbase 中获取数据 按分钟
	 * 
	 * @param tableName
	 * @param rowKey
	 * @param valueMap
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午2:15:40
	 */
	public static void getValueFromHbaseMinute(String tableName, String rowKey, Map<Integer, Double> valueMap)
			throws ServerException {
		try {
			Result result = HbaseProxyClient.getOneRecord(tableName, rowKey);
			if (result != null && !result.isEmpty()) {
				NavigableMap<byte[], byte[]> resultMap = result.getFamilyMap(Bytes.toBytes("stat"));
				if (resultMap != null && resultMap.size() > 0) {
					for (Map.Entry<byte[], byte[]> entry : resultMap.entrySet()) {
						String minute = Bytes.toString(entry.getKey());
						String value = Bytes.toString(entry.getValue());
						String[] arr = value.split("_");
						valueMap.put(Integer.parseInt(minute), Double.parseDouble(arr[0]));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.HBASE_ERROR, e);
		}
	}

	/**
	 * 从Hbase中获取数据
	 * 
	 * @param current
	 * @param tableName
	 * @param rowKey
	 * @param valueMap
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午2:46:13
	 */
	public static void getValueFromHbaseHour(String current, String tableName, String rowKey, Map<String, Double> valueMap)
			throws ServerException {
		try {
			Result result = HbaseProxyClient.getOneRecord(tableName, rowKey);
			if (result != null && !result.isEmpty()) {
				NavigableMap<byte[], byte[]> resultMap = result.getFamilyMap(Bytes.toBytes("stat"));
				if (resultMap != null && resultMap.size() > 0) {
					for (Map.Entry<byte[], byte[]> entry : resultMap.entrySet()) {
						String hour = Bytes.toString(entry.getKey());
						Long value = Bytes.toLong(entry.getValue());
						valueMap.put(current + " " + hour, Double.parseDouble(value.toString()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.HBASE_ERROR, e);
		}
	}
	
	public static void getLongValueFromHbaseHour(String current, String tableName, String rowKey, Map<String, Long> valueMap)
			throws ServerException {
		try {
			Result result = HbaseProxyClient.getOneRecord(tableName, rowKey);
			if (result != null && !result.isEmpty()) {
				NavigableMap<byte[], byte[]> resultMap = result.getFamilyMap(Bytes.toBytes("stat"));
				if (resultMap != null && resultMap.size() > 0) {
					for (Map.Entry<byte[], byte[]> entry : resultMap.entrySet()) {
						String hour = Bytes.toString(entry.getKey());
						Long value = Bytes.toLong(entry.getValue());
						valueMap.put(current + " " + hour, value);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.HBASE_ERROR, e);
		}
	}

	public static void getLongValueFromHbaseMinute(String tableName, String rowKey,
												   Map<Integer, Long> valueMap)
			throws ServerException {
		try {
			Result result = HbaseProxyClient.getOneRecord(tableName, rowKey);
			if (result != null && !result.isEmpty()) {
				NavigableMap<byte[], byte[]> resultMap = result.getFamilyMap(Bytes.toBytes("stat"));
				if (resultMap != null && resultMap.size() > 0) {
					for (Map.Entry<byte[], byte[]> entry : resultMap.entrySet()) {
						String minute = Bytes.toString(entry.getKey());
						String value = Bytes.toString(entry.getValue());
						String[] arr = value.split("_");
						valueMap.put(Integer.parseInt(minute), Long.parseLong(arr[0]));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.HBASE_ERROR, e);
		}
	}
	
	/**
	 * 获取机器总内存
	 * @param serverID
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 下午5:03:58
	 */
	public static int getServerMemory(int serverID) throws ServerException {
		JdbcTemplate jdbcTemplateMonitorStat = ApplicationContextUtil.getBeanJdbcTemplateMonitorStat();
		String sql = "select memory from server_list where id = ?";
		try {
			int memory = jdbcTemplateMonitorStat.queryForInt(sql, serverID);
			return memory;
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.HBASE_ERROR, e);
		}
		return 0;
	}
	
	/**
	 * 获取磁盘 分区详情
	 * @param serverID
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年2月23日 下午3:13:36
	 */
	public static String getDiskPartition(int serverID) throws ServerException{
		JdbcTemplate jdbcTemplateMonitorStat = ApplicationContextUtil.getBeanJdbcTemplateMonitorStat();
		String sql = "select disk_partition from server_list where id = ?";
		try {
			String disk_partition = jdbcTemplateMonitorStat.queryForString(sql, serverID);
			return disk_partition;
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.HBASE_ERROR, e);
		}
		return null;
	}

	/**
	 * 获取磁盘信息
	 * @param serverId
	 * @return
	 * @throws ServerException
     */
	public static String getDiskDetail(int serverId) throws ServerException {
		JdbcTemplate jdbcTemplateMonitorStat = ApplicationContextUtil.getBeanJdbcTemplateMonitorStat();
		String sql = "select disk_details from server_list where id = ?";
		try {
			String diskDetail = "";
			if (jdbcTemplateMonitorStat != null) {
				diskDetail = jdbcTemplateMonitorStat.queryForString(sql, serverId);
			}
			return diskDetail;
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.HBASE_ERROR, e);
		}
		return null;
	}

	public static void main(String[] args) throws IOException, ServerException {

//		BigDecimal b2 = new BigDecimal(123456789123456789L);
//		System.out.println(b2);
//		String table = Constant.Table.OMP_FEATURE_VALUE_STAT_HOUR;
//		String rowKey = "21#40#/#2016-02-26";
//		Result rs = HbaseProxyClient.getOneRecord(table, rowKey);
//		System.out.println(rs);
//
////		byte[] b= rs.getValue(Bytes.toBytes("stat"),
////				Bytes.toBytes("10"));
//
//		Long b1 = Bytes.toLong(rs.getValue(Bytes.toBytes("stat"),
//				Bytes.toBytes("10")));
//		System.out.println(b1.doubleValue());
//
//		BigDecimal b = Bytes.toBigDecimal(rs.getValue(Bytes.toBytes("stat"),
//				Bytes.toBytes("10")));
//
//		System.out.println(b);
//
//		long free = 15641923584L;
//		double v1 = new Double(65727412);
//		double total = 1024*1024*1024;
//		System.out.println(free/total);

//		int v=2;
//		//System.out.println(Bytes.toBytes(v));
		long start = System.currentTimeMillis();
		NavigableMap<byte[], byte[]> v1 =
				ReportHbaseUtils.getRowFromHbase(
						"omp_feature_value_stat_minute", "22#10#cpu0#2016-03-03");
		long end = System.currentTimeMillis();
		System.out.println(end - start);
//		System.out.println(ReportHbaseUtils.getOneHourValue(v1, v));
//
//		String s = "cpu1";
//		//System.out.println(s.indexOf("cpu"));
//
//		System.out.println(s.substring(3));

//		Long v = 123456789L;
//		//long v1 = 23456789L;
//		System.out.println((v - v1) / v.doubleValue());
//
//		System.out.println(ServerUtils.numberFormatDouble4RtnDouble((v - v1) / v.doubleValue()));

	}




}
