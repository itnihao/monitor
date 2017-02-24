package com.dataeye.exception;

/**
 * <pre>
 * 异常处理器
 * 
 * @author Ivan <br>
 * @date 2015年4月3日 下午3:22:51 <br>
 *
 */
public class ExceptionHandler {
	/**
	 * 
	 * <pre>
	 * 抛出参数错误的异常
	 * 
	 * @param statusCode
	 * @throws ClientException
	 * @author Ivan<br>
	 * @date 2015年4月3日 下午3:33:25 <br>
	 */
	public static void throwParameterException(int statusCode) throws ClientException {
		throw new ClientException(statusCode);
	}

	/**
	 * 
	 * <pre>
	 * 抛出权限错误的异常
	 * 
	 * @param statusCode
	 * @throws ClientException
	 * @author Ivan<br>
	 * @date 2015年4月3日 下午3:35:20 <br>
	 */
	public static void throwPermissionException(int statusCode) throws ClientException {
		throw new ClientException(statusCode);
	}

	/**
	 * 
	 * <pre>
	 * 抛出数据库错误的异常
	 * 
	 * @param statusCode
	 * @throws ServerException
	 * @author Ivan<br>
	 * @date 2015年4月3日 下午3:36:39 <br>
	 */
	public static void throwDatabaseException(int statusCode, Throwable th) throws ServerException {
		throw new ServerException(statusCode, th.getMessage());
	}

	public static void throwDatabaseException(int statusCode, String message) throws ServerException {
		throw new ServerException(statusCode, message);
	}

	public static void throwLogicException(int statusCode, Throwable th) throws ServerException {
		throw new ServerException(statusCode, th.getMessage());
	}

	public static void throwLogicException(int statusCode, String message) throws ServerException {
		throw new ServerException(statusCode, message);
	}

	/**
	 * 
	 * <pre>
	 * 抛出类错误异常
	 * 
	 * @param statusCode
	 * @throws ServerException
	 * @author Ivan<br>
	 * @date 2015年4月8日 下午5:44:21 <br>
	 */
	public static void throwClassException(int statusCode, String message) throws ServerException {
		throw new ServerException(statusCode, message);
	}

	public static void throwHttpClientException(int statusCode, Throwable th) throws ServerException {
		throw new ServerException(statusCode, th.getMessage());
	}

	public static void throwHttpClientException(int statusCode, String message) throws ServerException {
		throw new ServerException(statusCode, message);
	}

	public static void throwCustomMessageException(int statusCode, String message) throws CustomMessageException{
		throw new CustomMessageException(statusCode, message);
	}

}
