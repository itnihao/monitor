package com.dataeye.omp.module.cmdb.device;

import com.dataeye.exception.ExceptionHandler;
import com.dataeye.omp.common.KeyValue;
import com.dataeye.omp.common.PageData;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.omp.module.cmdb.business.Business;
import com.dataeye.omp.module.cmdb.business.BusinessDao;
import com.dataeye.omp.module.cmdb.employee.Department;
import com.dataeye.omp.module.cmdb.employee.DepartmentDao;
import com.dataeye.common.CachedObjects;
import com.dataeye.exception.ClientException;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.module.cmdb.employee.EmployeeDao;
import com.dataeye.utils.ValidateUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author wendy
 * @since  2015/12/15 11:25
 */
@Service
public class DeviceService {

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private BusinessDao businessDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private EmployeeDao employeeDao;

    /**
     * 添加设备信息
     * @param device
     * @throws ServerException
     * @throws ClientException
     */
    public void addDevice(Device device)
            throws ServerException, ClientException {

        //验证设备号  设备名称重复
        boolean isUsed =deviceDao.checkDeviceExists(
                device.getId(),device.getDevId(),device.getHostName());
        if(isUsed){
            ExceptionHandler.throwParameterException(
                    StatusCode.EXISTS);
        }
        boolean isIpused = checkIpAvailable(device.getDeviceIPList(), device.getId());
        if (isIpused) {
            ExceptionHandler.throwParameterException(
                    StatusCode.IP_USERD);
        }

        //插入设备基本信息
        long id = deviceDao.addDevice(device);

        // 插入设备ip信息
        deviceDao.batchInsertServerIp(device.getDeviceIPList(),id);
        //插入业务信息
        deviceDao.batchInsertBusiness(device.getBusiModuleList(), id);
    }

    private boolean checkIpAvailable(List<DeviceIP> deviceiPList, int devId)
            throws ClientException, ServerException {
        boolean isIpused = false;
        for (DeviceIP deviceIP : deviceiPList) {
            isIpused = deviceDao.checkIpUsed(deviceIP.getIp(), devId);
            if (isIpused) {
                return true;
            }
        }
        return isIpused;
    }

    /**
     * 修改设备
     * @param device
     */
    public void updateDevice(Device device)
            throws ServerException, ClientException {

        //验证设备号  设备名称重复
        boolean isUsed = deviceDao.checkDeviceExists(
                device.getId(),device.getDevId(),device.getHostName());
        if(isUsed){
            ExceptionHandler.throwParameterException(
                    StatusCode.EXISTS);
        }
        boolean isIpused = checkIpAvailable(device.getDeviceIPList(), device.getId());
        if (isIpused) {
            ExceptionHandler.throwParameterException(
                    StatusCode.IP_USERD);
        }

        deviceDao.updateDevice(device);
        deviceDao.deleteServerIpByDevId(device.getId());
        deviceDao.batchInsertServerIp(device.getDeviceIPList(), device.getId());
        deviceDao.deleteBusiModuleByDevId(device.getId());
        deviceDao.batchInsertBusiness(device.getBusiModuleList(), device.getId());
    }

    /**
     * 删除设备
     * @param id
     */
    public void deleteDevice(int id) throws ServerException, ClientException {
        // 如果设备上有业务则不能删除
        boolean isUsed = deviceDao.checkDeviceUsed(id);
        if(isUsed){
            ExceptionHandler.throwParameterException(
                    StatusCode.CAN_NOT_DELETE);
        }
        deviceDao.deleteDevice(id);
    }

    /**
     * 分页获取设备信息列表
     * @param pageId
     * @param pageSize
     * @return
     * @throws ServerException
     */
    public PageData getDeviceList(int searchType,String searchKey,
          int pageId,int pageSize) throws ServerException {
        PageData pageData = null;

        if (ValidateUtils.isEmpty(searchKey) || searchType == 0) {
            pageData =
                    deviceDao.getDeviceListByAll(pageId, pageSize, searchKey);

        } else if (searchType == 1) {
            pageData =
                    deviceDao.getDeviceListByIDC(pageId, pageSize, searchKey);

        } else if (searchType == 2) {
            pageData =
                    deviceDao.getDeviceListByBusi(pageId, pageSize, searchKey);
        }

        //为每个设备设置Ip信息
        setDeviceIp(pageData);
        return pageData;
    }

    /**
     * 设置设备Ip信息
     * @param pageData
     * @throws ServerException
     */
    private void setDeviceIp(PageData pageData) throws ServerException {
        List<Device> deviceList =
                (List<Device>)pageData.getContent();

        List<DeviceIP> iPlist=deviceDao.getAllDeviceIp();

        for(Device device:deviceList){
            List<DeviceIP> subList = new ArrayList<>();
            for(DeviceIP deviceIP:iPlist){
                if (device.getId() == deviceIP.getDevId()) {
                    subList.add(deviceIP);
                }
            }
            device.setDeviceIPList(subList);
        }
    }

    /**
     * 获取下拉初始数据
     * @return
     * @throws ServerException
     */
    public Map<String,List<DicValue>> getDeviceInitData()
            throws ServerException {
        Map<String,List<DicValue>> map = new HashMap();
        map.putAll(deviceDao.getDictionaryData());
        map.put("idc",deviceDao.getIdcDicList());
        map.put("cabinet",deviceDao.getCabinetDicList());
        map.put("business",businessDao.getBusiDicList());
        map.put("module",businessDao.getModuleDicList());
        map.put("isp", deviceDao.getIspList());
        map.put("dept", departmentDao.getDeptDicList());
        map.put("employee", employeeDao.getEmployeeSelectData());
        return map;
    }

    public List<DicValue> getSelectDeviceList() throws ServerException {
        List<DicValue> list=deviceDao.getSelectDeviceList();
        return list;
    }

}
