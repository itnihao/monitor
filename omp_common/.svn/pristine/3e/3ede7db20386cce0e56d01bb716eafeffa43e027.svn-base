package com.dataeye.omp.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;

import com.dataeye.omp.common.Constant;
import com.dataeye.util.log.DELogger;
import com.qq.jutil.crypto.HexUtil;
import com.qq.jutil.crypto.TEACoding;

/**
 * 常用的工具类
 * 
 * @author chenfanglin
 * @date 2016年2月29日 下午6:21:53
 */
public class ServerUtils {

	private final static Logger logger = DELogger.getLogger("server_log");
	
	/**
	 * 转换为json字符串
	 * @param obj
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:22:47
	 */
	public static String toJson(Object obj) {
		return Constant.GSON.toJson(obj);
	}
	
	/**
	 * 生成token
	 * @param uid
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:28:45
	 */
	public static String getTokenByUID(String uid) {
		TEACoding coding = new TEACoding(new byte[] { 'd', 'a', 't', 'a', 'e', 'y', 'e', 'd', 'a', 't', 'a', 'e', 'y',
				'e', '0', '1' });
		byte[] result = coding.encode(uid.getBytes());
		String token = HexUtil.bytes2HexStr(result);
		return token.toLowerCase();
	}


	public static int getSixSecurityCode(){
		Double d = (Math.random() * 9 + 1) * 100000;
		return d.intValue();
	}


	/**
	 * 从token里面获取uid
	 * @param token
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:28:52
	 */
	public static String getUIDFromToken(String token) {
		try {
			TEACoding coding = new TEACoding(new byte[] { 'd', 'a', 't', 'a', 'e', 'y', 'e', 'd', 'a', 't', 'a', 'e',
					'y', 'e', '0', '1' });
			byte[] result = HexUtil.hexStr2Bytes(token);
			byte[] source = coding.decode(result);
			return new String(source);
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * 获取配置文件
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月19日 下午2:17:47
	 */
	public static Properties getConfigProperties() {
		Properties properties = new Properties();
		try {
			properties.load(ClassLoader.getSystemResourceAsStream("config.properties"));
		} catch (IOException e) {
			logger.error("读取config.properties异常" + printStackTrace(e));
		}
		return properties;
	}
	
	/**
	 * 发送验证邮件
	 * @param subject
	 * @param message
	 * @author chenfanglin <br>
	 * @date 2016年4月19日 上午11:03:39
	 */
	public static void sendCheckMail(String uid, String message){
		Properties properties = getConfigProperties();
		Map<String, String> param = new HashMap<String, String>();
		param.put("to", uid);
		param.put("subject", properties.getProperty("token_subject"));
		param.put("content", message);
		try {
			HttpClientUtil.post(properties.getProperty("sso_check_url"), param);
		} catch (Exception e) {
			logger.error(printStackTrace(e));
		}
	}
	
	/**
	 * 发送告警邮件
	 * @param subject
	 * @param message
	 * @author chenfanglin <br>
	 * @date 2016年4月19日 下午5:24:46
	 */
	public static void sendAlarmMail(String message){
		Properties properties = getConfigProperties();
		Map<String, String> param = new HashMap<String, String>();
		param.put("to", properties.getProperty("alarm_email"));
		param.put("subject", properties.getProperty("alarm_subject"));
		param.put("content", message);
		try {
			HttpClientUtil.post(properties.getProperty("sso_check_url"), param);
		} catch (Exception e) {
			logger.error(printStackTrace(e));
		}
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
}
