package com.dataeye.omp.alarm.kafka.consume;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.util.log.DELogger;

/**
 * 消费kafka消息
 * @author chenfanglin
 * @date 2016年2月22日 下午3:30:07
 */
@Service
public class AlarmAdapter {
	private final static Logger logger = DELogger.getLogger("kafka_Log");

	private static final int numThreads = 3;
	@Autowired
	private AlarmConsumer consumer;

	/**
	 * 消费kafka
	 * @author chenfanglin <br>
	 * @date 2016年2月22日 下午3:28:44
	 */
	public void handle() {
		logger.info("AlarmHandler create consumer to handle");
		consumer.consume(numThreads);
	}

	public void release() {
		if (null != consumer) {
			consumer.shutdown();
		}
		logger.info("AlarmHandler release consumer");
	}
}
