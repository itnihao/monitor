package com.dataeye.omp.process.kafka.consume;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.util.log.DELogger;

@Service
public class ProcessAlarmAdapter {
	private final static Logger logger = DELogger.getLogger("process_Log");

	private static final int numThreads = 2;
	@Autowired
	private ProcessAlarmConsumer processAlarmConsumer;

	/**
	 * 消费kafka
	 * @author chenfanglin <br>
	 * @date 2016年2月22日 下午3:28:44
	 */
	public void handle() {
		logger.info("ProcessAlarmAdapter create consumer to handle");
		processAlarmConsumer.consume(numThreads);
	}

	public void release() {
		if (null != processAlarmConsumer) {
			processAlarmConsumer.shutdown();
		}
		logger.info("ProcessAlarmAdapter release consumer");
	}
	
}
