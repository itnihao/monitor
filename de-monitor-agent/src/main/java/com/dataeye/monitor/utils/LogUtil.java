package com.dataeye.monitor.utils;


import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.dataeye.monitor.common.constants.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LogUtil {

    private static final Logger logger;

    static {

        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        loggerContext.reset();

        try {
            configurator.doConfigure(new File(Constant.CONF_DIR + File.separator + "logback.xml"));
        } catch (JoranException e) {
            throw new RuntimeException("load logback config failed, you need restart server", e);
        } finally {
            logger = LoggerFactory.getLogger("agent_log");
        }

    }

    public static Logger getLogger(String name){
        return logger;
    }

    public static void main(String[] args) {
        System.out.println(Constant.CONF_DIR + File.separator + "logback.xml");

    }
}
