package com.dataeye.monitor.task;

import com.dataeye.monitor.common.annocations.FourMinuteTask;
import com.dataeye.monitor.common.constants.Commands;
import com.dataeye.monitor.common.constants.Constant;
import com.dataeye.monitor.common.constants.FID;
import com.dataeye.monitor.common.constants.ReportUrl;
import com.dataeye.monitor.utils.*;

import java.rmi.ServerException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wendy on 2016/7/4.
 */
@FourMinuteTask
public class MysqlStatusMonitor extends BaseTask {

    private String reportData;
    private static final String HOST = "127.0.0.1";
    private static final String USER = "monitor_agent";
    private static final String PASSWORD = "Ylc5dWFYUn";
    private static long lastUpdateTime = 0;
    private static final long HALFDAY = 12 * 60 * 60;
    private List<Mysql> mysqlList = null;
    private static List<String> instances = null;
    private static boolean isMysqlInstanceExits = true;

    public MysqlStatusMonitor() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            ExceptionHandler.error("mysql driver jar not exits.", e);
        }
        getMysqlInstance();
        if (instances.size() == 0) {
            isMysqlInstanceExits = false;
        }
    }

    @Override
    public void run() {
        try {
            if (!isMysqlInstanceExits) {
                return;
            }
            getMysqlInstance();
            collectData();
            generateData();
            report();
        } catch (Throwable e) {
            ExceptionHandler.error("MysqlStatusMonitor error", e);
        }
    }

    private void getMysqlInstance() {
        long now = System.currentTimeMillis() / 1000;
        long timeSinceLastUpdate = now - lastUpdateTime;
        instances = new ArrayList<>();
        if (timeSinceLastUpdate < HALFDAY) {
            return;
        }

        lastUpdateTime = now;
        String instance = ProcessUtil.execute(Commands.mysqlInstance);
        if (StringUtils.isNotEmpty(instance)) {
            instance = instance.trim();
            for (String line : instance.split(Constant.LINE)) {
                line = StringUtils.replaceMultiBlankSpace(line);
                String arr[] = line.split(Constant.BLANKSPACE);
                String port = arr[3].substring(arr[3].lastIndexOf(Constant.COLON) + 1);
                //检测此实例是否添加了检测账户，即此端口能否连接上
                if (isPortMonitorable(port)) instances.add(port);
            }
        }
    }

    private boolean isPortMonitorable(String port) {
        boolean flag = true;
        try {
            DriverManager.getConnection(getJdbcUrl(port), USER, PASSWORD);
        } catch (SQLException e) {
            flag = false;
        }
        return flag;
    }

    private void report() throws ServerException {
        if (StringUtils.isNotEmpty(reportData))
            HttpClientUtil.post(ReportUrl.mysqlReportUrl, reportData);
    }

    private void generateData() {
        MysqlFeature mysqlfeature = new MysqlFeature();
        mysqlfeature.setFeature_list(new ArrayList<MysqlFeature>());
        for (Mysql mysql : mysqlList) {
            MysqlFeature mysqlConnFeature = new MysqlFeature(
                    FID.mysql_conn.getValue(),
                    FID.mysql_conn.getName(),
                    mysql.getServerId(),
                    mysql.getMysqlConn(),
                    mysql.getPort()
            );
            mysqlfeature.addFeature(mysqlConnFeature);

            MysqlFeature mysqlLockNumFeature = new MysqlFeature(
                    FID.mysql_lock_num.getValue(),
                    FID.mysql_lock_num.getName(),
                    mysql.getServerId(),
                    mysql.getMysqlLockNum(),
                    mysql.getPort());
            mysqlfeature.addFeature(mysqlLockNumFeature);

            MysqlFeature mysqlOpenFilesFeature = new MysqlFeature(
                    FID.mysql_open_files.getValue(),
                    FID.mysql_open_files.getName(),
                    mysql.getServerId(),
                    mysql.getMysqlOpenFiles(),
                    mysql.getPort()
            );
            mysqlfeature.addFeature(mysqlOpenFilesFeature);

            MysqlFeature mysqlOpenFilesLimitFeature = new MysqlFeature(
                    FID.mysql_open_files_limit.getValue(),
                    FID.mysql_open_files_limit.getName(),
                    mysql.getServerId(),
                    mysql.getMysqlOpenFilesLimit(),
                    mysql.getPort()
            );
            mysqlfeature.addFeature(mysqlOpenFilesLimitFeature);

            MysqlFeature mysqlLockTimeFeature = new MysqlFeature(
                    FID.mysql_lock_time.getValue(),
                    FID.mysql_lock_time.getName(),
                    mysql.getServerId(),
                    mysql.getMysqlLockTime(),
                    mysql.getPort()
            );
            mysqlfeature.addFeature(mysqlLockTimeFeature);
        }
        reportData = Constant.GSON.toJson(mysqlfeature);
    }

    private void collectData() {
        reportData = null;
        Mysql mysql = null;
        mysqlList = new ArrayList<>();
        for (String port : instances) {
            try {
                mysql = new Mysql();
                mysql.setPort(port);
                queryMysqlId(mysql);
                querymysqlConnNum(mysql);
                queryMysqlLockNum(mysql);
                queryMysqlOpenFiles(mysql);
                queryMysqlOpenFilesLimit(mysql);
                queryMysqlLockTime(mysql);
                mysqlList.add(mysql);
                logger.debug(mysql.toString());
            } catch (Exception e) {
                ExceptionHandler.error("MysqlStatusMonitor sql error: ", e);
            }
        }
    }

    private void queryMysqlLockTime(Mysql mysql) {
        String port = mysql.getPort();
        String jdbcUrl = getJdbcUrl(port);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
            stmt = conn.prepareStatement(Commands.mysqlLockTime);
            rs = stmt.executeQuery();
            long mysqlLockTime = 0;
            if (rs.next()) mysqlLockTime = rs.getInt(1);
            mysql.setMysqlLockTime(String.valueOf(mysqlLockTime));
        } catch (SQLException e) {
            throw new RuntimeException("queryMysqlLockTime error");
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("queryMysqlLockTime error");
            }
        }
    }

    private void queryMysqlOpenFilesLimit(Mysql mysql) {
        String port = mysql.getPort();
        String jdbcUrl = getJdbcUrl(port);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
            stmt = conn.prepareStatement(Commands.mysqlOpenFilesLimit);
            rs = stmt.executeQuery();
            if (rs.next()) mysql.setMysqlOpenFilesLimit(rs.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException("queryMysqlOpenFilesLimit error");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("queryMysqlOpenFilesLimit error");
            }
        }

    }

    private void queryMysqlOpenFiles(Mysql mysql) {
        String port = mysql.getPort();
        String jdbcUrl = getJdbcUrl(port);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
            stmt = conn.prepareStatement(Commands.mysqlOpenFiles);
            rs = stmt.executeQuery();
            if (rs.next()) mysql.setMysqlOpenFiles(rs.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException("queryMysqlOpenFiles error");
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("queryMysqlOpenFiles error");
            }
        }
    }

    private void queryMysqlLockNum(Mysql mysql) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String port = mysql.getPort();
        String jdbcUrl = getJdbcUrl(port);
        try {
            conn = DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
            stmt = conn.prepareStatement(Commands.mysqlLockNum);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String lockNum = String.valueOf(rs.getInt(1));
                mysql.setMysqlLockNum(lockNum);
            }
        } catch (SQLException e) {
            throw new RuntimeException("queryMysqlLockNum error");
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("queryMysqlLockNum error");
            }
        }
    }

    private void querymysqlConnNum(Mysql mysql) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String port = mysql.getPort();
        String jdbcUrl = getJdbcUrl(port);
        try {
            conn = DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
            stmt = conn.prepareStatement(Commands.mysqlConn);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String mysqlConn = String.valueOf(rs.getInt(1));
                mysql.setMysqlConn(mysqlConn);
            }
        } catch (SQLException e) {
            throw new RuntimeException("querymysqlConnNum error");
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("querymysqlConnNum error");
            }
        }
    }

    private void queryMysqlId(Mysql mysql) {
        String port = mysql.getPort();
        String jdbcUrl = getJdbcUrl(port);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
            stmt = conn.prepareStatement(Commands.mysqlId);
            rs = stmt.executeQuery();
            mysql.setPort(port);
            if (rs.next()) {
                String serverId = rs.getString(1);
                mysql.setServerId(serverId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("queryMysqlId error");
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("queryMysqlId error");
            }
        }
    }

    private String getJdbcUrl(String port) {
        StringBuffer sb = new StringBuffer();
        sb.append("jdbc:mysql://")
                .append(HOST)
                .append(Constant.COLON)
                .append(port)
                .append("/mysql?useUnicode=true&characterEncoding")
                .append("=utf-8&allowMultiQueries=true");
        return sb.toString();
    }

    class Mysql {
        private String port;
        private String serverId;
        private String mysqlConn;
        private String mysqlLockNum;
        private String mysqlOpenFiles;
        private String mysqlOpenFilesLimit;
        private String mysqlLockTime;

        @Override
        public String toString() {
            return "Mysql{" +
                    "mysqlLockTime='" + mysqlLockTime + '\'' +
                    ", mysqlOpenFilesLimit='" + mysqlOpenFilesLimit + '\'' +
                    ", mysqlOpenFiles='" + mysqlOpenFiles + '\'' +
                    ", mysqlLockNum='" + mysqlLockNum + '\'' +
                    ", mysqlConn='" + mysqlConn + '\'' +
                    ", serverId='" + serverId + '\'' +
                    ", port='" + port + '\'' +
                    '}';
        }

        public String getServerId() {
            return serverId;
        }

        public void setServerId(String serverId) {
            this.serverId = serverId;
        }

        public String getMysqlConn() {
            return mysqlConn;
        }

        public void setMysqlConn(String mysqlConn) {
            this.mysqlConn = mysqlConn;
        }

        public String getMysqlLockNum() {
            return mysqlLockNum;
        }

        public void setMysqlLockNum(String mysqlLockNum) {
            this.mysqlLockNum = mysqlLockNum;
        }

        public String getMysqlOpenFiles() {
            return mysqlOpenFiles;
        }

        public void setMysqlOpenFiles(String mysqlOpenFiles) {
            this.mysqlOpenFiles = mysqlOpenFiles;
        }

        public String getMysqlOpenFilesLimit() {
            return mysqlOpenFilesLimit;
        }

        public void setMysqlOpenFilesLimit(String mysqlOpenFilesLimit) {
            this.mysqlOpenFilesLimit = mysqlOpenFilesLimit;
        }

        public String getMysqlLockTime() {
            return mysqlLockTime;
        }

        public void setMysqlLockTime(String mysqlLockTime) {
            this.mysqlLockTime = mysqlLockTime;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }
    }

    class MysqlFeature {
        private Integer fid;
        private String object;
        private String server_id;
        private String value;
        private String client;
        private String port;
        private String time;
        private List<MysqlFeature> feature_list;

        public MysqlFeature() {
        }

        public MysqlFeature(int fid, String object, String server_id,
                            String value, String port) {
            this.fid = fid;
            this.object = object;
            this.server_id = server_id;
            this.value = value;
            this.port = port;
            this.setClient(CommonUtils.getLocalIp());
            this.setTime(DateUtis.currentTime());
        }

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public void setFid(Integer fid) {
            this.fid = fid;
        }

        public String getServer_id() {
            return server_id;
        }

        public void setServer_id(String server_id) {
            this.server_id = server_id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<MysqlFeature> getFeature_list() {
            return feature_list;
        }

        public void setFeature_list(List<MysqlFeature> feature_list) {
            this.feature_list = feature_list;
        }

        public void addFeature(MysqlFeature mysqlFeature) {
            this.getFeature_list().add(mysqlFeature);
        }
    }

}
