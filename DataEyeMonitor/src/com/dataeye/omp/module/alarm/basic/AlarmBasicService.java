package com.dataeye.omp.module.alarm.basic;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.PageData;
import com.qq.jutil.string.StringUtil;

@Service
public class AlarmBasicService {
	
	@Autowired
	private AlarmBasicDAO alarmBasicDAO;

	/**
	 * 查询服务器监控告警规则
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年3月4日 下午4:57:12
	 */
	public PageData<AlarmBasicRule> queryAlarmBasicRuleList(int pageID, int pageSize, String keyValue, String orderBy, int order) throws ServerException {
		PageData<AlarmBasicRule> pageData = new PageData<AlarmBasicRule>(pageSize, pageID);
		List<AlarmBasicRule> list = new ArrayList<AlarmBasicRule>();
		list = alarmBasicDAO.queryAlarmBasicRuleList();
		pageData.setContent(list);
		if (StringUtil.isNotEmpty(keyValue)) {
			searchKeyValue(list, keyValue);
		}
		pageData.setTotalRecord(list.size());
		pageData.setTotalPage();
		pageData = buildPageData(pageData, list, orderBy, order);
		return pageData;
	}

	private PageData<AlarmBasicRule> buildPageData(PageData<AlarmBasicRule> pageData, List<AlarmBasicRule> list,
			String orderBy, int order) {
		
		Comparator<AlarmBasicRule> comp = null;
		if ("featureName".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.Feature_NAME_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.Feature_NAME_DESC;
			}
		} else if ("object".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.OBJECT_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.OBJECT_DESC;
			}
		} else if ("alarmObjectType".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.ALARM_OBJECT_TYPE_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.ALARM_OBJECT_TYPE_DESC;
			}
		} else if ("servers".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.SERVERS_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.SERVERS_DESC;
			}
		} else if ("businessName".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.BUSINESS_NAME_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.BUSINESS_NAME_DESC;
			}
		} else if ("moduleName".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.MODULE_NAME_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.MODULE_NAME_DESC;
			}
		} else if ("groupName".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.GROUP_NAME_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.GROUP_NAME_DESC;
			}
		} else if ("alarmSectionType".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.ALARM_SECTION_TYPE_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.ALARM_SECTION_TYPE_DESC;
			}
		} else if ("maxThreshold".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.MAX_THRESHOLD_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.MAX_THRESHOLD_DESC;
			}
		} else if ("minThreshold".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.MIN_THRESHOLD_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.MIN_THRESHOLD_DESC;
			}
		} else if ("maxMoM".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.MAX_MOM_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.MAX_MOM_DESC;
			}
		} else if ("minMoM".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.MIN_MOM_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.MIN_MOM_DESC;
			}
		} else if ("maxFrequency".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.MAX_FREQUENCY_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.MAX_FREQUENCY_DESC;
			}
		} else if ("alarmType".equals(orderBy)) {
			if (order == 0) {
				comp = AlarmBasicRuleComparator.ALARM_TYPE_ASC;
			}
			if (order == 1) {
				comp = AlarmBasicRuleComparator.ALARM_TYPE_DESC;
			}
		}
		if (comp != null) {
			Collections.sort(list, comp);
		}
		
		// 获取分页数据
		int startIndex = (pageData.getPageID() - 1) * pageData.getPageSize();
		int endIndex = pageData.getPageID() * pageData.getPageSize() - 1;
		// startIndex就超限了
		if (startIndex >= list.size()) {
			return pageData;
		}
		endIndex = endIndex > (list.size() - 1) ? (list.size() - 1) : endIndex;
		List<AlarmBasicRule> resultList = list.subList(startIndex, endIndex + 1);

		pageData.setContent(resultList);
		return pageData;
	}

	private void searchKeyValue(List<AlarmBasicRule> list, String keyValue) {
		Set<AlarmBasicRule> listRs = new HashSet<>();
		for (AlarmBasicRule rule : list) {
			if (rule.getFeatureName() != null && rule.getFeatureName().toLowerCase().indexOf(keyValue.toLowerCase()) >= 0) {
				listRs.add(rule);
			} else if (rule.getObject() != null && rule.getObject().toLowerCase().indexOf(keyValue.toLowerCase()) >= 0) {
				listRs.add(rule);
			} else if (rule.getServers() != null && rule.getServers().toLowerCase().indexOf(keyValue.toLowerCase()) >= 0) {
				listRs.add(rule);
			} else if (rule.getBusinessName() != null && rule.getBusinessName().toLowerCase().indexOf(keyValue.toLowerCase()) >= 0) {
				listRs.add(rule);
			} else if (rule.getModuleName() != null && rule.getModuleName().toLowerCase().indexOf(keyValue.toLowerCase()) >= 0) {
				listRs.add(rule);
			} else if (rule.getGroupName() != null && rule.getGroupName().toLowerCase().indexOf(keyValue.toLowerCase()) >= 0) {
				listRs.add(rule);
			} else if (rule.getAlarmType() != null && rule.getAlarmType().toLowerCase().indexOf(keyValue.toLowerCase()) >= 0) {
				listRs.add(rule);
			}
		}

		list.clear();
		for (AlarmBasicRule rule : listRs) {
			list.add(rule);
		}
	}
}
