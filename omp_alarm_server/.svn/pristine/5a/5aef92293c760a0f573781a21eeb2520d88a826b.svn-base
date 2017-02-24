package com.dataeye.omp.clientAlarm.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.dataeye.omp.clientAlarm.DcAlarmDomain;
import com.dataeye.omp.clientAlarm.response.AlarmResponseContent;

public class ClientAlarmCmdsUtil {

	/**
	 * 转换从数据库查询得到的DcAlarmDomainList 为返回给客户端的AlarmResponseContentList
	 * <pre>
	 *   同时比较请求others与模糊查询得到的数据，筛选功能
	 * </pre>
	 * @param dcAlarmDomainList
	 * @return
	 */
	public static List<AlarmResponseContent> turnDcAlarmDomainListToAlarmResponseContent(List<DcAlarmDomain> dcAlarmDomainList,String others){
		List<AlarmResponseContent> resultList = new ArrayList<AlarmResponseContent>();
		if(null!=dcAlarmDomainList && 0<dcAlarmDomainList.size()){
			for(DcAlarmDomain domain : dcAlarmDomainList){				
					AlarmResponseContent content = new AlarmResponseContent();
					BeanUtils.copyProperties(domain, content);
					content.setAlarmId(domain.getId());
					resultList.add(content);
				}
		}
		
		return resultList;
	}
	
}
