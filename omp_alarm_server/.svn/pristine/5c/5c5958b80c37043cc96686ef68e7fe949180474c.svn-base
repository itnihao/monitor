package com.dataeye.omp.detect.kafka.consume;

import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

/**
 * 消费任务
 * @author chenfanglin
 * @date 2016年1月26日 下午6:04:05
 */
public class DetectTask implements Runnable {
	private KafkaStream<String, String> stream;

	public DetectTask(KafkaStream<String, String> stream) {
		this.stream = stream;
	}

	public void run() {
		for (MessageAndMetadata<String, String> kafkaMsg : stream) {
			// 入库和检测告警规则
//			DetectAlarmRuleHandlerSuper.getInstance().postOne(kafkaMsg.message());
		}
	}
}