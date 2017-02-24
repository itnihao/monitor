package com.dataeye.omp.detect.kafka.consume;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dataeye.omp.common.AlarmBasicRule;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.common.DiskPartition;
import com.dataeye.omp.utils.ServerUtils;
import com.google.gson.reflect.TypeToken;

/**
 * 根据不同的特性数据比较
 * 
 * @author chenfanglin
 * @date 2016年3月1日 上午11:54:02
 */
public class CheckReportDataSuperUtils {

	/**
	 * 判断上报值
	 *
	 * @param value     上报值
	 * @param featureID
	 * @param object
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月1日 下午4:27:19
	 */
	public static boolean checkReportValue(BigDecimal value, int featureID, String object,
										   AlarmBasicRule alarmRule, int serverID) {
		BigDecimal maxThreshold = alarmRule.getMaxThreshold();
		BigDecimal minThreshold = alarmRule.getMinThreshold();
		if (Constant.CPU.equals(object) || featureID == Constant.UTILMAX
				|| featureID == Constant.SVCTMTIMEMAX || featureID == Constant.AWAITTIMEMAX
				|| featureID == Constant.AVGRQSZ || featureID == Constant.AVGQUSZMAX) {
			// CPU 相关 的都 乘以100 再比较
			return comparedValue(value, maxThreshold.multiply(Constant.HUNDRED),
					minThreshold.multiply(Constant.HUNDRED));
		} else if (Constant.PGPGIN.equals(object) || Constant.PGPGOUT.equals(object)
				|| Constant.PSWPIN.equals(object) || Constant.PSWPOUT.equals(object)
				|| featureID == Constant.OUTFLOW || featureID == Constant.INFLOW
				|| featureID == Constant.IDCNETIN || featureID == Constant.IDCNETOUT) {
			// 内存相关，IO相关，出入流量 除以 1024*1024再比较
			return comparedValue(value.divide(Constant.UNIT), maxThreshold, minThreshold);
		} else if (featureID >= Constant.OUTPACKAGEVOLUME && featureID <= Constant.UDPSENDDATAGRAM) {
			// 直接比较
			return comparedValue(value, maxThreshold, minThreshold);
		} else if (Constant.FREEPARTITION == featureID) {
			// 计算机器的 根分区使用率
			String diskPartition = ServerUtils.getDiskPartition(serverID);
			// json字符串转对象集合
			List<DiskPartition> listPartition = Constant.GSON.fromJson(diskPartition,
					new TypeToken<List<DiskPartition>>() {
					}.getType());
			Map<String, BigDecimal> map = changeListToMap(listPartition);
			// 分区总大小，转换为字节
			BigDecimal total = map.get(object).multiply(Constant.KB);
			BigDecimal used = total.subtract(value);
			BigDecimal val = used.divide(total, 2, BigDecimal.ROUND_HALF_UP);
			return comparedValue(val, maxThreshold.divide(Constant.HUNDRED),
					minThreshold.divide(Constant.HUNDRED));
		} else if (Constant.PHYSIC_MEMORY_USE_OBJECT.equals(object) &&
				featureID == Constant.PHYSIC_MEMORY_USE_FEATURE) {
			// 计算物理内存使用率
			// 1. 获取总内存（单位：kb）
			BigDecimal totalMemory = ServerUtils.getMemoryTotal(serverID);
			if (totalMemory != null) {
				// 转换为字节
				BigDecimal total = totalMemory.multiply(Constant.KB);
				BigDecimal val = value.divide(total, 2, BigDecimal.ROUND_HALF_UP);
				return comparedValue(val, maxThreshold.divide(Constant.HUNDRED), minThreshold.divide(Constant.HUNDRED));
			}
		}
		return false;
	}

	/**
	 * 把list转换为map
	 * 
	 * @param list
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月10日 下午2:30:31
	 */
	private static Map<String, BigDecimal> changeListToMap(List<DiskPartition> list) {
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		for (DiskPartition partition : list) {
			map.put(partition.getPartition(), new BigDecimal(partition.getPartitionSize()));
		}
		return map;
	}

