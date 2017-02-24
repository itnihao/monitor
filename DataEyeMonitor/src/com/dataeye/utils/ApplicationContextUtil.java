package com.dataeye.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.dataeye.omp.constant.Constant.BeanName;
import com.xunlei.jdbc.JdbcTemplate;

/**
 * 
 * <pre>
 * 辅助类从spring容器里面直接取对象
 * @author Ivan          <br>
 * @date 2014-9-3 下午8:10:35 <br>
 * @version 1.0
 * <br>
 */
@Service
public class ApplicationContextUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;
	private static String[] allBeans;

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		ApplicationContextUtil.applicationContext = arg0;
		allBeans = arg0.getBeanDefinitionNames();
	}

	public static Object getBean(String bean) {
		return applicationContext.getBean(bean);
	}

	public static String[] getAllBeans() {
		return allBeans;
	}
	
	/**
	 * jdbcTemplateMonitor
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月7日 下午6:53:36
	 */
	public static JdbcTemplate getBeanJdbcTemplateMonitor() {
		Object obj = applicationContext.getBean(BeanName.JDBCTEMPLATE_DATAEYE_MONITOR);
		if (obj != null && obj instanceof JdbcTemplate) {
			return (JdbcTemplate) obj;
		}
		return null;
	}

	/**
	 * jdbcTemplateMonitorStat
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月7日 下午6:54:01
	 */
	public static JdbcTemplate getBeanJdbcTemplateMonitorStat() {
		Object obj = applicationContext.getBean(BeanName.JDBCTEMPLATE_DATAEYE_MONITOR_STAT);
		if (obj != null && obj instanceof JdbcTemplate) {
			return (JdbcTemplate) obj;
		}
		return null;
	}
}