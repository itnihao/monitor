package com.dataeye.omp.alarm.kafka.consume;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.omp.utils.ServerUtils;
import com.dataeye.util.log.DELogger;

/**
 * CallBackMsgConsumer
 * 详细可以参考：https://cwiki.apache.org/confluence/display/KAFKA/Consumer+Group+Example
 * @author chenfanglin
 * @date 2016年1月26日 下午6:03:03
 */
@Service
public class AlarmConsumer {
	private final static Logger logger = DELogger.getLogger("kafka_Log");
	private final ConsumerConnector consumer;
	private final String topic;
	private ExecutorService executor;

	@Autowired
	private AlarmMailHandler alarmMailHandler;
	
	public AlarmConsumer() {
		ConsumerConfig config = createConsumerConfig();
		consumer = Consumer.createJavaConsumerConnector(config);
		topic = ServerUtils.getAlarmConsumerProperties().getProperty("kafka.event.callback.topic");
	}

	public void shutdown() {
		if (consumer != null) {
			consumer.shutdown();
		}
		if (executor != null) {
			executor.shutdown();
		}
	}

	public void consume(int numThreads) {
		logger.info("开始消费kafka邮件消息，topic:" + topic);
		Map<String, Integer> topicMap = new HashMap<String, Integer>();
		topicMap.put(topic, numThreads);

		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

		Map<String, List<KafkaStream<String, String>>> consumerMap =
				consumer.createMessageStreams(topicMap, keyDecoder, valueDecoder);
		List<KafkaStream<String, String>> streams = consumerMap.get(topic);

		executor = Executors.newFixedThreadPool(numThreads);

		for (final KafkaStream<String, String> stream : streams) {
			executor.submit(new Runnable() {
				public void run() {
					alarmMailHandler.handle(stream);
				}
			});
		}
	}

	private static ConsumerConfig createConsumerConfig() {
		Properties props = ServerUtils.getAlarmConsumerProperties();
		return new ConsumerConfig(props);
	}
}