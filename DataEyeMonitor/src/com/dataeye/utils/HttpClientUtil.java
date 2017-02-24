package com.dataeye.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.springframework.stereotype.Service;

import com.dataeye.common.CachedObjects;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.Constant.ServerCfg;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.omp.filter.GetTokenInfoResult;
import com.dataeye.omp.sso.AppInfoSso;
import com.dataeye.omp.sso.SsoResponse;
import com.google.common.reflect.TypeToken;

/**
 * 连接邮件服务器
 * 
 * @author ivan
 * @since 2013-9-3 下午8:25:03
 */

@Service
public class HttpClientUtil {

	public static String get(String url) {
		DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
		try {
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpget);
			// int code = response.getStatusLine().getStatusCode();
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				byte[] bytes = EntityUtils.toByteArray(httpEntity);
				return new String(bytes, "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 发送post请求
	 * 
	 * @param url
	 * @param para
	 * @return
	 * @author Ivan<br>
	 * @throws ServerException
	 * @throws Exception
	 * @date 2015年8月12日 下午2:07:29 <br>
	 */
	public static String post(String url, Map<String, String> para) throws ServerException {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (para != null && para.size() > 0)
				for (Map.Entry<String, String> entry : para.entrySet()) {
					params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			// Post请求
			// 设置参数
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			// 发送请求
			HttpResponse httpresponse = httpClient.execute(httppost);
			HttpEntity httpEntity = httpresponse.getEntity();
			if (httpEntity != null) {
				byte[] bytes = EntityUtils.toByteArray(httpEntity);
				return new String(bytes, "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwHttpClientException(StatusCode.SERVER_STATUS_ERROR,
					"服务器通讯异常，接口请求失败，请联系客服处理url=" + url);
		}
		return null;
	}
	// 上面两个只是通用接口，提供一些跟统一登录接口直接通信的方法，直接调用

	public static GetTokenInfoResult getTokenInfo(String token) throws ServerException {
		Map<String, String> para = new HashMap<String, String>();
		para.put("token", token);
		String content;
		try {
			content = post(ServerCfg.URL_GET_TOKEN_INFO, para);
			System.out.println(content);
			if (content != null) {
				try {
					GetTokenInfoResult result = CachedObjects.GSON.fromJson(content, GetTokenInfoResult.class);
					if (result.getStatusCode() == 200) {
						return result;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (ServerException e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.SERVER_STATUS_ERROR, "统一登录服务器通讯异常,请联系客服处理");
		}
		return null;

	}

	/**
	 * 获取uid下的所有游戏信息
	 * 
	 * @param uid
	 * @return
	 * @author ivantan
	 * @throws ServerException
	 * @throws JSONException
	 * @date 2015年8月18日 下午3:15:16
	 */

	public static List<AppInfoSso> getAppInfoByUid(int uid) throws ServerException {
		List<AppInfoSso> list = new ArrayList<AppInfoSso>();
		Map<String, String> para = new HashMap<String, String>();
		para.put("createrID", uid + "");
		String content = post(ServerCfg.URL_GETACCOUNTAPPLIST, para);
		System.out.println(content);
		if (content != null) {
			SsoResponse<List<AppInfoSso>> list2 = CachedObjects.GSON.fromJson(content,
					new TypeToken<SsoResponse<List<AppInfoSso>>>() {
					}.getType());
			return list2.getContent() == null ? list : list2.getContent();
		}
		return list;
	}

	/**
	 * http://127.0.0.1:48080/ptlogin/innerapi/addApp.do?createrID=13271&appName
	 * =testApiGame&source=1
	 * 
	 * @param uid
	 * @return
	 * @throws ServerException
	 * @author ivantan
	 * @date 2015年9月8日 上午11:25:58
	 */
	public static String createNewApp(int uid, String name) throws ServerException {
		Map<String, String> para = new HashMap<String, String>();
		para.put("createrID", uid + "");
		para.put("appName", name);
		para.put("source", "6");
		String content = post(ServerCfg.URL_CREATENEWAPP, para);
		System.out.println(content);
		if (content != null) {
			SsoResponse<String> result = CachedObjects.GSON.fromJson(content, new TypeToken<SsoResponse<String>>() {
			}.getType());
			return result.getContent() == null ? "" : result.getContent();
		}
		return "";
	}

	public static Map<String, AppInfoSso> getAppInfoMapByUid(int uid) throws ServerException {
		Map<String, AppInfoSso> map = new HashMap<String, AppInfoSso>();
		Map<String, String> para = new HashMap<String, String>();
		para.put("createrID", uid + "");
		String content = post(ServerCfg.URL_GETACCOUNTAPPLIST, para);
		System.out.println("----------------------------------");
		System.out.println(ServerCfg.URL_GETACCOUNTAPPLIST);
		System.out.println(content);
		if (content != null) {
			try {
				SsoResponse<List<AppInfoSso>> list2 = CachedObjects.GSON.fromJson(content,
						new TypeToken<SsoResponse<List<AppInfoSso>>>() {
						}.getType());
				if (list2.getContent() != null) {
					for (AppInfoSso appInfoSso : list2.getContent()) {
						map.put(appInfoSso.getAppID(), appInfoSso);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				ExceptionHandler.throwDatabaseException(StatusCode.SERVER_STATUS_ERROR, e);
			}
		}
		return map;
	}

	public static void main(String[] args) throws ServerException {
		System.out.println(getAppInfoMapByUid(1));
	}
}
