package com.dataeye.monitor.common.constants;

/**
 * 机器特性抓取脚本
 * Created by wendy on 2016/7/4.
 */
public class Commands {
    public static final String CPU = "cat /proc/stat|grep cpu";
    public static final String LOADAVERAGE = "cat /proc/loadavg";

    public static final String MEM_INFO = "cat /proc/meminfo "
            + "| grep -E 'MemTotal|MemFree|Buffers|Cached|SwapTotal|SwapFree'";

    public static final String PRIVATE_MEM = " ps -ef |grep -v pts "
            + "|grep -v PID |awk '{print $2 }'|xargs  -i  cat  /proc/{}/smaps |grep Rss "
            + "|grep -v cat | awk '{sum+=$2} END {print \"Sum: \"sum}'|awk  '{print $2}'";

    public static final String VIRTUAL_MEM = "ps -ef | grep -v pts "
            + "|grep -v PID |awk '{print $2}'|xargs -i cat /proc/{}/status|grep VmSize"
            + " | awk '{sum+=$2} END {print \"Sum: \"sum}'|awk '{print $2}'";

    public static final String IPCS = "ipcs -m | grep -v bytes| grep -v Segments "
            + "| awk '{print $5}' | awk '{sum+=$1} END {print \"Sum: \"sum}'|awk '{print $2}'";

    public static final String VMSTAT = "cat /proc/vmstat | grep -E 'pgpgin|pgpgout|pswpin|pswpout'";
    public static final String DISKSTATS = "lsblk -l | grep disk | awk '{print $1}' "
            + "| xargs -i grep -w {} /proc/diskstats";

    public static final String PARTITION = "df -lk | grep -v tmp | grep -v Filesystem | grep -v 1K-块";

    public static final String NETWORK_NAME = "ifconfig| grep -E -o  \"^[a-z0-9]+\" | grep -v lo | uniq";
    public static final String NET_DEV = "cat /proc/net/dev";

    public static final String PING = "ping -c2 -w2 114.114.114.114 |grep rtt|awk -F / '{print $5}' |awk -F . '{print $1}' ";
    public static final String NET_SNMP = "cat /proc/net/snmp | grep -E 'Tcp|Udp' | grep -v 'UdpLite'";
    public static final String TIME_WAIT = "cat /proc/net/sockstat|grep TCP|awk '{ print $7 }'";

    public static final String netCardNum = "lspci | grep Ethernet |wc -l";
    public static final String cpuNum = "cat /proc/cpuinfo |grep physical|sort|grep -v add|uniq |wc -l";
    public static final String cpuType = "cat /proc/cpuinfo |grep model|grep name|uniq |awk -F : '{print $2}'";
    public static final String cpuPhysicalCores = "cat /proc/cpuinfo|grep core|grep -v cpu |sort|uniq |wc -l";
    public static final String cpuLogicCores = "cat /proc/cpuinfo |grep proc |awk '{print $3}' |wc -l";
    public static final String osType = "cat /etc/redhat-release";
    public static final String kernal = "uname -r";
    public static final String memory = "free |grep Mem|awk '{print $2}'";
    public static final String diskNum = "lsblk| grep disk| wc -l";
    public static final String diskSize = "cat /sys/block/sd*/size |awk '{ SUM += $1 } END { print SUM*1024/2 }' ";
    public static final String DiskDetail = "lsblk -b | grep disk";

    public static final String hardProc = "omreport chassis processors|awk '/^Health/{if($NF==\"Ok\") {print 1} else {print 0}}'";
    public static final String hardMem = "awk -v hardware_memory=`omreport chassis memory|awk '/^Health/{print $NF}'\n" +
            "` 'BEGIN{if(hardware_memory==\"Ok\") {print 1} else {print 0}}'";
    public static final String hardTemp = "omreport chassis temps|awk '/^Status/{if($NF==\"Ok\") {print 1} else {print 0}}'|head -n 1";

    public static final String hardDisk = "awk -v hardware_physics_disk_number=`omreport storage pdisk controller=0|grep -c \"^ID\"` -v hardware_physics_disk=`omreport storage pdisk controller=0|awk'/^Status/{if($NF==\"Ok\") count+=1}END{print count}'` 'BEGIN{if(hardware_physics_disk_number==hardware_physics_disk) {print 1} else {print 0}}'";

    public static final String hardPwr = "awk -v hardware_power_number=`omreport chassis pwrsupplies|grep -c \"Index\"` -v hardware_power\n" +
            "=`omreport chassis pwrsupplies|awk '/^Status/{if($NF==\"Ok\") count+=1}END{print count}'` 'BEGIN{if(hardware_power_number==hardware_power) {print 1} else {print 0}}'";
    public static final String hardBatt = "omreport chassis batteries|awk '/^Status/{if($NF==\"Ok\") {print 1} else {print 0}}'";
    public static final String hardNics = "awk -v hardware_nic_number=`omreport chassis nics |grep -c \"Interface Name\"` -v hardware_nic=\n" +
            "`omreport chassis nics |awk '/^Connection Status/{print $NF}'|wc -l` 'BEGIN{if(hardware_nic_number==hardware_nic) {print 1} else {print 0}}'";
    public static final String hardFans = "awk -v hardware_fan_number=`omreport chassis fans|grep -c \"^Index\"` -v hardware_fan=\n" +
            "`omreport chassis fans|awk '/^Status/{if($NF==\"Ok\") count+=1}END{print count}'` 'BEGIN{if(hardware_fan_number==hardware_fan) {print 1} else {print 0}}'";

    public static final String procNameUniqStatus = "ps aux | grep -v grep | grep {} |awk '{print $8}' | uniq";
    public static final String procNameMultiStatus = "ps aux | grep -v grep | grep {1}| grep {2} |awk '{print $8}' | uniq";
    public static final String portStatus = "netstat -na | grep LISTEN | grep {}";

    public static final String idcNetIn = "snmpwalk {} -c hdcx -v 1 IF-MIB::ifInOctets.130 | awk '{print $NF}'";
    public static final String idcNetOut = "snmpwalk {} -c hdcx -v 1 IF-MIB::ifOutOctets.130 |awk ' {print $NF}'";

    public static final String mysqlInstance = "netstat -antp |grep LISTEN |grep mysql";
    public static final String mysqlId = "SELECT VARIABLE_VALUE FROM information_schema.GLOBAL_VARIABLES"
            + " WHERE VARIABLE_NAME='SERVER_ID';";
    public static final String mysqlConn
            = "SELECT count(1) FROM information_schema.processlist;";
    public static final String mysqlLockNum
            = "SELECT count(1) FROM information_schema.processlist WHERE state LIKE '%lock%';";
    public static final String mysqlOpenFiles
            = "SELECT VARIABLE_VALUE FROM information_schema.GLOBAL_STATUS"
            + " WHERE VARIABLE_NAME='open_files';";
    public static final String mysqlOpenFilesLimit
            = "SELECT VARIABLE_VALUE FROM information_schema.GLOBAL_VARIABLES"
            + " WHERE VARIABLE_NAME='open_files_limit';";
    public static final String mysqlLockTime
            = "SELECT time FROM information_schema.processlist WHERE state LIKE '%lock%';";

}
