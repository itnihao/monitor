package com.dataeye.omp.module.monitor.server;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.KeyValue;
import com.dataeye.omp.common.PageData;
import com.dataeye.omp.constant.Constant.Order;
import com.dataeye.omp.constant.Constant.OrderBy;
import com.dataeye.omp.module.cmdb.device.DicValue;
import com.qq.jutil.string.StringUtil;

/**
 * 
 * @author chenfanglin
 * @date 2016年1月12日 下午5:27:35
 */
@Service
public class MachineService {

	@Autowired
	private MachineDAO machineDAO;

	/**
	 * 查询主机列表
	 * 
	 * @param pageID
	 * @param pageSize
	 * @param keyValue
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月12日 下午5:28:41
	 */
	public PageData<Machine> queryRoomServerList(int pageID, int pageSize, String keyValue, String orderBy, int order, int idcId)
			throws ServerException {
		PageData<Machine> pageData = new PageData<Machine>(pageSize, pageID);
		List<Machine> machines = new ArrayList<Machine>();
		pageData.setContent(machines);
		machines = machineDAO.queryRoomServerList(idcId);
		if (StringUtil.isNotEmpty(keyValue)) {
			searchKeyValue(machines, keyValue);
		}
		pageData.setTotalRecord(machines.size());
		pageData.setTotalPage();
		pageData = buildPageData(pageData, machines, orderBy, order);
		return pageData;
	}

	/**
	 * 业务视图
	 * 
	 * @param pageID
	 * @param pageSize
	 * @param keyValue
	 * @param orderBy
	 * @param order
	 * @param bussineID
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月13日 下午3:55:15
	 */
	public PageData<Machine> queryBusinessServerList(int pageID, int pageSize, String keyValue, String orderBy, int order,
			int bussineID, int moduleID) throws ServerException {
		PageData<Machine> pageData = new PageData<Machine>(pageSize, pageID);
		List<Machine> machines = new ArrayList<Machine>();
		pageData.setContent(machines);
		machines = machineDAO.queryBusinessServerList(bussineID, moduleID);
		if (StringUtil.isNotEmpty(keyValue)) {
			searchKeyValue(machines, keyValue);
		}
		pageData.setTotalRecord(machines.size());
		pageData.setTotalPage();
		pageData = buildPageData(pageData, machines, orderBy, order);
		return pageData;
	}

	/**
	 * 分组视图
	 * @param pageID
	 * @param pageSize
	 * @param keyValue
	 * @param orderBy
	 * @param order
	 * @param groupID
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月13日 下午4:16:39
	 */
	public PageData<Machine> queryGroupServerList(int pageID, int pageSize, String keyValue, String orderBy, int order,
			int groupID) throws ServerException {
		PageData<Machine> pageData = new PageData<Machine>(pageSize, pageID);
		List<Machine> machines = new ArrayList<Machine>();
		pageData.setContent(machines);
		machines = machineDAO.queryGroupServerList(groupID);
		if (StringUtil.isNotEmpty(keyValue)) {
			searchKeyValue(machines, keyValue);
		}
		pageData.setTotalRecord(machines.size());
		pageData = buildPageData(pageData, machines, orderBy, order);
		return pageData;
	}
	
	/**
	 * 关键字搜索
	 * 
	 * @author chenfanglin <br>
	 * @date 2016年1月23日 下午3:29:38
	 */
	private void searchKeyValue(List<Machine> machines, String keyValue){
		List<Machine> list = new ArrayList<Machine>();
		for (Machine mac : machines) {
			List<KeyValue<String,Integer>> iplist = mac.getIp();
			for (KeyValue<String,Integer> ips : iplist) {
				String ip = ips.getKey();
				if (StringUtil.isNotEmpty(ip) && ip.toLowerCase().indexOf(keyValue.toLowerCase()) == -1) {
					continue;
				} else {
					list.add(mac);
				}
			}
			if (mac.getHostName().toLowerCase().indexOf(keyValue.toLowerCase()) == -1) { // 忽略大小写
                continue;
            } else {
            	list.add(mac);
            }
		}
		machines.clear();
		machines.addAll(list);
	}
	
