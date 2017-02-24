package com.dataeye.omp.common;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import com.google.gson.Gson;

/**
 * 常量
 *
 * @author chenfanglin
 * @date 2016年1月27日 上午11:11:00
 */
public class Constant {

	public class Resource {
		public static final String DIR = "";

		public static final String ALARM_FILE = DIR + "alarm.properties";

	}

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

	public static class Rtn {
		public Rtn() {
		}

		public int rtn = 0;
		public String desc = "";
		public SMSReturnXML xml;
	}

	public static class SMSReturnXML {
		public String returnstatus;
		public String message;
		public String remainpoint;
		public String taskID;
		public String successCounts;

		public String toString() {
			return "SMSReturnXML [returnstatus=" + returnstatus + ", message=" + message + ", remainpoint="
					+ remainpoint + ", taskID=" + taskID + ", successCounts=" + successCounts + "]";
		}
	}

	public static Gson GSON = new Gson();

	public final static SimpleDateFormat BASIC_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public final static String RTN_CONTENT_TOO_LONG = "{\"rtn\":-1,\"desc\":\"内容超过300字\"}";

	// 告警规则
	public final static String DC_ALARM_RULE = "dc_alarm_rule";

	// 告警历史
	public final static String DC_ALARM_HISTORY = "dc_alarm_history";

	public final static String DC_ALARM_RULE_BASIC = "dc_alarm_rule_basic";
	// 已发送告警
	public final static String DC_ALARM_SEND = "dc_alarm_send";

	public final static String SERVER_IP_LIST = "server_ip_list";

	public final static String SERVER_BUSINESS = "server_business";

	public final static String SERVER_GROUP = "server_group";

	public final static String SERVER_LIST = "server_list";

	public final static String BUSINESS = "business";

	public final static String EMPLOYEE = "employee";

	public final static String FEATURE_OBJECT = "feature_object";

	public final static String DC_ALARM_MAIL = "dc_alarm_mail";

	public final static String DC_PROCESS_INFO = "dc_process_info";

	public final static String DC_PROCESS_ALARM_RULE = "dc_process_alarm_rule";

	public final static String DC_PROCESS_ALARM_CONTROL = "dc_process_alarm_control";

	public final static String DC_PROCESS_ALARM_EVERY_DAY = "dc_process_alarm_every_day";

	public final static String DC_ALARM_RULE_CUSTOMIZE = "dc_alarm_rule_customize";

	public final static String DC_ALARM_CUSTOMIZE_INFO = "dc_alarm_customize_info";

	public final static String DC_ALARM_CUSTOMIZE_CONTROL = "dc_alarm_customize_control";

	public final static String DC_ALARM_CUSTOMIZE_EVERY_DAY = "dc_alarm_customize_every_day";

	// 告警对象表
	public final static String OMP_CUSTOMIZE_ALARM_OBJECT = "omp_customize_alarm_object";
	// 列族
	public final static String COLUMNFAMILY = "stat";


    public static final String DC_ALARM_MYSQL_RULE = "dc_monitor_mysql_rule";

    public static final String DC_ALARM_MYSQL_CONTROL = "dc_monitor_mysql_control";

    public static final String DC_ALARM_MYSQL_EVERYDAY = "dc_monitor_mysql_everyday";


    public static final String TABLE_CUSTOM_MONITOR_OBJECT = "omp_customize_monitor_object";

    public static final String TABLE_CUSTOM_MONITOR_STATE = "omp_customize_monitor_state";

	// 正常
	public final static int OK = 0;
	// 警告
	public final static int WARN = 1;

	// 逗号
	public final static String SEPARATOR = ",";

	public final static String COLON = "：";



	public final static String CUTTINGBREAKS = "==>";
	/**
	 * 成功接收了邮件请求
	 */
	public static final int STATUS_CODE_SUCCESS = 200;
	/**
	 * 参数错误
	 */
	public static final int STATUS_CODE_PARM_ERROR = 201;
	public static final int STATUS_CODE_FAIL = 202;

	public static final int SUBSCRIPTION_DATA_BASIC = 1;
	public static final int SUBSCRIPTION_DATA_PAY = 2;
	public static final int SUBSCRIPTION_DATA_CUMU = 3;
	public static final int SUBSCRIPTION_DATA_CHANNEL = 4;

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

    public static final int IDCNETIN = 60;
    public static final int IDCNETOUT = 61;

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

    /**mysql打卡文件数*/
    public static final int OPENFILES=80;

    /**系统文件限制使用数*/
    public static final int OPENFILELIMIT=81;

    /**
     * mysql连接数
     */
    public static final int MYSQL_CONN=82;

    /**
     * mysql锁数量
     */
    public static final int LOCK_NUM=83;

    /**
     * mysql锁时长
     */
    public static final int LOCK_TIME=84;

    /**
     * mysql 慢sql数
     */
    public static final int SLOW_SQL = 85;

    // cpu
    public static final String CPU = "cpu";

    public static final String MEM_USED = "mem_used";

    public static final String MEM_PRI = "mem_pri";

    public static final String MEM_VIR = "mem_vir";

    public static final String MEM_SWAP_USED = "mem_swap_used";

    public static final String MEM_SWAP_TOTAL = "mem_swap_total";
    //24
    public static final String MPRI_IPCS = "mpri_ipcs";
    
    
    //31#pgpgin
    public static final String PGPGIN = "pgpgin";
    //32#pgpgout                                
    public static final String PGPGOUT = "pgpgout";
    //33#pswpin
    public static final String PSWPIN = "pswpin";
    //34#pswpout
    public static final String PSWPOUT = "pswpout";
    //35#svctm  
    public static final String SVCTM = "Svctm_time_max";
    //36#await 
    public static final String AWAIT = "Await_time_max";
    //37#aveq              
    public static final String AVEQ = "avgqu_sz_max";
    //38#avgrq_sz   
    public static final String AVGRQ_SZ = "avgrq_sz";
    //39#util
    public static final String UTIL = "util_max";

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
    public static final String HARD_TEMPS = "hard_temps";

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

    // 单位
    public static final BigDecimal UNIT = new BigDecimal(1024 * 1024);

    public static final BigDecimal KB = new BigDecimal(1024);

    public static final BigDecimal HUNDRED = new BigDecimal(100);

    public static final String MILLION = "M";

    public static final String BYTE = "b/s";

    // 磁盘根分区使用率
    public static final int ROOTPARTITION = 43;
    // 磁盘分区使用率
    public static final int DISKPARTITION = 44;

    public static final String DISK_USAGE = "disk_usage";

    // 私有内存使用率
    public static final int MEMORYPRIVATE = 29;
    public static final String PRIMEM_USAGE = "primem_usage";
    
    //物理内存使用量
    public static final int PHYSIC_MEMORY_USE_FEATURE = 27;
    //27
    public static final String PHYSIC_MEMORY_USE_OBJECT = "mem_res_total";

    public static final String SECOND = "ms";
    
    public static final String SVCTM_TIME_MAX = "平均每次设备I/O操作的服务时间";
    public static final String AWAIT_TIME_MAX = "平均每次设备I/O操作的等待时间";
    public static final String AVGQU_SZ_MAX = "平均I/O队列长度";
    public static final String IO_AVGRQ_SZ = "平均每次设备I/O操作的数据大小 (扇区)";
    public static final String UTIL_MAX = "一秒中有百分之多少的时间用于I/O操作";
    
    public static final String ALARM_TYPE = "g";
}
