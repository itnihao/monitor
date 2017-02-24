package com.dataeye.omp.alarm.kafka.produce;

import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;

import com.dataeye.omp.utils.ServerUtils;
import com.xunlei.util.Log;

/**
 * 发送上报数据到 kafka
 * @author chenfanglin
 * @date 2016年1月26日 下午5:02:14
 */
public class KafkaProducerAgent {
	private static Producer<String, String> producer = null;
	public static String topic;
 
	private final static Logger serverLog = Log.getLogger("server_log");
	
	static {
		init();
	}

	/**
	 * 防止外部构造
	 */
	private KafkaProducerAgent() {
	};

	/**
	 * 初始化
	 * 
	 * @author chenfanglin <br>
	 * @date 2016年1月26日 下午5:43:36
	 */
	private static void init() {
		Properties properties = ServerUtils.getAlarmProducerProperties();
		ProducerConfig config = new ProducerConfig(properties);
		producer = new Producer<String, String>(config);
		topic = properties.getProperty("kafka.event.topic");
		if(topic.equals("")){
			serverLog.error("miss kafka.event.topic, exit!!!");
			System.exit(-1);
		}
		serverLog.warn("KAFKA_TOPIC_CONFIG:\n"
					+"kafka.event.topic: " + topic  
				);
	}

	/**
	 * 发送消息
	 * @param list
	 * @author chenfanglin <br>
	 * @date 2016年1月26日 下午5:43:28
	 */
	public static void send(List<KeyedMessage<String,String>> list) {
		producer.send(list);
	}
}
