package com.dataeye.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.slf4j.Logger;

import com.dataeye.util.log.DELogger;

/**
 * Hbase 连接池
 * @author chenfanglin
 * @date 2016年1月7日 上午11:52:22
 */
public class HbasePool {

	private final static Logger logger = DELogger.getLogger("hbase_client_log");
	private final static String propertiesFileName = "/hbase.properties";

	private static String hbase_zookeeper_quorum = "dchbase2,dchbase1,dcnamenode1";
	private static String hbase_zookeeper_property_clientPort = "2181";

	private static Configuration configuration = null;

	static {
		try {
			init();
		} catch (Throwable t) {
			logger.error("HbasePool init error", t);
		}
	}

	private static void init() {
		reload();
	}

	/**
	 * 加载Hbase配置文件
	 * @param fileName
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月7日 下午2:48:05
	 */
	public static Properties loadFromFile(String fileName) {
		Properties properties = new Properties();
		try {
			System.out.println("hbase config:" + HbasePool.class.getResourceAsStream(fileName));
			properties.load(HbasePool.class.getResourceAsStream(fileName));
			return properties;
		} catch (IOException e) {
			System.err.println("read " + fileName + " in classpath error");
			e.printStackTrace();
			logger.error("read " + fileName + " in classpath error");
		}
		return null;
	}

	/**
	 * 重新加载
	 */
	public static void reload() {
		System.out.println("reload.....");
		Properties properties = loadFromFile(propertiesFileName);
		if (properties != null) {
			System.out.println("get hbase properties succ ");
			Object obj1 = properties.get("hbase.zookeeper.quorum");
			Object obj2 = properties.get("hbase.zookeeper.property.clientPort");
			if (obj1 == null || obj2 == null) {
				return;
			}
			String tmphbase_zookeeper_quorum = (String) obj1;
			String tmphbase_zookeeper_property_clientPort = (String) obj2;
			if (!hbase_zookeeper_quorum.equals(tmphbase_zookeeper_quorum)
					|| !hbase_zookeeper_property_clientPort.equals(tmphbase_zookeeper_property_clientPort)) {
				hbase_zookeeper_quorum = tmphbase_zookeeper_quorum;
				hbase_zookeeper_property_clientPort = tmphbase_zookeeper_property_clientPort;
			}
		}

		configuration = HBaseConfiguration.create();
		configuration.set("hbase.zookeeper.quorum", hbase_zookeeper_quorum);
		configuration.set("hbase.zookeeper.property.clientPort", hbase_zookeeper_property_clientPort);
		// configuration.set("hbase.master", hbase_master);
		// TODO:存在多个Hbase集群或配置时，如何清除掉老的connection？？

		if (hConnection != null) {
			try {
				hConnection.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		hConnection = null;

	}

	private static HConnection hConnection = null;

	/**
	 * 获取Hbase连接
	 * @return
	 * @throws ZooKeeperConnectionException
	 * @author chenfanglin <br>
	 * @date 2016年1月7日 下午2:48:35
	 */
	public static HConnection getConnection() throws ZooKeeperConnectionException {
		if (hConnection == null) {
			synchronized (configuration) {
				if (hConnection == null) {
					hConnection = HConnectionManager.createConnection(configuration);
				}
			}
		}
		return hConnection;
	}

	/**
	 * 关闭连接
	 * @param table
	 * @author chenfanglin <br>
	 * @date 2016年1月7日 下午2:48:46
	 */
	public static void close(HTableInterface table) {
		try {
			if (table != null)
				table.close();
		} catch (IOException e) {
			logger.error("HbasePool table close error", e);
		}
	}
}
