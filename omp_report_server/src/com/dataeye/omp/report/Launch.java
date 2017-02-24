package com.dataeye.omp.report;

import com.xunlei.netty.httpserver.Bootstrap;
import com.xunlei.netty.httpserver.util.HttpServerConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Launch {

	public static void main(String[] args)
			throws IOException, InterruptedException {
		Bootstrap.main(args, new Runnable() {
			@Override
			public void run() {

			}
		}, new Runnable() {
			@Override
			public void run() {
				HttpServerConfig.releaseExternalResources();
			}
		}, "classpath:applicationContext.xml");
	}
}
