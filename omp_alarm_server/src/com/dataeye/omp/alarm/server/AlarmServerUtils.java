package com.dataeye.omp.alarm.server;

import com.dataeye.omp.common.AlarmMail;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.common.Constant.Rtn;
import com.dataeye.omp.common.Constant.SMSReturnXML;
import com.dataeye.omp.common.ResourceHandler;
import com.dataeye.omp.utils.ServerUtils;
import com.dataeye.util.log.DELogger;
import com.qq.jutil.string.StringUtil;
import com.xunlei.springutil.MailTemplate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.StringReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class AlarmServerUtils {

	private final static Logger mail_log = DELogger.getLogger("mail_log");

	@Resource(name = "mailTemplateAlarm")
	private MailTemplate mailTemplate;


	@Resource(name = "splusMailTemplateAlarm")
	private MailTemplate splusMailTemplate;

	/**
	 * 发送告警邮件
	 * 
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年2月24日 下午2:18:24
	 */
	/**
	 * 发送告警邮件
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年2月24日 下午2:18:24
	 */
	public void sendAlarmMail(AlarmMail currentMail){
		// 发送失败了，重试三次
		int sendTimes = 0;
		while (sendTimes < 3) {
			boolean result = false;
			try {
				//收件人去重
				String[] arr = currentMail.getTo().split(",");
				Set<String> set = new HashSet<String>();

				for (String s : arr) {
					if (s == null || "null".equals(s)) {
						continue;
					}
					set.add(s);
				}
				String[] to = set.toArray(new String[set.size()]);
				String s = ArrayUtils.toString(to);
				currentMail.setTo(s.substring(1, s.length() - 1));

				result = mailTemplate.sendTextMail(to, currentMail.getSubject()
								+ "[" + Constant.BASIC_TIME_FORMAT.format(new Date()) + "]"
						, currentMail.getContent());

			} catch (Throwable e) {
				mail_log.error("send mail error", e);
			}

			if (result) {
				mail_log.info("mail to {}->subject={}->content={}->result={}", currentMail.getTo(),
						currentMail.getSubject(), currentMail.getContent(), result);
				break;
			} else {
				sendTimes++;
			}
		}
		if (sendTimes == 3) {
			// 发送失败，记录日志
			mail_log.error("mail to {}->subject={}->content={}", currentMail.getTo(),
					currentMail.getSubject(), currentMail.getContent());
			String smsto = ResourceHandler.getProperties("alarm_internal_group");
			String smscontent = ResourceHandler.getProperties("alarm_sms_key") + "【告警邮件发送失败】[收件人]:"+currentMail.getTo()
					+ "\n[主题]:"+currentMail.getSubject() + "\n[内容]:"+currentMail.getContent();
			ServerUtils.sendAppAlarm(smsto, "邮件告警发送失败", smscontent);
		}
	}

	/**
	 * 发送Html格式告警邮件
	 * 
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年2月24日 下午2:18:24
	 */
	public void sendAlarmMineMail(AlarmMail currentMail) {
		// 发送失败了，重试三次
		int sendTimes = 0;
		while (sendTimes < 3) {
			boolean result = mailTemplate.sendMimeMail(currentMail.getTo().split(","), currentMail.getSubject() + "["
					+ Constant.BASIC_TIME_FORMAT.format(new Date()) + "]", currentMail.getContent());
			if (result) {
				mail_log.info("mail to {}->subject={}->content={}->result={}", currentMail.getTo(),
						currentMail.getSubject(), currentMail.getContent(), result);
				break;
			} else {
				sendTimes++;
			}
		}
		if (sendTimes == 3) {
			// 发送失败，记录日志
			mail_log.error("mail to {}->subject={}->content={}", currentMail.getTo(),
					currentMail.getSubject(), currentMail.getContent());

			String smsto = ResourceHandler.getProperties("alarm_internal_group");
			String smscontent = ResourceHandler.getProperties("alarm_sms_key") + "【告警邮件发送失败】[收件人]:"
					+ currentMail.getTo() + "\n[主题]:" + currentMail.getSubject() + "\n[内容]:" + currentMail.getContent();
			ServerUtils.sendAppAlarm(smsto, "邮件告警发送失败", smscontent);
		}
	}

	/**
	 * 发送告警短信
	 * 
	 * @param content
	 * @param to
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月1日 上午11:02:23
	 */
	public String sendAlarmSMS(String content, String to) {
		String s = "http://118.145.18.236:9999/sms.aspx?"
				+ "action=send&userid=2030&account=zgjzxc&password=12345678&mobile=" + to + "&content="
				+ StringUtil.URLEncodeUTF8(content);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(s);
		HttpResponse response;
		String xml = null;
		Rtn rtn = new Rtn();
		try {
			response = httpclient.execute(httppost);
			xml = EntityUtils.toString(response.getEntity());
			SMSReturnXML smsReturnXML = parseReturnXML(xml);
			rtn.rtn = 0;
			rtn.desc = "succss";
			rtn.xml = smsReturnXML;
		} catch (DocumentException e) {
			mail_log.error("返回的xml有异常" + xml, e);
			rtn.rtn = -2;
			rtn.desc = "请求URL" + s + ",错误的返回:" + xml;
		} catch (Exception e) {
			mail_log.error("请求异常" + xml, e);
			rtn.rtn = -1;
			rtn.desc = "请求URL" + s + ",错误的返回:" + xml;
		}
		return Constant.GSON.toJson(rtn);
	}

	public SMSReturnXML parseReturnXML(String xml) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(new StringReader(xml));
		Element root = document.getRootElement();
		SMSReturnXML smsReturnXML = new SMSReturnXML();
		smsReturnXML.returnstatus = root.elementText("returnstatus");
		smsReturnXML.message = root.elementText("message");
		smsReturnXML.remainpoint = root.elementText("remainpoint");
		smsReturnXML.taskID = root.elementText("taskID");
		smsReturnXML.successCounts = root.elementText("successCounts");
		return smsReturnXML;
	}

	public void sendSplusAlarmMail(AlarmMail mail) {
// 发送失败了，重试三次
		int sendTimes = 0;
		while (sendTimes < 3) {
			boolean result = false;
			try {
				//收件人去重
				String[] arr = mail.getTo().split(",");
				Set<String> set = new HashSet<String>();

				for (String s : arr) {
					if (s == null || "null".equals(s)) {
						continue;
					}
					set.add(s);
				}
				String[] to = set.toArray(new String[set.size()]);
				String s = ArrayUtils.toString(to);
				mail.setTo(s.substring(1, s.length() - 1));
				result = splusMailTemplate.sendTextMail(to, mail.getSubject()
						, mail.getContent());
			} catch (Throwable e) {
				mail_log.error("send splus mail error", e);
			}

			if (result) {
				mail_log.info("mail to {}->subject={}->content={}->result={}", mail.getTo(),
						mail.getSubject(), mail.getContent(), result);
				break;
			} else {
				sendTimes++;
			}
		}
		if (sendTimes == 3) {
			// 发送失败，记录日志
			mail_log.error("mail to {}->subject={}->content={} failed", mail.getTo(),
					mail.getSubject(), mail.getContent());
		}

	}
}
