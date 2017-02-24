package com.dataeye.omp.report.utils;

import com.dataeye.util.log.DELogger;
import org.slf4j.Logger;

import java.nio.file.*;
import java.util.Vector;

public class FileWatcher {

	private final static Logger logger = DELogger.getLogger("file_watcher_log");

	private static String directory = "./";
	
	private static Runnable monitor = new Runnable() {

		@Override
		public void run() {
			try {
				WatchService watchService = FileSystems.getDefault()
						.newWatchService();
				Path path = Paths.get(directory);
				path.register(watchService,
						StandardWatchEventKinds.ENTRY_MODIFY,
						StandardWatchEventKinds.ENTRY_CREATE);

				while (true) {
					WatchKey key = watchService.take();
					for (WatchEvent<?> watchEvent : key.pollEvents()) {
						WatchEvent.Kind<?> kind = watchEvent.kind();
						Path file = (Path) watchEvent.context();
						logger.error("file {} changed", file.getFileName());
						notice(file.toFile().getName());
					}
					key.reset();
				}
			} catch (Exception t) {
				logger.error("watch file modifyed error", t);
			}
		}

	};

	private static Vector<FileUpdate> noticeList = new Vector<FileUpdate>();

	public static void registe(FileUpdate fileUpdate) {
		noticeList.add(fileUpdate);
	}

	private static void notice(String fileName) {
		if (noticeList != null) {
			for (FileUpdate fileUpdate : noticeList) {
				fileUpdate.fileChanged(fileName);
			}
		}
	}

	public static String getBaseDirectory() {
		return directory;
	}

	static{
		if(directory.equals("")){
			logger.error("configDirectory is emtpy, fileWatcher exit!!");
		}else{
			try {
				new Thread(monitor).start();
			} catch (Throwable t) {
				logger.error("monitor start error!", t);
			}
		}
	}
	
	/*
	public static synchronized FileWatcher getInstance(String directory){
		if(directory.equals("")){
			logger.error("directory is emtpy, get instance failed");
			return null;
		}
		
		if(!fileWatcherMap.containsKey(directory)){
			FileWatcher fileWatcher = new FileWatcher();
			fileWatcher.startWatch(directory);
			fileWatcherMap.put(directory, fileWatcher);
		}
		
		return fileWatcherMap.get(directory);
	}*/

}
