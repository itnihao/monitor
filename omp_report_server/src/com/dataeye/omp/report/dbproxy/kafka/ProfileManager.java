package com.dataeye.omp.report.dbproxy.kafka;

import java.io.IOException;
import java.util.Properties;

public class ProfileManager {

	private static Properties properties;

	static {
		init();
	}

	public static void init() {
		properties = new Properties();
		try {
			properties.load(ClassLoader
					.getSystemResourceAsStream("stormkafka.properties"));
		} catch (IOException e) {
			System.err
					.println("read kafkaserver.properties in classpath error");
			e.printStackTrace();
		}
	}


	public static String get(String key) {
		return (String)properties.get(key);
	}

	public static Properties get() {
		return properties;
	}

	public static void main(String[] args) {



	}

}
