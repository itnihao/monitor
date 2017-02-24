package com.dataeye.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.dataeye.common.CachedObjects;
import com.dataeye.omp.constant.Constant.ServerCfg;


/**
 * <pre>
 * 日期操作类
 * 
 * @author Ivan <br>
 * @date 2015年3月31日 下午7:15:25 <br>
 *
 */
public class DateUtils {
 
	/**
	 * 
	 * <pre>
	 * 获取毫秒级时间戳
	 * 
	 * @return 毫秒级时间戳
	 * @author Ivan<br>
	 * @date 2015年3月31日 下午7:17:12 <br>
	 */
	public static final long winTimestamp() {
		return System.currentTimeMillis();
	}

	/**
	 * 
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
	 * 
	 * <pre>
	 * 解析yymmdd格式的时间，如150402得到unix时间戳
	 * 
	 * @param yymmdd
	 * @return
	 * @author Ivan<br>
	 * @date 2015年4月2日 上午10:24:32 <br>
	 */
	public static int getUnixTimestampFromyyMMdd(String yymmdd) {
		if (yymmdd == null || yymmdd.length() != 6) {
			return ServerCfg.INVALID_NUMBER;
		}
		yymmdd = "20" + yymmdd;
		try {
			int year = Integer.parseInt(yymmdd.substring(0, 4));
			int month = Integer.parseInt(yymmdd.substring(4, 6));
			int day = Integer.parseInt(yymmdd.substring(6, 8));
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month - 1, day, 0, 0, 0);
			return (int) (calendar.getTimeInMillis() / 1000);
		} catch (Exception e) {
		}
		return ServerCfg.INVALID_NUMBER;
	}

	public static int getUnixTimestampFromyyyyMMdd(String yyyy_mm_dd) {
		try {
			Date d = CachedObjects.SDF_yyyy_MM_dd.parse(yyyy_mm_dd);
			return (int) (d.getTime() / 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return unixTimestamp();
	}

	public static int getUnixTimestampFromyyyyMMddHHmmss(String yyyy_mm_dd_HH_mm_ss) {
		try {
			Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(yyyy_mm_dd_HH_mm_ss);
			return (int) (d.getTime() / 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return unixTimestamp();
	}
	
	public static String getNextDate(String date,String format){
		try {
			SimpleDateFormat formater = new SimpleDateFormat(format);
			Date d = formater.parse(date);
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(d);
			cal.add(Calendar.DATE, 1);
			return formater.format(cal.getTime());	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	
	public static String getDateString(long timestamp,String format){
		SimpleDateFormat formater = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance(); 
		cal.setTimeInMillis(timestamp*1000L);
		return formater.format(cal.getTime());	
	}

	/**
	 * 
	 * <pre>
	 * 获取当前日期时间字符串
	 * 
	 * @return
	 * @author Ivan<br>
	 * @date 2015年4月3日 下午4:11:17 <br>
	 */
	public static final String now() {
		return CachedObjects.SDF_yyyy_MM_dd_HH_mm_ss.format(new Date());
	}

	/**
	 * 获取昨天的日期
	 * 
	 * @return
	 * @author ivantan
	 * @date 2015年8月17日 下午1:38:39
	 */
	public static final String yesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当前日期字符串
	 * 
	 * @return
	 * @author ivantan
	 * @date 2015年8月17日 下午1:01:52
	 */
	public static final String currentDate() {
		return CachedObjects.SDF_yyyy_MM_dd.format(new Date());
	}

	/**
	 * 获取当前时间字符串
	 * 
	 * @return
	 * @author ivantan
	 * @date 2015年8月17日 下午1:02:40
	 */
	public static final String currentTime() {
		return CachedObjects.SDF_yyyy_MM_dd_HH_mm_ss.format(new Date());
	}

	/**
	 * 判断是否只是今天的时间
	 * 
	 * @param date
	 * @return
	 * @author ivantan
	 * @date 2015年8月17日 下午1:20:55
	 */
	public static boolean isToday(String date) {
		return currentDate().equals(date) ? true : false;
	}

	public static String getyyyyMMddFromTimestamp(long time) {
		return CachedObjects.SDF_yyyy_MM_dd.format(new Date(time * 1000L));
	}


	public static String getyyyyMMddHHmmssFromTimestamp(long time) {
		return CachedObjects.SDF_yyyy_MM_dd_HH_mm_ss.format(new Date(time * 1000L));
	}
	
	public static String getMMddFromTimestamp(int time) {
		return CachedObjects.SDF_MM_dd.format(new Date(time * 1000L));
	}


	public static long getFirstDayOfMonthTimestamp() {
		Calendar cal = Calendar.getInstance();//获取当前日期
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
		return cal.getTimeInMillis() / 1000;
	}

	public static long getLastOneHour(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -1);
		return cal.getTimeInMillis() / 1000;
	}




	public static void main(String[] args) {
		System.out.println(getLastOneHour());
		System.out.println(System.currentTimeMillis() / 1000);
		long value = 1471485963L - 1471482363L;
		System.out.println(value / 60 / 60);
		System.out.println(getyyyyMMddHHmmssFromTimestamp(getLastOneHour()));

	}


	public static String formatHour(String hour){
		int iHour = Integer.parseInt(hour);
		return String.format("%02d", iHour);
	}
	/**
	 * 获取当天0:0:0 的linux时间戳
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月7日 下午4:03:22
	 */
	public static long getLiunxTimestamp000() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis() / 1000;
	}

}
