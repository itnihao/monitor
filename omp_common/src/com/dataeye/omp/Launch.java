package com.dataeye.omp;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.omp.sso.SsoHandler;
import com.xunlei.netty.httpserver.Bootstrap;
import com.xunlei.netty.httpserver.spring.BeanUtil;
import com.xunlei.netty.httpserver.spring.SpringBootstrap;
import com.xunlei.netty.httpserver.util.HttpServerConfig;

/**
 * 
 * @author chenfanglin
 * @date 2016年1月26日 下午6:00:28
 */
@Service
public class Launch {

	public static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(4);
	@Autowired
	public SsoHandler ssoHandler;
	
	/**
	 * 初始化
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午6:43:42
	 */
	public void init() {
		ssoHandler.init();
		ssoHandler.start();
	}

	/**
	 * 停止服务
	 * @author chenfanglin <br>
	 * @date 2016年2月29日 下午6:43:52
	 */
	public void stop() {
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
