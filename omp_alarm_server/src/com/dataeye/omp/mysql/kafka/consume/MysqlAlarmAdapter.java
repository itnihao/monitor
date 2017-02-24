package com.dataeye.omp.mysql.kafka.consume;

import com.dataeye.util.log.DELogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wendywang
 * @since 2016/4/4 13:30
 */
@Service
public class MysqlAlarmAdapter {

    private Logger logger = DELogger.getLogger("mysql_log");

    private static final int num_thread = 3;

    @Autowired
    private MysqlAlarmConsumer mysqlAlarmConsumer;

    public void handle() {
        logger.info("create mysqlConsume to handle");
        mysqlAlarmConsumer.consume(num_thread);
    }


    public void release() {
        if (null != mysqlAlarmConsumer) {
            mysqlAlarmConsumer.shutdown();
        }
        logger.info("ProcessAlarmAdapter release consumer");
    }
}
