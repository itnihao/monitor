package com.dataeye.monitor.utils;


import com.dataeye.monitor.task.ExceptionHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
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
			ExceptionHandler.error("url: " + url, "");
		}
		return null;
	}


    /**
     * 发送post请求
     * @param url
     * @param content
     * @return
     */
	public static String post(String url, String content){
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			HttpPost httppost = new HttpPost(url);

			// Post请求 设置参数
            StringEntity params = new StringEntity(content);
            httppost.setEntity(params);

            // 发送请求
			HttpResponse httpresponse = httpClient.execute(httppost);
			HttpEntity httpEntity = httpresponse.getEntity();
			if (httpEntity != null) {
				byte[] bytes = EntityUtils.toByteArray(httpEntity);
				return new String(bytes, "utf-8");
			}
		} catch (Exception e) {
			ExceptionHandler.error("url: "+url + " content: " + content, e);
			return "fail";
		}
		return "succ";
	}


    public static String post(String url, Map<String, String> para) {
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
		}
        return null;
    }
}
