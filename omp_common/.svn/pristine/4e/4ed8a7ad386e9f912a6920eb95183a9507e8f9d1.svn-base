package com.dataeye.omp.utils;


/**
 *
 * <pre>
 * Http状态码
 * @author Ivan          <br>
 * @date 2015年4月8日 下午3:03:51 <br>
 * @version 1.0
 * <br>
 */
public enum HttpStatusCode {
    /** 处理成功 */
    SUCCESS(200),
    /** 客户端错误，包含参数错误权限错误 */
    CLIENT_EXCEPTION(400),
    /** 服务器端错误,包含数据库错误等 */
    SERVER_EXCEPTION(500);
    /** 状态码 */
    private int code;

    private HttpStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}