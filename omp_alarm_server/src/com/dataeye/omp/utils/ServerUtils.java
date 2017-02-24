package com.dataeye.omp.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;

import com.dataeye.omp.common.Constant;
import com.dataeye.omp.common.Constant.OsType;
import com.dataeye.omp.common.ResourceHandler;
import com.dataeye.omp.dbproxy.mysql.MysqlUtil;
import com.dataeye.util.log.DELogger;
import com.google.gson.reflect.TypeToken;
import com.xunlei.jdbc.JdbcTemplate;

/**
 * 常用的工具类
 * 
 * @author chenfanglin
 * @date 2016年2月29日 下午6:21:53
 */
public class ServerUtils {

	private final static Logger logger = DELogger.getLogger("exception_log");

	/**
	 * 读取alarmconsumer.properties
	 * 
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午7:49:06
	 */
	public static Properties getAlarmConsumerProperties() {
		Properties properties = new Properties();
		try {
			properties.load(ClassLoader.getSystemResourceAsStream("alarmconsumer.properties"));
		} catch (IOException e) {
			logger.error("读取alarmconsumer.properties异常" + e.getMessage());
			// TODO 告警！！
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 读取stormkafka.properties
	 * 
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午7:48:15
	 */
	public static Properties getAlarmProducerProperties() {
		Properties properties = new Properties();
		try {
			properties.load(ClassLoader.getSystemResourceAsStream("stormkafka.properties"));
		} catch (IOException e) {
			logger.error("读取stormkafka.properties异常" + e.getMessage());
			// TODO 告警！！
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 读取detectconsumer.properties
	 * 
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午7:00:07
	 */
	public static Properties getDetectConsumerProperties() {
		Properties properties = new Properties();
		try {
			properties.load(ClassLoader.getSystemResourceAsStream("detectconsumer.properties"));
		} catch (IOException e) {
			logger.error("读取detectconsumer.properties异常" + e.getMessage());
			// TODO 告警！！
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 读取processconsumer.properties
	 * 
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月5日 下午5:40:55
	 */
	public static Properties getProcessConsumerProperties() {
		Properties properties = new Properties();
		try {
			properties.load(ClassLoader.getSystemResourceAsStream("processconsumer.properties"));
		} catch (IOException e) {
			logger.error("读取processconsumer.properties异常" + e.getMessage());
			// TODO 告警！！
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 读取mysqlconsumer.properties
	 * @return Properties
	 *
     */
	public static Properties getMysqlAlarmProperties() {
		Properties properties = new Properties();
		try {
			properties.load(ClassLoader.getSystemResourceAsStream("mysqlconsumer.properties"));
		} catch (IOException e) {
			logger.error("读取mysqlconsumer.properties失败" + e.getMessage());
		}
		return properties;
	}

	/**
	 * 获取操作系统类型(非常简单的判断，不严谨)
	 * 
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午6:21:46
	 */
	public static String getOsType() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			return OsType.WINDOWS;
		} else if (osName.contains("linux")) {
			return OsType.LINUX;
		} else {
			return OsType.OTHER;
		}
	}

	/**
	 * 得到百分数格式
	 * 
	 * @param d
	 * @return
	 * @author ivantan
	 * @date 2015年8月17日 下午2:15:22
	 */
	public static final String precent(double d) {
		return String.format("%.2f", d) + "%";
	}

	/**
	 * 根据ip获取对应的serverID
	 * 
	 * @param ip
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月27日 下午3:15:00
	 */
	public static int getServerIDByIP(String ip) {
		try {
			JdbcTemplate jdbcTemplateMonitor = MysqlUtil.getJdbcTemplateMonitor();
			String sql = "select distinct svr_id from " + Constant.SERVER_IP_LIST + " where ip = ?";
			int serverID = jdbcTemplateMonitor.queryForInt(sql, ip);
			return serverID;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【异常】根据ip获取对应的serverID：" + e.getMessage());
			// TODO ??告警
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
	public static String getDiskPartition(int serverID){
		JdbcTemplate jdbcTemplateMonitorStat = MysqlUtil.getJdbcTemplateMonitorStat();
		String sql = "select disk_partition from "+Constant.SERVER_LIST+" where id = ?";
		try {
			String disk_partition = jdbcTemplateMonitorStat.queryForString(sql, serverID);
			return disk_partition;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【异常】 获取磁盘 分区详情：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 获取内存总大小
	 * @param serverID
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月14日 下午12:03:53
	 */
	public static BigDecimal getMemoryTotal(int serverID){
		JdbcTemplate jdbcTemplateMonitorStat = MysqlUtil.getJdbcTemplateMonitorStat();
		String sql = "select memory from "+Constant.SERVER_LIST+" where id = ?";
		try {
			long memory = jdbcTemplateMonitorStat.queryForLong(sql, serverID);
			return new BigDecimal(memory);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【异常】 获取内存总大小：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 得到异常的堆栈信息
	 * @param e
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月9日 上午11:51:13
	 */
	public static String printStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		e.printStackTrace(pw);
		if (pw != null) {
			pw.close();
		}
		if (sw != null) {
			try {
				sw.close();
			} catch (IOException e1) {
				// e1.printStackTrace();
			}
		}
		return sw.toString();
	}
	
	/**
	 * 发送告警短信
	 * @param to
	 * @param content
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年2月24日 下午4:27:48
	 */
	public static void sendAlarmSms(String to,String content){
		Map<String, String> param = new HashMap<String, String>();
		param.put("to", to);
		param.put("content", content);
		try {
			HttpClientUtil.post(ResourceHandler.getProperties("alarm_sms_url"), param);
		} catch (Exception e) {
			logger.error("发送短信告警异常：" + printStackTrace(e));
		}
		
	}
	
	/**
	 * 发送APP告警
	 * @param to
	 * @param subject
	 * @param content
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年4月15日 下午6:12:01
	 */
	public static void sendAppAlarm(String to,String subject, String content) {
		Map<String, String> para = new HashMap<String, String>();
		para.put("title", subject);
		para.put("type", Constant.ALARM_TYPE);
		para.put("content", content);
		para.put("others", to);
		try {
			HttpClientUtil.post(ResourceHandler.getProperties("alarm_app_report_url"), para);
		} catch (Exception e) {
			logger.error("发送App告警异常：" + printStackTrace(e));
		}
	}


	/**
	 * 读取monitorconsumer.properties
	 *
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月5日 下午5:40:55
	 */

	private static final String consumerConfigFile
			= "monitorconsumer.properties";

	public static Properties getMonitorConsumerProperties() {
		Properties properties = new Properties();
		try {
			properties.load(
					ClassLoader.getSystemResourceAsStream(consumerConfigFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public static void main(String[] args) {
		String type = "[]";
		List<Integer> list = Constant.GSON.fromJson(type, new TypeToken<List<Integer>>() {}.getType());
		System.out.println(list.size());
	}
}
