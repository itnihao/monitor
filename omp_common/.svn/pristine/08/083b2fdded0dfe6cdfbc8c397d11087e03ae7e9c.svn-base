package com.dataeye.omp.sso;

import com.dataeye.omp.common.Constant;
import com.dataeye.omp.common.Result;
import com.dataeye.omp.utils.DateUtils;
import com.dataeye.omp.utils.ServerUtils;
import com.dataeye.util.log.DELogger;
import com.qq.jutil.string.StringUtil;
import com.xunlei.netty.httpserver.cmd.BaseCmd;
import com.xunlei.netty.httpserver.cmd.CmdMapper;
import com.xunlei.netty.httpserver.component.XLHttpRequest;
import com.xunlei.netty.httpserver.component.XLHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 登陆验证码
 *
 * @author chenfanglin
 * @date 2016年4月18日 下午5:40:18
 */
@Service
public class SsoCmd extends BaseCmd {

	private final static Logger logger = DELogger.getLogger("server_log");

	@Autowired
	private SsoHandler ssoHandler;


	private static ConcurrentHashMap<String, SecurityCode> securityCodeMap = new ConcurrentHashMap<>();

	private static final String SUCC = "succ";
	private static final String FAIL = "fail";
	private static final String EXPIRED = "expired";

	/**
	 * 获取token 1.获取token，如果token没过期，直接登录，
	 * 如果已经过期，重新生成，发送验证邮件到用户邮箱
	 *
	 * @param request
	 * @param response
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:32:15
	 */
	@CmdMapper("/common/token/get")
	public String getToken(XLHttpRequest request, XLHttpResponse response) {

		String uid = request.getParameter("uid", "");
		Result result = null;
		String json = null;
		try {
			if (StringUtil.isEmpty(uid)) {
				result = new Result(Constant.PARAMETER_ERROR, "uid Empty");
			} else {
				// 生成token
				Token token = ssoHandler.getTokenFromCacheByUID(uid);
				if (token != null) {
					// 缓存中有
					int currentTime = DateUtils.unixTimestamp();
					// 过期时间
					int expireTime = token.getExpireTime();
					if (currentTime < expireTime) {
						// 没过期,更新时间后返回token
						token.setUpdateTime(currentTime);
						// 直接登录
						result = new Result(Constant.SUCCESS, token.getToken());
						json = ServerUtils.toJson(result);
						return json;
					}
				}
				// 缓存中没有，重新生成 ;获取已经过期
				String tokenStr = ssoHandler.getNewToken(uid);
				// 发送验证邮件
//				String message = "http://119.147.212.253:38083/common/token/auth?uid=" + uid + "&token=" + tokenStr;
				String message = "Token验证码：" + tokenStr;
				ServerUtils.sendCheckMail(uid, message);
				result = new Result(Constant.AGAIN, tokenStr);
			}
		} catch (Exception e) {
			logger.error(DateUtils.now() + ServerUtils.printStackTrace(e));
			ServerUtils.sendAlarmMail(ServerUtils.printStackTrace(e));
			result = new Result(Constant.SERVER_ERROR, ServerUtils.printStackTrace(e));
		}
		json = ServerUtils.toJson(result);
		return json;
	}


	/**
	 * 使token失效
	 *
	 * @param request
	 * @param response
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:32:30
	 */
	@CmdMapper("/common/token/delete")
	public String deleteToken(XLHttpRequest request, XLHttpResponse response) {
		String UID = request.getParameter("UID", "");
		Result result = null;
		String json = null;
		try {
			if (StringUtil.isEmpty(UID)) {
				result = new Result(Constant.PARAMETER_ERROR, "uid Empty");
			} else {
				// 删除token
				ssoHandler.deleteTokenByUID(UID);
				result = new Result(Constant.SUCCESS, "ok");
			}
		} catch (Exception e) {
			logger.error(DateUtils.now() + ServerUtils.printStackTrace(e));
			ServerUtils.sendAlarmMail(ServerUtils.printStackTrace(e));
			result = new Result(Constant.SERVER_ERROR, ServerUtils.printStackTrace(e));
		}
		json = ServerUtils.toJson(result);
		return json;
	}

	/**
	 * 验证token的有效性
	 *
	 * @param request
	 * @param response
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:32:39
	 */
	@CmdMapper("/common/token/auth")
	public String authToken(XLHttpRequest request, XLHttpResponse response) {
		String UID = request.getParameter("uid", "");
		String token = request.getParameter("token", "");
		Result result = null;
		String json = null;
		try {
			if (StringUtil.isEmpty(UID) || StringUtil.isEmpty(token)) {
				result = new Result(Constant.PARAMETER_ERROR, "uid and token Empty");
			} else {
				// 验证token和UID是否匹配
				boolean success = ssoHandler.authTokenByUID(UID, token);
				if (success) {
					// 验证成功
					result = new Result(Constant.SUCCESS, "验证成功。");
				} else {
					// 验证失败
					result = new Result(Constant.FAIL, "验证失败：链接失效，请重新获取。");
				}
			}
		} catch (Exception e) {
			logger.error(DateUtils.now() + ServerUtils.printStackTrace(e));
			ServerUtils.sendAlarmMail(ServerUtils.printStackTrace(e));
			result = new Result(Constant.SERVER_ERROR, ServerUtils.printStackTrace(e));
		}
		json = ServerUtils.toJson(result);
		return json;
	}

