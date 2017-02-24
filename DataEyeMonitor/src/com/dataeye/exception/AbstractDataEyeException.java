package com.dataeye.exception;

/**
 * 
 * <pre>
 * 自定义异常父类
 * @author Ivan          <br>
 * @date 2015年4月3日 上午11:38:08 <br>
 * @version 1.0
 * <br>
 */
public abstract class AbstractDataEyeException extends Exception {
	/** 描述一个异常的错误码 */
	private int statusCode;

	public AbstractDataEyeException(int statusCode) {
		this.statusCode = statusCode;
	}

	public AbstractDataEyeException(int statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}