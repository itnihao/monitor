package com.dataeye.omp.alarm.server;

import java.util.Map;
import java.util.NavigableMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xunlei.jdbc.JdbcTemplate;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.omp.alarm.custom.CustomAlarmHandle;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.dbproxy.hbase.HbaseProxyClient;
import com.dataeye.util.log.DELogger;
import com.qq.jutil.string.StringUtil;
import com.xunlei.netty.httpserver.cmd.BaseCmd;
import com.xunlei.netty.httpserver.cmd.CmdMapper;
import com.xunlei.netty.httpserver.component.XLHttpRequest;
import com.xunlei.netty.httpserver.component.XLHttpResponse;

import javax.annotation.Resource;

@Service
public class AlarmCustomizeCmd extends BaseCmd{

	private final static Logger server_log = DELogger.getLogger("server_log");
	private static ExecutorService executor = Executors.newFixedThreadPool(100);
	@Autowired
	private AlarmServerUtils alarmServerUtils;
	
	@Autowired
	private CustomAlarmHandle customAlarmHandle;

	@Resource(name = "jdbcTemplateMonitor")
	private JdbcTemplate jdbcTemplateMonitor;
	
	/**
	 * 自定义告警
	 * 参数：
	 * alarmItem 告警项
	 * alarmObject 告警对象
	 * subject 告警主题
	 * content 告警内容
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author chenfanglin <br>
	 * @date 2016年3月3日 下午5:33:11
	 */
	@CmdMapper("/alarm/customize")
	public Object alarmCustomize(XLHttpRequest request, XLHttpResponse response) throws Exception {
		final String alarmItem = request.getParameter("alarmItem");
		final String alarmObject = request.getParameter("alarmObject");
		final String subject = request.getParameter("subject");
		final String content = request.getParameter("content");

		if (StringUtil.isEmpty(alarmItem)) {
			return "empty to alarmItem";
		}

		if (StringUtil.isEmpty(alarmObject)) {
			return "empty alarmObject";
		}

		if (StringUtil.isEmpty(subject) || StringUtil.isEmpty(content)) {
			return "empty subject,content";
		}

		if (!isValidCharacter(alarmItem) || !isValidCharacter(alarmObject)) {
			return "fail";
		}

		if (!isAlarmItemOpened(alarmItem)) {
			return alarmItem + " is not in use";
		}

		// 告警对象写入Hbase数据库
		if (StringUtil.isNotEmpty(alarmObject)) {
			executor.submit(new Runnable() {
				public void run() {
					try {
						saveAlarmObjectToHbase(alarmItem, alarmObject);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		// TODO 我想使用 redis 缓存，之后添加
		executor.submit(new Runnable() {
			public void run() {
				customAlarmHandle.handle(alarmItem, alarmObject, subject, content);
			}
		});
		return true;
	}


	public static boolean isValidCharacter(String alarmObject) {
		String[] skipCharacter = {",", "=", "'", "(", ")", "%", "*", "[", "]", "\\", ":", ".","&","@","|","^"};
		for (String s : skipCharacter) {
			if (alarmObject.contains(s)) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		String object = "test) AND (SELECT 1219 FROM(SELECT COUNT(*),CONCAT(0x736475685978,(SELECT (CASE WHEN (1219=1219) THEN 1 ELSE 0 END)),0x614b59495470,FLOOR(RAND(0)*2))x FROM INFORMATION_SCHEMA.CHARACTER_SETS GROUP BY x)a) AND (1219=1219";

		System.out.println(isValidCharacter("("));
	}


	private boolean isAlarmItemOpened(String alermItem) {
		String sql = "select status from dc_alarm_customize_info where alarmItem=?";
		int stauts = jdbcTemplateMonitor.queryForInt(sql, alermItem);
		return stauts == 1;
	}
	
	/**
	 * 告警对象写入Hbase数据库
	 * 
	 * @author chenfanglin <br>
	 * @throws Exception 
	 * @date 2016年3月5日 下午3:42:20
	 */
	private void saveAlarmObjectToHbase(String alarmItem, String alarmObject) throws Exception{
		// 1.先检查告警对象是否存在Hbase中，存在则不插入，不存在就插入
		if (!isExistAlarmObject(alarmItem, alarmObject)) {
			// 2.不存在，写入数据库
			// 使用UUID前八位作为列
			String key = "";
			synchronized (this) {
				UUID uuid = UUID.randomUUID();
				key = uuid.toString().substring(0, 8);
			}
			HbaseProxyClient.addRecord(Constant.OMP_CUSTOMIZE_ALARM_OBJECT, alarmItem, Constant.COLUMNFAMILY, key, alarmObject);
		}
		
	}
	
	/**
	 * 先检查告警对象是否存在Hbase中，存在则不插入，存在就不插入
	 * @param alarmItem
	 * @param alarmObject
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年3月5日 下午3:58:27
	 */
	private boolean isExistAlarmObject(String alarmItem, String alarmObject){
		try {
			Result result = HbaseProxyClient.getOneRecord(Constant.OMP_CUSTOMIZE_ALARM_OBJECT, alarmItem);
			if (result != null && !result.isEmpty()) {
				NavigableMap<byte[], byte[]> resultMap = result.getFamilyMap(Bytes.toBytes(Constant.COLUMNFAMILY));
				if (resultMap != null && resultMap.size() > 0) {
					for (Map.Entry<byte[], byte[]> entry : resultMap.entrySet()) {
						String value = Bytes.toString(entry.getValue());
						if (alarmObject.equals(value)) {
							// 存在
							return true;
						}
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 不入库
			return true;
		}
		return false;
	}
}
