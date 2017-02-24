package com.dataeye.omp.report.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {


	public static String formatYesterdayyyyyMMdd(String time) {
		Date date = parseDate(time);
		Date yes = new Date(date.getTime() - 24 * 60 * 60 * 1000);
		return SDF_yyyy_MM_dd.format(yes);
	}

	public static final SimpleDateFormat HH = new SimpleDateFormat("HH");
	public static String formatKK(String time){
		Date date = parseDate(time);
		return HH.format(date);
	}

	public static final SimpleDateFormat SDF_yyyy_MM_dd_HH
			= new SimpleDateFormat("yyyy-MM-dd HH");
	public static String formatyyyyMMddHH(String time){
		Date date = parseDate(time);
		return SDF_yyyy_MM_dd_HH.format(date);
	}

	/**
	 * 日期格式化 yyyy-MM-dd
	 */
	public static final SimpleDateFormat SDF_yyyy_MM_dd
			= new SimpleDateFormat("yyyy-MM-dd");
	public static String formatyyyyMMdd(String time){
		Date date = parseDate(time);
		return SDF_yyyy_MM_dd.format(date);
	}

	/**
	 * <pre>
	 * 获取秒级时间戳
	 *
	 * @return 秒级时间戳
	 * @author Ivan<br>
	 * @date 2015年3月31日 下午7:18:24 <br>
	 */
	public static final int unixTimestamp() {
		return (int) (System.currentTimeMillis() / 1000);
	}


	/**
	 * 获取当前时间字符串
	 *
	 * @return
	 * @author ivantan
	 * @date 2015年8月17日 下午1:02:40
	 */
	public static final SimpleDateFormat SDF_yyyy_MM_dd_HH_mm_ss
			= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String currentTime() {
		return SDF_yyyy_MM_dd_HH_mm_ss.format(new Date());
	}

	/**
	 * 获取一天中的第几分钟
	 * @param time
	 * @return
     */
	public static final SimpleDateFormat mm = new SimpleDateFormat("mm");
	public static int getMinuteBetweenDay(String time){
		Date date = parseDate(time);
		int hour = Integer.parseInt(HH.format(date));
		int minute = Integer.parseInt(mm.format(date));
		return hour * 60 + minute;
	}

	public static int getHourBetweenDay(String time) {
		Date date = parseDate(time);
		return Integer.parseInt(DateUtils.HH.format(date));
	}

	private static final String timeFormat = "yyyy-MM-dd HH:mm:ss";
	public static Date parseDate(String time) {
		Date date = null;
		try {
			date = new SimpleDateFormat(timeFormat).parse(time);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return date;
	}

}
