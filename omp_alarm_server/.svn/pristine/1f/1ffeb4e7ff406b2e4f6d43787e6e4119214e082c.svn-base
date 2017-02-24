package com.dataeye.omp.clientAlarm;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.omp.clientAlarm.request.CloseAlarmRequest;
import com.dataeye.omp.clientAlarm.request.GetAlarmRequest;
import com.dataeye.omp.clientAlarm.request.ReportAlarmRequest;
import com.dataeye.omp.clientAlarm.response.AlarmResponseContent;
import com.dataeye.omp.clientAlarm.response.DataResponse;
import com.dataeye.omp.clientAlarm.util.ClientAlarmCmdsUtil;
import com.xunlei.json.JSONUtil;
import com.xunlei.netty.httpserver.cmd.BaseCmd;
import com.xunlei.netty.httpserver.cmd.CmdMapper;
import com.xunlei.netty.httpserver.component.XLHttpRequest;
import com.xunlei.netty.httpserver.component.XLHttpResponse;
/**
 * 
 * <pre>
 * 客户端告警服务接口提供，包含客户端上报告警接口，客户端拉取告警数据接口，客户端关闭告警数据接口  三个接口
 * <pre>
 *   改善原有的服务端邮件告警方式
 * </pre>
 * @author chenfanglin          <br>
 * @date 2015-10-20 上午10:20:04 <br>
 * @version 1.0
 * <br>
 */
@Service
public class ClientAlarmCmds extends BaseCmd{
	
	@Autowired
	private DcAlarmDao dcAlarmDao;

	//客户端上报告警接口
	@CmdMapper("/de/reportAlarm")
	public Object reportAlarm(XLHttpRequest request, XLHttpResponse response) throws Exception {
		DataResponse dataResponse = new DataResponse();		
		ReportAlarmRequest reportAlarmRequest = new ReportAlarmRequest(request);			
		if(reportAlarmRequest.isParameterValid(dataResponse)){
			DcAlarmDomain dcAlarmDomain = new DcAlarmDomain();
			BeanUtils.copyProperties(reportAlarmRequest, dcAlarmDomain);
			//插入到对外告警表【关闭会正常删除】
			long insertId = dcAlarmDao.insertDcAlarmData(dcAlarmDomain);	
			//插入到备份告警表【数据不删除，只更新】
			dcAlarmDao.insertDcAlarmBakData(dcAlarmDomain,insertId);
		}
		return JSONUtil.fromObject(dataResponse, false);
	}
	//客户端获取告警数据接口
	@CmdMapper("/de/getAlarm")
	public Object getAlarm(XLHttpRequest request,XLHttpResponse response) throws Exception {
		DataResponse dataResponse = new DataResponse();	
		GetAlarmRequest getAlaramRequest = new GetAlarmRequest(request);
		if(getAlaramRequest.isParameterValid(dataResponse)){
			DcAlarmDomain dcAlarmDomain = new DcAlarmDomain();
			BeanUtils.copyProperties(getAlaramRequest, dcAlarmDomain);
			//根据type+others查询告警数据
			List<DcAlarmDomain> resultDomainList = dcAlarmDao.queryDcAlarmDataByTypeAndOthers(dcAlarmDomain);
			List<AlarmResponseContent> resultContentList = ClientAlarmCmdsUtil.turnDcAlarmDomainListToAlarmResponseContent(resultDomainList,getAlaramRequest.getOthers());			
			dataResponse.setContent(resultContentList);					
		}		
		return JSONUtil.fromObject(dataResponse, false);
	}
	//客户端关闭告警接口
	@CmdMapper("/de/closeAlarm")
	public Object closeAlarm(XLHttpRequest request,XLHttpResponse response) throws Exception {
		DataResponse dataResponse = new DataResponse();	
		CloseAlarmRequest closeAlarmRequest = new CloseAlarmRequest(request);
		if(closeAlarmRequest.isParameterValid(dataResponse)){
			//根据id删除告警
			dcAlarmDao.batchDeleteDcAlarmById(closeAlarmRequest.getIdList());
			//根据normal_alarm_id批量更新备份数据库中的告警关闭人
			dcAlarmDao.batchUpdateDcAlarmBakDataByNormalAlarmId(closeAlarmRequest.getCloseUser(),closeAlarmRequest.getIdList());						
		}		
		return JSONUtil.fromObject(dataResponse, false);
	}		
}
