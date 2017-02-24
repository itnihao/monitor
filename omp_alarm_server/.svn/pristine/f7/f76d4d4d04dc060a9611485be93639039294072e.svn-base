package com.dataeye.omp.utils;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

/**
 * Http请求Util
 * @author chenfanglin
 * @date 2016年2月29日 下午6:19:46
 */
@Service
public class HttpClientUtil {

	public static String get(String url) {
		DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler();
		httpClient.setHttpRequestRetryHandler(retryHandler);
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
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler();
			httpClient.setHttpRequestRetryHandler(retryHandler);
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
		}
		return null;
	}
}
