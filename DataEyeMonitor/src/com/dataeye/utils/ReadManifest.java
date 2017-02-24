package com.dataeye.utils;

import com.dataeye.common.CachedObjects;
import com.xunlei.util.StringTools;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ReadManifest implements ServletContextListener {

	private static Map<String, String> KvMap = new HashMap<String, String>();

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		InputStream inputStream = context.getResourceAsStream("tools/manifest.json");
		InputStreamReader reader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(reader);
		try {
			StringBuilder result = new StringBuilder();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line);
			}
			if (StringTools.isNotEmpty(result)) {
				@SuppressWarnings("unchecked")
				Map<String, String> map = CachedObjects.GSON.fromJson(result.toString(), Map.class);
				KvMap.putAll(map);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("--------------------------");
		System.out.println(KvMap);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		KvMap = null;
	}

	/**
	 * 
	 * <pre>
	 * 根据key取配置值
	 * 
	 * @param key
	 * @return
	 * @author Ivan<br>
	 * @date 2015年3月4日 下午2:48:26 <br>
	 */
	public static String getKey(String key, String dir, Boolean debug) {
		if (debug) {
			return dir + key + "?v=" + String.valueOf(System.currentTimeMillis());
		}
		if (KvMap != null) {
			return dir + key + "?v=" + KvMap.get(key);
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 取所有的配置值并返回json格式(map对象的json格式)
	 * 
	 * @return
	 * @author Ivan<br>
	 * @date 2015年3月4日 下午2:49:01 <br>
	 */
	public static String getMap() {
		if (KvMap != null) {
			return CachedObjects.GSON.toJson(KvMap);
		}
		return null;
	}
}
