package com.dataeye.omp.module.monitor.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.DEResponse;
import com.dataeye.omp.common.ValueItem;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.constant.Constant.Table;

@Service
public class ReportService {

	@Autowired
	private ReportDAO reportDAO;

	/**
	 * CPU相关- CPU 总利用率
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月14日 下午3:56:05
	 */
	public DEResponse getCpuTotalUsage(int serverID, String startdate, String enddate, String lang)
			throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getCpuTotalUsageMinute(serverID, Constant.CPUUSAGE, startdate, tableName, Constant.CPU,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getCpuTotalUsageHour(serverID, Constant.CPUUSAGE, startdate, enddate, tableName,
					Constant.CPU,lang);
		}
	}
	
	/**
	 * CPU 1/5/15分钟负载
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param lang
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 上午11:28:00
	 */
	public DEResponse getCpuEveryMinUsage(int serverID, String startdate, String enddate, String lang)
			throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getCpuEveryMinUsageMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getCpuEveryMinUsageHour(serverID, startdate, enddate, tableName,lang);
		}
	}

	/**
	 * 各核心CPU使用率
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param lang
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午3:31:48
	 */
	public DEResponse getCpuEveryCoreUsage(int serverID, String startdate, String enddate, String lang)
			throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getCpuEveryCoreUsageMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getCpuEveryCoreUsageHour(serverID, startdate, enddate, tableName,lang);
		}
	}
	
	/**
	 * 内存相关- 内存总使用率
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午3:03:11
	 */
	public DEResponse getMemTotalUsage(int serverID, String startdate, String enddate, String lang)
			throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getMemTotalUsageMinute(serverID, Constant.MENUSAGE, startdate, tableName,
					Constant.MEM_USED, lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getMemTotalUsageHour(serverID, Constant.MENUSAGE, startdate, enddate, tableName,
					Constant.MEM_USED,lang);
		}
	}

	/**
	 * 内存使用情况
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月14日 下午6:55:03
	 */
	public DEResponse getMemUseState(int serverID, String startdate, String enddate,String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getMemUseStateMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getMemUseStateHour(serverID, startdate, enddate, tableName,lang);
		}
	}

	/**
	 * SWAP内存使用率 WAP使用大小/SWAP总大小
	 * 
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午3:30:27
	 */
	public DEResponse getSwapMemTotalUsage(int serverID, String startdate, String enddate,String lang)
			throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getSwapMemTotalUsageMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getSwapMemTotalUsageHour(serverID, startdate, enddate, tableName,lang);
		}
	}
	
	/**
	 * SWAP内存使用量
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午4:35:30
	 */
	public DEResponse getSwapMemUseState(int serverID, String startdate, String enddate,String lang) throws ServerException{
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getSwapMemUseStateMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getSwapMemUseStateHour(serverID, startdate, enddate, tableName,lang);
		}
	}
	
	
	
	/**
	 * 打开的Tcp连接数
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月15日 下午7:24:39
	 */
	public DEResponse getTcpConn(int serverID, String startdate, String enddate,String lang) throws ServerException{
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getTcpConnMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getTcpConnHour(serverID, startdate, enddate, tableName,lang);
		}
	}
	
	/**
	 * 被动打开的Tcp连接数
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月18日 下午3:41:30
	 */
	public DEResponse getPassiveTcpConn(int serverID, String startdate, String enddate,String lang) throws ServerException{
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getPassiveTcpConnMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getPassiveTcpConnHour(serverID, startdate, enddate, tableName,lang);
		}
	}

	/**
	 * 内网网卡出入流量
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月19日 下午5:02:05
	 */
	public DEResponse getIntranetNICFlow(int serverID, String startdate, String enddate, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getIntranetNICFlowMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getIntranetNICFlowHour(serverID, startdate, enddate, tableName,lang);
		}
	}
	
	/**
	 *  内网网卡出入包量
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午5:01:39
	 */
	public DEResponse getIntranetNICPackageVolume(int serverID, String startdate, String enddate, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getIntranetNICPackageVolumeMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getIntranetNICPackageVolumeHour(serverID, startdate, enddate, tableName,lang);
		}
	}

	/**
	 * 外网网卡出入流量
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午5:02:22
	 */
	public DEResponse getExtranetNICFlow(int serverID, String startdate, String enddate, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getExtranetNICFlowMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getExtranetNICFlowHour(serverID, startdate, enddate, tableName,lang);
		}
	}

	/**
	 * 外网网卡出入包量
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午5:03:00
	 */
	public DEResponse getExtranetNICPackageVolume(int serverID, String startdate, String enddate, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getExtranetNICPackageVolumeMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getExtranetNICPackageVolumeHour(serverID, startdate, enddate, tableName,lang);
		}
	}

	
	/**
	 * UDP收发数据报
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午5:03:28
	 */
	public DEResponse getUDPDatagram(int serverID, String startdate, String enddate, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getUDPDatagramMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getUDPDatagramHour(serverID, startdate, enddate, tableName,lang);
		}
	}

	/**
	 * 磁盘IO相关 - 磁盘分区使用率
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午6:58:31
	 */
	public DEResponse getIOTotalUsage(int serverID, String startdate, String enddate, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getIOTotalUsageMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getIOTotalUsageHour(serverID, startdate, enddate, tableName,lang);
		}
	}

	/**
	 *  磁盘IO相关 - 磁盘IO使用量
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午6:58:22
	 */
	public DEResponse getIOUseState(int serverID, String startdate, String enddate, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getIOUseStateMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getIOUseStateHour(serverID, startdate, enddate, tableName,lang);
		}
	}

	public DEResponse getFeatureFrom35To39(int serverID, String startDate, String endDate, int featureId, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startDate.equals(endDate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getFeatureFrom35To39ByMinute(serverID, startDate, featureId, tableName, lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getFeatureFrom35To39ByHour(serverID, startDate, endDate, featureId, tableName, lang);
		}
	}

	/**
	 * 磁盘IO相关 - 磁盘IO读写
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午6:58:13
	 */
	public DEResponse getDiskIOReadWrite(int serverID, String startdate, String enddate, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getDiskIOReadWriteMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getDiskIOReadWriteHour(serverID, startdate, enddate, tableName,lang);
		}
	}

	/**
	 *  磁盘IO相关 - SWAP 使用情况
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月19日 下午6:58:02
	 */
	public DEResponse getIOSwapUseState(int serverID, String startdate, String enddate, String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getIOSwapUseStateMinute(serverID, startdate, tableName,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getIOSwapUseStateHour(serverID, startdate, enddate, tableName,lang);
		}
	}

	/**
	 * 35	Svctm_time_max
	 * 36	Await_time_max
	 * 37	avgqu_sz_max
	 * 38	avgrq_sz
	 * 39	util_max
	 * @param serverID
	 * @param startdate
	 * @param enddate
	 * @param featrueID
	 * @param object
	 * @param lang
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月21日 上午11:14:31
	 */
	public DEResponse getIOUseTrend(int serverID, String startdate, String enddate, int featrueID, String object,
			String lang) throws ServerException {
		String tableName = "";
		// 当选择一天时，按分钟；当选择多天时，按小时
		if (startdate.equals(enddate)) {
			tableName = Table.OMP_FEATURE_VALUE_STAT_MINUTE;
			return reportDAO.getIOUseTrendMinute(serverID, startdate, tableName,featrueID,object,lang);
		} else {
			tableName = Table.OMP_FEATURE_VALUE_STAT_HOUR;
			return reportDAO.getIOUseTrendHour(serverID, startdate, enddate, tableName,featrueID,object,lang);
		}
	}

	/**
	 * 获取硬件状态
	 * @param serverID
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月25日 上午10:51:24
	 */
	public List<ValueItem> getHardWareStatus(int serverID,String lang) throws ServerException {
		List<ValueItem> list = reportDAO.getHardWareStatus(serverID,lang);
		return list;
	}
}
