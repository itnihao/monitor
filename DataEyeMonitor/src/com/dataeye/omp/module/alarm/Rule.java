package com.dataeye.omp.module.alarm;

import java.math.BigDecimal;
import java.util.List;

import com.dataeye.common.CachedObjects;
import com.google.gson.annotations.Expose;
import com.qq.thessian.util.Alarm;

/**
 * 告警规则
 * 
 * @author chenfanglin
 * @date 2016年1月23日 下午2:34:28
 */
public class Rule {

	// 0阀值 1环比
	@Expose
	private int alarmSectionType;
	// 最大阀值
	@Expose
	private BigDecimal maxThreshold;
	// 最小阀值
	@Expose
	private BigDecimal minThreshold;
	// 最大环比
	@Expose
	private BigDecimal maxMoM;
	// 最小环比
	@Expose
	private BigDecimal minMoM;
	// 最大告警次数
	@Expose
	private int maxFrequency;

	@Expose
	//告警级别  0：紧急  1：一般
	private int alarmLevel;


	// 告警类型  0.邮件告警  1.短信告警   2.APP推送
	@Expose
	private List<Integer> alarmType;
	/**
	 * @return the maxThreshold
	 */
	public BigDecimal getMaxThreshold() {
		return maxThreshold;
	}

	/**
	 * @param maxThreshold
	 *            the maxThreshold to set
	 */
	public void setMaxThreshold(BigDecimal maxThreshold) {
		this.maxThreshold = maxThreshold;
	}

	/**
	 * @return the minThreshold
	 */
	public BigDecimal getMinThreshold() {
		return minThreshold;
	}

	/**
	 * @param minThreshold
	 *            the minThreshold to set
	 */
	public void setMinThreshold(BigDecimal minThreshold) {
		this.minThreshold = minThreshold;
	}

	/**
	 * @return the maxMoM
	 */
	public BigDecimal getMaxMoM() {
		return maxMoM;
	}

	/**
	 * @param maxMoM
	 *            the maxMoM to set
	 */
	public void setMaxMoM(BigDecimal maxMoM) {
		this.maxMoM = maxMoM;
	}

	public BigDecimal getMinMoM() {
		return minMoM;
	}

	public void setMinMoM(BigDecimal minMoM) {
		this.minMoM = minMoM;
	}

	/**
	 * @return the maxFrequency
	 */
	public int getMaxFrequency() {
		return maxFrequency;
	}

	/**
	 * @param maxFrequency
	 *            the maxFrequency to set
	 */
	public void setMaxFrequency(int maxFrequency) {
		this.maxFrequency = maxFrequency;
	}

	public List<Integer> getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(List<Integer> alarmType) {
		this.alarmType = alarmType;
	}

	public int getAlarmSectionType() {
		return alarmSectionType;
	}

	public void setAlarmSectionType(int alarmSectionType) {
		this.alarmSectionType = alarmSectionType;
	}

	public int getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	/**
	 * 把当前对象转换为json格式
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月25日 下午7:45:43
	 */
	public String toJson() {
		return CachedObjects.GSON_ONLY_EXPOSE.toJson(this);
	}
}
