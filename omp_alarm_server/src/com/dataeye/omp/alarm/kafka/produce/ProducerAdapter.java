package com.dataeye.omp.alarm.kafka.produce;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kafka.producer.KeyedMessage;

import com.dataeye.omp.common.AlarmMail;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.utils.ServerUtils;

/**
 * 发送告警邮件到kafka， 适配器
 * @author chenfanglin
 * @date 2016年2月29日 下午7:35:45
 */
public class ProducerAdapter {
	public static String TOPIC;

	/**
	 * 发送告警邮件到kafka
	 * @param content
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午7:52:59
	 */
	public static void sendAlarmTOKafka(String to, String subject, String content){
		Properties properties = ServerUtils.getAlarmProducerProperties();
		TOPIC = properties.getProperty("kafka.event.topic");
        List<KeyedMessage<String, String>> list = new ArrayList<KeyedMessage<String,String>>();
        AlarmMail mail = new AlarmMail(to, subject, content);
        String jsonMail = Constant.GSON.toJson(mail);
        KeyedMessage<String, String> message = new KeyedMessage<String, String>(TOPIC, jsonMail);
        list.add(message);
        KafkaProducerAgent.send(list);
	}
}
