package com.dataeye;

import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import javax.servlet.http.Cookie;
import org.slf4j.Logger;
import com.xunlei.util.Log;

/**
 * 简单解析Header: Cookie
 * 
 * @author ZengDong
 * @since 2010-12-24 下午05:02:43
 */
public class CookieDecoder {

	public static final CookieDecoder INSTANCE = new CookieDecoder();
	private static final Logger log = Log.getLogger();
	public static final Set<String> RESERVED_NAMES = new TreeSet<String>();
	static {
		RESERVED_NAMES.add("$Domain");
		RESERVED_NAMES.add("$Path");
		RESERVED_NAMES.add("$Comment");
		RESERVED_NAMES.add("$CommentURL");
		RESERVED_NAMES.add("$Discard");
		RESERVED_NAMES.add("$Port");
		RESERVED_NAMES.add("$Max-Age");
		RESERVED_NAMES.add("$Expires");
		RESERVED_NAMES.add("$Version");
		RESERVED_NAMES.add("$Secure");
		RESERVED_NAMES.add("$HTTPOnly");
	}

	public static String stripQuote(String value) {
		if ((value.startsWith("\"")) && (value.endsWith("\""))) {
			try {
				return value.substring(1, value.length() - 1);
			} catch (Exception ex) {
			}
		}
		return value;
	}

	/**
	 * <pre>
	 * http://kickjava.com/src/org/apache/tomcat/util/http/Cookies.java.htm
	 * http://www.docjar.com/html/api/org/apache/tomcat/util/http/Cookies.java.html
	 * 
	 * 其他可见：
	 * http://www.docjar.com/html/api/com/sonalb/net/http/cookie/RFC2965CookieParser.java.html
	 */
	public static Map<String, Cookie> decode(String cookieString, Map<String, Cookie> map) {
		// normal cookie, with a string value.
		// This is the original code, un-optimized - it shouldn't
		// happen in normal case
		StringTokenizer tok = new StringTokenizer(cookieString, ";", false);
		// if (!tok.hasMoreTokens()) {
		// return Collections.emptyMap();
		// }
		// Map<String, Cookie> r = new HashMap<String, Cookie>();
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			int i = token.indexOf("=");
			if (i > -1) {
				// XXX
				// the trims here are a *hack* -- this should
				// be more properly fixed to be spec compliant
				String name = token.substring(0, i).trim();
				if (RESERVED_NAMES.contains(name)) {
					continue;
				}

				String value = stripQuote(token.substring(i + 1).trim()); // RFC 2109 and bug
				try {
					map.put(name, new Cookie(name, value));
				} catch (Exception e) {
					log.warn("new DefaultCookie fail,name:{},value:{}", new Object[] { name, value });// 这里不打印堆栈
				}
			} else {
				// we have a bad cookie.... just let it go
			}
		}
		return map;
	}
}
