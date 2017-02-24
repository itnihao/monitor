package com.dataeye.monitor.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * Created by wendy on 2016/6/24.
 */
public class CommonUtils {

    public static String getLocalIp(){
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ip = inetAddress.getHostAddress();
            return ip;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLocalHostName(){
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ip = inetAddress.getCanonicalHostName();
            return ip;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getLocalHostName());
    }


}
