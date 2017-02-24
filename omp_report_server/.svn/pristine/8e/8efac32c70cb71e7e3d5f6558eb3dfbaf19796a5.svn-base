package com.dataeye.omp.report.dbproxy.hbase;

import com.dataeye.util.log.DELogger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.slf4j.Logger;

import java.io.IOException;

public class HbasePool {

	private final static Logger logger = DELogger.getLogger("hbase_client_log");
	private static String hbase_zookeeper_quorum = "dcnamenode1,dchbase1,dchbase2";
	private static String hbase_zookeeper_property_clientPort = "2181";
	
	private static Configuration configuration = null;

	static{
		try {
			reload();
		} catch (Throwable t) {
			logger.error("HbasePool init error", t);
			t.printStackTrace();
		}
	}

	public static void reload() {
		configuration = HBaseConfiguration.create();
		configuration.set("hbase.zookeeper.quorum", hbase_zookeeper_quorum);
		configuration.set("hbase.zookeeper.property.clientPort", hbase_zookeeper_property_clientPort);
		if(hConnection != null){
			try {
				hConnection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		hConnection = null;
	}

	private static HConnection hConnection = null;

	public static HConnection getConnection()
			throws ZooKeeperConnectionException {
		if (hConnection == null) {
			synchronized (configuration) {
				if (hConnection == null) {
					hConnection = HConnectionManager
							.createConnection(configuration);
				}
			}
		}
		return hConnection;
	}



	public static void close(HTableInterface table) {
		try {
			if (table != null)
				table.close();
		} catch (IOException e) {
			logger.error("HbasePool table close error", e);
		}
	}
}
