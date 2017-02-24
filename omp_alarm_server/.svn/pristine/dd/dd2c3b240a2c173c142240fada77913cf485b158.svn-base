package com.dataeye.omp.detect.kafka.consume;

import java.math.BigDecimal;

import com.dataeye.omp.common.AlarmRule;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.utils.ServerUtils;

/**
 * 根据不同的特性数据比较
 * @author chenfanglin
 * @date 2016年3月1日 上午11:54:02
 */
public class CheckReportDataUtils {

	/**
	 * 判断上报值
	 * @param value 上报值
	 * @param featureID
	 * @param object
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月1日 下午4:27:19
	 */
	public static boolean checkReportValue(BigDecimal value, int featureID,String object,AlarmRule alarmRule){
		BigDecimal maxThreshold = alarmRule.getMaxThreshold();
		BigDecimal minThreshold = alarmRule.getMinThreshold();
		if (Constant.CPU.equals(object)) {
			// CPU 相关 的都 乘以100 再比较
			return comparedValue(value, maxThreshold.multiply(Constant.HUNDRED), minThreshold.multiply(Constant.HUNDRED));
		} else if (Constant.MEM_USED.equals(object) || Constant.MEM_PRI.equals(object) || Constant.MEM_VIR.equals(object)
				|| Constant.MEM_SWAP_USED.equals(object) || Constant.MEM_SWAP_TOTAL.equals(object) || Constant.MPRI_IPCS.equals(object)
				|| Constant.PGPGIN.equals(object) || Constant.PGPGOUT.equals(object) || Constant.PSWPIN.equals(object)
				|| Constant.PSWPOUT.equals(object) || featureID == Constant.OUTFLOW || featureID == Constant.INFLOW) {
			// 内存相关，IO相关，出入流量   除以 1024*1024再比较
			return comparedValue(value.divide(Constant.UNIT), maxThreshold, minThreshold);
		} else if (featureID >= Constant.OUTPACKAGEVOLUME && featureID <= Constant.UDPSENDDATAGRAM) {
			// 直接比较
			return comparedValue(value, maxThreshold, minThreshold);
		}
		return false;
	}
	
	/**
	 * 比较
	 * @param value
	 * @param maxThreshold
	 * @param minThreshold
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月1日 下午4:47:02
	 */
	private static boolean comparedValue(BigDecimal value,BigDecimal maxThreshold,BigDecimal minThreshold){
		if (value.compareTo(maxThreshold) == 1 || value.compareTo(minThreshold) == -1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 根据特性 转换上报的值
	 * @param value
	 * @param featureID
	 * @param object
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月1日 下午6:41:38
	 */
	public static String getReportValueByfeature(BigDecimal value, int featureID,String object){
		if (Constant.CPU.equals(object)) {
			// CPU 相关 的都 除以100 
			double val = value.divide(Constant.HUNDRED, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (featureID == 10) {
				return ServerUtils.precent(val);
			} else {
				return val+"";
			}
		} else if (Constant.MEM_USED.equals(object) || Constant.MEM_PRI.equals(object) || Constant.MEM_VIR.equals(object)
				|| Constant.MEM_SWAP_USED.equals(object) || Constant.MEM_SWAP_TOTAL.equals(object) || Constant.MPRI_IPCS.equals(object)
				|| Constant.PGPGIN.equals(object) || Constant.PGPGOUT.equals(object) || Constant.PSWPIN.equals(object)
				|| Constant.PSWPOUT.equals(object) || featureID == Constant.OUTFLOW || featureID == Constant.INFLOW) {
			// 内存相关，IO相关，出入流量   除以 1024*1024
			String val = value.divide(Constant.UNIT).toString();
			return val + Constant.MILLION;
		} else if (featureID >= Constant.OUTPACKAGEVOLUME && featureID <= Constant.UDPSENDDATAGRAM) {
			return value.toString();
		}
		return value.toString();
	}
}