	/**
	 * 从token中获取UID
	 *
	 * @param request
	 * @param response
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:33:36
	 */
	@CmdMapper("/common/token/uid")
	public String getUIDToken(XLHttpRequest request, XLHttpResponse response) {
		String token = request.getParameter("token", "");
		Result result = null;
		String json = null;
		try {
			if (StringUtil.isEmpty(token)) {
				result = new Result(Constant.PARAMETER_ERROR, "token Empty");
			} else {
				// 通过token得到UID
				String UID = ssoHandler.getUIDByToken(token);
				if (StringUtil.isEmpty(UID)) {
					result = new Result(Constant.PARAMETER_ERROR, "uid Empty");
				} else {
					result = new Result(Constant.SUCCESS, UID);
				}
			}
		} catch (Exception e) {
			logger.error(DateUtils.now() + ServerUtils.printStackTrace(e));
			ServerUtils.sendAlarmMail(ServerUtils.printStackTrace(e));
			result = new Result(Constant.SERVER_ERROR, ServerUtils.printStackTrace(e));
		}
		json = ServerUtils.toJson(result);
		return json;
	}


	/**
	 * 发送验证码 五分钟内发送同一验证码，
	 * 且同一验证码发送不能超过三次，
	 * 超过五分钟重新生成验证码
	 *
	 * @param request
	 * @param response
	 * @return
	 * @author wendy <br>
	 * @date 2016年4月18日 下午6:33:36
	 */
	@CmdMapper("/common/securityCode/send")
	public String sendSixSecurityCode(XLHttpRequest request, XLHttpResponse response) {
		String app = request.getParameter("app");
		String email = request.getParameter("uid");
		String jsonCallBack = request.getParameter("jsoncallback");
		Result result;
		if (StringUtil.isEmpty(app) || StringUtil.isEmpty(email)) {
			//response.setStatus(new HttpResponseStatus(Constant.CLIENT_ERROR, "parameter error"));
			result = new Result(Constant.PARAMETER_ERROR, "参数异常");
		} else {
			try {
				String key = app + "_" + email;
				SecurityCode sCode = securityCodeMap.get(key);
				int ctime = DateUtils.unixTimestamp();
				if (null == sCode || (ctime - sCode.getTimestamp()) > 5 * 60) {
					sCode = new SecurityCode(ServerUtils.getSixSecurityCode(), DateUtils.unixTimestamp(), 1);
					securityCodeMap.put(key, sCode);
					ServerUtils.sendCheckMail(email, String.valueOf(sCode.getCode()));
					result = new Result(Constant.SUCCESS, "成功");
				} else {
					if (sCode.getSendTimes() >= 3) {
						result = new Result(Constant.EMAIL_ERROR, "send too many times");
					} else {
						sCode.setSendTimes(sCode.getSendTimes() + 1);
						ServerUtils.sendCheckMail(email, String.valueOf(sCode.getCode()));
						result = new Result(Constant.SUCCESS, "成功");
					}
				}

			} catch (Exception e) {
				response.setStatus(new HttpResponseStatus(Constant.SERVER_ERROR, "server error"));
				result = new Result(Constant.SERVER_ERROR, "服务器异常");
				logger.error(DateUtils.now() + ServerUtils.printStackTrace(e));
				ServerUtils.sendAlarmMail(ServerUtils.printStackTrace(e));
			}
		}
		if (StringUtil.isNotEmpty(jsonCallBack)) {
			return jsonCallBack + "(" + ServerUtils.toJson(result) + ")";
		} else {
			return ServerUtils.toJson(result);
		}
	}

	/**
	 * 验证 验证码
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@CmdMapper("/common/securityCode/verify")
	public String verifySecurityCode(XLHttpRequest request, XLHttpResponse response) {
		String email = request.getParameter("uid");
		String app = request.getParameter("app");
		String receiveCode = request.getParameter("code");

		if (securityCodeMap == null) {
			return FAIL;
		}

		if (StringUtil.isNotEmpty(email) && StringUtil.isNotEmpty(app)
				&& StringUtil.isNotEmpty(receiveCode)) {

			String key = app + "_" + email;
			SecurityCode sCode = securityCodeMap.get(key);
			if (null == sCode) {
				return EXPIRED;
			}

			if ((DateUtils.unixTimestamp() - sCode.getTimestamp()) > 5 * 60) {
				securityCodeMap.remove(key);
				return EXPIRED;
			}

			if (receiveCode.equals(String.valueOf(sCode.getCode()))) {
				securityCodeMap.remove(key);
				return SUCC;
			} else {
				return FAIL;
			}
		} else {
			return FAIL;
		}
	}


	private String getRealIP(XLHttpRequest request) {
		String realip = request.getHeader("X-Real-IP");
		if (StringUtil.isEmpty(realip)) {
			realip = request.getRemoteIP();
		}
		return realip;
	}
}