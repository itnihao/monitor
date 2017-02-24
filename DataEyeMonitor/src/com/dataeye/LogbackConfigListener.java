package com.dataeye;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * <pre>
 * 初始化logback
 * @author Ivan          <br>
 * @date 2014年10月24日 下午5:18:34 <br>
 * @version 1.0
 * <br>
 */
public class LogbackConfigListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {
		LogbackWebConfigurer.initLogging(event.getServletContext());
	}

	public void contextDestroyed(ServletContextEvent event) {
		LogbackWebConfigurer.shutdownLogging(event.getServletContext());
	}
}