	/**
	 * 根据排序规则和分页数据封装返回结果
	 * @param pageData
	 * @param machines
	 * @param orderBy
	 * @param order
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月13日 下午4:10:05
	 */
	private PageData<Machine> buildPageData(PageData<Machine> pageData, List<Machine> machines, String orderBy, int order) {
		// 根据排序规则和分页数据封装返回结果
		Comparator<Machine> comparator = MachineComparator.DEFAULT_ID_DESC;
		if (OrderBy.ORDERBY_HOSTNAME.equals(orderBy)) {
			if (Order.ASC == order) {
				comparator = MachineComparator.CLICK_HOSTNAME_ASC;
			} else {
				comparator = MachineComparator.CLICK_HOSTNONE_DESC;
			}
		}
//		if (OrderBy.ORDERBY_IP.equals(orderBy)) {
//			if (Order.ASC == order) {
//				comparator = MachineComparator.CLICK_IP_ASC;
//			} else {
//				comparator = MachineComparator.CLICK_IP_DESC;
//			}
//		}
		if (OrderBy.ORDERBY_MACHINEROOM.equals(orderBy)) {
			if (Order.ASC == order) {
				comparator = MachineComparator.CLICK_MACHINEROOM_ASC;
			} else {
				comparator = MachineComparator.CLICK_MACHINEROOM_DESC;
			}
		}
		if (OrderBy.ORDERBY_BUSINESS.equals(orderBy)) {
			if (Order.ASC == order) {
				comparator = MachineComparator.CLICK_BUSINESS_ASC;
			} else {
				comparator = MachineComparator.CLICK_BUSINESS_DESC;
			}
		}
		if (OrderBy.ORDERBY_CPUUSAGE.equals(orderBy)) {
			if (Order.ASC == order) {
				comparator = MachineComparator.CLICK_CPUUSAGE_ASC;
			} else {
				comparator = MachineComparator.CLICK_CPUUSAGE_DESC;
			}
		}
		if (OrderBy.ORDERBY_FIVELOAD.equals(orderBy)) {
			if (Order.ASC == order) {
				comparator = MachineComparator.CLICK_FIVELOAD_ASC;
			} else {
				comparator = MachineComparator.CLICK_FIVELOAD_DESC;
			}
		}
		if (OrderBy.ORDERBY_STATUS.equals(orderBy)) {
			if (Order.ASC == order) {
				comparator = MachineComparator.CLICK_STATUS_ASC;
			} else {
				comparator = MachineComparator.CLICK_STATUS_DESC;
			}
		}
		Collections.sort(machines, comparator);

		// 获取分页数据
		int startIndex = (pageData.getPageID() - 1) * pageData.getPageSize();
		int endIndex = pageData.getPageID() * pageData.getPageSize() - 1;
		// startIndex就超限了
		if (startIndex >= machines.size()) {
			return pageData;
		}
		endIndex = endIndex > (machines.size() - 1) ? (machines.size() - 1) : endIndex;
		List<Machine> resultList = machines.subList(startIndex, endIndex + 1);
		pageData.setContent(resultList);
		return pageData;
	}

	/**
	 * 根据业务ID查询模块
	 * @param busiId
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月13日 下午2:20:25
	 */
	public List<DicValue> queryModuleByBusiness(int busiId) throws ServerException {
		List<DicValue> values = machineDAO.queryModuleByBusiness(busiId);
		return values;
	}
	
	/**
	 * 给分组添加机器
	 * @param groupID
	 * @param serverID
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月13日 下午5:11:45
	 */
	public long addMachineForGroup(int groupID, int serverID) throws ServerException  {
		return machineDAO.addMachineForGroup(groupID, serverID);
	}


	public Map<String,Object> getServerBasisData(int serverID) throws ServerException {
		return machineDAO.getServerBasisData(serverID);
	}
}
