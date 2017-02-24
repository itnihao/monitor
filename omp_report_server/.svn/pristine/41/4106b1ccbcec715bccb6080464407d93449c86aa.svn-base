package com.dataeye.omp.report.dbproxy.hbase;


import com.dataeye.util.log.DELogger;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class HbaseProxyClient {

	private final static Logger logger = DELogger.getLogger("hbase_client_log");
	/**
	 * 插入一行记录
	 */
	public static void addRecord(String tableName, String rowKey,
			String family, String qualifier, String value) throws IOException {
		HTableInterface table = null;
		try {
			table = HbasePool.getConnection().getTable(tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
					Bytes.toBytes(value));
			table.put(put);
		} catch (Exception e) {
			logger.error("add record exception tableName = " + tableName
					+ ", rowKey = " + rowKey + ", family" + family + ", qualifier = "
					+ qualifier + ", value = " + value, e);
			throw new RuntimeException(e);
		} finally {
			HbasePool.close(table);
		}
	}

	/**
	 * 插入一行记录
	 */
	public static void addRecord(String tableName, String rowKey,
								 String family, String qualifier, long value) throws IOException {
		HTableInterface table = null;
		try {
			table = HbasePool.getConnection().getTable(tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
					Bytes.toBytes(value));
			table.put(put);
		} catch (Exception e) {
			logger.error("add record exception tableName=" + tableName
					+ ",rowKey=" + rowKey + ",family" + family + ",qualifier="
					+ qualifier + ",value=" + value, e);
			throw new RuntimeException(e);
		} finally {
			HbasePool.close(table);
		}
	}

	public static void addRecord(String tableName, String rowKey,
			String family, Map<byte[], byte[]> qualifierValueMap) {
		HTableInterface table = null;
		try {
			table =HbasePool.getConnection().getTable(tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			byte[] familyBytes = Bytes.toBytes(family);
			for (Entry<byte[], byte[]> entry : qualifierValueMap.entrySet()) {
				put.add(familyBytes, entry.getKey(), entry.getValue());
			}
			table.put(put);
		} catch (Exception e) {
			logger.error("add record exception tableName=" + tableName
					+ ",rowKey=" + rowKey + ",family" + family
					+ ",qualifierValueMap=" + qualifierValueMap, e);
			throw new RuntimeException(e);
		} finally {
			HbasePool.close(table);
		}
	}

	/**
	 * 删除一行记录
	 */
	public static void delRecord(String tableName, String rowKey)
			throws IOException {
		HTableInterface table = null;
		try {
			table = HbasePool.getConnection().getTable(tableName);
			List<Delete> list = new ArrayList<Delete>();
			Delete del = new Delete(rowKey.getBytes());
			list.add(del);
			table.delete(list);
		} catch (Exception e) {
			logger.error("del record exception tableName=" + tableName
					+ ",rowKey=" + rowKey, e);
			throw new RuntimeException(e);
		} finally {
			HbasePool.close(table);
		}

	}

	/**
	 * 批量插入
	 * @param list
	 * @param tableName
     */
	public synchronized static void batchAdd(List<Put> list, String tableName)
			throws IOException{
		HTableInterface table = null;
		try {
			table = HbasePool.getConnection().getTable(tableName);
			table.setAutoFlush(false);
			table.batch(list);
		} catch (Exception e) {
			logger.error("batch add exception tableName = " + tableName, e);
			throw new RuntimeException(e);
		} finally {
			HbasePool.close(table);
		}
	}

	/**
	 * 查找一行记录
	 */
	public static Result getOneRecord(String tableName, String rowKey)
			throws IOException {
		HTableInterface table = null;
		try {
			table = HbasePool.getConnection().getTable(tableName);
			Get get = new Get(rowKey.getBytes());
			Result rs = table.get(get);

			return rs;
		} catch (Exception e) {
			logger.error("get record exception tableName= " + tableName
					+ ",rowKey= " + rowKey, e);
			throw new RuntimeException(e);
		} finally {
			HbasePool.close(table);
		}
	}

}
