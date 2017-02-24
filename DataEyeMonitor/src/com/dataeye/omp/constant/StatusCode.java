package com.dataeye.omp.constant;

/**
 * <pre>
 * DataEye状态码
 * 
 * @author Ivan <br>
 * @date 2015年4月8日 下午3:06:28 <br>
 *
 */
public class StatusCode {
	/** 请求正常处理 */
	public static final int SUCCESS = 200;
	// **********************Client Exception
	// 4xx**********************************
	/** 参数错误，一般是必填参数为空 */
	public static final int PARAMETER_ERROR = 401;
	/** 没有权限 */
	public static final int NO_PRIV = 403;
	/** 无效的AppID */
	public static final int APPID_INVALID = 404;
	/** 时间格式错误 */
	public static final int PARAMETER_ERROR_TIME = 405;
	/** 已经存在 */
	public static final int EXISTS = 406;
	/** 已被使用(类似外键)，无法删除 */
	public static final int CAN_NOT_DELETE = 408;

	public static final int IP_USERD = 407;
	public static final int PASSWORD_ERROR = 402;
	public static final int VERIFYCODE_ERROR = 410;
	public static final int RULEEXISTS = 409;


	// **********************Server Exception
	// 5xx**********************************
	/** 类错误 */
	public static final int CLASS_ERROR = 501;
	/** 数据库连接错误 */
	public static final int DB_CONN_ERROR = 502;
	/** 数据库初始化失败 */
	public static final int DB_INIT_ERROR = 503;
	/** SQL 执行失败 */
	public static final int DB_SQL_ERROR = 504;
	/** 数据库错误 */
	public static final int HBASE_ERROR = 506;
	/** 服务器状态异常 */
	public static final int SERVER_STATUS_ERROR = 505;
	public static final int LOGIC_ERROR = 507;

}
