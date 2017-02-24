package com.dataeye.omp.module.cmdb.device;

import com.dataeye.common.CachedObjects;
import com.dataeye.omp.common.DEParameter;
import com.google.gson.annotations.Expose;
import com.google.protobuf.Descriptors;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设备信息
 *
 * @author wendy
 * @since 2015/12/15 10:36
 */
public class Device {
    @Expose
    private int id;

    /**
     * 设备编号
     */
    @Expose
    private String devId;

    /**
     * 设备型号
     */
    @Expose
    private int devType;

    /**
     * 机房ID
     */
    @Expose
    private int idcId;

    /**
     *机房名称
     */
    @Expose
    private String idcName;

    /**
     * 机柜ID
     */
    @Expose
    private int cabinetId;

    /**
     * 机柜名称
     */
    @Expose
    private String cabinetName;

    /**
     * 主机名称
     */
    @Expose
    private String hostName;

    /**
     * 网卡数量
     */
    @Expose
    private int netCardNum;

    /**
     * CPU数量
     */
    @Expose
    private int cpuNum;

    /**
     * CPU类型
     */
    @Expose
    private int cpuType;

    /**
     * CPU物理核数
     */
    @Expose
    private int cpuPhysicalCores;

    /**
     * CPU逻辑核数
     */
    @Expose
    private int cpuLogicCores;

    /**
     * 操作系统
     */
    @Expose
    private int os;

    /**
     * 内核
     */
    @Expose
    private int kernal;

    /**
     * 内存个数
     */
    @Expose
    private int memory;

    /**
     * 磁盘数量
     */
    @Expose
    private int diskNum;

    /**
     * 磁盘容量
     */
    @Expose
    private int diskSize;

    /**
     * 磁盘详情
     */
    @Expose
    private String diskDetails;


    /**
     * 磁盘详情信息列表
     */
    @Expose
    private List<DeviceDetail> deviceDetailList;

    /**
     * 部门ID
     */
    @Expose
    private int deptId;

    /**
     * 业务ID
     */
    @Expose
    private int busiId;

    /**业务名称*/
    @Expose
    private String busiName;

    /**
     * 模块ID
     */
    @Expose
    private int moduleId;

    /**
     * 模块名称
     */
    @Expose
    private String moduleName;


    /**
     * 负责人
     */
    @Expose
    private int admin;

    /**
     * 备份负责人
     */
    @Expose
    private String backupAdmin;

    /**
     * 描述
     */
    @Expose
    private String descs;

    /**
     * 设备IP列表
     */
    @Expose
    private List<DeviceIP> deviceIPList;

    /**
     * 业务模块信息
     */
    @Expose
    private List<DeviceBusiness> busiModuleList;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public int getDevType() {
        return devType;
    }

    public void setDevType(int devType) {
        this.devType = devType;
    }

    public int getIdcId() {
        return idcId;
    }

    public void setIdcId(int idcId) {
        this.idcId = idcId;
    }

    public void setCpuType(int cpuType) {
        this.cpuType = cpuType;
    }

    public void setOs(int os) {
        this.os = os;
    }

