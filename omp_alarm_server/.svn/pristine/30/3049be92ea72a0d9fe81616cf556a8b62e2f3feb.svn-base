package com.dataeye.omp.mysql.kafka.consume;

import com.google.gson.Gson;

import java.util.List;

/**
 * @author wendy
 * @since 2016/3/31 14:13
 */
public class MysqlMsg {

    private static Gson gson = new Gson();

    private List<MysqlMsg> feature_list;


    /**
     * 特性id
     */
    private int fid;

    /**
     * 监控的对象
     */
    private String object;

    /**mysql实例唯一标识*/
    private String server_id;

    /**端口*/
    private int port;

    /**
     * 所在的server内网IP
     */
    private String client;

    /**
     * 值
     */
    private String value;

    /**
     * 时间戳
     */
    private String time;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<MysqlMsg> getFeature_list() {
        return feature_list;
    }

    public void setFeature_list(List<MysqlMsg> feature_list) {
        this.feature_list = feature_list;
    }

    public static MysqlMsg parseJson(String content) {
        return gson.fromJson(content, MysqlMsg.class);
    }
}
