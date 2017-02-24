package com.dataeye.omp.sso;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dataeye.omp.Launch;
import com.dataeye.omp.common.Constant.Table;
import com.dataeye.omp.utils.DateUtils;
import com.dataeye.omp.utils.ServerUtils;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import com.xunlei.spring.Config;

/**
 * sso处理器
 *
 * @author chenfanglin
 * @date 2016年4月19日 下午2:32:47
 */
@Service
public class SsoHandler {

	@Resource(name = "jdbcTemplateDcGlobal")
	private JdbcTemplate jdbcTemplateDcGlobal;

	@Config
	private int insertSsoDelay = 20;
	/**
	 * 缓存token
	 */
	private ConcurrentHashMap<String, Token> tokenCache = new ConcurrentHashMap<String, Token>();
	/**
	 * seconds of one week
	 */
	@Config
	private int one_day = 24 * 60 * 60;


	private ConcurrentHashMap<String, Integer> sixSecurityCodeCache = new ConcurrentHashMap<String, Integer>();


	/**
	 * 从缓存中获取Token
	 *
	 * @param uid
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月19日 下午3:48:41
	 */
	public Token getTokenFromCacheByUID(String uid) {
		Token cachedToken = tokenCache.get(uid);
		return cachedToken;
	}

	/**
	 * 获取新的token
	 *
	 * @param uid
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月19日 下午3:52:16
	 */
	public String getNewToken(String uid) {
		String tokenNew = ServerUtils.getTokenByUID(uid);
		if (StringUtil.isEmpty(tokenNew)) {
			return null;
		}
		return tokenNew;
	}

	/**
	 * 删除token
	 *
	 * @param UID
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:50:19
	 */
	public void deleteTokenByUID(String UID) {
		tokenCache.remove(UID);
	}

	/**
	 * 验证某个token是否和UID匹配
	 *
	 * @param UID
	 * @param tokenStr
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:50:26
	 */
	public boolean authTokenByUID(String uid, String tokenNew) {
		Token token = tokenCache.get(uid);
		int currentTime = DateUtils.unixTimestamp();
		if (token != null) {
			if (tokenNew.equals(token.getToken())) {

				int expireTime = token.getExpireTime();
				if (currentTime < expireTime) {
					// 验证成功
					return true;
				} else {
					// 过期,重新验证
					return false;
				}
			} else {
				//不匹配，重新验证
				return false;
			}
		} else {
			String UID = getUIDByToken(tokenNew);
			if (UID != null && UID.equals(uid)) {
				token = new Token(uid, tokenNew, currentTime, currentTime + one_day);
				tokenCache.put(uid, token);
				return true;
			} else {
				return false;
			}

		}
	}

	/**
	 * 通过token获取UID
	 *
	 * @param tokenStr
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:50:33
	 */
	public String getUIDByToken(String tokenStr) {
		// 反解密出UID
		String UID = ServerUtils.getUIDFromToken(tokenStr);
		if (UID == null) {
			return null;
		}
		return UID;
	}

	/**
	 * 初始化操作，从数据库中读出之前的cache,加载special
	 *
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:50:48
	 */
	public void init() {
		// 从数据库里面读出上次服务器关闭插入的缓存项目
		String sql = "select uid,token,updateTime,expireTime from " + Table.DC_SSO_INFO;
		jdbcTemplateDcGlobal.query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				String uid = rs.getString("uid");
				String token = rs.getString("token");
				int updateTime = rs.getInt("updateTime");
				int expireTime = rs.getInt("expireTime");
				int currentTime = DateUtils.unixTimestamp();
				if (currentTime < expireTime) {
					Token token2 = new Token(uid, token, updateTime, expireTime);
					tokenCache.put(uid, token2);
				}
			}
		});
		System.err.println("init done,size=" + tokenCache.size());
	}

	/**
	 * 开启一个线程定时把缓存入库
	 *
	 * @author chenfanglin <br>
	 * @date 2016年4月18日 下午6:50:56
	 */
	public void start() {
		System.err.println(">>>>>>>>>>>SsoHandler Start At " + DateUtils.now());
		Launch.SCHEDULER.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				System.err.println(">>>>>>>>>>>Insert to Db Start At " + DateUtils.now());
				String sql = "insert into " + Table.DC_SSO_INFO + "(uid,token,updateTime,expireTime) values(?,?,?,?) on duplicate key update token=?,updateTime=?,expireTime=?";
				Collection<Token> values = tokenCache.values();
				List<Object[]> args = new ArrayList<Object[]>();
				for (Token value : values) {
					String UID = value.getUID();
					String token = value.getToken();
					int updateTime = value.getUpdateTime();
					int expireTime = value.getExpireTime();
					// 把现在有效的入库
					int currentTime = DateUtils.unixTimestamp();
					if (currentTime < expireTime) {
						Object[] obj = new Object[]{UID, token, updateTime, expireTime, token, updateTime, expireTime};
						args.add(obj);
					}
				}
				if (args.size() > 0) {
					jdbcTemplateDcGlobal.batchUpdate(sql, args);
				}
			}
		}, insertSsoDelay, insertSsoDelay, TimeUnit.SECONDS);
	}
}
