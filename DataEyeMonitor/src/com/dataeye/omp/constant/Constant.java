package com.dataeye.omp.constant;


/**
 * <pre>
 * 常量
 *
 * @author Ivan <br>
 * @date 2015年3月31日 下午8:13:50 <br>
 */
public class Constant {
    /**
     * <pre>
     *
     * @author Ivan <br>
     * @version 1.0 <br>
     * @date 2015年3月31日 下午8:38:03 <br>
     */
    public class ServerCfg {
    	
    	/**
    	 * 获取员工信息
    	 */
    	public static final String GET_EMPLOYEE_INFO = "http://crm.dataeye.com/user/getAllEmployees.do";
    	
        /**
         * 统一登录主机
         */
        public static final String PTLOGIN_HOST = "http://ptlogin";

        /**
         * 不合法的数字
         */
        public static final int INVALID_NUMBER = -100;
        /**
         * 0代表全部 ,比如全部区服、全部渠道
         */
        public static final int ALL = -1;
        /**
         * 默认语言
         */
        public static final String DEFAULT_LANG = LANG.ZH;
        /**
         * 允许的最小查询时间
         */
        public static final int MIN_QUERY_START_TIME = 0;
        /**
         * 查询开始时间和结束时间之间允许最大跨度多少天
         */
        public static final int MAX_QUERY_TIME_SPAN_DAYS = 60;
        /**
         * Content-Type Json
         */
        public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
        /**
         * demo@dataeye.com
         */
        public static final String DEMO_ACCOUNT = "demo@dataeye.com";
        /**
         * 统一登录页面
         */
        public static final String LOGIN_PAGE = "http://www.dataeye.com/ptlogin/login.jsp";
        /**
         * 初始化应用信息的jsp页面
         */
        public static final String INIT_APP_PAGE = "init_app.jsp";

        public static final String DATA_CENTER_PAGE = "home.jsp";
        /**
         * 统一登录的 sso 验证接口
         */
        public static final String URL_GETUID = PTLOGIN_HOST + ":48080/ptlogin/innerapi/getuid.do";
        /**
         * 获取用户的所有app list
         */
        public static final String URL_GETACCOUNTAPPLIST = PTLOGIN_HOST + ":48080/ptlogin/innerapi/getAccountApp.do";
        public static final String URL_CREATENEWAPP = PTLOGIN_HOST + ":48080/ptlogin/innerapi/addApp.do";
        /**
         * 数据库前缀
         */
        public static final String DATABASE_PREFIX = "adt_stat_";
        /**
         * 查看游戏数据的JSP
         */
        public static final String MAIN_JSP = "main.jsp";
        /**
         * compaign_daily_stat_tpl 和 natural_daily_stat_tpl 这两种表的最大数量
         */
        public static final int MAX_COMPAIGN_STAT_TABLE_INDEX = 100;
        public static final int MAX_NATURAL_STAT_TABLE_INDEX = 10;

        public static final String SHORT_URL_HOST = "http://119.147.212.251:17080/";

        public static final String URL_GET_TOKEN_INFO = PTLOGIN_HOST + ":48080/ptlogin/innerapi/getTokenInfo.do";

        public static final String ROWKEY_SPLITER = "#";
        
		public static final String CONTENT_TYPE_EXCEL = "application/vnd.ms-excel;charset=utf-8";

		public static final String ENCODING_UTF8 = "utf-8";

    }



    /**
     * <pre>
     * 分隔符
     *
     * @author Ivan <br>
     * @version 1.0 <br>
     * @date 2015年4月1日 下午2:27:28 <br>
     */
    public class Separator {
        /**
         * 默认分隔符
         */
        public static final String DEFAULT = ",";
        
        public static final String TILDE = "~";

        public static final String UNDERLINE = "_";
    }

    /**
     * <pre>
     * 存放cookie名的常量
     *
     * @author Ivan <br>
     * @version 1.0 <br>
     * @date 2015年4月1日 下午3:33:04 <br>
     */
    public class CookieName {
        /**
         * 存放语言
         */
        public static final String LANG = "lang";
        public static final String TOKEN_GLOBAL = "tokenglobal";
    }

    /**
     * <pre>
     *
     * @author Ivan <br>
     * @version 1.0 <br>
     * @date 2015年8月11日 下午6:01:46 <br>
     */
    public class SessionName {
        /**
         * session中表示当前用户
         */
        public static final String CURRENT_USER = "CURRENT_USER";
    }

