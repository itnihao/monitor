package com.dataeye.common;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Vector;

import com.dataeye.omp.constant.Constant.Resource;
import com.dataeye.utils.FileUtils;
import com.digitcube.dbproxyclient.util.FileUpdate;

/**
 * 
 * <pre>
 * 文件监控器
 * @author Ivan          <br>
 * @date 2015年1月8日 下午4:05:13 <br>
 * @version 1.0
 * <br>
 */
public class FileWatcher {
	/** 是否需要关闭 */
	private static volatile boolean stop;
	/** 保存当前线程 */
	private static Thread currentThread;

	private final static Runnable monitor = new Runnable() {
		@Override
		public void run() {
			currentThread = Thread.currentThread();
			try {
				WatchService watchService = FileSystems.getDefault().newWatchService();
				// 监控classpath:resources目录
				Path path = Paths.get(FileUtils.getPathOfClasspathResource(Resource.DIR));
				path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);
				while (!stop) {
					WatchKey key = watchService.take();
					for (WatchEvent<?> watchEvent : key.pollEvents()) {
						Path file = (Path) watchEvent.context();
						notice(file.toFile().getName());
					}
					key.reset();
				}
			} catch (Exception t) {
				t.printStackTrace();
			}
		}

	};

	private static Vector<FileUpdate> noticeList = new Vector<FileUpdate>();

	public static void registe(FileUpdate fileUpdate) {
		noticeList.add(fileUpdate);
	}

	private synchronized static void notice(String fileName) {
		if (noticeList != null) {
			for (FileUpdate fileUpdate : noticeList) {
				fileUpdate.fileChanged(fileName);
			}
		}
	}

	/**
	 * 开启当前线程
	 */
	static {
		try {
			Thread thread = new Thread(monitor);
			thread.start();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * 
	 * <pre>
	 * 关闭当前线程
	 * @author Ivan<br>
	 * @date 2015年1月8日 下午4:37:10
	 * <br>
	 */
	public static void shutdown() {
		stop = true;
		if (currentThread != null) {
			currentThread.interrupt();
		}

	}
}
