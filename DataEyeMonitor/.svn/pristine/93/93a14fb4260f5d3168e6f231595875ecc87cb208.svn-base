package com.dataeye.utils;

import java.util.List;
import java.util.Map;

import com.dataeye.omp.constant.Constant.LANG;
import com.dataeye.omp.constant.Constant.ServerCfg;

/**
 * <pre>
 * 与验证相关的工具类
 * @author Ivan          <br>
 * @date 2015年4月1日 下午3:58:11
 * <br>
 *
 */
public class ValidateUtils {
	/**
	 * 
	 * <pre>
	 * 判断是不是null
	 *  @param object
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月8日 下午8:34:56
	 * <br>
	 */
	public static boolean isNull(Object object) {
		return object == null;
	}

	/**
	 * 
	 * <pre>
	 * 判断不是null
	 *  @param object
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月8日 下午8:34:56
	 * <br>
	 */
	public static boolean isNotNull(Object object) {
		return !isNull(object);
	}

	/**
	 * 
	 * <pre>
	 * 验证语言是否合法
	 *  @param lang
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月1日 下午4:15:56
	 * <br>
	 */
	public static boolean isValidLang(String lang) {
		if (lang != null && (LANG.EN.equals(lang) || LANG.ZH.equals(lang) || LANG.FT.equals(lang))) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * <pre>
	 * 是否是有效数字int
	 *  @param number
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月1日 下午5:42:52
	 * <br>
	 */
	public static boolean isValidNumber(int number) {
		if (number == ServerCfg.INVALID_NUMBER) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * <pre>
	 * 是否是有效数字long
	 *  @param number
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月8日 下午5:36:35
	 * <br>
	 */
	public static boolean isValidNumber(long number) {
		if (number == ServerCfg.INVALID_NUMBER) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * <pre>
	 *
	 * @param str
	 * @return  
	 * @author Ivan<br>
	 * @date 2015年3月31日 下午8:19:54
	 * <br>
	 */
	private static boolean isBlank(CharSequence str) {
		if (str == null) {
			return true;
		}
		int len = str.length();
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			if (c > ' ') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * <pre>
	 * 判断给定参数列表中是否存在空(null和任意多个空格都认为是空)
	 *  @param args
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年3月31日 下午8:20:07
	 * <br>
	 */
	public static boolean isEmpty(CharSequence... args) {
		if (args == null) {
			return true;
		}
		for (CharSequence arg : args) {
			if (isBlank(arg)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * <pre>
	 * 判断给定参数列表中是否都不空(null和任意多个空格都认为是空)
	 *  @param args
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年3月31日 下午8:26:30
	 * <br>
	 */
	public static boolean isNotEmpty(CharSequence... args) {
		return !isEmpty(args);
	}

	/**
	 * 
	 * <pre>
	 * 是否是空map
	 *  @param map
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月1日 下午3:21:11
	 * <br>
	 */
	public static boolean isEmptyMap(Map<?, ?> map) {
		if (map == null || map.size() == 0)
			return true;
		return false;
	}

	/**
	 * 
	 * <pre>
	 * map不为空
	 *  @param map
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月1日 下午3:22:32
	 * <br>
	 */
	public static boolean isNotEmptyMap(Map<?, ?> map) {
		return !isEmptyMap(map);
	}

	/**
	 * 
	 * <pre>
	 * 是否是空数组
	 *  @param arr
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月7日 上午10:56:58
	 * <br>
	 */
	public static <T> boolean isEmptyArray(T[] arr) {
		if (arr == null || arr.length == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * <pre>
	 * 是否是空List
	 *  @param list
	 *  @return  
	 *  @author Ivan<br>
	 *  @date 2015年4月7日 上午10:58:57
	 * <br>
	 */
	public static boolean isEmptyList(List<?> list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}
}
