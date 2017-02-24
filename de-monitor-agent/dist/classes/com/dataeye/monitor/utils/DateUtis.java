package com.dataeye.monitor.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Created by wendy on 2016/6/24.
 */
public class DateUtis {

    private static final SimpleDateFormat SDF_YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String currentTime() {
        return SDF_YMDHMS.format(new Date());
    }

    public static void main(String[] args) {
        System.out.println(SDF_YMDHMS.format(new Date()));
    }

}
