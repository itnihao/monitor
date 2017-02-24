package com.dataeye.monitor.common.constants;


import com.dataeye.monitor.utils.ResourceLoad;

import java.io.File;

/**
 * 上报接口
 * Created by wendy on 2016/7/4.
 */
public class ReportUrl {

        public static final String confPath =
                System.getProperty("user.dir") + File.separator + "conf"
                        + File.separator + "agent.properties";
        private static ResourceLoad resourceLoad;

        public static String serverFeaturUrl;

        public static String serverBasicUrl;
        public static String processCofigUrl;

        public static String processReportUrl;

        public static String mysqlReportUrl;

        public static String testUrl;

        public static String agentErrorUrl;

        public static String idcNetCollectDataHost;


        static {
                //        serverFeaturUrl=http://192.168.1.209:9000/report/post
                //        serverBasicUrl=http://192.168.1.209:9000/report/server
                //        processCofigUrl=http://192.168.1.209:9000/process/config
                //        processReportUrl=http://192.168.1.209:9000/process/report
                //        mysqlReportUrl=http://192.168.1.209:9000/report/mysql
                //        agentErrorUrl=http://192.168.1.209:9000/agent/exception
                //        testUrl=http://localhost:38888/test/test
                resourceLoad = ResourceLoad.getInstance();
                serverFeaturUrl = resourceLoad.getValue(confPath, "serverFeaturUrl");
                serverBasicUrl = resourceLoad.getValue(confPath, "serverBasicUrl");
                processCofigUrl = resourceLoad.getValue(confPath, "processCofigUrl");
                processReportUrl = resourceLoad.getValue(confPath, "processReportUrl");
                mysqlReportUrl = resourceLoad.getValue(confPath, "mysqlReportUrl");
                agentErrorUrl = resourceLoad.getValue(confPath, "agentErrorUrl");
                testUrl = resourceLoad.getValue(confPath, "testUrl");
                idcNetCollectDataHost = resourceLoad.getValue(confPath, "idcNetCollectDataHost");
        }
}
