package com.dataeye.omp.common;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.dataeye.omp.common.Constant.Resource;
import com.dataeye.omp.utils.FileWatcher;
import com.digitcube.dbproxyclient.util.FileUpdate;

/**
 * 文件处理器
 * @author chenfanglin
 * @date 2016年2月29日 下午6:44:41
 */
public class ResourceHandler {
	/** 存放告警内容的键值 */
	public static Map<String, String> ALARM;

	/**
	 * 初始化资源文件 
	 * 
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午6:45:02
	 */
	public static void init() {
		reloadFile();
		registe();
	}

	/**
	 * 处理文件改变 
	 * 
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午6:45:10
	 */
	public static void registe() {
		FileWatcher.registe(new FileUpdate() {
			@Override
			public void fileChanged(String fileName) {
				if (Resource.ALARM_FILE.endsWith(fileName)) {
					reloadFile();
				}
			}
		});
	}

	/**
	 * 加载告警文件
	 * 
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午6:45:38
	 */
	private static synchronized void reloadFile() {
		System.err.println("reload " + Resource.ALARM_FILE + " >>>>>>>>>");
		Map<String, String> alarm = new ConcurrentHashMap<String, String>();
		Properties properties = new Properties();
		try {
			properties.load(ResourceHandler.class.getClassLoader().getResourceAsStream(Resource.ALARM_FILE));
			Enumeration<?> kvs = properties.propertyNames();// 得到配置文件的名字
			while (kvs.hasMoreElements()) {
				String k = (String) kvs.nextElement();
				String v = properties.getProperty(k);
				alarm.put(k, v);
			}
			ALARM = alarm;
			System.err.println("reload " + Resource.ALARM_FILE + " done,size=" + alarm.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 根据key取对应语言的属性
	 * @param key
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午6:45:45
	 */
	public static String getProperties(String key) {
		return ALARM.get(key);
	}
}