    /**
     * <pre>
     * 语言
     *
     * @author Ivan <br>
     * @version 1.0 <br>
     * @date 2015年4月1日 下午3:48:13 <br>
     */
    public class LANG {
        /**
         * 中文
         */
        public static final String ZH = "zh";
        /**
         * 英文
         */
        public static final String EN = "en";
        /**
         * 繁体
         */
        public static final String FT = "ft";
    }



    /**
     * <pre>
     * 国际化相关的文件
     *
     * @author Ivan <br>
     * @version 1.0 <br>
     * @date 2015年4月3日 下午4:25:15 <br>
     */
    public class Resource {
        public static final String DIR = "/";
        /**
         * 英文
         */
        public static final String EN_FILE =  "en.properties";
        /**
         * 中文
         */
        public static final String ZH_FILE =  "zh.properties";
        /**
         * 繁体
         */
        public static final String FT_FILE =  "ft.properties";

    }

    /**
     * <pre>
     * 操作系统类型
     *
     * @author Ivan <br>
     * @version 1.0 <br>
     * @date 2015年4月3日 下午4:31:42 <br>
     */
    public class OsType {
        /**
         * windows
         */
        public static final String WINDOWS = "win";
        /**
         * linux
         */
        public static final String LINUX = "linux";
        /**
         * 其他
         */
        public static final String OTHER = "other";
    }

    /**
     * <pre>
     * 数据库和表
     *
     * @author Ivan <br>
     * @version 1.0 <br>
     * @date 2015年8月12日 下午5:06:30 <br>
     */
    public class Table {
        public static final String DEVICE_INFO="device_info";
        public static final String DC_CONFIG = "dc_config";
        // mysql
        public static final String ADT_STAT_TPL = "adt_stat_tpl";
        public static final String USER = "adt_business.user";
        public static final String APP_LIST = "adt_business.app_list";

        public static final String COMPAIGN_DAILY_STAT_TPL = "compaign_daily_stat";
        public static final String NATURAL_DAILY_STAT_TPL = "natural_daily_stat";
        
        // de_monitor
        public static final String BUSINESS = "business";
        public static final String PROCESSINFO = "dc_process_info";
        public static final String PROCESSRULE = "dc_process_alarm_rule";

        public static final String CABINET_LIST = "cabinet_list";
        public static final String DEPARTMENT = "department";
        public static final String DICTIONARY_ITEM = "dictionary_item";
        public static final String DICTIONARY_VALUE = "dictionary_value";
        public static final String EMPLOYEE = "employee";
        public static final String FEATURE_LIST = "feature_list";
        public static final String FEATURE_OBJECT = "feature_object";
        public static final String IDC_LIST = "idc_list";
        public static final String ISP_LIST = "isp_list";
        public static final String SERVER_BUSINESS = "server_business";
        public static final String SERVER_GROUP = "server_group";
        public static final String SERVER_GROUP_OWNER = "server_group_owner";
        public static final String SERVER_IP_LIST = "server_ip_list";
        public static final String SERVER_LIST = "server_list";
        public static final String TEAMS = "teams";
        public static final String DC_ALARM_RULE = "dc_alarm_rule";
        public static final String DC_ALARM_RULE_BASIC = "dc_alarm_rule_basic";
        
        // hbase
        public static final String omp_server_feature_stat = "omp_server_feature_stat";
        public static final String OMP_FEATURE_VALUE_STAT_HOUR = "omp_feature_value_stat_hour";
        public static final String OMP_FEATURE_VALUE_STAT_MINUTE = "omp_feature_value_stat_minute";
        public static final String OMP_SERVER_FEATURE_STAT = "omp_server_feature_stat";

        public static final String OMP_PROCESS_CURRENT_STATE = "omp_process_current_state";

        public static final String FAMILY = "stat";
        public static final String OMP_CUSTOM_MONITOR_STAT = "omp_customize_monitor_state";


        public static final String OMP_CUSTOM_MONITOR_OBJECT = "omp_customize_monitor_object";
    }

    public class Process {
        public static final String PORT = "port";

        public static final String PROCESS = "process";

        public static final int NOT_MONITOR = 2;

    }

    /**
     * <pre>
     * 用户状态
     *
     * @author Ivan <br>
     * @version 1.0 <br>
     * @date 2015年8月12日 下午5:14:19 <br>
     */
    public class UserStatus {
        /**
         * init success
         */
        public static final int SUCCESS = 1;
        /**
         * init fail
         */
        public static final int FAIL = 2;

    }

