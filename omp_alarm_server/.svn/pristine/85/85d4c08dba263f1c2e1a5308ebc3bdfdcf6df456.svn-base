package com.dataeye.omp.dbproxy.mysql;

import com.xunlei.netty.httpserver.Bootstrap;
import com.xunlei.netty.httpserver.spring.ConfigAnnotationBeanPostProcessor;
import org.slf4j.Logger;

import com.dataeye.util.log.DELogger;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.netty.httpserver.spring.BeanUtil;
import com.xunlei.netty.httpserver.spring.SpringBootstrap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 获取 jdbcTemplate
 * @author chenfanglin
 * @date 2016年1月27日 上午10:40:28
 */
public class MysqlUtil {



	private final static Logger logger = DELogger.getLogger("server_log");

	public static JdbcTemplate getJdbcTemplateMonitor() {
		JdbcTemplate jdbcTemplateMonitor = null;
		try {
			jdbcTemplateMonitor = BeanUtil.getTypedBean(SpringBootstrap.getContext(),
					"jdbcTemplateMonitor");
		} catch (Exception e) {
			logger.error("jdbcTemplate Exceptiong:" + e.getMessage());
		}

		if (null == jdbcTemplateMonitor) {
			logger.error("callBackUrl", "jdbcTemplateMonitor is null,return");
		}
		return jdbcTemplateMonitor;
	}
	
	public static JdbcTemplate getJdbcTemplateMonitorStat() {
		JdbcTemplate jdbcTemplateMonitor = null;
		try {
			jdbcTemplateMonitor = BeanUtil.getTypedBean(SpringBootstrap.getContext(),
					"jdbcTemplateMonitorStat");
		} catch (Exception e) {
			logger.error("jdbcTemplate Exceptiong:" + e.getMessage());
		}

		if (null == jdbcTemplateMonitor) {
			logger.error("callBackUrl", "jdbcTemplateMonitorStat is null,return");
		}
		return jdbcTemplateMonitor;
	}
}
