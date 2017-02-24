package com.dataeye.omp.report.utils;

import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import com.xunlei.netty.httpserver.spring.BeanUtil;
import com.xunlei.netty.httpserver.spring.SpringBootstrap;
import com.xunlei.util.Log;
import org.slf4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Created by wendy on 2016/5/23.
 */
public class ServerIDCache {

    private final static Logger serverLog = Log.getLogger("server_log");

    private static ConcurrentHashMap<String, Integer> serverIDCache =
            new ConcurrentHashMap<>();


    public static ConcurrentHashMap<String, Integer> getServerIDCache() {
        return serverIDCache;
    }

    static {
        loadServerIDCache();
    }

    private static void loadServerIDCache() {
        JdbcTemplate jdbcTemplateMonitor= BeanUtil.getTypedBean(SpringBootstrap.getContext(),
                "jdbcTemplateMonitor");

        String sql = "select ip,svr_id from server_ip_list";

        jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                serverIDCache.put(rs.getString("ip"), rs.getInt("svr_id"));
            }
        });
        serverLog.info("ServerIDs cacheSize = "+serverIDCache.size());
    }


    public static Integer getServerId(String ip) {
        Integer srvId = serverIDCache.get(ip);
        if (null == srvId) {
            srvId = getDevIdByServerIp(ip);
            if (srvId != -1) {
                addServerId(ip, srvId);
            }
        }
        return srvId;
    }

    public static int getDevIdByServerIp(String ip) {
        JdbcTemplate jdbcTemplateMonitor = BeanUtil.getTypedBean(
                SpringBootstrap.getContext(), "jdbcTemplateMonitor");

        try {
            String sql = "select svr_id from server_ip_list where ip= ? ";
            return jdbcTemplateMonitor.queryForInt(sql, ip);
        } catch (Exception e) {
            serverLog.error("query devId by ip error, ip=" + ip, e);
            throw new RuntimeException(e);
        }
    }

    public static void addServerId(String ip, Integer id) {
        serverIDCache.put(ip, id);
    }

}