    /**
     * 常用的bean name
     * @author chenfanglin
     * @date 2016年1月7日 下午6:55:02
     */
	public class BeanName {
		/** jdbcTemplate de_monitor */
		public static final String JDBCTEMPLATE_DATAEYE_MONITOR = "jdbcTemplateMonitor";
		/** jdbcTemplate de_monitor_stat */
		public static final String JDBCTEMPLATE_DATAEYE_MONITOR_STAT = "jdbcTemplateMonitorStat";

	}

	public class OrderBy {
		// 排序的几个字段
		public static final String ORDERBY_HOSTNAME = "hostName";
		public static final String ORDERBY_IP = "ip";
		public static final String ORDERBY_MACHINEROOM = "machineRoom";
		public static final String ORDERBY_BUSINESS = "business";
		public static final String ORDERBY_CPUUSAGE = "cpuUsage";
		public static final String ORDERBY_FIVELOAD = "fiveLoad";
		public static final String ORDERBY_STATUS = "status";

        public static final String ORDERBY_ALARM_OBJECT_NAME = "alarmObjectName";

        public static final String ORDREBY_ALARM_OBJECT_TYPE = "alarmObjectType";

        public static final String ORDERBY_BUSINAME = "busiName";
        public static final String ORDERBY_MODULENAME = "moduleName";
        public static final String ORDERBY_PROCESSNUM = "processNum";
        public static final String ORDERBY_MAINPRINCIPAL = "mainPrincipal";
        public static final String ORDERBY_PROCESSNAME = "processName";
        public static final String ORDERBY_PROCESSSTATUS = "processStatus";
        public static final String ORDERBY_PORT = "port";
        public static final String ORDERBY_PRIVATEIP = "privateIp";





    }

	public class Order {
		public static final int ASC = 0;
		public static final int DESC = 1;
	}
	
    /**
     * 一个特性ID对应一个采集指标
     */
    // CPU使用率
    public static final int CPUUSAGE = 10;
    
    // cpu1分组负载
    public static final int ONEMIN = 11;
    
    // CPU5分钟负载
    public static final int FIVEMIN = 12;
    
    // cpu15分钟负载
    public static final int FIFTEENMIN = 13;
    
    // 应用程序使用内存
    public static final int MEMPROC = 20;
    
    // 内存使用率
    public static final int MENUSAGE = 21;
    
    //所有进程的private内存占用总和
    public static final int MEMPRIVATE = 22;
    
    //所有进程的virtual内存占用总和
    public static final int MEMVIRTUAL = 23;
    
    //所有进程的private内存+ipcs内存的总和
    public static final int MEMPRIVATEIPCS = 24;

    //物理内存使用量
    public static final int PHYSIC_MEMORY_USE_FEATURE = 27;
    
    // SWAP的使用大小
    public static final int MEMSWAPUSED = 25;
    
    // SWAP的总大小
    public static final int MEMSWAPTOTAL = 26;

    // 平均每秒把数据从硬盘读到物理内存的数据量 
    public static final int DISKIOREAD = 31;
    
    // 平均每秒把数据从物理内存写到硬盘的数据量
    public static final int DISKIOWRITE = 32;
    
    //平均每秒把数据从磁盘交换区装入内存的数据量
    public static final int SWAPSI = 33;
    
    //平均每秒把数据从内存转储到磁盘交换区的数据量
    public static final int SWAPSO = 34;
    
    // 平均每次设备I/O操作的服务时间*100（取所有分区最大值）
    public static final int SVCTMTIMEMAX = 35;
    
    // 平均每次设备I/O操作的等待时间*100（取所有分区最大值）
    public static final int AWAITTIMEMAX = 36;
    
    //平均I/O队列长度*100（取所有分区最大值）
    public static final int AVGQUSZMAX = 37;
    
    // 平均每次设备I/O操作的数据大小（取第一个分区）
    public static final int AVGRQSZ = 38;
    
    // 1秒之中有百分之几的时间用于IO操作（取所有分区最大值）
    public static final int UTILMAX = 39;
    
    //分区空闲大小
    public static final int FREEPARTITION = 40;
    
    // 分区总大小
    public static final int TOTALPARTITION= 41;
    
    // 出流量
    public static final int OUTFLOW = 51;
    
    // 入流量
    public static final int INFLOW = 50;
    
    // 出包量
    public static final int OUTPACKAGEVOLUME = 53;
    
