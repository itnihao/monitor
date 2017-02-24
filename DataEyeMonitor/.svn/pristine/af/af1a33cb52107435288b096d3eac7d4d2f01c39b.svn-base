package com.dataeye.exception;

/**
 * <pre>
 * 客户端异常的父类
 * @author Ivan          <br>
 * @date 2015年4月3日 下午2:01:15
 * <br>
 *
 */
public class ClientException extends AbstractDataEyeException {
	public ClientException(int statusCode) {
		super(statusCode);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}