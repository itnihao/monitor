package com.dataeye.omp.common;

/**
 * 
 * <pre>
 * 一封告警邮件
 * @author Ivan          <br>
 * @date 2014-6-30 下午3:07:15 <br>
 * @version 1.0
 * <br>
 */
public class AlarmMail {
	private String to;
	private String subject;
	private String content;
	private int size;
	private long id;

	public AlarmMail() {

	}

	public AlarmMail(String to, String subject, String content) {
		this.to = to;
		this.subject = subject;
		this.content = content;
		this.size = 1;
	}


	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTo() {
		return to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void appendContent(String other) {
		if (this.size == 2) {
			content = "===========NO:1=================\n" + content + "\n\n";
		}
		content = content + "===========NO:" + this.size + "=================\n" + other + "\n\n";
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void incSize(int size) {
		this.size += size;
	}

	public int getSize() {
		return size;
	}
}
