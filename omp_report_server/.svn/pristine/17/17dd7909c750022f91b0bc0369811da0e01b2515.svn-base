package com.dataeye.omp.report.dbproxy.kafka;

import com.xunlei.util.Log;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;

import java.util.List;
import java.util.Properties;

/**
 * 发送上报数据到 kafka
 * @author chenfanglin
 * @date 2016年1月26日 下午5:02:14
 */
public class KafkaProducerAgent {
	private static Producer<String, String> producer = null;
	public static String topic;

	private final static Logger kafka_log
			= Log.getLogger("kafka_log");
	
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
		Properties properties = ProfileManager.get();
		ProducerConfig config = new ProducerConfig(properties);
		producer = new Producer<String, String>(config);
	}

	/**
	 * 发送消息到kafka
	 *
	 * @param content
	 * @author chenfanglin <br>
	 * @date 2016年1月26日 下午5:44:12
	 */
	public static void pushMassage2Kafka(String content
			, String topic) {
		topic = ProfileManager.get(topic);
		KeyedMessage<String, String> message
				= new KeyedMessage<String, String>(topic, content);
		producer.send(message);
	}
}
