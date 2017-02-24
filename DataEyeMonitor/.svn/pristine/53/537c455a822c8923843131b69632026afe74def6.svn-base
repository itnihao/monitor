package com.dataeye.omp.module.jvm;

import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wendy on 2016/6/3.
 */
@Service
public class JvmMonitorDao {

    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplate;


    public List<JvmMonitorServer> getServerList() {
        String sql = "select sl.host_name, sp.ip from server_list sl left join server_ip_list sp " +
                "on sl.id=sp.svr_id where sp.type=0";
        final List<JvmMonitorServer> jvmMonitorServers = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                JvmMonitorServer server = new JvmMonitorServer();
                server.setHostName(rs.getString("host_name"));
                server.setIp(rs.getString("ip"));
                jvmMonitorServers.add(server);
            }
        });
        return jvmMonitorServers;
    }
}
