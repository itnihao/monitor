package com.dataeye.omp.clientAlarm;

import java.util.Date;

public class DcAlarmDomain {
	public static final String CLOASE_URL="http://119.147.212.252:38082/de/closeAlarm";
	
	public static final String SLOVE_FINISH_LABEL="slove";
	public static final String NON_SLOVE_FINISH_LABEL="non-slove";
	
	private int id;
	private String title;
	private String type;
	private String content;
	private String closeUrl=DcAlarmDomain.CLOASE_URL;
	private String others;
	private Date c_time;
	private Date m_time;
	
	
	private long normal_alarm_id;
	private String close_user;
	private String finish_label=DcAlarmDomain.NON_SLOVE_FINISH_LABEL;
	
	public DcAlarmDomain(){
		
	}	
	
	
	public long getNormal_alarm_id() {
		return normal_alarm_id;
	}

	public void setNormal_alarm_id(long normal_alarm_id) {
		this.normal_alarm_id = normal_alarm_id;
	}

	public String getFinish_label() {
		return finish_label;
	}

	public void setFinish_label(String finish_label) {
		this.finish_label = finish_label;
	}

	public String getClose_user() {
		return close_user;
	}

	public void setClose_user(String close_user) {
		this.close_user = close_user;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCloseUrl() {
		return closeUrl;
	}
	public void setCloseUrl(String closeUrl) {
		this.closeUrl = closeUrl;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	public Date getC_time() {
		return c_time;
	}
	public void setC_time(Date c_time) {
		this.c_time = c_time;
	}
	public Date getM_time() {
		return m_time;
	}
	public void setM_time(Date m_time) {
		this.m_time = m_time;
	}

	@Override
	public String toString() {
		return "DcAlarmDomain [id=" + id + ", title=" + title + ", type="
				+ type + ", content=" + content + ", closeUrl=" + closeUrl
				+ ", others=" + others + ", c_time=" + c_time + ", m_time="
				+ m_time + ", normal_alarm_id=" + normal_alarm_id
				+ ", close_user=" + close_user + ", finish_label="
				+ finish_label + "]";
	}
}
