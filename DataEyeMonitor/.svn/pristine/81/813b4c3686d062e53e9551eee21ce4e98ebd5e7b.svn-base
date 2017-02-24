package com.dataeye.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dataeye.common.CachedObjects;
import com.dataeye.common.HttpStatusCode;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.Constant.OsType;
import com.dataeye.omp.constant.Constant.ServerCfg;
import com.dataeye.omp.constant.StatusCode;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;

/**
 * <pre>
 * 常用的工具类
 * 
 * @author Ivan <br>
 * @date 2015年3月31日 下午7:39:18 <br>
 *
 */
public class ServerUtils {
	/**
	 * 
	 * <pre>
	 * 生成随机三位数字
	 * 
	 * @return 100-999之间的某个随机值
	 * @author Ivan<br>
	 * @date 2015年3月31日 下午7:42:59 <br>
	 */
	public static final int random3number() {
		return CachedObjects.RANDOM.nextInt(900) + 100;
	}

	/**
	 * 
	 * <pre>
	 * 获取16位 Request ID
	 * 
	 * @return 格式为winTimestamp+随机三位数字
	 * @author Ivan<br>
	 * @date 2015年3月31日 下午7:40:54 <br>
	 */
	public static String getResponseID() {
		long winTimestamp = DateUtils.winTimestamp();
		int random = random3number();
		return winTimestamp + "" + random;
	}

	/**
	 * 
	 * <pre>
	 * 获取请求的接口名或者jsp名
	 * 
	 * @param request
	 * @return
	 * @author Ivan<br>
	 * @date 2015年3月31日 下午8:07:00 <br>
	 */
	public static String getResourceName(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		return getBasename(requestUri);
	}

	/**
	 * 
	 * <pre>
	 * 取出基础路径名/a/b/c/d则取出最后一个d
	 * 
	 * @param uri
	 * @return
	 * @author Ivan<br>
	 * @date 2015年4月8日 下午8:41:19 <br>
	 */
	public static String getBasename(String uri) {
		int index = uri.lastIndexOf("/");
		if (index > -1) {
			return uri.substring(index + 1);
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 用UTF8 解码
	 * 
	 * @param string
	 * @return
	 * @author Ivan<br>
	 * @date 2015年3月31日 下午8:30:33 <br>
	 */
	public static final String decodeWithUTF8(String message) {
		if (ValidateUtils.isNotEmpty(message)) {
			try {
				return URLDecoder.decode(message, "UTF8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 首字母大写
	 * 
	 * @param word
	 * @return
	 * @author Ivan<br>
	 * @date 2015年4月1日 上午10:38:33 <br>
	 */
	public static String toUppercaseFirstLetter(String word) {
		if (ValidateUtils.isNotEmpty(word)) {
			return word.substring(0, 1).toUpperCase() + word.substring(1);
		}
		return word;
	}

	/**
	 * 
	 * <pre>
	 * 获取操作系统类型(非常简单的判断，不严谨)
	 * 
	 * @return
	 * @author Ivan<br>
	 * @date 2015年4月3日 下午3:37:53 <br>
	 */
	public static String getOsType() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			return OsType.WINDOWS;
		} else if (osName.contains("linux")) {
			return OsType.LINUX;
		} else {
			return OsType.OTHER;
		}
	}

	/**
	 * 
	 * <pre>
	 * 把String[]放入List<String>
	 * 
	 * @param valueArr
	 * @param valueList
	 * @author Ivan<br>
	 * @date 2015年4月7日 上午10:53:52 <br>
	 */
	public static void putStringArr2List(String[] valueArr, List<String> valueList) {
		if (ValidateUtils.isEmptyArray(valueArr) || valueList == null) {
			return;
		}
		for (String str : valueArr) {
			valueList.add(str);
		}
	}

	/**
	 * 
	 * <pre>
	 * 把接口的处理结果返回给客户端
	 * 
	 * @param json
	 * @param response
	 * @author Ivan<br>
	 * @date 2014年12月22日 下午3:28:08 <br>
	 */
	public static void writeToResponse(HttpServletResponse response, HttpStatusCode httpStatusCode, String body) {
		response.setContentType(ServerCfg.CONTENT_TYPE_JSON);
		response.setStatus(httpStatusCode.getCode());
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(body);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * 
	 * <pre>
	 * 获取数据库连接
	 * 
	 * @return
	 * @author Ivan<br>
	 * @throws ServerException
	 * @date 2015年8月12日 下午4:29:05 <br>
	 */
	public static JdbcTemplate getJdbcTemplateTracking() throws ServerException {
		Object bean = ApplicationContextUtil.getBean("jdbcTemplateTracking");
		if (bean != null && bean instanceof JdbcTemplate) {
			return (JdbcTemplate) bean;
		}
		ExceptionHandler.throwDatabaseException(StatusCode.DB_CONN_ERROR, "数据库连接异常,请稍等片刻再试,或者联系客服处理");
		return null;
	}

	/**
	 * 
	 * <pre>
	 * 根据AppID获取其存放的表位置
	 * 
	 * @param appID
	 * @param tableSize
	 * @return
	 * @author Ivan<br>
	 * @date 2015年8月14日 下午2:45:27 <br>
	 */
	public static int getTableIndex(String appID, int tableSize) {
		int index = 0;
		if (StringUtil.isNotEmpty(appID) && tableSize > 0) {
			index = Math.abs(appID.hashCode()) % tableSize;
		}
		return index;
	}

	public static void sendAlarmEmail(String to, String subject, String message) {
		Map<String, String> param = new HashMap<>();
		String alarmurl = "http://119.147.212.252:38082/alarm/mail";
		param.put("to", to);
		param.put("subject", subject);
		param.put("content", message);
		try {
			HttpClientUtil.post(alarmurl, param);
		} catch (ServerException e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
		System.out.println(precent(0.123));
	}

	/**
	 * 
	 * <pre>
	 * 保留小数点后两位
	 * 
	 * @param d
	 * @return
	 * @author Ivan<br>
	 * @date 2015年8月15日 下午2:41:11 <br>
	 */
	public static final String numberFormatDouble2RtnStr(double d) {
		return String.format("%.2f", d);
	}

	public static final double numberFormatDouble4RtnDouble(double d) {
		return Double.valueOf(String.format("%.4f", d));
	}
	
	public static final double numberFormatDouble2RtnDouble(double d) {
		return Double.valueOf(String.format("%.2f", d));
	}

	/**
	 * 得到百分数格式
	 * 
	 * @param d
	 * @return
	 * @author ivantan
	 * @date 2015年8月17日 下午2:15:22
	 */
	public static final String precent(double d) {
		return String.format("%.2f", d) + "%";
	}

	/**
	 * 保留两位小数
	 * @param d
	 * @return
	 */
	public static final double format(double d) {
		return Double.parseDouble(String.format("%.2f", d));
	}

}
