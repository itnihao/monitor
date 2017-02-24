package com.dataeye.monitor.common.constants;

import com.dataeye.monitor.utils.CommonUtils;
import com.dataeye.monitor.utils.ResourceLoad;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

/**
 * 常量
 * Created by wendy on 2016/6/24.
 */
public class Constant {

    public static final Gson GSON = new Gson();
    public static final Gson GSON_ONLY_EXPOSE = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    public static final String BLANKSPACE = " ";
    public static final String COLON = ":";
    public static final String LINE = "\n";

    public static final String PUBLIC_NETWORK = "em1";
    public static final String PRIVATE_NETWORK = "em2";

    public static final String IP = CommonUtils.getLocalIp();
    public static final String HOSTNAME = CommonUtils.getLocalHostName();

    public static final String BLOCK_SIZE = "4096";

    public static final long FIFTEENSECONDS = 15 * 1000;

    public static final String FAIL = "fail";

    public static final String USER_DIR = System.getProperty("user.dir");

    public static final String CONF_DIR = USER_DIR + File.separator + "conf";

    public static ResourceLoad RESOURCE_LOAD = ResourceLoad.getInstance();

}