	/**
	 * 比较
	 * 
	 * @param value
	 * @param maxThreshold
	 * @param minThreshold
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月1日 下午4:47:02
	 */
	private static boolean comparedValue(BigDecimal value, BigDecimal maxThreshold, BigDecimal minThreshold) {
		if (value.compareTo(new BigDecimal(0)) == -1) {
			// 为负数不告警
			return false;
		}
		if (value.compareTo(maxThreshold) == 1 || value.compareTo(minThreshold) == -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据特性 转换上报的值
	 * 
	 * @param value
	 * @param featureID
	 * @param object
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月1日 下午6:41:38
	 */
	public static String getReportValueByfeature(BigDecimal value, int featureID, String object, int serverID) {
		if (Constant.CPU.equals(object)) {
			// CPU 相关 的都 除以100
			double val = value.divide(Constant.HUNDRED, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (featureID == 10) {
				return ServerUtils.precent(val);
			} else {
				return val + "";
			}
		} else if (Constant.PGPGIN.equals(object) || Constant.PGPGOUT.equals(object) || Constant.PSWPIN.equals(object)
				|| Constant.PSWPOUT.equals(object) ) {
			// 内存相关，IO相关， 除以 1024*1024
			String val = value.divide(Constant.UNIT,2, BigDecimal.ROUND_HALF_UP).toString();
			return val + Constant.MILLION;
		} else if (featureID == Constant.OUTFLOW || featureID == Constant.INFLOW
				|| featureID == Constant.IDCNETIN || featureID == Constant.IDCNETOUT) {
			//出入流量 Mb
			String val = value.divide(Constant.UNIT, 2, BigDecimal.ROUND_HALF_UP).toString();
			return val + Constant.MILLION + Constant.BYTE;
		} else if (featureID >= Constant.OUTPACKAGEVOLUME && featureID <= Constant.UDPSENDDATAGRAM) {
			return value.toString();
		} else if (Constant.FREEPARTITION == featureID) {
			// 计算机器的 根分区使用率
			String diskPartition = ServerUtils.getDiskPartition(serverID);
			// json字符串转对象集合
			List<DiskPartition> listPartition = Constant.GSON.fromJson(diskPartition,
					new TypeToken<List<DiskPartition>>() {
					}.getType());
			Map<String, BigDecimal> map = changeListToMap(listPartition);
			// 分区总大小，转换为字节
			BigDecimal total = map.get(object).multiply(Constant.KB);
			BigDecimal used = total.subtract(value);
			double val = used.divide(total, 2, BigDecimal.ROUND_HALF_UP).multiply(Constant.HUNDRED).doubleValue();
			return object + Constant.CUTTINGBREAKS + ServerUtils.precent(val);
		} else if (Constant.PHYSIC_MEMORY_USE_OBJECT.equals(object) && featureID == Constant.PHYSIC_MEMORY_USE_FEATURE) {
			// 计算物理内存使用率
			// 1. 获取总内存（单位：kb）
			BigDecimal totalMemory = ServerUtils.getMemoryTotal(serverID);
			if (totalMemory != null) {
				// 转换为字节
				BigDecimal total = totalMemory.multiply(Constant.KB);
				double val = value.divide(total, 2, BigDecimal.ROUND_HALF_UP).multiply(Constant.HUNDRED).doubleValue();
				return ServerUtils.precent(val);
			}
		} else if (featureID == Constant.SVCTMTIMEMAX) {
			return Constant.SVCTM_TIME_MAX + object + Constant.CUTTINGBREAKS + value.divide(Constant.HUNDRED, 2, BigDecimal.ROUND_HALF_UP).toString() + Constant.SECOND;
		} else if (featureID == Constant.AWAITTIMEMAX) {
			return Constant.AWAIT_TIME_MAX + object + Constant.CUTTINGBREAKS + value.divide(Constant.HUNDRED, 2, BigDecimal.ROUND_HALF_UP).toString() + Constant.SECOND;
		} else if (featureID == Constant.UTILMAX) {
			return Constant.UTIL_MAX + object + Constant.CUTTINGBREAKS + ServerUtils.precent(value.divide(Constant.HUNDRED, 2, BigDecimal.ROUND_HALF_UP).doubleValue());
		} else if (featureID == Constant.AVGRQSZ) {
			return Constant.IO_AVGRQ_SZ + object + Constant.CUTTINGBREAKS + value.divide(Constant.HUNDRED, 2, BigDecimal.ROUND_HALF_UP).toString();
		} else if (featureID == Constant.AVGQUSZMAX) {
			return Constant.AVGQU_SZ_MAX + object + Constant.CUTTINGBREAKS + value.divide(Constant.HUNDRED, 2, BigDecimal.ROUND_HALF_UP).toString();
		}
		return value.toString();
	}
}
