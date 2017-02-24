package com.dataeye.monitor.task;

import com.dataeye.monitor.common.annocations.OneMinuteTask;
import com.dataeye.monitor.common.constants.Commands;
import com.dataeye.monitor.common.constants.Constant;
import com.dataeye.monitor.common.constants.ReportUrl;
import com.dataeye.monitor.utils.DateUtis;
import com.dataeye.monitor.utils.HttpClientUtil;
import com.dataeye.monitor.utils.ProcessUtil;
import com.dataeye.monitor.utils.StringUtils;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进程状态和端口状态数据抓取和监控
 * Created by wendy on 2016/7/4.
 */
@OneMinuteTask
public class ProcessStatusMonitor extends BaseTask {
    private List<ProcessInfo> processList = null;
    private String reportData;
    @Override
    public void run() {
        try {
            collectData();
            generateData();
            report();
        } catch (ServerException e) {
            ExceptionHandler.error("ProcessStatusMonitor error", e);
        }

    }

    private void report() throws ServerException {
        if (StringUtils.isNotEmpty(reportData)) {
            HttpClientUtil.post(ReportUrl.processReportUrl, reportData);
           // batchReportHandler.put(ReportUrl.processReportUrl, reportData);
        }
    }

    private void generateData() {
        ProcessInfo procInfo = new ProcessInfo();
        if(!processList.isEmpty()){
            procInfo.setProcList(processList);
            reportData = Constant.GSON.toJson(procInfo);
        }
    }

    private void collectData() {
        reportData = null;
        processList = new ArrayList<>();
        Map<String, List<ProcessConfig>> procConfigMap = getProcConfig();
        for (Map.Entry<String, List<ProcessConfig>> entry : procConfigMap.entrySet()) {
            collectEachProcStatus(entry.getValue());
        }
    }

    private void collectEachProcStatus(List<ProcessConfig> configs) {
        for (ProcessConfig config : configs) {
            String processName = config.getName();
            int port = config.getPort();

            ProcessInfo process = new ProcessInfo(processName, String.valueOf(port));

            if (configs.size() == 1) {
                process.setProcessStatus(isProcExits(processName, port, true)
                        ? "0"
                        : "1"
                );
            } else {
                process.setProcessStatus(isProcExits(processName, port, false)
                        ? "0"
                        : "1"
                );
            }
            process.setPortStatus(isPortExits(port) ? "0" : "1");
            processList.add(process);
        }
    }

    private boolean isPortExits(int port) {

        String portStatusCmd = Commands.portStatus.replace("{}", String.valueOf(port));
        String portStatus = ProcessUtil.execute(portStatusCmd);
        if (portStatus.contains("LISTEN")) {
            return true;
        }

        return false;
    }

    private boolean isProcExits(String processName
            , int port, boolean isProcNameUniq) {
        String procStatusCmd = null;
        if (isProcNameUniq) {
            procStatusCmd = Commands.procNameUniqStatus.replace("{}", processName);
        } else {
            procStatusCmd = Commands.procNameMultiStatus.replace("{1}", processName)
                    .replace("{2}", String.valueOf(port));

        }

        String procStatus = ProcessUtil.execute(procStatusCmd);

        if (StringUtils.isEmpty(procStatus) || "N".equals(procStatus) || "T".equals(procStatus)) {
            return false;
        }
        return true;
    }


    private Map<String, List<ProcessConfig>> getProcConfig() {
        String url = ReportUrl.processCofigUrl + "?ip=" + Constant.IP;
        String configJsonString = HttpClientUtil.get(url);
        ProcessConfig procConfig = Constant.GSON.fromJson(configJsonString, ProcessConfig.class);
        List<ProcessConfig> configs = procConfig.getProcess_list();

        //进程名和进程名对应的进程列表 一般是一对一的，
        // 但是配置进程信息是可能会出现一个进程名ps -aux 可以查到多个进程的情况
        Map<String, List<ProcessConfig>> procMap = new HashMap<>();
        for (ProcessConfig config : configs) {
            List<ProcessConfig> list = procMap.get(config.getName());
            if (list == null) {
                list = new ArrayList<>();
                procMap.put(config.getName(), list);
            }
            list.add(config);
        }
        return procMap;
    }

    class ProcessConfig {
        private String name;
        private int port;
        private int enable;

        private List<ProcessConfig> process_list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getEnable() {
            return enable;
        }

        public void setEnable(int enable) {
            this.enable = enable;
        }

        public List<ProcessConfig> getProcess_list() {
            return process_list;
        }

        public void setProcess_list(List<ProcessConfig> process_list) {
            this.process_list = process_list;
        }
    }

    class ProcessInfo {


        private String name;
        private String ip;
        private String port;
        private String processStatus;
        private String portStatus;
        private String time;


        public ProcessInfo() {
        }

        public ProcessInfo(String name, String port) {
            this.name = name;
            this.port = port;
            this.ip = Constant.IP;
            this.time = DateUtis.currentTime();
        }

        @Override
        public String toString() {
            return "ProcessInfo{" +
                    "name='" + name + '\'' +
                    ", ip='" + ip + '\'' +
                    ", port='" + port + '\'' +
                    ", processStatus='" + processStatus + '\'' +
                    ", portStatus='" + portStatus + '\'' +
                    ", time='" + time + '\'' +
                    ", process_list=" + process_list +
                    '}';
        }

        private List<ProcessInfo> process_list;

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

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getProcessStatus() {
            return processStatus;
        }

        public void setProcessStatus(String processStatus) {
            this.processStatus = processStatus;
        }

        public String getPortStatus() {
            return portStatus;
        }

        public void setPortStatus(String portStatus) {
            this.portStatus = portStatus;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<ProcessInfo> getProcess_list() {
            return process_list;
        }

        public void setProcList(List<ProcessInfo> process_list) {
            this.process_list = process_list;
        }

        public void addProcess(ProcessInfo process) {
            this.getProcess_list().add(process);
        }
    }


}