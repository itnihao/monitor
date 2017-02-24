package com.dataeye.omp.alarm.server;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.omp.common.AlarmMail;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.utils.ServerUtils;
import com.dataeye.util.log.DELogger;
import com.qq.jutil.string.StringUtil;
import com.xunlei.netty.httpserver.cmd.BaseCmd;
import com.xunlei.netty.httpserver.cmd.CmdMapper;
import com.xunlei.netty.httpserver.component.XLHttpRequest;
import com.xunlei.netty.httpserver.component.XLHttpResponse;

/**
 * 发送内部告警
 * @author chenfanglin
 * @date 2016年2月29日 下午5:38:06
 */
@Service
public class AlarmServerCmd extends BaseCmd{
	
	private final static Logger server_log = DELogger.getLogger("server_log");
	
	@Autowired
	private AlarmServerUtils alarmServerUtils;
	
	/**
	 * 发送告警邮件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午5:40:19
	 */
	@CmdMapper("/alarm/mail")
	public Object alarmMail(XLHttpRequest request, XLHttpResponse response) throws Exception {
		String to = request.getParameter("to", "");
		if (StringUtil.isEmpty(to)) {
			return "empty to parameter";
		}
		String subject = request.getParameter("subject", "");
		String content = request.getParameter("content", "");

		if (StringUtil.isEmpty(subject)
				|| StringUtil.isEmpty(content)) {
			return "empty subject,content parameters";
		}

		server_log.info("Receive One Messge:" + to + ":" + subject + ":" + content);
		AlarmMail mail = new AlarmMail(to, subject, content);
		alarmServerUtils.sendAlarmMail(mail);
		return true;
	}

	@CmdMapper("/alarm/splusMail")
	public Object alarmMailSPlus(XLHttpRequest request,
								 XLHttpResponse response) throws Exception {
		String to = request.getParameter("to", "");
		if (StringUtil.isEmpty(to)) {
			return "empty to parameter";
		}

		String subject = request.getParameter("subject", "");
		String content = request.getParameter("content", "");
		if (StringUtil.isEmpty(subject)) {
			return "empty subject";
		}

		if (StringUtil.isEmpty(content)) {
			return "empty content";
		}
		server_log.info("Receive One Messge:" + to + ":" + subject + ":" + content);
		AlarmMail mail = new AlarmMail(to, subject, content);
		alarmServerUtils.sendSplusAlarmMail(mail);
		return true;
	}
	
	/**
	 * 发送Html格式的邮件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月30日 下午6:45:19
	 */
	@CmdMapper("/alarm/minemail")
	public Object alarmMineMail(XLHttpRequest request, XLHttpResponse response) throws Exception {
		String to = request.getParameter("to", "");
		if (StringUtil.isEmpty(to)) {
			return "empty to parameter";
		}
		String subject = request.getParameter("subject", "");
		String content = request.getParameter("content", "");
		if (StringUtil.isEmpty(subject) || StringUtil.isEmpty(content)) {
			return "empty subject,content parameters";
		}
		server_log.info("Receive One Messge:" + to + ":" + subject + ":" + content);
		AlarmMail mail = new AlarmMail(to, subject, content);
		alarmServerUtils.sendAlarmMineMail(mail);
		return true;
	}
	
	/**
	 * 发送告警短信
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月1日 上午11:10:47
	 */
	@CmdMapper("/alarm/sms")
	public Object alarmSms(XLHttpRequest request, XLHttpResponse response) throws Exception {
		String to = request.getParameter("to", "");
		String content = request.getParameter("content", "");
		content = "【告警短信】" + StringUtil.URLDecodeUTF8(content);
		to = to.replaceAll(";|\\|", ",");
		if (content.getBytes("UTF-8").length > 300) {
			server_log.info(to + "\t" + content + "\t" + Constant.RTN_CONTENT_TOO_LONG);
			return Constant.RTN_CONTENT_TOO_LONG;
		}
		String s = alarmServerUtils.sendAlarmSMS(content, to);
		server_log.info(to + "\t" + content + "\t" + s);
		return s;
	}
	
	/**
	 * App告警
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年4月15日 下午6:23:43
	 */
	@CmdMapper("/alarm/app")
	public Object alarmapp(XLHttpRequest request, XLHttpResponse response) throws Exception {
		String to = request.getParameter("to", "");
		if (StringUtil.isEmpty(to)) {
			return "empty to parameter";
		}
		String subject = request.getParameter("subject", "");
		String content = request.getParameter("content", "");
		if (StringUtil.isEmpty(subject) || StringUtil.isEmpty(content)) {
			return "empty subject,content parameters";
		}
		ServerUtils.sendAppAlarm(to, subject, content);
		return "succ";
	}
	
}
