package com.dataeye.omp.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.dataeye.exception.ClientException;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.omp.module.cmdb.device.Device;
import com.dataeye.utils.CookieUtils;
import com.dataeye.utils.ReflectUtils;
import com.dataeye.utils.ServerUtils;
import com.dataeye.utils.Tracker;
import com.dataeye.utils.ValidateUtils;
import com.qq.jutil.string.StringUtil;

/**
 * <pre>
 * 与参数解析相关的对象
 *
 * @author Ivan <br>
 * @date 2015年3月31日 下午7:34:05 <br>
 */
public class DEParameter extends Object {
    // ***********************************系统自己设置的,没有提供set方法的属性*********************************
    /**
     * 保存该类所有的属性以及父类属性
     */
    public static Set<String> FIELDSET;
    /**
     * 保存该类所有的方法以及父类方法
     */
    public static Set<String> METHODSET;

    /**
     * 类加载的时候通过反射获取该类所有的属性以及方法
     */
    static {
        FIELDSET = ReflectUtils.getAllFieldsIncludeParent(DEParameter.class);
        METHODSET = ReflectUtils.getAllMethodIncludeParent(DEParameter.class);
    }

    /**
     * 存放所有参数
     */
    private Map<String, String[]> parameterMap = new HashMap<String, String[]>();

    /**
     * 获取所有参数
     * @return
     */
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    private Device device;

    public Device getDevice() {
        return device;
    }

    /**
     * request uri
     */
    private String uri;
    /**
     * 接口名
     */
    private String doName;
    /**
     * 查询字符串
     */
    private String queryString;
    /**
     * url
     */
    private String url;

    /**
     * cookie
     */
    private Map<String, Cookie> cookieMap;
    /**
     * 语言
     */
    private String lang;

    private String ID;

    // ***********************************接收参数的属性*********************************
    /**
     * 每页显示多少条记录
     */
    private int pageSize = 10;

    /**
     * 请求第几页
     */
    private int pageID = 1;
    
    private String orderBy;
    
    private int order;
    
    private int groupID;
    
    private String groupName;
    
    private int serverID;
    
    private int featrueID;

    private int alarmLevel;
    
    private String startdate;
    
    private String enddate;
    
    // 对比开始时间
    private String starttime;
    
    // 对比结束时间
    private String endtime;
    
    private String email;
    
    private String password;
    
    // 采集对象
    private String object;

    /**监控机器列表 */
    private String servers;
    
    private BigDecimal maxThreshold;
    
    private BigDecimal minThreshold;
    
    private BigDecimal maxMoM;
    
    private BigDecimal minMoM;
    
    private int maxFrequency;
    
    private String alarmType;

    private String shieldStart;
    
    private String shieldEnd;

    //告警区间类型  0 阀值  1 环比
    private int alarmSectionType;

    //告警对象类型  0 机器 1业务
    private int alarmObjectType;

    private int alarmRuleId;
    
    /**设备主键id*/
    private int deviceId;

    /**设备编号*/
    private String devId;

    /**设备型号*/
    private int devType;

    /**机房ID*/
    private int idcId;

    /**机柜ID*/
    private int cabinetId;

    /**主机名称*/
    private String hostName;

    /**网卡数量*/
    private int netCardNum;

    /**CPU数量*/
    private int cpuNum;

    /**CPU类型*/
    private int cpuType;

    /**CPU物理核数*/
    private int cpuPhysicalCores;

    /**CPU逻辑核数*/
    private int cpuLogicCores;

    /**操作系统*/
    private int os;

    /**内核*/
    private int kernal;

    /**内存个数*/
    private int memory;

    /**磁盘数量*/
    private int diskNum;

    /**磁盘容量*/
    private int diskSize;

    /**磁盘详情*/
    private String diskDetails;

    /**设备Ip列表，json字符串*/
    private String deviceIps;

    /**单个设备IP*/
    private String ip;

    /**部门ID*/
    private int deptId;

    /**业务ID*/
    private int busiId;

    /**模块ID*/
    private int moduleId;

    /**负责人*/
    private int admin;

