package com.dataeye.omp.clientAlarm.request;

import com.dataeye.omp.clientAlarm.response.DataResponse;
import com.dataeye.omp.common.Constant;
import com.xunlei.netty.httpserver.component.XLHttpRequest;

public class GetAlarmRequest {
	
	public static String QUERY_ALL_TYPE="all";//定义不管任何类型都获取到的type类型

	private String type;
	private String others;
	
	public GetAlarmRequest(XLHttpRequest request){
		this.type = request.getParameter("type");
		this.others = request.getParameter("others");
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	/**
	 * 判断请求参数是否正确
	 * @param dataResponse
	 * @return
	 */
	public boolean isParameterValid(DataResponse dataResponse){
		if(null==this.getType() || "".equals(this.getType().trim())){
			dataResponse.setStatusCode(Constant.STATUS_CODE_PARM_ERROR+"");
			dataResponse.setContent("request parameter have error,type cannot be null");
			return false;
		}
		if(null==this.getOthers() || "".equals(this.getOthers().trim())){
			dataResponse.setStatusCode(Constant.STATUS_CODE_PARM_ERROR+"");
			dataResponse.setContent("request parameter have error,others cannot be null");
			return false;
		}else if(this.getOthers().trim().endsWith(",")){
			dataResponse.setStatusCode(Constant.STATUS_CODE_PARM_ERROR+"");
			dataResponse.setContent("request parameter have error,others cannot be end with ,");
			return false;
		}		
		return true;
	}
	@Override
	public String toString() {
		return "GetAlarmRequest [type=" + type + ", others=" + others + "]";
	}
	
	
}
