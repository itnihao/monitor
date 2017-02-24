package com.dataeye.omp.custom.monitor.consume;

import com.dataeye.util.log.DELogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wendy
 * @since 2016/3/17 11:37o
 */
@Service
public class CustomMonitorAdapter {
    private final static Logger logger = DELogger.getLogger("monitor_Log");

    private static final int numThreads = 6;
    @Autowired
    private CustomMonitorConsumer customComsumer;


    public void handler() {

        initConfig();
        logger.info("CustomMonitorConsumer create consumer to handle");
        customComsumer.consume(numThreads);
    }

    private void initConfig() {


    }

    public void release() {
        if (null != customComsumer) {
            customComsumer.shutdown();
        }
        logger.info("CustomMonitorConsumer release consumer");
    }
}
