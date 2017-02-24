package com.dataeye.omp.common;

/**
 * 进程信息
 * @author chenfanglin
 * @date 2016年3月7日 下午2:35:58
 */
public class ProcessInfo {
    /**进程名称*/
    private String name;

    /**ip*/
    private String ip;

    /**端口*/
    private int port;

    /**进程状态*/
    private int processStatus;

    /**端口状态*/
    private int portStatus;

    /**时间戳*/
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public int getPortStatus() {
        return portStatus;
    }

    public void setPortStatus(int portStatus) {
        this.portStatus = portStatus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
