package com.dataeye.omp.report.dao;

import com.dataeye.omp.report.bean.ProcessConfig;
import com.dataeye.omp.report.bean.ProcessInfo;
import com.dataeye.omp.report.dbproxy.hbase.HbaseBatchPutHandler;
import com.dataeye.omp.report.dbproxy.hbase.PutItem;
import com.dataeye.omp.report.utils.Constant;
import com.dataeye.omp.report.utils.DateUtils;
import com.dataeye.omp.report.utils.ServerIDCache;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import com.xunlei.util.Log;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wendy
 * @since 2016/7/29
 */
@Service
public class ProcessReportDao {
    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplateMonitor;

    private final static Logger testLog = Log.getLogger("test_log");

    public void saveProcess2Hbase(String message) {
        List<ProcessInfo> processList = new ArrayList<ProcessInfo>();
        if (StringUtil.isNotEmpty(message)) {
            ProcessInfo processInfo =
                    Constant.gson.fromJson(message, ProcessInfo.class);
            processList = processInfo.getProcess_list();
        }
        String tableName = Constant.TABLE_PROCESS_CURRENT;

        ProcessInfo param = processList.get(0);
        if ("192.168.1.209".equals(param.getIp())) {
            testLog.info(message);
        }

        for (ProcessInfo process : processList) {
            int serverId = ServerIDCache.getServerId(process.getIp());
            String rowkey = String.valueOf(serverId);
            StringBuffer portStatusQualifier = new StringBuffer();
            portStatusQualifier.append(process.getPort())
                    .append(Constant.SEPARATER)
                    .append(process.getName())
                    .append(Constant.SEPARATER)
                    .append(Constant.PORTSTATUS);

            String portStatus = process.getPortStatus()
                    + "_" + DateUtils.unixTimestamp();
            Put put = new Put(Bytes.toBytes(rowkey));
            put.add(Bytes.toBytes(Constant.COLUMN_FAMILY)
                    , Bytes.toBytes(portStatusQualifier.toString())
                    , Bytes.toBytes(portStatus));
            HbaseBatchPutHandler.saveItem(new PutItem(tableName, put));

            StringBuffer procStatusQualifier = new StringBuffer();
            procStatusQualifier.append(
                    process.getPort())
                    .append(Constant.SEPARATER)
                    .append(process.getName())
                    .append(Constant.SEPARATER)
                    .append(Constant.PROCESSSTATUS);
            String procStatus = process.getProcessStatus()
                    + "_" + DateUtils.unixTimestamp();
            put = new Put(Bytes.toBytes(rowkey));
            put.add(Bytes.toBytes(Constant.COLUMN_FAMILY),
                    Bytes.toBytes(procStatusQualifier.toString()),
                    Bytes.toBytes(procStatus));
            HbaseBatchPutHandler.saveItem(new PutItem(tableName, put));
        }
    }


    /**
     * 根据IP获取配置信息列表
     * @param ip
     * @return
     */
    public List<ProcessConfig> getProcessConfigByIP(String ip) {
        int serverId = ServerIDCache.getServerId(ip);
        try {
            String sql = "select processName, port, status enable from "
                    + "dc_process_info where serverID = ? and status=1";
            final List<ProcessConfig> list = new ArrayList<>();
            jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    ProcessConfig config = new ProcessConfig();
                    config.setName(rs.getString("processName"));
                    config.setPort(rs.getInt("port"));
                    config.setEnable(rs.getInt("enable"));
                    list.add(config);
                }
            }, serverId);
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
