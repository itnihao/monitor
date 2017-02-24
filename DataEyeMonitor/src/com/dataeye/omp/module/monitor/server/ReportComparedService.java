package com.dataeye.omp.module.monitor.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.DEResponse;
import com.dataeye.omp.constant.Constant.Table;

/**
 * 对比图
 * @author chenfanglin
 * @date 2016年1月20日 上午10:38:30
 */
@Service
public class ReportComparedService {

	@Autowired 
	private ReportComparedDAO reportComparedDAO;
	
	/**
	 * 10 CPU 总使用率对比图
	 * 11 CPU 1分钟负载
	 * 12 CPU 5分钟负载
	 * 13 CPU 15分钟负载
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param lang
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 上午10:49:07
	 */
	public DEResponse getComparedCpuUsage(int serverID, String startdate, String enddate, String starttime,
			String endtime, int featrueID, String lang) throws ServerException{
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportComparedDAO.getComparedCpuUsageMinute(serverID, startdate,starttime, tableName,featrueID,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportComparedDAO.getComparedCpuUsageHour(serverID, startdate, enddate,starttime,endtime, tableName,featrueID,lang);
		}
	}

	/**
	 * 内存使用率
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param featrueID
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月20日 下午4:59:35
	 */
	public DEResponse getComparedMemUsage(int serverID, String startdate, String enddate, String starttime,
			String endtime, int featrueID, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportComparedDAO.getComparedMemUsageMinute(serverID, startdate,starttime, tableName,featrueID,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportComparedDAO.getComparedMemUsageHour(serverID, startdate, enddate,starttime,endtime, tableName,featrueID,lang);
		}
	}

	/**
	 * 内存相关
	 * 21 mem_used 内存使用率
	 * 22 mem_pri Private内存
	 * 23 mem_vir Virtual内存
	 * 24         Private+IPCS
	 * 25 mem_swap_used SWAP内存使用量
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月20日 下午5:28:40
	 */
	public DEResponse getComparedMemUsageAmount(int serverID, String startdate, String enddate, String starttime,
			String endtime, int featrueID, String object, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportComparedDAO.getComparedMemUsageAmountMinute(serverID, startdate,starttime, tableName,featrueID,object,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportComparedDAO.getComparedMemUsageAmountHour(serverID, startdate, enddate,starttime,endtime, tableName,featrueID,object,lang);
		}
	}

	/**
	 * 内存相关- SWAP内存使用率， 使用以下特性计算 SWAP使用大小/SWAP总大小
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月20日 下午5:44:46
	 */
	public DEResponse getComparedMemSwapUsage(int serverID, String startdate, String enddate, String starttime,
			String endtime, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportComparedDAO.getComparedMemSwapUsageMinute(serverID, startdate,starttime, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportComparedDAO.getComparedMemSwapUsageHour(serverID, startdate, enddate,starttime,endtime, tableName,lang);
		}
	}

	/**
	 * 网络相关
	 * 50 em2内网网卡出流量
	 * 51 em2内网网卡入流量
	 * 50 em1外网网卡出流量
	 * 51 em1 外网网卡入流量
	 * 52 em2内网网卡出包量
	 * 53 em2内网网卡入包量
	 * 52 em1外网网卡出包量
	 * 53 em1外网网卡入包量
	 * 54 被动打开TCP连接数
	 * 55TCP连接数
	 * 56UDP接收数据报
	 * 57UDP发送数据报
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月20日 下午7:03:09
	 */
	public DEResponse getComparedNetworkUsage(int serverID, String startdate, String enddate, String starttime,
			String endtime, int featrueID, String object, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportComparedDAO.getComparedNetworkUsageMinute(serverID, startdate,starttime, tableName,featrueID,object,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportComparedDAO.getComparedNetworkUsageHour(serverID, startdate, enddate,starttime,endtime, tableName,featrueID,object,lang);
		}
	}

	/**
	 * IO相关-磁盘分区使用率
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月20日 下午7:34:53
	 */
	public DEResponse getComparedIOPartitionUsage(int serverID, String startdate, String enddate, String starttime,
			String endtime, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportComparedDAO.getComparedIOPartitionUsageMinute(serverID, startdate,starttime, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportComparedDAO.getComparedIOPartitionUsageHour(serverID, startdate, enddate,starttime,endtime, tableName,lang);
		}
	}

	/**
	 * IO相关-磁盘分区使用量
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月20日 下午7:34:56
	 */
	public DEResponse getComparedIOPartitionAmount(int serverID, String startdate, String enddate, String starttime,
			String endtime, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportComparedDAO.getComparedIOPartitionAmountMinute(serverID, startdate,starttime, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportComparedDAO.getComparedIOPartitionAmountHour(serverID, startdate, enddate,starttime,endtime, tableName,lang);
		}
	}

	/**
	 * IO相关-
 	 *featrueID=31&object=DISK IO IN
	 *featrueID=32&object=DISK IO OUT
	 *featrueID=33&object=SWAP SI
	 *featrueID=34&object=SWAP SO
	 *featrueID=35&object=’Svctm_time_max’
	 *featrueID=36&object=’Await_time_max’
	 *featrueID=37&object=’avgqu_sz_max’
	 *featrueID=38&object=’avgrq_sz’
	 *featrueID=39&object=’util_max’
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param starttime
	 * @param endtime
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月20日 下午7:35:00
	 */
	public DEResponse getComparedIOUsage(int serverID, String startdate, String enddate, String starttime,
			String endtime, int featrueID, String object, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportComparedDAO.getComparedIOUsageMinute(serverID, startdate,starttime, tableName,featrueID,object,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportComparedDAO.getComparedIOUsageHour(serverID, startdate, enddate,starttime,endtime, tableName,featrueID,object,lang);
		}
	}
}