    /**备份负责人*/
    private String backupAdmin;

    /**描述*/
    private String descs;

    /**查询类别*/
    private int searchType;

    /**查询关键字*/
    private String searchKey;

    /**业务名称*/
    private String busiName;

    /**运维负责人*/
    private String omPerson;

    /**模块名称*/
    private String moduleName;

    private int teamId;

    private String itemId;

    private String busiModules;

    /**进程ID*/
    private int processID;

    private String processName;

    public int busiID;

    /**模块ID*/
    private int moduleID;

    /**端口*/
    private int port;

    /**部署路径*/
    private String deployPath;

    /**配置路径*/
    private String configPath;

    /**日志路径*/
    private String logPath;

    /**主要负责人*/
    private int mainPrincipal;

    /**备份负责人 多个*/
    private String bakPrincipal;

    private String app;

    private int verifyCode;

    private int pid;

    private String cmd;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public int getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(int verifyCode) {
        this.verifyCode = verifyCode;
    }

    public int getBusiID() {
        return busiID;
    }

    public void setBusiID(int busiID) {
        this.busiID = busiID;
    }

    private int monitorType;

    public int getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(int monitorType) {
        this.monitorType = monitorType;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDeployPath() {
        return deployPath;
    }

    public void setDeployPath(String deployPath) {
        this.deployPath = deployPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public int getMainPrincipal() {
        return mainPrincipal;
    }

    public void setMainPrincipal(int mainPrincipal) {
        this.mainPrincipal = mainPrincipal;
    }

    public String getBakPrincipal() {
        return bakPrincipal;
    }

    public void setBakPrincipal(String bakPrincipal) {
        this.bakPrincipal = bakPrincipal;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }


    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public int getCabinetId() {
        return cabinetId;
    }

    public String getHostName() {
        return hostName;
    }

    public int getNetCardNum() {
        return netCardNum;
    }

    public int getCpuNum() {
        return cpuNum;
    }

    public int getCpuPhysicalCores() {
        return cpuPhysicalCores;
    }

    public int getCpuLogicCores() {
        return cpuLogicCores;
    }

    public int getMemory() {
        return memory;
    }

    public int getDiskNum() {
        return diskNum;
    }

    public int getDiskSize() {
        return diskSize;
    }

    public String getDiskDetails() {
        return diskDetails;
    }

    public int getDeptId() {
        return deptId;
    }

    public int getBusiId() {
        return busiId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public int getAdmin() {
        return admin;
    }

    public String getBackupAdmin() {
        return backupAdmin;
    }

    public String getDescs() {
        return descs;
    }

    public int getSearchType() {
        return searchType;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public String getBusiName() {
        return busiName;
    }

    public String getOmPerson() {
        return omPerson;
    }

    public String getModuleName() {
        return moduleName;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setCabinetId(int cabinetId) {
        this.cabinetId = cabinetId;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setNetCardNum(int netCardNum) {
        this.netCardNum = netCardNum;
    }

    public void setCpuNum(int cpuNum) {
        this.cpuNum = cpuNum;
    }


    public void setCpuPhysicalCores(int cpuPhysicalCores) {
        this.cpuPhysicalCores = cpuPhysicalCores;
    }

    public void setCpuLogicCores(int cpuLogicCores) {
        this.cpuLogicCores = cpuLogicCores;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public void setDiskNum(int diskNum) {
        this.diskNum = diskNum;
    }

    public void setDiskSize(int diskSize) {
        this.diskSize = diskSize;
    }

    public void setDiskDetails(String diskDetails) {
        this.diskDetails = diskDetails;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public void setBusiId(int busiId) {
        this.busiId = busiId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public void setBackupAdmin(String backupAdmin) {
        this.backupAdmin = backupAdmin;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }

    public void setOmPerson(String omPerson) {
        this.omPerson = omPerson;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public static Set<String> getFIELDSET() {
        return FIELDSET;
    }

    public static void setFIELDSET(Set<String> FIELDSET) {
        DEParameter.FIELDSET = FIELDSET;
    }

    public int getKernal() {
        return kernal;
    }

    public void setKernal(int kernal) {
        this.kernal = kernal;
    }

    public int getOs() {
        return os;
    }

    public void setOs(int os) {
        this.os = os;
    }

    public int getCpuType() {
        return cpuType;
    }

    public void setCpuType(int cpuType) {
        this.cpuType = cpuType;
    }

    public int getIdcId() {
        return idcId;
    }

    public void setIdcId(int idcId) {
        this.idcId = idcId;
    }

    public int getDevType() {
        return devType;
    }

    public void setDevType(int devType) {
        this.devType = devType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDeviceIps() {
        return deviceIps;
    }

    public void setDeviceIps(String deviceIps) {
        this.deviceIps = deviceIps;
    }

    public String getBusiModules() {
        return busiModules;
    }

    public void setBusiModules(String busiModules) {
        this.busiModules = busiModules;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getAlarmSectionType() {
        return alarmSectionType;
    }

    public void setAlarmSectionType(int alarmSectionType) {
        this.alarmSectionType = alarmSectionType;
    }

    public int getAlarmObjectType() {
        return alarmObjectType;
    }

    public void setAlarmObjectType(int alarmObjectType) {
        this.alarmObjectType = alarmObjectType;
    }

    public int getAlarmRuleId() {
        return alarmRuleId;
    }

    public void setAlarmRuleId(int alarmRuleId) {
        this.alarmRuleId = alarmRuleId;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public int getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(int alarmLevel) {
        this.alarmLevel = alarmLevel;
    }



    /**
     * <pre>
     * 参数名
     *
     * @author Ivan <br>
     * @version 1.0 <br>
     * @date 2015年4月8日 下午4:38:06 <br>
     */
    public static class Keys {

        public static final String EMAIL = "email";

        public static final String PASSWORD = "password";

        /**
         * 设备编号
         */
        public static final String DEVID = "devId";

        /**
         * 设备型号
         */
        public static final String DEV_TYPE = "devType";

        /**
         * 机房ID
         */
        public static final String IDC_ID = "idcId";

        /**
         * 机柜ID
         */
        public static final String CABINETID = "cabinetId";

        /**
         * 主机名称
         */
        public static final String HOST_NAME = "hostName";

        /**
         * 网卡数量
         */
        public static final String NETCARD_NUM = "netCardNum";

        /**
         * CPU数量
         */
        public static final String CPU_NUM = "cpuNum";

        /**
         * CPU类型
         */
        public static final String CPU_TYPE = "cpuType";

        /**
         * CPU物理核数
         */
        public static final String CPU_PHYSICAL_CORES = "cpuPhysicalCores";


        /**
         * CPU逻辑核数
         */
        public static final String CPU_LOGIC_CORES = "cpuLogicCores";

        /**
         * 操作系统
         */
        public static final String OS = "os";

        /**
         * 内核
         */
        public static final String KERNEL = "kernal";

        /**
         * 内存个数
         */
        public static final String MEMORY = "memory";

        /**
         * 磁盘数量
         */
        public static final String DISKNUM = "diskNum";

        /**
         * 磁盘容量
         */
        public static final String DISKSIZE = "diskSize";

        /**
         * 磁盘详情
         */
        public static final String DISKDETAILS = "diskDetails";

        public static final String DEVICEIPS = "deviceIps";

        public static final String IP = "ip";

        /**
         * 部门ID
         */
        public static final String DEPTID = "deptId";

        /**
         * 业务ID
         */
        public static final String BUSI_ID = "busiId";

        public static final String BUSIID = "busiID";

        /**
         * 模块ID
         */
        public static final String MODULE_ID = "moduleID";

        /**
         * 负责人
         */
        public static final String ADMIN = "admin";

        /**
         * 备份负责人
         */
        public static final String BACKUPADMIN = "backupAdmin";

        /**
         * 描述
         */
        public static final String DESCS = "descs";

        /**
         * 查询开始时间
         */
        public static final String STARTDATE = "startdate";

        /**
         * 结束时间
         */
        public static final String ENDDATE = "enddate";

        public static final String STARTTIME = "starttime";

        public static final String ENDTIME = "endtime";

        /**
         * 分页号
         */
        public static final String PAGEID = "pageID";

        /**
         * 每页大小
         */
        public static final String PAGESIZE = "pageSize";

        /**
         * 查询列别
         */
        public static final String SEARCHTYPE = "searchType";

        /**
         * 查询关键字
         */
        public static final String SEARCHKEY = "searchKey";

        /**
         * 业务名称
         */
        public static final String BUSINAME = "busiName";

        /**
         * 模块名称
         */
        public static final String MODULENAME = "moduleName";

        /**
         * 运维负责人
         */
        public static final String OMPERSION = "omPerson";

        /**
         * 小组id
         */
        public static final String TEAMID = "teamId";

        public static final String GROUPID = "groupID";

        public static final String SERVERID = "serverID";
        public static final String ITEMID = "itemId";
        public static final String FEATRUEID = "featrueID";


        public static final String OBJECT = "object";

        /**进程ID*/
        public static final String PROCESSID = "processID";

        /**进程*/
        public static final String PROCESS_NAME = "processName";

        /**端口*/
        public static final String PORT = "port";

        public static final String SERVERS = "servers";


        /**部署路径*/
        public static final String DEPLOYPATH = "deployPath";

        /**主要负责人*/
        public static final String MAIN_PRINCIPAL = "mainPrincipal";

        public static final String MONITOR_TYPE = "monitorType";

        public static final String ALARM_INTERVAL = "alarmInterval";

        public static final String MAX_FREQUENCY = "maxFrequency";

        public static final String ALARM_TYPE = "alarmType";

        public static final String MONITORITEM = "monitorItem";




        //---------------- 自定义告警参数名 --------------------

        /**
         * 自定义告警id
         */
        public static final String CUSTOMIZE_ALARM_ID = "customizeAlarmId";
        /**
         * 告警项
         */
        public static final String ALARM_ITEM = "alarmItem";
        /**
         * 主要负责人
         */
        public static final String MAIN_EMPLOYEE = "mainEmployee";
        /**
         * 其他负责人
         */
        public static final String OTHERS = "others";
        /**
         * 自定义告警启停状态
         */
        public static final String STATUS = "status";
        /**
         * 自定义告警备注
         */
        public static final String REMARK = "remark";


        public static final String APP = "app";

        public static final String VERIFYCODE = "verifyCode";


    }

    /**
     * 构造方法
     *
     * @param request
     * @param id
     */
    public DEParameter(HttpServletRequest request, String id) {
        this.ID = id;
        if (request == null)
            return;
        // 取出所有参数并尽可能地自动设置到属性中
        autoSetField(request);
        // 得到uri/queryString/url等
        this.uri = request.getRequestURI();
        this.doName = ServerUtils.getResourceName(request);
        this.queryString = parseQueryString();
        this.url = this.uri + this.queryString;
        // 解析Cookie
        this.cookieMap = CookieUtils.parseCookie(request);
        // 为了方便测试如果url中有传递lang就优先使用url的设置,如果url中没有再去cookie中尝试读取
        if (!ValidateUtils.isValidLang(getLang())) {
            this.lang = CookieUtils.getLangFromCookie(this.cookieMap);
        }
        System.out.println(this);
    }

    /**
     * <pre>
     * 获取查询字符串
     *
     * @return
     * @author Ivan<br>
     * @date 2015年4月1日 下午2:49:54 <br>
     */
    public String parseQueryString() {
        StringBuilder queryString = new StringBuilder();
        if (parameterMap != null && parameterMap.size() > 0) {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue()[0];
                queryString.append(key).append("=").append(value).append("&");
            }
            if (ValidateUtils.isNotEmpty(queryString)) {
                // 删除掉最后面那个&(只要有参数后面必定有一个&)
                queryString.deleteCharAt(queryString.length() - 1);
                return "?" + queryString.toString();
            }
        }
        return queryString.toString();
    }

    // ***********************常用获取参数值的方法********************************************

    // **********************获取参数的时候自动设置到属性里**************************************

    /**
     * <pre>
     * 自动设置参数值到对象中
     *
     * @author Ivan<br>
     * @date 2015年4月1日 上午10:30:56 <br>
     */
    @SuppressWarnings("unchecked")
    private void autoSetField(HttpServletRequest request) {
        /** 解析出所有的参数列表并存入parameterMap */
        this.parameterMap.putAll(request.getParameterMap());
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()[0];
            if (StringUtil.isEmpty(value)) {
                continue;
            }
            value = ServerUtils.decodeWithUTF8(value).trim();
            String setMethod = "set" + ServerUtils.toUppercaseFirstLetter(key);
            if (FIELDSET.contains(key) && METHODSET.contains(setMethod)) {// 调用set方法来设置值
                ReflectUtils.callMethodSet(DEParameter.class, key, this, value);
            }
        }
    }

    /**
     * 获取参数值
     *
     * @param key
     * @return
     * @author ivantan
     * @date 2015年8月25日 上午10:01:14
     */
    public String getParameter(String key) {
        String[] values = this.parameterMap.get(key);
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    /**
     * <pre>
     * 检查必填参数是否为非法值,如果为字符串验证是否为空，如果为数字验证是否为invalid_number
     *
     * @param parameterNames
     * @throws ClientException
     * @throws ServerException
     * @author Ivan<br>
     * @date 2015年4月1日 下午4:38:17 <br>
     */
    public void checkParameter(String... parameterNames) throws ClientException, ServerException {
        for (String attr : parameterNames) {
            Tracker.add(ID, "check parameter:" + attr);
            Class<?> clz = ReflectUtils.getFieldType(DEParameter.class, attr);
            Object object = ReflectUtils.getFieldValue(this, attr);
            if (object == null) {
                Tracker.add(ID, attr + " is null");
                ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);
            }
            if (clz == String.class) {// 是String类型，调用get方法，得到的值是否为空
                String value = (String) object;
                if (ValidateUtils.isEmpty(value)) {
                    Tracker.add(ID, attr + " is String but empty");
                    ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);
                }
                // 对时间格式多做一个匹配
                if (Keys.STARTDATE.equals(attr) || Keys.ENDDATE.equals(attr)) {
                    if (!value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR_TIME);
                    }
                }
            } else if (clz == Integer.class || clz == int.class) {
                if (!ValidateUtils.isValidNumber((Integer) object)) {
                    Tracker.add(ID, attr + " is Integer but emtpy");
                    ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);
                }
            } else if (clz == Long.class || clz == long.class) {
                if (!ValidateUtils.isValidNumber((Long) object)) {
                    Tracker.add(ID, attr + " is Long but emtpy");
                    ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);
                }
            } else if (clz == List.class) {
                List l = (List) object;
                if (ValidateUtils.isEmptyList(l)) {
                    Tracker.add(ID, attr + " is List but emtpy");
                    ExceptionHandler.throwParameterException(StatusCode.PARAMETER_ERROR);
                }
            } else {
                ExceptionHandler.throwClassException(StatusCode.CLASS_ERROR,
                        "unsupport class name in checkParameter:" + clz.getName());
            }
            Tracker.add(ID, attr + " is ok");
        }
        Tracker.add(ID, "parameter all ok");
    }

    public String getDoName() {
        return doName;
    }

    public String getUrl() {
        return url;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    @Override
    public String toString() {
        return "DEParameter [uri=" + uri + ", doName=" + doName + ", queryString=" + queryString + ", url=" + url
                +", cookieMap=" + cookieMap+", pagesize="+ pageSize+ ", pageid=" + pageID+ "]";
    }

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @return the groupID
	 */
	public int getGroupID() {
		return groupID;
	}

	/**
	 * @param groupID the groupID to set
	 */
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the serverID
	 */
	public int getServerID() {
		return serverID;
	}

	/**
	 * @param serverID the serverID to set
	 */
	public void setServerID(int serverID) {
		this.serverID = serverID;
	}

	/**
	 * @return the featrueID
	 */
	public int getFeatrueID() {
		return featrueID;
	}

	/**
	 * @param featrueID the featrueID to set
	 */
	public void setFeatrueID(int featrueID) {
		this.featrueID = featrueID;
	}

	/**
	 * @return the startdate
	 */
	public String getStartdate() {
		return startdate;
	}

	/**
	 * @param startdate the startdate to set
	 */
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	/**
	 * @return the enddate
	 */
	public String getEnddate() {
		return enddate;
	}

	/**
	 * @param enddate the enddate to set
	 */
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}



	/**
	 * @return the starttime
	 */
	public String getStarttime() {
		return starttime;
	}

	/**
	 * @param starttime the starttime to set
	 */
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	/**
	 * @return the endtime
	 */
	public String getEndtime() {
		return endtime;
	}

	/**
	 * @param endtime the endtime to set
	 */
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	/**
	 * @return the object
	 */
	public String getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(String object) {
		this.object = object;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the maxThreshold
	 */
	public BigDecimal getMaxThreshold() {
		return maxThreshold;
	}

	/**
	 * @param maxThreshold the maxThreshold to set
	 */
	public void setMaxThreshold(BigDecimal maxThreshold) {
		this.maxThreshold = maxThreshold;
	}

	/**
	 * @return the minThreshold
	 */
	public BigDecimal getMinThreshold() {
		return minThreshold;
	}

	/**
	 * @param minThreshold the minThreshold to set
	 */
	public void setMinThreshold(BigDecimal minThreshold) {
		this.minThreshold = minThreshold;
	}

	/**
	 * @return the maxMoM
	 */
	public BigDecimal getMaxMoM() {
		return maxMoM;
	}

	/**
	 * @param maxMoM the maxMoM to set
	 */
	public void setMaxMoM(BigDecimal maxMoM) {
		this.maxMoM = maxMoM;
	}

    public BigDecimal getMinMoM() {
        return minMoM;
    }

    public void setMinMoM(BigDecimal minMoM) {
        this.minMoM = minMoM;
    }

    /**
	 * @return the maxFrequency
	 */
	public int getMaxFrequency() {
		return maxFrequency;
	}

	/**
	 * @param maxFrequency the maxFrequency to set
	 */
	public void setMaxFrequency(int maxFrequency) {
		this.maxFrequency = maxFrequency;
	}

	/**
	 * @return the alarmType
	 */
	public String getAlarmType() {
		return alarmType;
	}

	/**
	 * @param alarmType the alarmType to set
	 */
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	/**
	 * @return the shieldStart
	 */
	public String getShieldStart() {
		return shieldStart;
	}

	/**
	 * @param shieldStart the shieldStart to set
	 */
	public void setShieldStart(String shieldStart) {
		this.shieldStart = shieldStart;
	}

	/**
	 * @return the shieldEnd
	 */
	public String getShieldEnd() {
		return shieldEnd;
	}

	/**
	 * @param shieldEnd the shieldEnd to set
	 */
	public void setShieldEnd(String shieldEnd) {
		this.shieldEnd = shieldEnd;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the pageID
	 */
	public int getPageID() {
		return pageID;
	}

	/**
	 * @param pageID the pageID to set
	 */
	public void setPageID(int pageID) {
		this.pageID = pageID;
	}


    //================================================
    //                  自定义告警参数  （开始）
    //================================================

    /** 自定义告警id*/
    private long customizeAlarmId;

    /** 告警项*/
    private String alarmItem;

    /** 主要负责人*/
    private int mainEmployee;

    /** 其他负责人*/
    private String others;

    /** 自定义告警启停状态(0--暂停  1--启动)*/
    private int status;

    /** 自定义告警备注*/
    private String remark;

    /** 自定义告警间隔*/
    private int alarmInterval;

    /** 告警恢复类型  0--恢复间隔时间一到自动恢复   1--收到上报恢复信息恢复 */
    private int restoreType;

    /** 恢复间隔时间 */
    private int restoreInterval;

    /** 告警对象*/
    private String alarmObject;

    public long getCustomizeAlarmId() {
		return customizeAlarmId;
	}

	public void setCustomizeAlarmId(long customizeAlarmId) {
		this.customizeAlarmId = customizeAlarmId;
	}

	public String getAlarmItem() {
        return alarmItem;
    }

    public void setAlarmItem(String alarmItem) {
        this.alarmItem = alarmItem;
    }

    public int getMainEmployee() {
        return mainEmployee;
    }

    public void setMainEmployee(int mainEmployee) {
        this.mainEmployee = mainEmployee;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getAlarmInterval() {
        return alarmInterval;
    }

    public void setAlarmInterval(int alarmInterval) {
        this.alarmInterval = alarmInterval;
    }

    public int getRestoreType() {
        return restoreType;
    }

    public void setRestoreType(int restoreType) {
        this.restoreType = restoreType;
    }

    public int getRestoreInterval() {
        return restoreInterval;
    }

    public void setRestoreInterval(int restoreInterval) {
        this.restoreInterval = restoreInterval;
    }

    public String getAlarmObject() {
        return alarmObject;
    }

    public void setAlarmObject(String alarmObject) {
        this.alarmObject = alarmObject;
    }

    //================================================
    //                  自定义告警参数 （结束）
    //================================================





    //================================================
    //                  自定义监控参数  （开始）
    //================================================

    //自定义监控id
    private int customMonitorId;

    //自定义监控项
    private String monitorItem;

    //自定义监控选项类型 1-->调用端  2-->接收端   3-->扩展属性1   4-->扩展属性2
    private int monitorSelectorType;

    //调用端
    private String caller;

    //接收端
    private String receiver;

    //扩展1
    private String ext1;

    //扩展2
    private String ext2;

    //当天日期
    private String currentDate;

    //对比日期
    private String comparedDate;

    public int getCustomMonitorId() {
        return customMonitorId;
    }

    public void setCustomMonitorId(int customMonitorId) {
        this.customMonitorId = customMonitorId;
    }

    public String getMonitorItem() {
        return monitorItem;
    }

    public void setMonitorItem(String monitorItem) {
        this.monitorItem = monitorItem;
    }

    public int getMonitorSelectorType() {
        return monitorSelectorType;
    }

    public void setMonitorSelectorType(int monitorSelectorType) {
        this.monitorSelectorType = monitorSelectorType;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getComparedDate() {
        return comparedDate;
    }

    public void setComparedDate(String comparedDate) {
        this.comparedDate = comparedDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    //================================================
    //                  自定义监控参数  （结束）
    //================================================





    //================================================
    //                  Mysql监控参数  （开始）
    //================================================

    //mysql监控mysql数据库表id
    private int mysqlMonitorId;

    //mysql实例id
    private int instanceID;

    //mysql打开文件数阀值
    private int openFile;

    //mysql连接数阀值
    private int connection;

    //mysql锁数量阀值
    private int lock;

    //mysql锁时长阀值
    private int lockTime;

    //mysql慢sql数阀值
    private int slowSql;



    public int getMysqlMonitorId() {
        return mysqlMonitorId;
    }

    public void setMysqlMonitorId(int mysqlMonitorId) {
        this.mysqlMonitorId = mysqlMonitorId;
    }

    public int getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(int instanceID) {
        this.instanceID = instanceID;
    }

    public int getOpenFile() {
        return openFile;
    }

    public void setOpenFile(int openFile) {
        this.openFile = openFile;
    }

    public int getConnection() {
        return connection;
    }

    public void setConnection(int connection) {
        this.connection = connection;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public int getLockTime() {
        return lockTime;
    }

    public void setLockTime(int lockTime) {
        this.lockTime = lockTime;
    }

    public int getSlowSql() {
        return slowSql;
    }

    public void setSlowSql(int slowSql) {
        this.slowSql = slowSql;
    }




    //================================================
    //                  Mysql监控参数  （结束）
    //================================================


    private int serverSortFlag;

    public int getServerSortFlag() {
        return serverSortFlag;
    }

    public void setServerSortFlag(int serverSortFlag) {
        this.serverSortFlag = serverSortFlag;
    }
}
