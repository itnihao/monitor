package com.dataeye.common;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * 
 * <pre>
 * 容器启动以后执行
 * @author Ivan          <br>
 * @date 2015年4月3日 下午3:45:46 <br>
 * @version 1.0
 * <br>
 */
@Service
public class DeApplicationListener implements ApplicationListener {
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// 容器启动完成以后，开启所有的定时任务
		if (event instanceof ContextRefreshedEvent) {

			ContextRefreshedEvent refreshedEvent = (ContextRefreshedEvent) event;
			// root application context
			if (refreshedEvent.getApplicationContext().getParent() != null) {
				try {
					PrivilegeHandler.load();
					// 开启Quartz定时任务
					Jobs.start();
					// 初始化语言文件
					ResourceHandler.init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// 容器关闭的时候，把相关的定时任务也关闭
		if (event instanceof ContextClosedEvent) {
			try {
				// 定时任务
				Jobs.shutdown();
				FileWatcher.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
