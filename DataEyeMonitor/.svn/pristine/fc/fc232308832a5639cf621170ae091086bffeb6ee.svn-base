package com.dataeye.omp.module.cmdb.process;

import com.qq.jutil.string.StringUtil;

import java.util.Comparator;

/**
 * 进程 比较器
 * @author wendy
 * @since 2016/3/7 10:39
 */
public class ProcessComparator {

    /**内网IP升序*/
    public static final Comparator<ProcessInfo> PROCESSSPRIVATEIP_ASC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (StringUtil.isNotEmpty(o1.getPrivateIp()) &&
                    StringUtil.isNotEmpty(o2.getPrivateIp())) {
                if (o1.getPrivateIp().compareTo(o2.getPrivateIp()) > 0) {
                    return 1;
                }
                if (o1.getPrivateIp().compareTo(o2.getPrivateIp()) < 0) {
                    return -1;
                }
            } else if (StringUtil.isNotEmpty(o1.getPrivateIp()) &&
                    StringUtil.isEmpty(o2.getPrivateIp())) {
                return 1;
            } else if (StringUtil.isEmpty(o1.getPrivateIp()) &&
                    StringUtil.isNotEmpty(o2.getPrivateIp())) {
                return -1;
            } else {
                return 1;
            }
            return 0;
        }
    };

    /**内网IP降序*/
    public static final Comparator<ProcessInfo> PROCESSPRIVATEIP_DESC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (StringUtil.isNotEmpty(o1.getPrivateIp()) &&
                    StringUtil.isNotEmpty(o2.getPrivateIp())) {
                if (o1.getPrivateIp().compareTo(o2.getPrivateIp()) > 0) {
                    return -1;
                }
                if (o1.getPrivateIp().compareTo(o2.getPrivateIp()) < 0) {
                    return 1;
                }
            }else if(StringUtil.isNotEmpty(o1.getPrivateIp())&&
                    StringUtil.isEmpty(o2.getPrivateIp())){
                return -1;
            }else if(StringUtil.isEmpty(o1.getPrivateIp())&&
                    StringUtil.isNotEmpty(o2.getPrivateIp())){
                return 1;
            } else {
                return -1;
            }
            return 0;
        }
    };

    /**端口升序*/
    public static final Comparator<ProcessInfo> PROCESSPORT_ASC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (o1.getPort()>o2.getPort()) {
                return 1;
            }
            if (o1.getPort()<o2.getPort()) {
                return -1;
            }
            return 0;
        }
    };

    /**端口降序*/
    public static final Comparator<ProcessInfo> PROCESSSPORT_DESC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (o1.getPort()>o2.getPort()) {
                return -1;
            }
            if (o1.getPort()<o2.getPort()) {
                return 1;
            }
            return 0;
        }
    };

    /**主机名称升序*/
    public static final Comparator<ProcessInfo> PROCESSHOSTNAME_ASC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (StringUtil.isNotEmpty(o1.getServerName()) &&
                    StringUtil.isNotEmpty(o2.getServerName())) {
                if (o1.getServerName().compareTo(o2.getServerName()) > 0) {
                    return 1;
                }
                if (o1.getServerName().compareTo(o2.getServerName()) < 0) {
                    return -1;
                }
            }else if(StringUtil.isNotEmpty(o1.getServerName())&&
                    StringUtil.isEmpty(o2.getServerName())){
                return 1;
            }else if(StringUtil.isEmpty(o1.getServerName())&&
                    StringUtil.isNotEmpty(o2.getServerName())){
                return -1;
            } else {
                return 1;
            }
            return 0;
        }
    };

    /**主机名称降序*/
    public static final Comparator<ProcessInfo> PROCESSHOSTNAME_DESC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (StringUtil.isNotEmpty(o1.getServerName()) &&
                    StringUtil.isNotEmpty(o2.getServerName())) {
                if (o1.getServerName().compareTo(o2.getServerName()) > 0) {
                    return -1;
                }
                if (o1.getServerName().compareTo(o2.getServerName()) < 0) {
                    return 1;
                }
            }else if(StringUtil.isNotEmpty(o1.getServerName())&&
                    StringUtil.isEmpty(o2.getServerName())){
                return -1;
            }else if(StringUtil.isEmpty(o1.getServerName())&&
                    StringUtil.isNotEmpty(o2.getServerName())){
                return 1;
            } else {
                return -1;
            }
            return 0;
        }
    };


    /**
     *按业务名称降序
     */
    public static Comparator<ProcessInfo> BUSINAME_DESC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (StringUtil.isNotEmpty(o1.getBusiName()) &&
                    StringUtil.isNotEmpty(o2.getBusiName())) {
                if (o1.getBusiName().compareTo(o2.getBusiName()) > 0) {
                    return -1;
                }
                if (o1.getBusiName().compareTo(o2.getBusiName()) < 0) {
                    return 1;
                }
            }else if(StringUtil.isNotEmpty(o1.getBusiName())&&
                    StringUtil.isEmpty(o2.getBusiName())){
                return -1;
            }else if(StringUtil.isEmpty(o1.getBusiName())&&
                    StringUtil.isNotEmpty(o2.getBusiName())){
                return 1;
            } else {
                return -1;
            }
            return 0;
        }
    };

    /**
     *按业务名称升序
     */
    public static Comparator<ProcessInfo> BUSINAME_ASC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (StringUtil.isNotEmpty(o1.getBusiName()) &&
                    StringUtil.isNotEmpty(o2.getBusiName())) {
                if (o1.getBusiName().compareTo(o2.getBusiName()) > 0) {
                    return 1;
                }
                if (o1.getBusiName().compareTo(o2.getBusiName()) < 0) {
                    return -1;
                }
            }else if(StringUtil.isNotEmpty(o1.getBusiName())&&
                    StringUtil.isEmpty(o2.getBusiName())){
                return 1;
            }else if(StringUtil.isEmpty(o1.getBusiName())&&
                    StringUtil.isNotEmpty(o2.getBusiName())){
                return -1;
            } else {
                return 1;
            }
            return 0;
        }
    };


    /**
     *按模块名称降序
     */
    public static Comparator<ProcessInfo> MODULE_DESC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (StringUtil.isNotEmpty(o1.getModuleName()) &&
                    StringUtil.isNotEmpty(o2.getModuleName())) {
                if (o1.getModuleName().compareTo(o2.getModuleName()) > 0) {
                    return -1;
                }
                if (o1.getModuleName().compareTo(o2.getModuleName()) < 0) {
                    return 1;
                }
            }else if(StringUtil.isNotEmpty(o1.getModuleName())&&
                    StringUtil.isEmpty(o2.getModuleName())){
                return -1;
            }else if(StringUtil.isEmpty(o1.getModuleName())&&
                    StringUtil.isNotEmpty(o2.getModuleName())){
                return 1;
            } else {
                return -1;
            }
            return 0;
        }
    };

    /**
     *按模块名称升序
     */
    public static Comparator<ProcessInfo> MODULE_ASC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (StringUtil.isNotEmpty(o1.getModuleName()) &&
                    StringUtil.isNotEmpty(o2.getModuleName())) {
                if (o1.getModuleName().compareTo(o2.getModuleName()) > 0) {
                    return 1;
                }
                if (o1.getModuleName().compareTo(o2.getModuleName()) < 0) {
                    return -1;
                }
            }else if(StringUtil.isNotEmpty(o1.getModuleName())&&
                    StringUtil.isEmpty(o2.getModuleName())){
                return 1;
            }else if(StringUtil.isEmpty(o1.getModuleName())&&
                    StringUtil.isNotEmpty(o2.getModuleName())){
                return -1;
            } else {
                return 1;
            }
            return 0;
        }
    };


    /**
     *按进程名称降序
     */
    public static Comparator<ProcessInfo> PROCESSNAME_DESC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (StringUtil.isNotEmpty(o1.getProcessName()) &&
                    StringUtil.isNotEmpty(o2.getProcessName())) {
                if (o1.getProcessName().compareTo(o2.getProcessName()) > 0) {
                    return -1;
                }
                if (o1.getProcessName().compareTo(o2.getProcessName()) < 0) {
                    return 1;
                }
            }else if(StringUtil.isNotEmpty(o1.getProcessName())&&
                    StringUtil.isEmpty(o2.getProcessName())){
                return -1;
            }else if(StringUtil.isEmpty(o1.getProcessName())&&
                    StringUtil.isNotEmpty(o2.getProcessName())){
                return 1;
            } else {
                return -1;
            }
            return 0;
        }
    };

    /**
     *按进程名称升序
     */
    public static Comparator<ProcessInfo> PROCESSNAME_ASC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (StringUtil.isNotEmpty(o1.getProcessName()) &&
                    StringUtil.isNotEmpty(o2.getProcessName())) {
                if (o1.getProcessName().compareTo(o2.getProcessName()) > 0) {
                    return 1;
                }
                if (o1.getProcessName().compareTo(o2.getProcessName()) < 0) {
                    return -1;
                }
            }else if(StringUtil.isNotEmpty(o1.getProcessName())&&
                    StringUtil.isEmpty(o2.getProcessName())){
                return 1;
            }else if(StringUtil.isEmpty(o1.getProcessName())&&
                    StringUtil.isNotEmpty(o2.getProcessName())){
                return -1;
            } else {
                return 1;
            }
            return 0;
        }
    };

    /**
     * 按进程状态 降序
     */
    public static final Comparator<ProcessInfo> PROCESSSTATUS_DESC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (o1.getProcessStatus()>o2.getProcessStatus()) {
                return -1;
            }
            if (o1.getProcessStatus()<o2.getProcessStatus()) {
                return 1;
            }
            return 0;
        }
    };

    /**
     * 按进程状态 升序
     */
    public static final Comparator<ProcessInfo> PROCESSSTATUS_ASC = new Comparator<ProcessInfo>() {
        @Override
        public int compare(ProcessInfo o1, ProcessInfo o2) {
            if (o1.getProcessStatus()>o2.getProcessStatus()) {
                return 1;
            }
            if (o1.getProcessStatus()<o2.getProcessStatus()) {
                return -1;
            }
            return 0;
        }
    };

}
