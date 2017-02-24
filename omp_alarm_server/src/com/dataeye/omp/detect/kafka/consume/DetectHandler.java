package com.dataeye.omp.detect.kafka.consume;

import com.dataeye.util.log.DELogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 检测 特性数据 处理中心
 * @author chenfanglin
 * @date 2016年1月26日 下午6:02:32
 */
@Service
public class DetectHandler {

	private final static Logger logger = DELogger.getLogger("kafka_Log");

	private static final int numThreads = 6;
	@Autowired
	private DetectConsumer consumer;


	/**
	 * 处理回调
	 */
	public void handle() {
		logger.info("DetectHandler create consumer to handle");
		consumer.consume(numThreads);
	}

	/**
	 * 释放kafka消费者连接
	 * 
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午6:51:11
	 */
	public void release() {
		if (null != consumer) {
			consumer.shutdown();
		}
		logger.info("DetectHandler release consumer");
	}

}
