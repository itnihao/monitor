package com.dataeye.monitor.task;

import com.dataeye.monitor.common.constants.Constant;
import com.dataeye.monitor.common.constants.ReportUrl;
import com.dataeye.monitor.utils.DateUtis;
import com.dataeye.monitor.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wendy
 * @since 2016/8/4
 */
public class ExceptionHandler  {

    private static ExceptionHandler instance = new ExceptionHandler();
    private static Logger logger = LoggerFactory.getLogger("agent_log");

    private ExceptionHandler() {
    }

    public static String print(Throwable e) {
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out, true);
        if (e != null) {
            e.printStackTrace(pw);
        }
        pw.close();
        return out.toString();
    }

    /**
     * 异常信息统一发送上报agent，方便查问题，
     * 并且单独发送告警邮件(防止上报agent挂掉时，无法接收到异常告警,
     * 同时也能监控上报agent是否正常)
     *
     * @param message
     * @param error
     */
    public static void error(String message, Throwable error) {
        Excep excep = instance.new Excep();
        excep.setDate(DateUtis.currentTime());
        excep.setIp(Constant.IP);
        excep.setHostName(Constant.HOSTNAME);
        String errorMessage = message + " " + print(error);
        excep.setMessage(errorMessage);

        HttpClientUtil.post(ReportUrl.agentErrorUrl, Constant.GSON.toJson(excep));
//
//        StringBuffer sb = new StringBuffer();
//        sb.append("\ndate: ").append(DateUtis.currentTime())
//                .append("\nip: ").append(Constant.IP)
//                .append("\nhostName：").append(Constant.HOSTNAME)
//                .append("\nerrormessage: ").append(errorMessage);
//        sendEmail(sb.toString());
    }


    /**
     * 异常信息统一发送上报agent，方便查问题，
     * 并且单独发送告警邮件(防止上报agent挂掉时，无法接收到异常告警,
     * 同时也能监控上报agent是否正常)
     *
     * @param message
     * @param error
     */
    public static void error(String message, String error) {
        Excep excep = instance.new Excep();
        excep.setDate(DateUtis.currentTime());
        excep.setIp(Constant.IP);
        excep.setHostName(Constant.HOSTNAME);
        excep.setMessage(message + " " + error);
        HttpClientUtil.post(ReportUrl.agentErrorUrl, Constant.GSON.toJson(excep));

//        StringBuffer sb = new StringBuffer();
//        sb.append("\ndate: ").append(DateUtis.currentTime())
//                .append("\nip: ").append(Constant.IP)
//                .append("\nhostName：").append(Constant.HOSTNAME)
//                .append("\nerrormessage: ").append(message + " " + error);
//        sendEmail(sb.toString());
    }

    public static void sendEmail(String alarmContent) {
        String alarmUrl = "http://alarm.dataeye.com/alarm/mail";
        Map<String, String> parms = new HashMap<String, String>();
        String to = "wendy@dataeye.com";
        String alarmSubject = "机器监控agent异常";
        parms.put("to", to);
        parms.put("subject", alarmSubject);
        parms.put("content", alarmContent);
        HttpClientUtil.post(alarmUrl, parms);
    }

    private class Excep {
        private String ip;
        private String hostName;
        private String date;
        private String message;

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
