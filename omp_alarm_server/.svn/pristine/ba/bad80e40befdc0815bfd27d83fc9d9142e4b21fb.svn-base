package com.dataeye.omp.jobs;


import com.dataeye.omp.alarm.server.AlarmServerUtils;
import com.dataeye.omp.common.AlarmMail;
import com.dataeye.omp.common.ResourceHandler;
import com.dataeye.omp.common.ServerInfo;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ServerBasicDetailAlarmTask {

    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplateMonitor;

    @Resource(name = "jdbcTemplateMonitorStat")
    private JdbcTemplate jdbcTemplateStat;

    @Autowired
    private AlarmServerUtils alarmUtil;

    public void run(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkDiskCount();
            }
        }, 0, 30 * 60 * 1000);
    }

    private void checkDiskCount() {
        final List<ServerInfo> list = new ArrayList<>();
        String sql_monitor = "select a.host_name,b.ip,a.disk_num from (select id,disk_num,host_name from " +
                "server_list) a left join server_ip_list b on a.id=b.svr_id where type=0";
        String sql_stat = "select disk_num from server_list where host_name=? order by hb_time desc limit 1";
        try {
            jdbcTemplateMonitor.query(sql_monitor, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    ServerInfo info = new ServerInfo();
                    String hostName = rs.getString(1);
                    String ip = rs.getString(2);
                    int diskNum = rs.getInt(3);
                    info.setHostName(hostName);
                    info.setDiskNum(diskNum);
                    info.setIp(ip);
                    list.add(info);
                }
            });

            for (ServerInfo info: list) {
                int diskNumCorrect = info.getDiskNum();
                int diskNumReport = jdbcTemplateStat.queryForInt(sql_stat, info.getHostName());
                if (diskNumCorrect != diskNumReport) {
                    //alarm
                    sendAlarmEmail(info, diskNumReport);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAlarmEmail(ServerInfo info, int currentDiskNum){
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        AlarmMail mail = new AlarmMail();
        String to = ResourceHandler.getProperties("alarm_omp");
        mail.setTo(to);
        mail.setSubject("【服务器基础信息告警】 磁盘数量 ["+ time +"]");
        mail.setContent("【时间】 : " + time + "\n" +
                "【主机】 : " + info.getHostName() + "\n" +
                "【内网】 : " + info.getIp() + "\n" +
                "标准磁盘数 : " + info.getDiskNum() + "; 当前磁盘数 : " + currentDiskNum);
        alarmUtil.sendAlarmMail(mail);
    }

}
