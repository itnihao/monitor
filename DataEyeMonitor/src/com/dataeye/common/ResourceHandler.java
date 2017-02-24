package com.dataeye.common;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.dataeye.omp.constant.Constant.LANG;
import com.dataeye.omp.constant.Constant.Resource;
import com.digitcube.dbproxyclient.util.FileUpdate;

/**
 * <pre>
 * 语言文件处理器
 * @author Ivan          <br>
 * @date 2015年4月3日 下午1:43:34
 * <br>
 *
 */
public class ResourceHandler {
	/** 存放中文的键值 */
	public static Map<String, String> ZH;
	/** 存放英文的键值 */
	public static Map<String, String> EN;
	/** 存放繁体的键值 */
	public static Map<String, String> TW;

	/**
	 * 
	 * <pre>
	 * 初始化语言资源文件 
	 * @author Ivan<br>
	 * @date 2015年4月3日 下午1:44:17
	 * <br>
	 */
	public static void init() {
		reloadZH();
		reloadEN();
		reloadTW();
		registe();
	}

	/**
	 * 
	 * <pre>
	 * 处理文件改变 
	 * @author Ivan<br>
	 * @date 2015年4月3日 下午4:19:58
	 * <br>
	 */
	public static void registe() {
		FileWatcher.registe(new FileUpdate() {
			@Override
			public void fileChanged(String fileName) {
				if (Resource.ZH_FILE.endsWith(fileName)) {
					reloadZH();
				}
				if (Resource.EN_FILE.endsWith(fileName)) {
					reloadEN();
				}
				if (Resource.FT_FILE.endsWith(fileName)) {
					reloadTW();
				}
			}
		});
	}

	/**
	 * 
	 * <pre>
	 * 加载中文语言文件
	 *  @param propertiesFileName
	 *  @param map  
	 *  @author Ivan<br>
	 *  @date 2015年4月3日 下午2:00:20
	 * <br>
	 */
	private static synchronized void reloadZH() {
		System.err.println("reload " + Resource.ZH_FILE + " >>>>>>>>>");
		Map<String, String> zh = new ConcurrentHashMap<String, String>();
		Properties properties = new Properties();
		try {
			properties.load(ResourceHandler.class.getClassLoader().getResourceAsStream(Resource.ZH_FILE));
			Enumeration<?> kvs = properties.propertyNames();// 得到配置文件的名字
			while (kvs.hasMoreElements()) {
				String k = (String) kvs.nextElement();
				String v = properties.getProperty(k);
				zh.put(k, v);
			}
			ZH = zh;
			System.err.println("reload " + Resource.ZH_FILE + " done,size=" + zh.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * <pre>
	 * 加载英文语言文件 
	 * @author Ivan<br>
	 * @date 2015年4月3日 下午1:49:20
	 * <br>
	 */
	private static synchronized void reloadEN() {
		System.err.println("reload " + Resource.EN_FILE + " >>>>>>>>>");
		Map<String, String> en = new ConcurrentHashMap<String, String>();
		Properties properties = new Properties();
		try {
			properties.load(ResourceHandler.class.getClassLoader().getResourceAsStream(Resource.EN_FILE));
			Enumeration<?> kvs = properties.propertyNames();// 得到配置文件的名字
			while (kvs.hasMoreElements()) {
				String k = (String) kvs.nextElement();
				String v = properties.getProperty(k);
				en.put(k, v);
			}
			EN = en;
			System.err.println("reload " + Resource.EN_FILE + " done,size=" + en.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * <pre>
	 * 加载繁体语言文件 
	 * @author Ivan<br>
	 * @date 2015年4月3日 下午1:49:20
	 * <br>
	 */
	private static synchronized void reloadTW() {
		System.err.println("reload " + Resource.FT_FILE + " >>>>>>>>>");
		Map<String, String> tw = new ConcurrentHashMap<String, String>();
		Properties properties = new Properties();
		try {
			properties.load(ResourceHandler.class.getClassLoader().getResourceAsStream(Resource.FT_FILE));
			Enumeration<?> kvs = properties.propertyNames();// 得到配置文件的名字
			while (kvs.hasMoreElements()) {
				String k = (String) kvs.nextElement();
				String v = properties.getProperty(k);
				tw.put(k, v);
			}
			TW = tw;
			System.err.println("reload " + Resource.FT_FILE + " done,size=" + tw.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * <pre>
	 * 根据key取对应语言的属性
	 *  @param key
	 *  @param lang
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月3日 下午5:28:55
	 * <br>
	 */
	public static String getProperties(String key, String lang) {
		if (LANG.ZH.equals(lang)) {
			return ZH.get(key);
		}
		if (LANG.EN.equals(lang)) {
			return EN.get(key);
		}
		if (LANG.FT.equals(lang)) {
			return TW.get(key);
		}
		return null;
	}
}
