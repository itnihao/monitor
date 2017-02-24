package com.dataeye.omp.alarm.kafka.consume;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.omp.alarm.server.AlarmServerUtils;
import com.dataeye.omp.common.AlarmMail;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.utils.DateUtils;
import com.dataeye.util.log.DELogger;
import com.xunlei.jdbc.JdbcTemplate;

/**
 * 告警邮件处理中心
 * @author chenfanglin
 * @date 2016年3月1日 上午10:25:51
 */
@Service
public class AlarmMailHandler {

	private final static Logger logger = DELogger.getLogger("mail_log");
	
	@Resource(name = "jdbcTemplateDcBusinessUser")
	private JdbcTemplate jdbcTemplateDcBusinessUser;
	
	@Resource(name = "jdbcTemplateMonitor")
	private JdbcTemplate jdbcTemplateMonitor;
	
	@Autowired
	private AlarmServerUtils alarmServerUtils;
	
	private static ExecutorService executor = Executors.newFixedThreadPool(10);
	/**
	 * 处理告警邮件
	 * @param callBack
	 * @author chenfanglin <br>
	 * @date 2016年2月22日 下午4:11:57
	 */
	public void handle(KafkaStream<String, String> stream) {
		try {
			for (MessageAndMetadata<String, String> kafkaMsg : stream) {
				logger.info("partition:" + kafkaMsg.partition() + " offset:" + kafkaMsg.offset());
				logger.info("邮件数据："+kafkaMsg.message());
				final AlarmMail mail = Constant.GSON.fromJson(kafkaMsg.message(), AlarmMail.class);
				String content = mail.getContent();
				// 检查是否需要过滤
				String sql2 = "select keyword from dc_alarm_exclude";
				List<String> list = jdbcTemplateDcBusinessUser.queryForList(sql2, String.class);
				boolean contains = false;
				for (String keyword : list) {
					if (content.contains(keyword)) {
						contains = true;
						logger.error("{}->{}->{}", "exclude", content, keyword);
						break;
					}
				}
				if (contains) {
					continue;
				}
				executor.submit(new Runnable() {
					public void run() {
						String sql = "insert into " + Constant.DC_ALARM_MAIL + "(addressee,subject,content,createTime) values(?,?,?,?)";
						jdbcTemplateMonitor.insert(sql, mail.getTo(),mail.getSubject(),mail.getContent(),DateUtils.unixTimestamp());
					}
				});
//				alarmServerUtils.sendAlarmMail(mail);
			}
		} catch (Exception e) {
			logger.error("【异常】邮件发送异常："+e.getMessage());
			e.printStackTrace();
		}
	}
	
}
