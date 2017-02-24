package com.dataeye.omp.report.cmds;

import com.dataeye.omp.report.bean.Device;
import com.dataeye.omp.report.bean.Feature;
import com.dataeye.omp.report.dao.ReportSvrDao;
import com.dataeye.omp.report.dbproxy.kafka.KafkaProducerAgent;
import com.dataeye.omp.report.utils.Constant;
import com.dataeye.omp.report.utils.DateUtils;
import com.dataeye.omp.report.utils.ServerIDCache;
import com.dataeye.omp.report.utils.StackTraceLogger;
import com.xunlei.netty.httpserver.cmd.BaseCmd;
import com.xunlei.netty.httpserver.cmd.CmdMapper;
import com.xunlei.netty.httpserver.component.XLHttpRequest;
import com.xunlei.netty.httpserver.component.XLHttpResponse;
import com.xunlei.util.Log;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportSvrCmd extends BaseCmd {

    @Autowired
    private ReportSvrDao reportSvrDao;
    private final static Logger serverLog = Log.getLogger("server_log");
    private final static Logger testLog = Log.getLogger("test_log");

    /**
     * 特性批量上报
     * @param req
     * @param rsp
     * @return
     */
    @CmdMapper("/report/post")
    public Object reportSvrFeature(XLHttpRequest req
            , XLHttpResponse rsp) {
        try {
            rsp.addHeader("Cache-Control", "no-cache");
            String message = req.getContentString().trim();
            //发消息到kafka
            sendMessageToKafka(message);
            List<Feature> params = parseParams(message);
            reportSvrDao.saveData2Hbase(params);
        } catch (Exception e) {
            StackTraceLogger.error(serverLog, e);
            return "fail";
        }
        return "succ";
    }

    private List<Feature> parseParams(String message) {
        Feature params = Constant.gson.fromJson(message, Feature.class);
        List<Feature> paramlist = params.getFeature_list();
        Feature param = paramlist.get(0);
        if ("192.168.1.209".equals(param.getClient())) {
            testLog.info(message);
        }
        return paramlist;
    }


    private void sendMessageToKafka(String content) {
        KafkaProducerAgent
                .pushMassage2Kafka(content
                        , Constant.FEATURE_TOPIC);
    }

    /**
     * 服务器基础信息上报
     *
     * @param req
     * @param rsp
     * @return
     */
    @CmdMapper("/report/server")
    public Object serverReport(XLHttpRequest req
            , XLHttpResponse rsp) {
        String ip = null;
        int devId = 0;
        try {
            rsp.addHeader("Cache-Control", "no-cache");
            String content = req.getContentString().trim();
            Device device = Constant.gson.fromJson(content, Device.class);
            ip = device.getIp();

            if ("192.168.1.209".equals(ip)) {
                testLog.info(content);
            }

            devId = ServerIDCache.getServerId(ip);
            device.setId(devId);
            device.convertByte2KB();
            reportSvrDao.addServer(device);

        } catch (Exception e) {
            serverLog.error(DateUtils.currentTime()
                    + " server report error, ip is "
                    + ip + ",serverID is " + devId, e);

            return "fail";
        }
        return "succ";
    }
}
