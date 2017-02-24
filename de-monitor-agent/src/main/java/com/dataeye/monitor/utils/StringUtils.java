package com.dataeye.monitor.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by wendy on 2016/6/2.
 */
public class StringUtils {

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static long[] strArr2longArr(String[] arr) {
        long[] arr1 = new long[arr.length];
        for (int j = 0; j < arr.length; j++) {
            arr1[j] = Long.parseLong(arr[j]);
        }
        return arr1;
    }


    public static String replaceMultiBlankSpace(String line) {
        Pattern p = Pattern.compile("\\s+");
        Matcher m = p.matcher(line.trim());
        line = m.replaceAll(" ");
        return line;
    }


}