    // 入包量
    public static final int INPACKAGEVOLUME = 52;
    
    // 被动打开TCP连接数
    public static final int PASSIVETCPCONN = 54; 
    
    // TCP连接数
    public static final int TCPCONN = 55;
    
    // UDP接收数据报
    public static final int UDPRECEIVEDATAGRAM = 56;
    
    // UDP发送数据报
    public static final int UDPSENDDATAGRAM = 57;
    
    // cpu处理器状态
    public static final int CPUSTATUS = 70;
    
    // 内存状态
    public static final int MEMORYSTATUS = 71;
    
    //机器温度状态
    public static final int TEMPSSTATUS = 72;
    
    // 物理硬盘状态
    public static final int DISKSTATUS = 73;
    
    // 电源状态
    public static final int PWRSUPPLIESSTATUS = 74;
    
    // 系统面板CMOS电池
    public static final int BATTERIESSTATUS = 75;
    
    // 网卡状态
    public static final int NICSSTATUS = 76;
    
    // 风扇状态
    public static final int FANSSTATUS = 77;

    //机器告警
    public static final int ALARM_TYPE_DEVICE = 0;

    //业务告警
    public static final int ALARM_TYPE_GROUP = 2;

    //分组告警
    public static final int ALARM_TYPE_BUSINESS = 1;

    //阀值
    public static final int SHIELD_SECTION_THRESHOLD = 0;

    //环比
    public static final int SHIELD_SECTION_MOM = 1;

    // cpu
    public static final String CPU = "cpu";
    
    public static final String MEM_USED = "mem_used";
    
    public static final String MEM_PRI = "mem_pri";
    
    public static final String MEM_VIR = "mem_vir";
    
    public static final String MEM_SWAP_USED = "mem_swap_used";
    
    public static final String MEM_SWAP_TOTAL = "mem_swap_total";
    //24
    public static final String MPRI_IPCS = "mpri_ipcs";
    //27
    public static final String PHYSIC_MEMORY_USE_OBJECT = "mem_res_total";
    //31#pgpgin
    public static final String PGPGIN = "pgpgin";
    //32#pgpgout                                
    public static final String PGPGOUT = "pgpgout";
    //33#pswpin
    public static final String PSWPIN = "pswpin";
    //34#pswpout
    public static final String PSWPOUT = "pswpout";
    //35#svctm  
    public static final String SVCTM = "svctm";
    //36#await 
    public static final String AWAIT = "await";
    //37#aveq              
    public static final String AVEQ = "aveq";
    //38#avgrq_sz   
    public static final String AVGRQ_SZ = "avgrq_sz";
    //39#util
    public static final String UTIL = "util";
    
    // 40
    public static final String ROOT = "/";
    
    public static final String BOOT = "/boot";
    
    public static final String DEVSHM = "/dev/shm";
    
    public static final String HOME = "/home";
    
    // 54被动打开的TCP
    public static final String PASSIVEOPENS = "passiveopens";
    
    // 55打开的TCP
    public static final String CURRESTAB = "TIME_ESTA";
    
    // 56
    public static final String INDATAGRAMS = "indatagrams";
    
    // 57
    public static final String OUTDATAGRAMS = "outdatagrams";
    
    // 外网网卡
    public static final String EM1 = "em1";
    
    // 内网网卡
    public static final String EM2 = "em2";
    
    // cpu处理器状态 70
    public static final String HARD_PROC = "hard_proc";
    
    // 71 内存状态
    public static final String HARD_MEM = "hard_mem";
    
    // 72 机器温度状态
    public static final String HARD_TEMPS = "hard_temp";
    
    // 73 物理硬盘状态
    public static final String HARD_DISK = "hard_disk";
    
    // 74 电源状态
    public static final String HARD_PWR = "hard_pwr";
    
    // 75	系统面板CMOS电池
    public static final String HARD_BATT = "hard_batt";
    
    // 76 网卡状态
    public static final String HARD_NICS = "hard_nics";
    
    // 77 风扇状态
    public static final String HARD_FANS = "hard_fans";
    
    // 10分钟
    public static final long SECONDS_TEN_MIN = 10 * 60;
    
    public static final long SECONDS_ONE_DAY = 24 * 60 * 60;

    public static final int TYPE_FEATURE= 1;
    public static final int TYPE_SERVER = 2;
    public static final int TYPE_PROCESS = 3;
    public static final int TYPE_MYSQL = 4;

}
