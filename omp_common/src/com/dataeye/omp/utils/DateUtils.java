package com.dataeye.omp.utils;

import java.util.Calendar;
import java.util.Date;

import com.dataeye.omp.common.Constant;

/**
 * 日期工具类
 * @author chenfanglin
 * @date 2016年2月29日 下午6:20:19
 */
public class DateUtils {

	public static final int MINUTE = 60;

	/**
	 * 当前linux时间戳
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:27:12
	 */
	public static int unixTimestamp() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	/**
	 * 当天零点时间戳
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:27:31
	 */
	public static int unixTimestamp000() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return (int) (calendar.getTimeInMillis() / 1000);
	}
	
	/**
	 * 得到yyyy-MM-dd HH:mm:ss的时间
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:26:22
	 */
	public static final String now() {
		return Constant.SDF_NOW.format(new Date());
	}
}
