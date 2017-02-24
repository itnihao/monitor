package com.dataeye.common;

import com.dataeye.jobs.CheckReportInfo;
import com.dataeye.jobs.SyncEmployeeInfo;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import com.dataeye.utils.DateUtils;

/**
 * 
 * <pre>
 * 定时任务
 * @author Ivan          <br>
 * @date 2015年3月7日 下午3:11:39 <br>
 * @version 1.0
 * <br>
 */
@Service
public class Jobs {
	/**
	 * 
	 * <pre>
	 * 开启所有的定时任务
	 *  @throws SchedulerException
	 *  @author Ivan<br>
	 *  @date 2014年12月23日 上午10:40:35
	 * <br>
	 */
	public static void start() throws SchedulerException {
		System.err.println(">>>>>>>>Quartz Job Start At " + DateUtils.now());
		//QuartzManager.addJob("loadConfig", DcConfigs.class, "0/5 * * * * ?");
		//QuartzManager.addJob("writetrackerlog", Tracker.class, "0/5 * * * * ?");
//		QuartzManager.addJob("ReportMachineInfo", ReportMachineInfo.class, "0 40 11 * * ?");
		//QuartzManager.addJob("SyncEmployeeInfo", SyncEmployeeInfo.class, "0 12 03 * * ?");
		QuartzManager.addJob("CheckReportInfo", CheckReportInfo.class, "0 0/10 * * * ?");
		QuartzManager.startJobs();
	}

	/**
	 * 
	 * <pre>
	 * 关闭所有的定时任务 
	 * @author Ivan<br>
	 * @date 2014年12月23日 上午10:40:45
	 * <br>
	 */
	public static void shutdown() {
		System.err.println(">>>>>>>>shuting down all jobs");
		QuartzManager.shutdownJobs();
	}

}
