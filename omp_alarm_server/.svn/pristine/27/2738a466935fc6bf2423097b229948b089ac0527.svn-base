package com.dataeye.omp.clientAlarm.request;

import java.util.ArrayList;
import java.util.List;

import com.dataeye.omp.clientAlarm.response.DataResponse;
import com.dataeye.omp.common.Constant;
import com.xunlei.netty.httpserver.component.XLHttpRequest;

public class CloseAlarmRequest {

	private String idListString;
	
	private String closeUser;
	
	private List<Integer> idList = new ArrayList<Integer>();
	
	public CloseAlarmRequest(XLHttpRequest request){
		this.idListString = request.getParameter("id");
		this.closeUser=request.getParameter("closeUser");
	}

	public String getCloseUser() {
		return closeUser;
	}
	public void setCloseUser(String closeUser) {
		this.closeUser = closeUser;
	}


	public String getIdListString() {
		return idListString;
	}

	public void setIdListString(String idListString) {
		this.idListString = idListString;
	}

	public List<Integer> getIdList() {
		return idList;
	}

	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}

	/**
	 * 判断请求参数是否合法
	 * @param dataResponse
	 * @return
	 */
	public boolean isParameterValid(DataResponse dataResponse){
		if(null==this.getIdListString() || "".equals(this.getIdListString().trim())){
			dataResponse.setStatusCode(Constant.STATUS_CODE_PARM_ERROR+"");
			dataResponse.setContent("request parameter have error,id cannot be null");
			return false;
		}else if(this.getIdListString().trim().endsWith(",")){
			dataResponse.setStatusCode(Constant.STATUS_CODE_PARM_ERROR+"");
			dataResponse.setContent("request parameter have error,id cannot be end with ,");
			return false;
		}
		if(null==this.getCloseUser() || "".equals(this.getCloseUser().trim())){
			dataResponse.setStatusCode(Constant.STATUS_CODE_PARM_ERROR+"");
			dataResponse.setContent("request parameter have error,closeUser cannot be null");
			return false;
		}
		try{
			String[] idStringArray = this.getIdListString().split(",");
			for(String idString : idStringArray){
				int id = Integer.valueOf(idString.trim());
				this.idList.add(id);
			}
		}catch(Exception e){
			dataResponse.setStatusCode(Constant.STATUS_CODE_PARM_ERROR+"");
			dataResponse.setContent("request parameter have error,id pattern is illegal");
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "CloseAlarmRequest [idListString=" + idListString
				+ ", closeUser=" + closeUser + ", idList=" + idList + "]";
	}
}
