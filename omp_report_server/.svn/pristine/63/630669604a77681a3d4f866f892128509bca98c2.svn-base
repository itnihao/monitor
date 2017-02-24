package com.dataeye.omp.report.dbproxy.hbase;

import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class HbaseBatchHandler {
	private final static Logger logger = LoggerFactory.getLogger(HbaseBatchHandler.class);
	private static HbaseBatchHandler instance = null;
	private BlockingQueue<StatItem> cacheQueue = new ArrayBlockingQueue<StatItem>(1000000);

	public synchronized static HbaseBatchHandler getInstance() {
		if (instance == null) {
			instance = new HbaseBatchHandler();
		}
		return instance;
	}
	 
	public void addStatItem(StatItem statItem){
		try {
			cacheQueue.put(statItem);
			//logger.info("cacheQueue size:" + cacheQueue.size());
		} catch (InterruptedException e) {
			logger.info("put statItem to queue error:"+e.getMessage());
		}
	}
	
	 
	public void run(int threadNum){
		for(int i = 0; i < threadNum ; i++){
			new Thread( new HbaseUpdater()).start();
		}
	}
	
	
	private class HbaseUpdater implements Runnable {
		private HashMap<String, HTableStatCache> batchCache = new HashMap<String, HTableStatCache>();
		private HashMap<String, HTableInterface> htableInterfaceMap = new HashMap<String, HTableInterface>();
		
		private long count = 0;
		
		@Override
		public void run() {
				logger.info("hbase Increment Updater thread start....");
			
				long lastUpdateHhaseTime = 0;
				ArrayList<StatItem> list = new ArrayList<StatItem>();

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
						
//						logger.info("cacheQeueue: " + Thread.currentThread().getId() + ": " + num);
						
						for(StatItem statItem : list){
							addStatItem(statItem);
						}
						list.clear();
						
						long now = System.currentTimeMillis() / 1000;
						if(count > 10000 || (now - lastUpdateHhaseTime) >= 3){
							updateIntoHbase();
							lastUpdateHhaseTime = now;
							count = 0;
						}else{
							Thread.sleep(1000);
						}
						
					} catch (Exception e) {
						logger.info("hbaseUpdater(Increment) thread error:"+e.getMessage());
					}
				}
		}
		
		private void updateIntoHbase(){
//			logger.info("exec BatchHandler updateIntoHbase");
			if(batchCache.size() == 0) return;
		
			Set<String> succTableList = new HashSet<String>();
			
			for(Entry<String, HTableStatCache> entry : batchCache.entrySet()){
				
				
				HashMap<String, StatItem> statItemList = entry.getValue().getStatItemList();
				HTableInterface hbaseTable = getHTableInterface(entry.getKey());
				
				if(hbaseTable == null){
					logger.error("HTableInterface is null, table:" + entry.getKey());
					continue;
				}
				
				List<Increment> incrementList = new ArrayList<Increment>();
				logger.info("exec batch increment to:"+entry.getKey()+"; size:"+statItemList.size());
				for(Entry<String, StatItem> statItemeEntry : statItemList.entrySet()){
					StatItem statItem = statItemeEntry.getValue();
					//logger.info(Thread.currentThread().getId() + " updateIntoHbase:" + statItem.toString() );
					Increment increment = new Increment(Bytes.toBytes(statItem.getRowKey()));
					increment.addColumn(Bytes.toBytes("info"), Bytes.toBytes(statItem.getStatKey()), statItem.getValue());
					incrementList.add(increment);
					
					if(incrementList.size() > 200){
						doBatchIncrement(hbaseTable, incrementList);
						incrementList.clear();
					}
				}
				
				if(incrementList.size() > 0){
					doBatchIncrement(hbaseTable, incrementList);
					incrementList.clear();
				}
				 
				succTableList.add(entry.getKey());
			}
			
			
			for(String tableName : succTableList){
				batchCache.remove(tableName);
			}
			
			//logger.info("after updateIntoHbase: " +  batchCache.size());
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

		
		private void doBatchIncrement(HTableInterface tableInterface,List<Increment> incrementList){
			if(incrementList.isEmpty()){
				return;
			}
			try {
				tableInterface.batch(incrementList);
			} catch (Throwable t) {
				logger.error("Error happened while doing batch increment...", t);
			}
		}
		
		private void addStatItem(StatItem statItem){
			count++;
			if(batchCache.containsKey(statItem.getHtable())){
				batchCache.get(statItem.getHtable()).addStatItem(statItem);
			}else{
				HTableStatCache tableStatCache = new HTableStatCache();
				tableStatCache.addStatItem(statItem);
				batchCache.put(statItem.getHtable(), tableStatCache);
			}
		}
	};


	class HTableStatCache{
		private HashMap<String,StatItem> statItemList = new HashMap<String, StatItem>();

		private String getKey(StatItem statItem){
			return statItem.getRowKey() + "_" + statItem.getStatKey();
		}

		public void addStatItem(StatItem statItem){
			String key = getKey(statItem);
			if(statItemList.containsKey(key)){
				statItemList.get(key).increase(statItem.getValue());
			}else{
				statItemList.put(key, statItem);
			}
		}

		public HashMap<String, StatItem> getStatItemList() {
			return statItemList;
		}

	}
}
