package com.dataeye.omp.report.cmds;

import com.dataeye.omp.report.bean.ProcessConfig;
import com.dataeye.omp.report.dbproxy.kafka.KafkaProducerAgent;
import com.dataeye.omp.report.dao.ProcessReportDao;
import com.dataeye.omp.report.utils.Constant;
import com.qq.jutil.string.StringUtil;
import com.xunlei.netty.httpserver.cmd.BaseCmd;
import com.xunlei.netty.httpserver.cmd.CmdMapper;
import com.xunlei.netty.httpserver.component.XLHttpRequest;
import com.xunlei.netty.httpserver.component.XLHttpResponse;
import com.xunlei.util.Log;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessReportCmd extends BaseCmd {

	private final static Logger processLog = Log.getLogger("process_log");

	@Autowired
	private ProcessReportDao processDao;

	/**
	 * 批量上报进程监控数据
	 *
	 * @param req XLHttpRequest
	 * @param rsp XLHttpResponse
	 * @return Object
	 */
	@CmdMapper("/process/report")
	public Object processReport(XLHttpRequest req
			, XLHttpResponse rsp) {
		try {
			rsp.addHeader("Cache-Control", "no-cache");
			String message = req.getContentString().trim();
			putMessage2kafka(message);
			processDao.saveProcess2Hbase(message);
		} catch (Exception e) {
			processLog.error("Process Report error", e);
			return "fail";
		}
		return "succ";
	}

	private void putMessage2kafka(String message) {
		if (StringUtil.isNotEmpty(message)) {
			KafkaProducerAgent
					.pushMassage2Kafka(message
							, Constant.PROCESS_TOPIC);
		}
	}

	/**
	 * 获取进程配置信息
	 *
	 * @param req
	 * @param rsp
	 */
	@CmdMapper("/process/config")
	public Object processConfig(XLHttpRequest req
			, XLHttpResponse rsp) {
		String ip = req.getParameter("ip");
		try {
			if (StringUtil.isNotEmpty(ip)) {
				List<ProcessConfig> configList =
						processDao.getProcessConfigByIP(ip);
				Map<String, List<ProcessConfig>> map = new HashMap<>();
				map.put("process_list", configList);
				return Constant.gson.toJson(map);
			}
		} catch (Exception e) {
			processLog.error("get Process Config error", e);
			return "fail";
		}
		return "fail";
	}
}


