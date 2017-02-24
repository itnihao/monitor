package com.dataeye.omp.common;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

/**
 * 告警规则
 * 
 * @author chenfanglin
 * @date 2016年1月23日 下午2:34:28
 */
public class AlarmRule {
	private final static Gson gson = new Gson();
	
	// 0设置阀值，1设置环比
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
	private BigDecimal minMom;
	// 最大告警次数
	@Expose
	private int maxFrequency;
	
	// 告警级别
	@Expose
	private int alarmLevel;
	
	// 告警类型  0邮件告警  1短信告警
	@Expose
	private List<Integer> alarmType;

	/**
	 * @return the alarmLevel
	 */
	public int getAlarmLevel() {
		return alarmLevel;
	}

	/**
	 * @param alarmLevel the alarmLevel to set
	 */
	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

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

	/**
	 * @return the minMom
	 */
	public BigDecimal getMinMom() {
		return minMom;
	}

	/**
	 * @param minMom
	 *            the minMom to set
	 */
	public void setMinMom(BigDecimal minMom) {
		this.minMom = minMom;
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


	/**
	 * @return the alarmType
	 */
	public List<Integer> getAlarmType() {
		return alarmType;
	}

	/**
	 * @param alarmType the alarmType to set
	 */
	public void setAlarmType(List<Integer> alarmType) {
		this.alarmType = alarmType;
	}

	/**
	 * @return the alarmSectionType
	 */
	public int getAlarmSectionType() {
		return alarmSectionType;
	}

	/**
	 * @param alarmSectionType the alarmSectionType to set
	 */
	public void setAlarmSectionType(int alarmSectionType) {
		this.alarmSectionType = alarmSectionType;
	}

	/**
	 * 把当前对象转换为json格式
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月25日 下午7:45:43
	 */
	public String toJson() {
		return gson.toJson(this);
	}
	
	public static AlarmRule parseJson(String json){
		return gson.fromJson(json, AlarmRule.class);
	}
}
