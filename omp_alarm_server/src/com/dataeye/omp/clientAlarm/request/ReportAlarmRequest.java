package com.dataeye.omp.clientAlarm.request;

import com.dataeye.omp.clientAlarm.response.DataResponse;
import com.dataeye.omp.common.Constant;
import com.xunlei.netty.httpserver.component.XLHttpRequest;

import java.util.ArrayList;
import java.util.List;

public class ReportAlarmRequest {
	private String title;
	private String type;
	private String content;
	private String others;
	
	private List<String> alarmReceiverMailList = new ArrayList<String>();;
	
	
	public ReportAlarmRequest(XLHttpRequest request){
		this.title=request.getParameter("title");
		this.type=request.getParameter("type");
		this.content=request.getParameter("content");
		this.others=request.getParameter("others");
	}
	
	public List<String> getAlarmReceiverMailList() {
		return alarmReceiverMailList;
	}
	public void setAlarmReceiverMailList(List<String> alarmReceiverMailList) {
		this.alarmReceiverMailList = alarmReceiverMailList;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}

	/**
	 * 请求参数校验
	 * @param dataResponse
	 * @return
	 */
	public boolean isParameterValid(DataResponse dataResponse){
		
		if(null==this.getTitle() || "".equals(this.getTitle().trim())){
			dataResponse.setStatusCode(Constant.STATUS_CODE_PARM_ERROR+"");
			dataResponse.setContent("request parameter have error,title cannot be null");
			return false;
		}		
		if(null==this.getType() || "".equals(this.getType().trim())){
			dataResponse.setStatusCode(Constant.STATUS_CODE_PARM_ERROR+"");
			dataResponse.setContent("request parameter have error,type cannot be null");
			return false;
		}
		if(null==this.getContent() || "".equals(this.getContent().trim())){
			dataResponse.setStatusCode(Constant.STATUS_CODE_PARM_ERROR+"");
			dataResponse.setContent("request parameter have error,content cannot be null");
			return false;
		}
		if(null==this.getOthers() || "".equals(this.getOthers().trim())){
			dataResponse.setStatusCode(Constant.STATUS_CODE_PARM_ERROR+"");
			dataResponse.setContent("request parameter have error,others cannot be null");
			return false;
		}		
		String[] alarmOthersArray = this.getOthers().split(",");
		for(String alarmOther : alarmOthersArray){
			this.alarmReceiverMailList.add(alarmOther);
		}		
		return true;
	}

	@Override
	public String toString() {
		return "ReportAlarmRequest [title=" + title + ", type=" + type
				+ ", content=" + content + ", others=" + others
				+ ", alarmReceiverMailList=" + alarmReceiverMailList + "]";
	}
}
