package com.dataeye.monitor.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wendy
 * @since 2016/8/19
 */
public abstract class BaseTask implements Runnable {
    //protected static BatchReportHandler batchReportHandler = BatchReportHandler.getInstance();
    protected static Logger logger = LoggerFactory.getLogger("agent_log");
}
