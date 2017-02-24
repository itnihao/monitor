package com.dataeye.omp.custom.monitor.consume;

import com.qq.jutil.string.StringUtil;

/**
 * 自定义监控项
 *
 * @author wendy
 * @since 2016/3/17 14:20
 */
public class CustomMonitorItem {

    private static final String SEPARATOR = ":";


    /**
     * 监控项
     */
    private String monitorItem;

    /**
     * 调用端
     */
    private String caller;

    /**
     * 接收端
     */
    private String receiver;

    /**
     * 扩展属性1
     */
    private String ext1;

    /**
     * 扩展属性2
     */
    private String ext2;

    /**
     * 耗时
     */
    private long cost;

    /**
     * 是否成功 0：成功  1：失败
     */
    private int isSucc;

    /**
     * 调用时间
     */
    private String time;

    /**
     * message   monitorItem:dc-monitor,caller:api,receiver:kafka,
     * ext1:null,ext2:null,cost:10,isSucc:1,
     * time:2016-03-19 16:39:03)
     *
     * @param message 一条监控消息
     */
    public CustomMonitorItem(String message) {
        String[] s = message.split(",");
        if (StringUtil.isNotEmpty(s[0])) {
            this.monitorItem = s[0].substring(s[0].indexOf(SEPARATOR) + 1).trim();
        }

        if (StringUtil.isNotEmpty(s[1])) {
            this.caller = s[1].substring(s[1].indexOf(SEPARATOR) + 1).trim();
        }

        if (StringUtil.isNotEmpty(s[2])) {
            this.receiver = s[2].substring(s[2].indexOf(SEPARATOR) + 1).trim();
        }

        if (StringUtil.isNotEmpty(s[3])) {
            this.ext1 = s[3].substring(s[3].indexOf(SEPARATOR) + 1).trim();
        }

        if (StringUtil.isNotEmpty(s[4])) {
            this.ext2 = s[4].substring(s[4].indexOf(SEPARATOR) + 1).trim();
        }

        if (StringUtil.isNotEmpty(s[5])) {
            this.cost = Integer.parseInt(s[5].split(SEPARATOR)[1]);
        }

        if (StringUtil.isNotEmpty(s[6])) {
            this.isSucc = Integer.parseInt(s[6].split(SEPARATOR)[1]);
        }

        if (StringUtil.isNotEmpty(s[7])) {
            this.time = s[7].substring(s[7].indexOf(SEPARATOR) + 1).trim();
        }
    }

    public String getMonitorItem() {
        return monitorItem;
    }


    public String getCaller() {
        return caller;
    }


    public String getReceiver() {
        return receiver;
    }


    public String getExt1() {
        return ext1;
    }


    public String getExt2() {
        return ext2;
    }


    public long getCost() {
        return cost;
    }


    public int getIsSucc() {
        return isSucc;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
