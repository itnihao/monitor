package com.dataeye.omp.report.cmds;

import com.dataeye.omp.report.utils.Constant;
import com.xunlei.netty.httpserver.cmd.BaseCmd;
import com.xunlei.netty.httpserver.cmd.CmdMapper;
import com.xunlei.netty.httpserver.component.XLHttpRequest;
import com.xunlei.netty.httpserver.component.XLHttpResponse;
import com.xunlei.util.Log;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * @author wendy
 * @since 2016/8/4
 */
@Service
public class ExceptionMonitorCmd extends BaseCmd {
    private final static Logger logger = Log.getLogger("agenterror_log");
    @CmdMapper("/agent/exception")
    public Object agentExceptionMonitor(XLHttpRequest req
            , XLHttpResponse rsp) {
        String content = req.getContentString();
        Excep exception = Constant.gson.fromJson(content, Excep.class);
        String date = exception.getDate();
        String ip = exception.getIp();
        String hostName = exception.getHostName();
        String errorMessage = exception.getMessage();

        StringBuffer sb = new StringBuffer();
        sb.append("\ndate: ").append(date)
                .append("\nip: ").append(ip)
                .append("\nhostName：").append(hostName)
                .append("\nerrormessage: ").append(errorMessage);

        //1.记录日志
        logger.error(sb.toString());

        //2.发送告警
//        String alarmUrl = "http://alarm.dataeye.com/alarm/customize";
//        Map<String, String> parms = new HashMap<String, String>();
//        String alarmItem = "agent_exception";
//        String alarmObject = "机器监控agent异常";
//        String alarmSubject = "机器监控agent异常";
//        String alarmContent = sb.toString();

//        parms.put("alarmItem", alarmItem);
//        parms.put("subject", alarmSubject);
//        parms.put("alarmObject", alarmObject);
//        parms.put("content", alarmContent);
        // String alarmObject = "机器监控agent异常";

//        String alarmUrl = "http://alarm.dataeye.com/alarm/mail";
//        Map<String, String> parms = new HashMap<String, String>();
//        String to = "wendy@dataeye.com";
//        String alarmSubject = "机器监控agent异常";
//        String alarmContent = sb.toString();
//
//        parms.put("to", to);
//        parms.put("subject", alarmSubject);
//        parms.put("content", alarmContent);
//        HttpClientUtil.post(alarmUrl, parms);
        return "succ";
    }

    private class Excep {
        private String ip;
        private String date;
        private String message;
        private String hostName;

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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
