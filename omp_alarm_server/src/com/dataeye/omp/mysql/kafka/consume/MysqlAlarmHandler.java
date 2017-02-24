package com.dataeye.omp.mysql.kafka.consume;

import com.dataeye.omp.alarm.kafka.produce.ProducerAdapter;
import com.dataeye.omp.common.Constant;
import com.dataeye.omp.common.ResourceHandler;
import com.dataeye.omp.dbproxy.mysql.MysqlUtil;
import com.dataeye.omp.utils.DateUtils;
import com.dataeye.omp.utils.ServerUtils;
import com.dataeye.util.log.DELogger;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wendywang
 * @since 2016/4/4 13:30
 */
public class MysqlAlarmHandler implements Runnable {

    private Logger logger = DELogger.getLogger("mysql_log");

    private KafkaStream<String, String> stream;

    private static final String DEFAULT_SEPARATOR = ",";

    public MysqlAlarmHandler(KafkaStream<String, String> stream) {
        this.stream = stream;
    }

    @Override
    public void run() {
        try {
            for (MessageAndMetadata<String, String> kafkaMsg : stream) {
                String message = kafkaMsg.message();

                logger.info(Thread.currentThread().getName() +
                        ": partition[" + kafkaMsg.partition() + "],"
                        + "offset[" + kafkaMsg.offset() + "], "
                        + kafkaMsg.message());

                if (StringUtil.isEmpty(message)) return;

                MysqlMsg mysqlMsg = MysqlMsg.parseJson(message);
                List<MysqlMsg> featureList = mysqlMsg.getFeature_list();
                if (null == featureList || featureList.size() == 0) return;

                for (MysqlMsg msg : featureList) {
                    String client = msg.getClient();
                    int serverID = ServerUtils.getServerIDByIP(client);
                    int port = msg.getPort();
                    MysqlAlarmRule rule = getMysqlAlarmRule(serverID, port);
                    if (rule.getServerID() > 0) {
                        checkRuleAndSendMessage(msg, rule, featureList);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("处理消息异常：" + e.getMessage());
        }
    }

    /**
     * @param msg  MysqlMsg
     * @param rule MysqlAlarmRule
     */
    private void checkRuleAndSendMessage(MysqlMsg msg, MysqlAlarmRule rule,
                                         List<MysqlMsg> featureList) {
        int fid = msg.getFid();
        int serverID = rule.getServerID();
        int port = rule.getPort();

        String value = msg.getValue();
        //  打开文件数/限定文件数 > N  告警
        if (Constant.OPENFILES == fid) {
            Integer fileOpenLimit = 0;
            for (MysqlMsg mysql : featureList) {
                if (mysql.getFid() == Constant.OPENFILELIMIT
                        && mysql.getPort()==msg.getPort()) {
                    fileOpenLimit = Integer.parseInt(mysql.getValue());
                }
            }
            double opened = new BigDecimal(value).divide(
                    new BigDecimal(fileOpenLimit),2,
                    BigDecimal.ROUND_DOWN).doubleValue() * 100;

            if (opened > rule.getOpenFile()) {
                coreProcess(msg, rule);
            } else {
                int status = getAlarmStatus(serverID, port, fid);
                if (status == 1) {
                    restoreAlarm(serverID, port, fid);
                }
            }

        }

        //  连接数大于N告警
        if (Constant.MYSQL_CONN == fid) {
            int v = Integer.parseInt(value);

            if (v > rule.getConnection()) {
                coreProcess(msg, rule);
            } else {
                int status = getAlarmStatus(serverID, port, fid);
                if (status == 1) {
                    restoreAlarm(serverID, port, fid);
                }
            }
        }

        //锁数量大于N 告警
        if (Constant.LOCK_NUM == fid) {
            int v = Integer.parseInt(value);
            if (v > rule.getLockNum()) {
                coreProcess(msg, rule);
            } else {
                int status = getAlarmStatus(serverID, port, fid);
                //如果状态异常则恢复
                if (status == 1) {
                    restoreAlarm(serverID, port, fid);
                }
            }
        }

        //最大锁时长大于N 告警
        if (Constant.LOCK_TIME == fid) {

            if (StringUtil.isEmpty(msg.getValue())) {
                msg.setValue(String.valueOf(0));
            }

            String[] vs = msg.getValue().split(DEFAULT_SEPARATOR);
            int lockTimeTop=0;

            for (String v : vs) {
                if (Integer.parseInt(v)>lockTimeTop) {
                    lockTimeTop = Integer.parseInt(v);
                }
            }

            if (lockTimeTop > rule.getLockTime()) {
                msg.setValue(String.valueOf(lockTimeTop));
                coreProcess(msg, rule);
            } else {
                int status = getAlarmStatus(serverID, port, fid);
                if (status == 1) {
                    restoreAlarm(serverID, port, fid);
                }
            }
        }

        // 慢sql数大于N 告警
        if (Constant.SLOW_SQL == fid) {
            int v = Integer.parseInt(value);
            if (v > rule.getSlowSql()) {
                coreProcess(msg, rule);
            } else {
                int status = getAlarmStatus(serverID, port, fid);
                if (status == 1) {
                    restoreAlarm(serverID, port, fid);
                }
            }
        }
    }

    private void coreProcess(MysqlMsg msg, MysqlAlarmRule rule) {
        int frequency = getCurrentFrequency(rule.getServerID(), rule.getPort(), msg.getFid());
        if (frequency > rule.getMaxFrequency()) {
            logger.info(Constant.BASIC_TIME_FORMAT.format(new Date())
                    + "超过周期内最大告警次数！"
                    + " 设备：" + rule.getServerID() + " 端口：" + rule.getPort()
                    + " 特性：" + msg.getFid());
            return;
        } else {
            // 2.1当前linux时间戳
            long currentTime = DateUtils.unixTimestamp();
            // 2.2获取上次发送告警的时间
            long alarmTime = getLastAlarmTime(rule.getServerID(), rule.getPort(), msg.getFid());

            if (alarmTime <= 0) {
                // 第一次发送告警，直接发送
                // 更新告警历史状态
                updateAlarmStatus(rule.getServerID(), rule.getPort(), msg.getFid(), frequency);
                // 发送告警邮件
                sendProcessAlarmMail(msg);
                recordSendAlarm(rule.getServerID(), rule.getPort(), msg.getFid());
            } else {
                // 2.3计算出下次应该发送告警的时间
                long nextAlarmTime = alarmTime + rule.getAlarmInterval() * 60;
                if (currentTime >= nextAlarmTime) {
                    // 更新告警历史状态
                    updateAlarmStatus(rule.getServerID(), rule.getPort(), msg.getFid(), frequency);
                    // 3.间隔时间到，发送告警
                    sendProcessAlarmMail(msg);
                    // 4.记录发送告警，用作后续统计
                    recordSendAlarm(rule.getServerID(), rule.getPort(), msg.getFid());
                }
            }

        }
    }

    /**
     * 发送告警邮件
     *
     * @param msg MysqlMsg
     */
    private void sendProcessAlarmMail(MysqlMsg msg) {
        int serverID = ServerUtils.getServerIDByIP(msg.getClient());
        int port = msg.getPort();
        String object = msg.getObject();
        String value = msg.getValue();
        String time = msg.getTime();
        int fid = msg.getFid();
        String email = getAlarmAddress(serverID, msg.getPort());
        String subject = ResourceHandler.getProperties("mysql_alarm");
        String content = getMysqlAlarmContent(fid, serverID, port, object, value, time);
        ProducerAdapter.sendAlarmTOKafka(email, subject, content);
    }

    /**
     * 获取告警内容
     *
     * @param devId
     * @param port
     * @param object
     * @param value
     * @param time
     * @return
     */
    private String getMysqlAlarmContent(int fid, int devId, int port, String object,
                                        String value, String time) {
        StringBuffer sb = new StringBuffer();
        try {
            String sql_host = "select host_name from " + Constant.SERVER_LIST
                    + " where id = ?";
            // 主机名
            JdbcTemplate jdbcTemplateMonitor = MysqlUtil.getJdbcTemplateMonitor();
            String hostName = jdbcTemplateMonitor.queryForString(sql_host, devId);
            String sql_nw_ip = "select group_concat(ip) from " + Constant.SERVER_IP_LIST
                    + " where svr_id = ? and type = 0";
            // 内网IP
            String nw = jdbcTemplateMonitor.queryForString(sql_nw_ip, devId);

            sb.append(ResourceHandler.getProperties("alarm_time")).append(time).append("\n")
                    .append(ResourceHandler.getProperties("host_name")).append(hostName).append("\n")
                    .append(ResourceHandler.getProperties("nwip_value")).append(nw).append("\n")
                    .append(ResourceHandler.getProperties("port_value")).append(port).append("\n")
                    .append(ResourceHandler.getProperties(object)).append(value);

            if (fid == Constant.LOCK_TIME) {
                sb.append(ResourceHandler.getProperties("seconds"));
            }

            logger.info(Constant.BASIC_TIME_FORMAT.format(new Date()) +
                    "获取告警内容:" + sb.toString());
        } catch (Exception e) {
            logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) +
                    "【异常】获取告警内容：" + e.getMessage());
        }
        return sb.toString();
    }

    private String getAlarmAddress(int serverID, int port) {
        String sql_main = "select b.email from " + Constant.DC_ALARM_MYSQL_RULE
                + " a," + Constant.EMPLOYEE
                + " b where a.mainPrincipal = b.id " + "and a.serverID = ? and a.port = ?";
        String sql_others = "select bakPrincipal from " + Constant.DC_ALARM_MYSQL_RULE
                + " where serverID = ? and port = ?";
        String sql_name = "select email from " + Constant.EMPLOYEE + " where id = ?";
        try {
            // 获取主要负责人
            JdbcTemplate jdbcTemplateMonitor = MysqlUtil.getJdbcTemplateMonitor();
            String mail = jdbcTemplateMonitor.queryForString(sql_main, serverID, port);
            String bak_id = jdbcTemplateMonitor.queryForString(sql_others, serverID, port);
            List<String> list = new ArrayList<String>();
            if (StringUtil.isNotEmpty(bak_id)) {
                String[] ids = bak_id.split(",");
                for (int i = 0; i < ids.length; i++) {
                    String name = jdbcTemplateMonitor.queryForString(sql_name, ids[i]);
                    list.add(name);
                }
            }
            String bak_email = StringUtil.join(Constant.SEPARATOR, list);
            if (StringUtil.isNotEmpty(bak_email)) {
                mail = mail + Constant.SEPARATOR + bak_email;
            }

            String omp = ResourceHandler.getProperties("alarm_omp");

            if (StringUtil.isNotEmpty(mail)) {
                mail = mail + Constant.SEPARATOR + omp;
            }
            logger.info(Constant.BASIC_TIME_FORMAT.format(new Date()) +
                    "获取邮件收件人, mail:" + mail );
            return mail;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) +
                    "【异常】获取邮件收件人：" + e.getMessage());
        }
        return null;
    }

    /**
     * 记录当前告警状态和时间
     * @param serverID
     * @param port
     * @param fid
     * @param frequency
     */
    private void updateAlarmStatus(int serverID, int port, int fid, int frequency) {
        try {
            long currentTime = DateUtils.unixTimestamp();

            JdbcTemplate jdbcTemplateMonitor = MysqlUtil.getJdbcTemplateMonitor();
            String sql = "insert into " + Constant.DC_ALARM_MYSQL_CONTROL
                    + " (serverID,port,featureID,alarmTime,frequency,status) values"
                    + " (?, ?, ?, ?, ?, ?)";

            if (frequency < 0) {
                jdbcTemplateMonitor.insert(sql, serverID, port, fid, currentTime, 1, 1);
            } else {

                sql = "update " + Constant.DC_ALARM_MYSQL_CONTROL
                        + " set alarmTime = ?,frequency = ?,status = 1 "
                        + "where serverID = ? and port = ? and featureID = ?";
                frequency++;
                jdbcTemplateMonitor.update(sql, currentTime, frequency, serverID, port, fid);
            }
        } catch (Exception e) {
            logger.error(Constant.BASIC_TIME_FORMAT.format(new Date())
                    + "【异常】更新告警历史状态：" + e.getMessage());
        }
    }

    /**
     * 记录当前告警状态和时间
     * @param serverID
     * @param port
     * @param fid
     */
    private int getAlarmStatus(int serverID, int port, int fid) {
        try {
            JdbcTemplate jdbcTemplateMonitor = MysqlUtil.getJdbcTemplateMonitor();
            String sql = "select status from " + Constant.DC_ALARM_MYSQL_CONTROL
                    + " where serverID = ? and port = ? and featureID = ? ";
            int status = jdbcTemplateMonitor.queryForInt(sql, serverID, port, fid);
            return status;
        } catch (Exception e) {
            logger.error(Constant.BASIC_TIME_FORMAT.format(new Date())
                    + "【异常】更新告警历史状态：" + e.getMessage());
        }
        return 0;
    }

    /**
     * 获取上次告警时间
     * @param serverID
     * @param port
     * @return
     */
    private long getLastAlarmTime(int serverID, int port,int fid) {
        String sql = "select alarmTime from " + Constant.DC_ALARM_MYSQL_CONTROL
                + " where serverID = ? and port = ? and featureID = ?";
        try {
            JdbcTemplate jdbcTemplateMonitor = MysqlUtil.getJdbcTemplateMonitor();
            long alarmTime = jdbcTemplateMonitor.queryForLong(sql, serverID, port, fid);
            return alarmTime;
        } catch (Exception e) {
            logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) +
                    "【异常】异常已经恢复：" + e.getMessage());
        }
        return 0;
    }

    /**
     * 获取当前已经告警次数
     *
     * @param serverID 机器ID
     * @param port 端口
     * @return frequency
     */
    private int getCurrentFrequency(int serverID, int port,int fid) {
        String sql = "select frequency from " + Constant.DC_ALARM_MYSQL_CONTROL
                + " where serverID = ? and port = ? and featureID = ? ";
        try {
            JdbcTemplate jdbcTemplateMonitor = MysqlUtil.getJdbcTemplateMonitor();
            int frequency = jdbcTemplateMonitor.queryForInt(sql, serverID, port, fid);
            return frequency;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) +
                    "【异常】获取当前已经告警次数：" + e.getMessage());
        }
        return 0;


    }

    /**
     * 获取mysql告警规则
     * @param devId  设备ID
     * @param port   端口
     * @return
     */
    private MysqlAlarmRule getMysqlAlarmRule(int devId, int port) {
        final MysqlAlarmRule rule = new MysqlAlarmRule();

        try {
            String sql = "select id, serverID,port, openFile, connection, lockNum, lockTime,"
                    + "slowSql, maxFrequency, mainPrincipal, bakPrincipal, alarmType,"
                    + "alarmInterval from dc_monitor_mysql_rule where serverID=? and port = ? ";
            JdbcTemplate jdbcTemplateMonitor = MysqlUtil.getJdbcTemplateMonitor();
            jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    rule.setServerID(rs.getInt("serverID"));
                    rule.setPort(rs.getInt("port"));
                    rule.setOpenFile(rs.getInt("openFile"));
                    rule.setConnection(rs.getInt("connection"));
                    rule.setLockNum(rs.getInt("lockNum"));
                    rule.setLockTime(rs.getInt("lockTime"));
                    rule.setSlowSql(rs.getInt("slowSql"));
                    rule.setMaxFrequency(rs.getInt("maxFrequency"));
                    rule.setMainPrincipal(rs.getInt("mainPrincipal"));
                    rule.setBakPrincipal(rs.getString("bakPrincipal"));
                    rule.setAlarmType(rs.getString("alarmType"));
                    rule.setAlarmInterval(rs.getInt("alarmInterval"));
                }
            }, devId, port);

        } catch (Exception e) {
            logger.info("get mysql alarm rule error");
        }
        return rule;
    }


    /**
     * 记录发送告警情况
     * @param serverID 机器ID
     * @param port      端口
     * @param fid       特性ID
     */
    private void recordSendAlarm(int serverID, int port, int fid) {
        long currentTime = DateUtils.unixTimestamp000();
        String sql = "insert into " + Constant.DC_ALARM_MYSQL_EVERYDAY
                + "(alarmTime, serverID, port, featureID) values(?,?,?,?)";
        try {
            JdbcTemplate jdbcTemplateMonitor = MysqlUtil.getJdbcTemplateMonitor();
            jdbcTemplateMonitor.insert(sql, currentTime, serverID, port, fid);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(Constant.BASIC_TIME_FORMAT.format(new Date())
                    + "【异常】记录发送告警情况：" + e.getMessage());
        }

    }

    /**
     * 异常已经恢复
     */
    private void restoreAlarm(int serverId, int port, int fid) {
        long currentTime = DateUtils.unixTimestamp();
        String sql = "update " + Constant.DC_ALARM_MYSQL_CONTROL
                + " set restoreTime = ?, frequency = 0, status = 0 "
                + "where serverID = ? and port = ? and featureID = ?";
        try {
            JdbcTemplate jdbcTemplateMonitor = MysqlUtil.getJdbcTemplateMonitor();
            jdbcTemplateMonitor.update(sql, currentTime, serverId, port, fid);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(Constant.BASIC_TIME_FORMAT.format(new Date()) +
                    "【异常】异常已经恢复：" + e.getMessage());
        }
    }


}
