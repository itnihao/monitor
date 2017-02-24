package com.dataeye.omp.module.jvm;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * jvm 监控服务器列表
 * Created by wendy on 2016/6/6.
 */
public class JvmMonitorServer {

    @Expose
    private String hostName;

    @Expose
    private String ip;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public static final Comparator<JvmMonitorServer> HOST_NAME_ASC = new Comparator<JvmMonitorServer>() {
        @Override
        public int compare(JvmMonitorServer o1, JvmMonitorServer o2) {

            String hostName1 = o1.getHostName() == null ? "" : o1.getHostName();

            String hostName2 = o2.getHostName() == null ? "" : o2.getHostName();

            return hostName1.compareTo(hostName2);
        }
    };

    public static final Comparator<JvmMonitorServer> HOST_NAME_DESC = new Comparator<JvmMonitorServer>() {
        @Override
        public int compare(JvmMonitorServer o1, JvmMonitorServer o2) {

            String hostName1 = o1.getHostName() == null ? "" : o1.getHostName();

            String hostName2 = o2.getHostName() == null ? "" : o2.getHostName();

            return hostName2.compareTo(hostName1);
        }
    };

    public static final Comparator<JvmMonitorServer> IP_ASC = new Comparator<JvmMonitorServer>() {
        @Override
        public int compare(JvmMonitorServer o1, JvmMonitorServer o2) {
            String ip1 = o1.getIp() == null ? "" : o1.getIp();
            String ip2 = o2.getIp() == null ? "" : o2.getIp();

            return ip1.compareTo(ip2);
        }
    };

    public static final Comparator<JvmMonitorServer> IP_DESC = new Comparator<JvmMonitorServer>() {
        @Override
        public int compare(JvmMonitorServer o1, JvmMonitorServer o2) {

            String ip1 = o1.getIp() == null ? "" : o1.getIp();
            String ip2 = o2.getIp() == null ? "" : o2.getIp();
            return ip2.compareTo(ip1);
        }
    };

    public static void main(String[] args) {
        JvmMonitorServer server = new JvmMonitorServer();
        server.setIp("10.1.2.1");
        server.setHostName("host1");

        JvmMonitorServer server1=new JvmMonitorServer();
        server1.setHostName("host2");
        server1.setIp("10.1.2.1");
        List<JvmMonitorServer> s = new ArrayList<>();
        s.add(server);
        s.add(server1);
        Collections.sort(s, HOST_NAME_ASC);

        for (JvmMonitorServer jvmMonitorServer : s) {
            System.out.println(jvmMonitorServer.getHostName());

        }
    }
}
