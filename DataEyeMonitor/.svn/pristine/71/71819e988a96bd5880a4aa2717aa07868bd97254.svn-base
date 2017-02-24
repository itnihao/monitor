package com.dataeye.omp.module.cmdb.process;

import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.utils.DateUtils;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  wendy
 * @since 2016/3/5 17:29
 */
@Service
public class ProcessDao {

    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplateMonitor;

    /**
     * 查询所有模块信息
     * @return List<Module>
     * @throws ServerException
     */
    public List<Module> queryModuleList() throws ServerException {
        final List<Module> modules = new ArrayList<>();
        try {
            String sql = "select b1.id moduleId, b1.name moduleName,"
                    + " b1.pid busiId,b2.name busiName,b3.processNum,"
                    + "e.name mainPrincipal from (select id, name, pid, om_person mainPrincipal from "
                    + Constant.Table.BUSINESS + " where id not in  (select id from "
                    + Constant.Table.BUSINESS + " where pid =0)) b1 left join "
                    + Constant.Table.BUSINESS + " b2 on b1.pid = b2.id left join "
                    + "(select moduleId, count(id) processNum from "
                    + Constant.Table.PROCESSINFO + " group by moduleId) b3 "
                    + "on b1.id= b3.moduleId left join employee e on b1.mainPrincipal= e.id";

            jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    Module module = new Module();
                    module.setModuleID(rs.getInt("moduleId"));
                    module.setModuleName(rs.getString("moduleName"));
                    module.setBusiID(rs.getInt("busiId"));
                    module.setBusiName(rs.getString("busiName"));
                    module.setMainPrincipal(rs.getString("mainPrincipal"));
                    module.setProcessNum(rs.getInt("processNum"));
                    modules.add(module);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
        return modules;
    }

    /**
     * 查询进程信息
     * @return List<ProcessInfo>
     * @throws ServerException
     */
    public List<ProcessInfo> queryProcessInfo() throws ServerException {
         final List<ProcessInfo> list = new ArrayList<>();
        try {
            String sql = "select dc.id, dc.processName, dc.port, sl.host_name serverName,sil.ip,"
                    + "dc.serverID, dc.businessID busiID, b1.name busiName,"
                    + "dc.moduleID, b2.name moduleName, dc.status, "
                    + " (select count(1) from "+ Constant.Table.PROCESSRULE+" where "
                    + "processID = dc.id) count from " + Constant.Table.PROCESSINFO + " dc "
                    + " LEFT JOIN " + Constant.Table.BUSINESS + " b1 on dc.businessID=b1.id "
                    + " LEFT JOIN " + Constant.Table.BUSINESS + " b2 on dc.moduleID=b2.id"
                    + " LEFT JOIN server_list sl on dc.serverID = sl.id"
                    + " Left JOIN server_ip_list sil on (dc.serverID = sil.svr_id and sil.type = 0)";
            jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    ProcessInfo  process= new ProcessInfo();
                    process.setId(rs.getInt("id"));
                    process.setProcessName(rs.getString("processName"));
                    process.setServeID(rs.getInt("serverID"));
                    process.setPort(rs.getInt("port"));
                    process.setBusiID(rs.getInt("busiID"));
                    process.setBusiName(rs.getString("busiName"));
                    process.setServerName(rs.getString("serverName"));
                    process.setPrivateIp(rs.getString("ip"));
                    process.setModuleID(rs.getInt("moduleID"));
                    process.setModuleName(rs.getString("moduleName"));
                    process.setMonitorStatus(rs.getInt("status"));
                    int count = rs.getInt("count");
                    if (count > 0) {
                        process.setAddEditFlag(1);
                    } else {
                        process.setAddEditFlag(0);
                    }

                    list.add(process);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
        return list;
    }

    public boolean checkPortAndNameExits(int serverID, int port, String processName)
            throws ServerException {
        int count = 0;
        try {
            String sql = "select count(1) from " + Constant.Table.PROCESSINFO
                    + " where serverID = ? and port = ? and processName = ?";

            count = jdbcTemplateMonitor.queryForInt(sql, serverID, port, processName);

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }

        return count > 0;
    }

    /**
     * 同一台机器端口不能相同
     * @param serverID 机器ID
     * @param port 端口
     * @return true 存在  false 不存在
     * @throws ServerException
     */
    public boolean checkProcessPortExitsByServerId(int serverID, int port)
            throws ServerException {
        int count = 0;
        try {
            String sql = "select count(1) from " + Constant.Table.PROCESSINFO
                    + " where serverID = ? and port = ?";
            count = jdbcTemplateMonitor.queryForInt(sql, serverID, port);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
        return count > 0;
    }

    /**
     * 同一台机器端口不能相同
     * @param serverID 机器ID
     * @param port  端口
     * @param processId 进程ID
     * @return true 存在  false 不存在
     * @throws ServerException
     */
    public boolean checkProcessPortExitsByServerId(int serverID, int port, int processId)
            throws ServerException {
        int count = 0;
        try {
            String sql = "select count(1) from " + Constant.Table.PROCESSINFO
                    + " where serverID = ? and port = ? and id <> ?";
            count = jdbcTemplateMonitor.queryForInt(sql, serverID, port, processId);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
        return count > 0;
    }


    /**
     * 同一台机器进程名不能相同
     * @param serverID 机器ID
     * @param processName 进程名称
     * @return  true 存在  false 不存在
     * @throws ServerException
     */
    public boolean checkProcessNameExitsByServerId(int serverID, String processName)
            throws ServerException {
        int count = 0;
        try {
            String sql = "select count(1) from " + Constant.Table.PROCESSINFO
                    + " where serverID = ? and processName = ?";
            count = jdbcTemplateMonitor.queryForInt(sql, serverID, processName);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
        return count > 0;
    }

    /**
     * 同一台机器进程名称不能相同
     * @param serverID 机器ID
     * @param processName 进程名称
     * @param processId 进程ID
     * @return true 存在  false 不存在
     * @throws ServerException
     */
    public boolean checkProcessNameExitsByServerId(int serverID, String processName, int processId)
            throws ServerException {
        int count = 0;
        try {
            String sql = "select count(1) from " + Constant.Table.PROCESSINFO
                    + " where serverID = ? and processName = ? and id <> ?";
            count = jdbcTemplateMonitor.queryForInt(sql, serverID, processName, processId);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
        return count > 0;
    }


    /**
     * 添加进程信息
     *
     * @param processID 进程ID
     * @param processName 进程名称
     * @param busiID  业务ID
     * @param moduleID 模块ID
     * @param servers  需要添加进程的服务器列表
     * @param port     端口
     * @param deployPath  部署路径
     * @param configPath  配置文件路径
     * @param logsPath   日志文件路径
     * @param monitorType 监控类型   0 进程死活  1 端口检测  2 全部  3 全不选
     * @param alarmInterval  告警时间间隔
     * @param mainPrincipal   主要负责人
     * @param bakPrincipal  备份负责人
     * @throws ServerException
     */
    public long addUpdateProcess(int processID, String processName,
                                 int busiID, int moduleID, List<Integer> servers,
                                 int port, String deployPath, String configPath,
                                 String logsPath, int mainPrincipal, String bakPrincipal,
                                 int monitorType, int alarmInterval, int maxFrequency,
                                 String alarmType) throws ServerException {
        long id = 0;
        try {
            String sql = "insert into " + Constant.Table.PROCESSINFO + " ("
                    + "processName, port, businessID, moduleID,"
                    + "serverID, deployPath, configPath, logsPath, mainPrincipal,"
                    + "bakPrincipal, createTime, status)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            if (processID < 0) {
                for (Integer serverID : servers) {
                    id = jdbcTemplateMonitor.insert(sql, processName, port, busiID,
                            moduleID, serverID, deployPath == null ? "" : deployPath,
                            configPath == null ? "" : configPath,
                            logsPath == null ? "" : logsPath,
                            mainPrincipal, bakPrincipal, DateUtils.unixTimestamp(), 1);

                    String sql1 = "insert into " + Constant.Table.PROCESSRULE
                            + "(processID, monitorType, alarmInterval,"
                            + "maxFrequency, alarmType, createTime) values ( ?, ?, ?, ?, ?, ?) ";

                    jdbcTemplateMonitor.insert(sql1, id, monitorType,
                            alarmInterval, maxFrequency, alarmType, DateUtils.unixTimestamp());
                }
            } else {
                sql = "update " + Constant.Table.PROCESSINFO + " set processName = ?,"
                        + " port =?, businessID = ?, moduleID = ?, serverID = ?, "
                        + "deployPath = ?, configPath = ?, logsPath = ?, "
                        + "mainPrincipal = ?, bakPrincipal = ?, updateTime = ? "
                        + "where id = ?";

                id = jdbcTemplateMonitor.update(sql, processName, port, busiID,
                        moduleID, servers.get(0), deployPath, configPath == null ? "" : configPath,
                        logsPath == null ? "" : logsPath, mainPrincipal, bakPrincipal,
                        DateUtils.unixTimestamp(), processID);

                sql = "update " + Constant.Table.PROCESSRULE + " set monitorType = ?, "
                        + "alarmInterval = ?, maxFrequency = ?, alarmType = ?, "
                        + "updateTime = ? where processID = ?";

                jdbcTemplateMonitor.update(sql, monitorType, alarmInterval,
                        maxFrequency, alarmType, DateUtils.unixTimestamp(), processID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
        return id;
    }

    /**
     * 添加进程告警规则
     * @param processID 进程ID
     * @param monitorType  监控类型
     * @param alarmInterval  告警时间间隔
     * @param maxFrequency  最大次数
     * @param alarmType  告警类型   0 邮件  1 短信  2 app推送
     */
    public void addProcessRule(int processID, int monitorType, int alarmInterval,
                               int maxFrequency, String alarmType) throws ServerException {
        try {

            String sql = "insert into " + Constant.Table.PROCESSRULE
                    + "(processID, monitorType, alarmInterval,"
                    + "maxFrequency, alarmType, createTime) values ( ?, ?, ?, ?, ?, ?) ";

            jdbcTemplateMonitor.insert(sql, processID, monitorType,
                alarmInterval, maxFrequency, alarmType, DateUtils.unixTimestamp());
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
    }

    /**
     * 修改进程规则
     * @param processID   进程ID
     * @param monitorType  监控类型
     * @param alarmInterval  告警时间间隔
     * @param maxFrequency  最大次数
     * @param alarmType    告警类型
     */
    public void updateProcessRule(int processID, int monitorType, int alarmInterval,
                                  int maxFrequency, String alarmType) throws ServerException {
        try {
        String sql = "update " + Constant.Table.PROCESSRULE + " set monitorType = ?, "
                + "alarmInterval = ?, maxFrequency = ?, alarmType = ?, "
                + "updateTime = ? where processID = ?";

        jdbcTemplateMonitor.update(sql, monitorType, alarmInterval,
                maxFrequency, alarmType, DateUtils.unixTimestamp(), processID);
    } catch (Exception e) {
        e.printStackTrace();
        ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
    }
    }

    /**
     * 获取进程详细信息
     * @param processID  进程ID
     * @return   Map
     * @throws ServerException
     */
    public Map<String, Object> getProcessInfoByID(int processID) throws ServerException {
        final Map<String, Object> map = new HashMap<>();
        try {
            String sql = "select id, processName, port, businessID, moduleID, serverID,"
                    + "deployPath, configPath, logsPath, mainPrincipal, bakPrincipal "
                    + "from " + Constant.Table.PROCESSINFO + " where id = ?";
            jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    map.put("id", rs.getInt("id"));
                    map.put("processName", rs.getString("processName"));
                    map.put("port", rs.getInt("port"));
                    map.put("moduleID", rs.getInt("moduleID"));
                    map.put("busiID", rs.getInt("businessID"));
                    map.put("serverID", rs.getInt("serverID"));
                    map.put("deployPath", rs.getString("deployPath"));
                    map.put("configPath", rs.getString("configPath"));
                    map.put("logPath", rs.getString("logsPath"));
                    map.put("mainPrincipal", rs.getInt("mainPrincipal"));
                    map.put("bakPrincipal", rs.getString("bakPrincipal"));

                }
            }, processID);

            sql = "select monitorType, alarmInterval, maxFrequency, alarmType "
                    + "from dc_process_alarm_rule "
                    + " where processID = ?";

            jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    map.put("monitorType", rs.getInt("monitorType"));
                    map.put("alarmInterval", rs.getInt("alarmInterval"));
                    map.put("alarmType", rs.getString("alarmType"));
                    map.put("maxFrequency", rs.getInt("maxFrequency"));
                }
            }, processID);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
        return map;
    }

    /**
     * 获取告警规则详情
     * @param processID 进程ID
     * @return  Map
     * @throws ServerException
     */
    public Map<String, Object> getProcessRuleByID(final int processID) throws ServerException {
        final Map<String, Object> map = new HashMap<>();
        try {
            String sql = "select dc.id, dr.monitorType, dr.alarmInterval,"
                    + "dr.maxFrequency, dr.alarmType, dc.processName,"
                    + "dc.businessID,dc.moduleID,dc.serverID,dc.port "
                    + "from dc_process_info dc "
                    + "left join dc_process_alarm_rule dr "
                    + "on dr.processID = dc.id where dc.id = ?";

            jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    map.put("processID", rs.getInt("id"));
                    map.put("businessID", rs.getInt("businessID"));
                    map.put("moduleID", rs.getInt("moduleID"));
                    map.put("serverID", rs.getInt("serverID"));
                    map.put("port", rs.getInt("port"));
                    map.put("processName", rs.getString("processName"));
                    map.put("monitorType", rs.getInt("monitorType"));
                    map.put("alarmInterval", rs.getInt("alarmInterval"));
                    map.put("alarmType", rs.getString("alarmType"));
                    map.put("maxFrequency", rs.getInt("maxFrequency"));
                }
            }, processID);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
        return map;
    }

    /**
     * 修改进程状态
     * @param processID 进程ID
     * @throws ServerException
     */
    public void changeProcessStatus(int processID) throws ServerException {
        try {
            String sql = "select status from " + Constant.Table.PROCESSINFO
                    + " where id = ?";
            int status = jdbcTemplateMonitor.queryForInt(sql, processID);
            if (status > -1) {
                sql = "update " + Constant.Table.PROCESSINFO
                        + " set status = ? where id = ?";
                if (status == 0) {
                    jdbcTemplateMonitor.update(sql, 1, processID);
                } else {
                    jdbcTemplateMonitor.update(sql, 0, processID);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
    }

    /**
     * 根据模块ID获取模块详细信息
     * @param moduleID 模块ID
     * @return Object
     * @throws ServerException
     */
    public Object getModuleDetailById(int moduleID) throws ServerException {
        final Map<String, Object> map = new HashMap<>();
        try {
            String sql = "select id, name , pid, om_person, " +
                    "bakPrincipal from " + Constant.Table.BUSINESS
                    +" where id = ?";
            jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    map.put("moduleID", rs.getInt("id"));
                    map.put("moduleName", rs.getString("name"));
                    map.put("busiID", rs.getInt("pid"));
                    map.put("mainPrincipal", rs.getInt("om_person"));
                    map.put("bakPrincipal", rs.getString("bakPrincipal"));
                }
            }, moduleID);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
        return map;
    }

    public void deleteProcessById(int processID) throws ServerException {
        try {
            String sql = "delete from " + Constant.Table.PROCESSINFO + " where id = ?";

            jdbcTemplateMonitor.execute(sql, processID);

            sql = "delete from " + Constant.Table.PROCESSRULE + " where processID = ?";

            jdbcTemplateMonitor.execute(sql, processID);

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
    }

    /**
     * 根据模块初始化路径和负责人信息
     * @param moduleID
     * @return
     * @throws ServerException
     */
    public Map<String, Object> getInitPathAndPrincipal(int moduleID) throws ServerException {
        String sql = "select moduleID, deployPath, configPath, logsPath, mainPrincipal, "
                + "max(bakPrincipal) bakPrincipal from dc_process_info where moduleID = ? limit 1";
        final Map<String, Object> map = new HashMap<>();
        try {
            jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    map.put("deployPath", rs.getString("deployPath"));
                    map.put("configPath", rs.getString("configPath"));
                    map.put("logsPath", rs.getString("logsPath"));
                    map.put("mainPrincipal", rs.getInt("mainPrincipal"));
                    map.put("bakPrincipal", rs.getString("bakPrincipal"));
                }
            },moduleID);

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
        }
        return map;
    }
}


