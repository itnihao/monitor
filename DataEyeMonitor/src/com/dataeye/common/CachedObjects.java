package com.dataeye.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * <pre>
 * 缓存一些对象
 * 
 * @author Ivan <br>
 * @date 2015年3月31日 下午7:46:06 <br>
 *
 */
public class CachedObjects {
	/** 生成随机数的Random对象 */
	public static final Random RANDOM = new Random();
	/** 日期格式化 yyyy-MM-dd HH:mm:ss */
	public static final SimpleDateFormat SDF_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** 日期格式化 yyyy-MM-dd */
	public static final SimpleDateFormat SDF_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
	/** 日期格式化 MM-dd */
	public static final SimpleDateFormat SDF_MM_dd = new SimpleDateFormat("MM-dd");
	/** 日期格式化 HH:mm:ss */
	public static final SimpleDateFormat HH_mm_ss = new SimpleDateFormat("HH:mm:ss");
	/** 格式化对象为json字符串 */
	public static final Gson GSON = new Gson();
	/** 把对象转为json字符串的工具,这个是只有添加了 @Expose 注解的 才会被添加到 json字符串中 */
	public static final Gson GSON_ONLY_EXPOSE = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	/** 空 list */
	public static final List<?> EMPTY_LIST = Collections.EMPTY_LIST;

	// 定义一些comparator

}