    public int getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(int cabinetId) {
        this.cabinetId = cabinetId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getNetCardNum() {
        return netCardNum;
    }

    public void setNetCardNum(int netCardNum) {
        this.netCardNum = netCardNum;
    }

    public int getCpuNum() {
        return cpuNum;
    }

    public void setCpuNum(int cpuNum) {
        this.cpuNum = cpuNum;
    }

    public int getCpuType() {
        return cpuType;
    }

    public int getOs() {
        return os;
    }

    public int getKernal() {
        return kernal;
    }

    public void setKernal(int kernal) {
        this.kernal = kernal;
    }

    public int getCpuPhysicalCores() {
        return cpuPhysicalCores;
    }

    public void setCpuPhysicalCores(int cpuPhysicalCores) {
        this.cpuPhysicalCores = cpuPhysicalCores;
    }

    public int getCpuLogicCores() {
        return cpuLogicCores;
    }

    public void setCpuLogicCores(int cpuLogicCores) {
        this.cpuLogicCores = cpuLogicCores;
    }



    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getDiskNum() {
        return diskNum;
    }

    public void setDiskNum(int diskNum) {
        this.diskNum = diskNum;
    }

    public int getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(int diskSize) {
        this.diskSize = diskSize;
    }

    public String getDiskDetails() {
        return diskDetails;
    }

    public void setDiskDetails(String diskDetails) {
        this.diskDetails = diskDetails;
        if (StringUtils.isEmpty(diskDetails)) {
            setDeviceDetailList(new ArrayList<DeviceDetail>());
            return;
        }
        List<DeviceDetail> deviceDetails =
                CachedObjects.GSON.fromJson(diskDetails, List.class);
        setDeviceDetailList(deviceDetails);

    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getBusiId() {
        return busiId;
    }

    public void setBusiId(int busiId) {
        this.busiId = busiId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getBackupAdmin() {
        return backupAdmin;
    }

    public void setBackupAdmin(String backupAdmin) {
        this.backupAdmin = backupAdmin;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

    public List<DeviceIP> getDeviceIPList() {
        return deviceIPList;
    }

    public void setDeviceIPList(List<DeviceIP> deviceIPList) {
        this.deviceIPList = deviceIPList;
    }

    public String getIdcName() {
        return idcName;
    }

    public void setIdcName(String idcName) {
        this.idcName = idcName;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public int getDeptId() {
        return deptId;
    }

    public String getBusiName() {
        return busiName;
    }

    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setBusiModuleList(List<DeviceBusiness> busiModuleList) {
        this.busiModuleList = busiModuleList;
    }

    public List<DeviceBusiness> getBusiModuleList() {
        return busiModuleList;
    }

    public void convertParam2Device(DEParameter param){
       // Device device = new Device();
        this.setId(param.getDeviceId());
        this.setDevId(param.getDevId());
        this.setDevType(param.getDevType());
        this.setCabinetId(param.getCabinetId());
        this.setCpuNum(param.getCpuNum());
        this.setCpuPhysicalCores(param.getCpuPhysicalCores());
        this.setOs(param.getOs());
        this.setMemory(param.getMemory());
        this.setDeptId(param.getDeptId());
        this.setBackupAdmin(param.getBackupAdmin());
        this.setHostName(param.getHostName());
        this.setIdcId(param.getIdcId());
        this.setNetCardNum(param.getNetCardNum());
        this.setCpuType(param.getCpuType());
        this.setCpuLogicCores(param.getCpuLogicCores());
        this.setKernal(param.getKernal());
        this.setDiskNum(param.getDiskNum());
        this.setDiskSize(param.getDiskSize());
//        List<Map> list =
//                CachedObjects.GSON.fromJson(param.getDeviceIps(), List.class);

        List<DeviceIP> deviceIPs = new ArrayList<>();

        List<Map> list =
                CachedObjects.GSON.fromJson(param.getDeviceIps(), deviceIPs.getClass());

        for (Map<String, String> map : list) {
            DeviceIP deviceIP = new DeviceIP();
            deviceIP.setIp(map.get("ip"));
            deviceIP.setIspId(Integer.parseInt(map.get("ispId")));
            deviceIP.setType(Integer.parseInt(map.get("type")));
            deviceIPs.add(deviceIP);
        }

        this.setDeviceIPList(deviceIPs);
        this.setDiskDetails(param.getDiskDetails());


        List<DeviceBusiness> deviceBusinessList=new ArrayList<>();
        List<Map> busiModules =
                CachedObjects.GSON.fromJson(param.getBusiModules(),
                        deviceBusinessList.getClass());

        for (Map<String, String> map : busiModules) {
            DeviceBusiness business = new DeviceBusiness();
            business.setBusiId(Integer.parseInt(map.get("busiId")));
            business.setModuleId(Integer.parseInt(map.get("moduleId")));
            deviceBusinessList.add(business);
        }
        this.setBusiModuleList(deviceBusinessList);
        this.setAdmin(param.getAdmin());
        this.setDescs(param.getDescs());

    }

    public List<DeviceDetail> getDeviceDetailList() {
        return deviceDetailList;
    }

    public void setDeviceDetailList(List<DeviceDetail> deviceDetailList) {
        this.deviceDetailList = deviceDetailList;
    }


}
