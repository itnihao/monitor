package com.dataeye.omp.report.dbproxy.hbase;

import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class HbaseBatchPutHandler {
	private final static Logger logger = LoggerFactory
			.getLogger(HbaseBatchPutHandler.class);

	private static HbaseBatchPutHandler instance = null;

	private BlockingQueue<PutItem> cacheQueue =
			new ArrayBlockingQueue<PutItem>(1000000);


	public synchronized static  HbaseBatchPutHandler getInstance(){
		if(instance == null){
			instance = new HbaseBatchPutHandler();
		}
		return instance;
	}

	public void put(PutItem putItem) {
		try {
			cacheQueue.put(putItem);
			logger.info("cacheQueue size:" + cacheQueue.size());
		} catch (InterruptedException e) {
			logger.error("put putItem to queue error:"+e.getMessage());
		}
	}


	public static void saveItem(PutItem putItem) {
		if (instance == null) {
			getInstance();
			instance.run(10);
		}
		instance.put(putItem);
	}
	 
	public void run(int threadNum){
		for(int i = 0; i < threadNum ; i++){
			new Thread( new HbaseUpdater()).start();
		}
	}

	private class HbaseUpdater implements Runnable {
		// tableName -> List<Put>; 每个table即将要插入的Put对象的列表
		private HashMap<String, List<Put>> batchDataMap = new HashMap<String, List<Put>>();
		private HashMap<String, HTableInterface> htableInterfaceMap = new HashMap<String, HTableInterface>();

		private long count = 0;
		
		@Override
		public void run() {
				logger.info("hbase put Updater thread start....");
			
				long lastUpdateHhaseTime = 0;
				ArrayList<PutItem> list = new ArrayList<PutItem>();

				int sleepCount=0;
				while(true){
					try {
						int num = cacheQueue.drainTo(list,10000);
						//max sleep 10 seconds, otherwise, force flush cache
						if(num <= 0 && sleepCount < 10){
							Thread.sleep(1000);
							sleepCount++;
							continue;
						}
						sleepCount = 0;
						
						logger.info("put cacheQeueue: " + Thread.currentThread().getId() + ": " + num);

						long now = System.currentTimeMillis() / 1000;
						if(count > 10000 || (now - lastUpdateHhaseTime) >= 3){
							updateIntoHbase(list);
							lastUpdateHhaseTime = now;
							count = 0;
						}else{
							Thread.sleep(1000);
						}
						
					} catch (Exception e) {
						logger.error("hbaseUpdater(Put) thread error:" + e.getMessage());
					}
				}
		}
		
		private void updateIntoHbase(List<PutItem> list){
			logger.info("exec BatchPutHandler updateIntoHbase");
			for(PutItem putItem:list){
				String tableName =	 putItem.getHtable();
				List<Put> dataLs = batchDataMap.get(putItem.getHtable());
				if(dataLs==null){
					dataLs = new ArrayList<Put>();
					batchDataMap.put(tableName, dataLs);
				}
				dataLs.add(putItem.getPut());
			}

			logger.info("put into batch map done.");
			
			for(String tableName: batchDataMap.keySet()){
				HTableInterface htable = getHTableInterface(tableName);
				List<Put> ls= batchDataMap.get(tableName);
				try {
					logger.info("exec batch put to:"+tableName+"; size:"+ls.size());
					htable.batch(ls);
				} catch (Exception e) {
					logger.error("Can't not batch put data into table:"
							+ tableName
							+ ". error:" + e.getMessage());
				}
			}
			
			batchDataMap.clear();
			list.clear();
			
		}
		
		private HTableInterface getHTableInterface(String tableName){

			HTableInterface hbaseTable = htableInterfaceMap.get(tableName);
			if(hbaseTable != null)  return hbaseTable;
			
			try {
				hbaseTable = HbasePool.getConnection().getTable(tableName);
			} catch (ZooKeeperConnectionException e) {
				logger.error("getConnection error:"+e.getMessage());
			} catch (IOException e) {
				logger.error("getConnection error:"+e.getMessage());
			}
			
			if(hbaseTable != null){
				htableInterfaceMap.put(tableName, hbaseTable);
			}
			
			return hbaseTable;
		}
	};
	

}
