package com.dataeye.omp.event;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.dataeye.omp.custom.monitor.consume.CustomMonitorAdapter;
import com.dataeye.omp.jobs.ServerBasicDetailAlarmTask;
import com.dataeye.omp.mysql.kafka.consume.MysqlAlarmAdapter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.omp.alarm.kafka.consume.AlarmAdapter;
import com.dataeye.omp.common.ResourceHandler;
import com.dataeye.omp.detect.kafka.consume.DetectHandler;
import com.dataeye.omp.jobs.AlarmMailTask;
import com.dataeye.omp.jobs.CustomAlarmRestoreTask;
import com.dataeye.omp.process.kafka.consume.ProcessAlarmAdapter;
import com.dataeye.omp.utils.DateUtils;
import com.xunlei.netty.httpserver.Bootstrap;
import com.xunlei.netty.httpserver.spring.BeanUtil;
import com.xunlei.netty.httpserver.spring.SpringBootstrap;
import com.xunlei.netty.httpserver.util.HttpServerConfig;
import com.xunlei.util.Log;

/**
 * 消费者，获取kafka消息入口
 * @author chenfanglin
 * @date 2016年1月26日 下午6:00:28
 */
@Service
public class Launch {

	private final static Logger serverLog = Log.getLogger("server_log");

	public static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(4);

	@Autowired
	private DetectHandler detectHandler;
	
	@Autowired
	private AlarmAdapter alarmHandler;
	
	@Autowired
	private AlarmMailTask alarmMailTask;
	
	@Autowired
	private CustomAlarmRestoreTask customAlarmRestoreTask;
	
	@Autowired
	private ProcessAlarmAdapter processAlarmAdapter;

	@Autowired
	private MysqlAlarmAdapter mysqlAlarmAdapter;

	@Autowired
	private ServerBasicDetailAlarmTask serverBasicDetailAlarmTask;

	@Autowired
	private CustomMonitorAdapter customMonitorAdapter;


	/**
	 * 初始化
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午6:43:42
	 */
	public void init() {
		serverLog.info("omp_alarm_server started at :" + DateUtils.unixTimestamp());
		alarmMailTask.alarmMailTask();
		customAlarmRestoreTask.customAlarmRestoreTask();
//		serverBasicDetailAlarmTask.run();
		 //获取kafka消息
		detectHandler.handle();
		alarmHandler.handle();
		processAlarmAdapter.handle();
		mysqlAlarmAdapter.handle();

		// 加载配置文件
		ResourceHandler.init();

		customMonitorAdapter.handler();
	}

	/**
	 * 停止服务
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午6:43:52
	 */
	public void stop() {
		detectHandler.release();
		mysqlAlarmAdapter.release();
		serverLog.info("omp_alarm_server stop at :" + DateUtils.unixTimestamp());
		customMonitorAdapter.release();
	}

	public static void main(String[] args) throws IOException {
		Bootstrap.main(args, new Runnable() {

			@Override
			public void run() {
				// 定时重加载配置
				Launch launch = BeanUtil.getTypedBean(SpringBootstrap.getContext(), "launch");
				launch.init();
			}
		}, new Runnable() {
			@Override
			public void run() {
				Launch launch = BeanUtil.getTypedBean(SpringBootstrap.getContext(), "launch");
				launch.stop();
				// 在关闭httpSever时,走安全关闭的步骤时,须关闭内部的boss线程跟worker线程
				// 可能关闭会很慢
				HttpServerConfig.releaseExternalResources();
			}
		}, "classpath:applicationContext.xml");
	}
}
