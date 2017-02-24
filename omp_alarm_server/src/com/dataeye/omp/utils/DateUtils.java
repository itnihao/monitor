package com.dataeye.omp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author chenfanglin
 * @date 2016年2月29日 下午6:20:19
 */
public class DateUtils {

	public static final int MINUTE = 60;

	public static int unixTimestamp() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static int unixTimestamp000() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return (int) (calendar.getTimeInMillis() / 1000);
	}

	/**
	 * 获取一天中的第几分钟
	 * @param time
	 * @return
	 */
	public static final SimpleDateFormat kk = new SimpleDateFormat("kk");
	public static final SimpleDateFormat mm = new SimpleDateFormat("mm");
	public static int getMinuteBetweenDay(String time){
		Date date = parseDate(time);
		int hour = Integer.parseInt(kk.format(date));
		int minute = Integer.parseInt(mm.format(date));
		return hour * 60 + minute;
	}


	private static final String s = "yyyy-MM-dd HH:mm:ss";
	public static Date parseDate(String time) {
		Date date = null;
		try {
			date = new SimpleDateFormat(s).parse(time);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return date;
	}

}
