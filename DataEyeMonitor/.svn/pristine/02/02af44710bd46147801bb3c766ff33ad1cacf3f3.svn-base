package com.dataeye.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dataeye.omp.common.DEResponse;

/**
 * <pre>
 * 跟踪器，记录处理过程
 * @author Ivan          <br>
 * @date 2015年4月8日 上午9:50:35
 * <br>
 *
 */
public class Tracker implements Job {
	/**
	 * 
	 * <pre>
	 * Track对象
	 * @author Ivan          <br>
	 * @date 2015年4月8日 上午9:55:59 <br>
	 * @version 1.0
	 * <br>
	 */
	public static class TrackObj {
		/** @see DEResponse#getID() */
		private String ID;
		/** 开始记录的时间 */
		private long startTime;
		/** 详细的日志流水 */
		private List<String> messageList = new ArrayList<String>();
		/** 是否结束 */
		private boolean finished;

		public TrackObj(String ID) {
			this.ID = ID;
			this.startTime = DateUtils.winTimestamp();
		}

		public TrackObj(String ID, String message) {
			this(ID);
			add(message);
		}

		public String getID() {
			return ID;
		}

		public long getStartTime() {
			return startTime;
		}

		public void add(String message) {
			if (!finished) {
				long currentTime = DateUtils.winTimestamp();
				messageList.add(message + "[" + (currentTime - startTime) + "]");
			}
		}

		public boolean isFinished() {
			return finished;
		}

		/*
		 * 设置结束标志
		 */
		public void finished() {
			if (!finished) {
				this.finished = true;
				long currentTime = DateUtils.winTimestamp();
				messageList.add("END[" + (currentTime - startTime) + "]");
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("\n").append("------").append(this.ID).append("------").append("\n");
			for (String str : this.messageList) {
				sb.append(str).append("\n");
			}
			return sb.toString();
		}
	}

	/** 缓存 */
	public static ConcurrentHashMap<String, TrackObj> CACHE = new ConcurrentHashMap<String, TrackObj>(1000);

	/**
	 * 
	 * <pre>
	 * 向跟踪器中加入跟踪日志
	 *  @param ID
	 *  @param message  
	 *  @author Ivan<br>
	 *  @date 2015年4月8日 上午10:51:15
	 * <br>
	 */
	public static void add(String ID, String message) {
		TrackObj trackObj = CACHE.get(ID);
		if (trackObj == null) {// not exists in cache
			synchronized (CACHE) {
				trackObj = CACHE.get(ID);
				if (trackObj == null) {
					TrackObj tmp = new TrackObj(ID, message);
					CACHE.put(ID, tmp);
					return;
				}
			}
		}
		// exists
		trackObj.add(message);
	}

	/**
	 * 
	 * <pre>
	 * 结束某个日志
	 *  @param ID  
	 *  @author Ivan<br>
	 *  @date 2015年4月8日 下午2:27:05
	 * <br>
	 */
	public static void Finish(String ID) {
		TrackObj trackObj = CACHE.get(ID);
		if (trackObj != null) {
			trackObj.finished();
		}
	}

	/**
	 * 定时执行逻辑，定时将日志信息写入日志文件
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// 从cache中取出所有对象，依次遍历,满足条件之一就写入日志文件
		// 1.TrackObj.isFinished=true
		// 2. currentTime-startTime>10s
		for (Map.Entry<String, TrackObj> entry : CACHE.entrySet()) {
			String ID = entry.getKey();
			TrackObj trackObj = entry.getValue();
			if (trackObj.isFinished()) {// 调用者自己标注了finished标志,写入文件,并从缓存移除
				LogUtils.logTracker(trackObj.toString());
				CACHE.remove(ID);
			} else {
				// 检查是否超时，如果超时了也写入文件,并从缓存移除
				long currentTime = DateUtils.winTimestamp();
				if (currentTime - trackObj.getStartTime() > 10 * 1000) {
					LogUtils.logTracker(trackObj.toString());
					CACHE.remove(ID);
				}
			}
		}
	}
}