package com.dataeye.utils;

import com.dataeye.util.log.DELogger;
import org.slf4j.Logger;

/**
 * <pre>
 * 日志工具类
 * @author Ivan          <br>
 * @date 2015年4月8日 上午10:54:33
 * <br>
 *
 */
public class LogUtils {
	/** Tracker 日志 */
	private static Logger log_track = DELogger.getLogger("tracker");
	/** 权限 日志 */
	private static Logger log_priv = DELogger.getLogger("priv");
	/** 请求日志 */
	private static Logger log_request = DELogger.getLogger("request");
	/** SQL */
	private static Logger log_sql = DELogger.getLogger("sql");

	/**
	 * 
	 * <pre>
	 * 记录跟踪日志
	 *  @param message  
	 *  @author Ivan<br>
	 *  @date 2015年4月8日 上午11:01:18
	 * <br>
	 */
	public static void logTracker(String message) {
		log_track.info(message);
	}

	/**
	 * 
	 * <pre>
	 * 记录权限过滤结果
	 *  @param ID
	 *  @param doName
	 *  @param type
	 *  @param result  
	 *  @author Ivan<br>
	 *  @date 2015年4月13日 下午3:33:41
	 * <br>
	 */
	public static void logPriv(String ID, String doName, String type, String result) {
		log_priv.info("{}->{}->{}->{}", ID, doName, type, result);
	}

	/**
	 * 
	 * <pre>
	 * 记录请求日志
	 *  @param ID
	 *  @param doName
	 *  @param classAndMethod
	 *  @param url
	 *  @param responseJson
	 *  @param cost  
	 *  @author Ivan<br>
	 *  @date 2015年4月13日 下午5:18:21
	 * <br>
	 */
	public static void logRequest(String ID, String doName, String classAndMethod, String url, String responseJson,
			long cost) {
		log_request.info("{}->{}->{}->{}->{}->{}->{}", ID, DateUtils.now(), doName, classAndMethod, url, responseJson,
				cost);
	}

	/**
	 * 
	 * <pre>
	 * 记录Sql语句
	 *  @param ID
	 *  @param fullSql  
	 *  @author Ivan<br>
	 *  @date 2015年4月13日 下午7:21:50
	 * <br>
	 */
	public static void logSql(String ID, String funDesc, String fullSql) {
		log_sql.info("{}->{}->{}", ID, funDesc, fullSql);
	}
}
