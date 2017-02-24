package com.dataeye.omp.module.monitor.mysql;

import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.PageData;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.utils.HbasePool;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author wendy
 * @since 2016/3/29 18:05
 */

@Repository
public class MysqlMonitorModuleDao {

    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplate;



    /**
     * 查询mysql实例列表
     * @param orderBy
     * @param order
     * @param pageID
     * @param pageSize
     * @param searchKey
     * @return
     */
    public PageData<MysqlMonitorTO> getMysqlInstanceList(String orderBy, int order, int pageID,
                                         int pageSize, String searchKey) throws ServerException {
        PageData<MysqlMonitorTO> pageData = new PageData<>(pageSize, pageID);

        List<MysqlMonitorTO> list = new ArrayList<>();
        HTableInterface table = null;

        //1, Scan HBase表 omp_mysql_stat中所有记录
        try {
            table = HbasePool.getConnection().getTable(Bytes.toBytes("omp_mysql_stat"));
            Scan scan = new Scan();
            ResultScanner rs = table.getScanner(scan);
            Iterator<Result> it = rs.iterator();
            //查询主机名
            String sql_hostName = "select host_name from server_list where id = ?";
            //查询内网ip
            String sql_ip = "select ip from server_ip_list where svr_id = ? and type = 0";
            //查询业务名
            String sql_business = "select a.name from business a right join (select businessID from dc_process_info " +
                    "where serverID=? and port=?) b on a.id=b.businessID";
            //查询某mysql实例是否设有告警规则
            String sql_alarmRule_exist = "select count(1) from dc_monitor_mysql_rule where serverID=? and port=?";
            while (it.hasNext()) {
                MysqlMonitorTO to = new MysqlMonitorTO();
                Result result = it.next();
                String rowKey = Bytes.toString(result.getRow());
                NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(Bytes.toBytes("stat"));
                String[] rowKeys = rowKey.split("#");
                if (rowKeys.length >= 2) {
                    int serverId = Integer.valueOf(rowKeys[0].trim());
                    int port =  Integer.valueOf(rowKeys[1].trim());
                    String hostName = jdbcTemplate.queryForString(sql_hostName, serverId);
                    String ip = jdbcTemplate.queryForString(sql_ip, serverId);
                    String businessName = jdbcTemplate.queryForString(sql_business, serverId, port);
                    int isRuleExists = jdbcTemplate.queryForInt(sql_alarmRule_exist, serverId, port);

                    //获取文件打开数
                    String openFileStr = Bytes.toString(familyMap.get(Bytes.toBytes("80#open_files")));
                    int openFile = 0;
                    if (openFileStr != null) {
                    	String[] openFileStrArr = openFileStr.split("_");
                    	if (openFileStrArr.length >= 2) {
                    		openFile = Integer.valueOf(openFileStrArr[0].trim());
                    	}
                    }

                    //获取连接数
                    String connectionStr = Bytes.toString(familyMap.get(Bytes.toBytes("82#mysql_conn")));
                    int connection = 0;
                    if (connectionStr != null) {
                    	String[] connectionStrArr = connectionStr.split("_");
                    	if (connectionStrArr.length >= 2) {
                    		connection = Integer.valueOf(connectionStrArr[0].trim());
                    	}
                    }
                    
                    //获取慢sql数
                    String slowSqlStr = Bytes.toString(familyMap.get(Bytes.toBytes("85#slow_sql")));
                    int slowSql = 0;
                    if (slowSqlStr != null) {
                    	String[] slowSqlStrArr = slowSqlStr.split("_");
                    	if (slowSqlStrArr.length >= 2) {
                    		slowSql = Integer.valueOf(slowSqlStrArr[0].trim());
                    	}
                    }
                    //获取锁数量
                    String lockCountStr = Bytes.toString(familyMap.get(Bytes.toBytes("83#lock_num")));
                    int lock = 0;
                    if (lockCountStr != null) {
                    	String[] lockCountStrArr = lockCountStr.split("_");
                    	if (lockCountStrArr.length >= 2) {
                    		lock = Integer.valueOf(lockCountStrArr[0].trim());
                    	}
                    }
                    //获取锁时长
                    String lockTimeStr = Bytes.toString(familyMap.get(Bytes.toBytes("84#lock_top")));
                    int lockTime = 0;
                    if (lockTimeStr != null) {
                    	String[] lockTimeStrArr = lockTimeStr.split("_");
                    	if (lockTimeStrArr.length >= 2) {
                    		lockTime = Integer.valueOf(lockTimeStrArr[0].trim());
                    	}
                    }
                    to.setHostName(hostName);
                    to.setServerID(serverId);
                    to.setBusiName(businessName);
                    to.setConnection(connection);
                    to.setIsRuleExists(isRuleExists > 0 ? 1 : 0);
                    to.setLock(lock);
                    to.setLockTime(lockTime);
                    to.setOpenFile(openFile);
                    to.setPort(port);
                    to.setPrivateIp(ip);
                    to.setSlowSql(slowSql);
                    list.add(to);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        } finally {
            if (table != null) {
                HbasePool.close(table);
            }
        }

        List<MysqlMonitorTO> retList = convertDataByFuzzySearchAndOrder(orderBy, order, searchKey, list);
        List<MysqlMonitorTO> newList = new ArrayList<>();

        int startIndex = (pageID - 1) * pageSize;
        int endIndex;
        if ((pageID * pageSize) > retList.size()) {
            endIndex = retList.size();
        } else {
            endIndex = pageID * pageSize;
        }
        for (int i = startIndex;i < endIndex; i++) {
            newList.add(retList.get(i));
        }

        pageData.setTotalRecord(retList.size());
        pageData.setTotalPage();
        pageData.setContent(newList);
        return pageData;
    }

    private List<MysqlMonitorTO> convertDataByFuzzySearchAndOrder(String orderBy, int order, String searchKey, List<MysqlMonitorTO> srcList) {
        List<MysqlMonitorTO> list = new ArrayList<>();

        searchKey = searchKey == null ? "" : searchKey;

        for (MysqlMonitorTO to : srcList) {
            if ((to.getHostName() != null && to.getHostName().contains(searchKey)) || 
            		(to.getBusiName() != null && to.getBusiName().contains(searchKey)) ||
                    (String.valueOf(to.getPort())).contains(searchKey) ||
                    (to.getPrivateIp() != null && to.getPrivateIp().contains(searchKey)) ||
                    (String.valueOf(to.getServerID())).contains(searchKey)) {
                list.add(to);
            }
        }

        if (orderBy != null && !"".equals(orderBy.trim())) {
            if ("hostName".equalsIgnoreCase(orderBy)) {
                if (order == 1) {
                    Collections.sort(list, MysqlMonitorComparators.HOSTNAME_ASC);
                }
                if (order == 0) {
                    Collections.sort(list, MysqlMonitorComparators.HOSTNAME_DESC);
                }
            }
            if ("port".equalsIgnoreCase(orderBy)) {
                if (order == 1) {
                    Collections.sort(list, MysqlMonitorComparators.PORT_ASC);
                }
                if (order == 0) {
                    Collections.sort(list, MysqlMonitorComparators.PORT_DESC);
                }
            }
            if ("openFile".equalsIgnoreCase(orderBy)) {
                if (order == 1) {
                    Collections.sort(list, MysqlMonitorComparators.OPEN_FILE_ASC);
                }
                if (order == 0) {
                    Collections.sort(list, MysqlMonitorComparators.OPEN_FILE_DESC);
                }
            }
            if ("connection".equalsIgnoreCase(orderBy)) {
                if (order == 1) {
                    Collections.sort(list, MysqlMonitorComparators.CONNECTION_ASC);
                }
                if (order == 0) {
                    Collections.sort(list, MysqlMonitorComparators.CONNECTION_DESC);
                }
            }
            if ("lock".equalsIgnoreCase(orderBy)) {
                if (order == 1) {
                    Collections.sort(list, MysqlMonitorComparators.LOCK_ASC);
                }
                if (order == 0) {
                    Collections.sort(list, MysqlMonitorComparators.LOCK_DESC);
                }
            }
            if ("slowSql".equalsIgnoreCase(orderBy)) {
                if (order == 1) {
                    Collections.sort(list, MysqlMonitorComparators.SLOW_SQL_ASC);
                }
                if (order == 0) {
                    Collections.sort(list, MysqlMonitorComparators.SLOW_SQL_DESC);
                }
            }
        }


        return list;
    }





    /**
     * 添加mysql告警规则
     * @param to mysql监控对象
     * @return
     */
    public long addMysqlRule(MysqlMonitorTO to) throws ServerException {
        String sql = "insert into dc_monitor_mysql_rule (serverID,port,openFile,connection,lockNum,lockTime,slowSql," +
                "maxFrequency,mainPrincipal,bakPrincipal,alarmType,alarmInterval,createTime) values " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        long ret = 0;
        try {

            ret = jdbcTemplate.insert(sql, to.getServerID(), to.getPort(), to.getOpenFile(), to.getConnection(), to.getLock(),
                    to.getLockTime(), to.getSlowSql(), to.getMaxFrequency(), to.getMainPrincipal(), to.getBakPrincipal(),
                    to.getAlarmType(), to.getAlarmInterval(), System.currentTimeMillis() / 1000);
        }catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return ret;
    }

    /**
     * 根据serverId and port 获取mysql监控对象
     * @param
     * @return
     */
    public MysqlMonitorTO loadByInstanceId(final int serverId, final int port) throws ServerException {
        final MysqlMonitorTO to = new MysqlMonitorTO();
        String sql = "select id,openFile,connection,lockNum,lockTime,slowSql,maxFrequency," +
                "mainPrincipal,bakPrincipal,alarmType,alarmInterval from dc_monitor_mysql_rule " +
                "where serverID = ? and port = ?";
        try {
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    to.setId(rs.getInt(1));
                    to.setOpenFile(rs.getInt(2));
                    to.setConnection(rs.getInt(3));
                    to.setLock(rs.getInt(4));
                    to.setLockTime(rs.getInt(5));
                    to.setSlowSql(rs.getInt(6));
                    to.setMaxFrequency(rs.getInt(7));
                    to.setMainPrincipal(rs.getInt(8));

                    String bakPrincipal = rs.getString(9);
                    to.setBakPrincipal(bakPrincipal);
                    to.setAlarmType(rs.getString(10));
                    to.setAlarmInterval(rs.getInt(11));
                    to.setServerID(serverId);
                    to.setPort(port);
                }
            }, serverId, port);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return to;
    }

    /**
     * 修改MySQL告警规则
     * @param to
     * @return
     */
    public int updateMysqlRule(MysqlMonitorTO to) throws ServerException {
        String sql = "update dc_monitor_mysql_rule set openFile = ?,connection = ?,lockNum = ?,lockTime = ?," +
                "slowSql = ?,maxFrequency = ?,mainPrincipal = ?,bakPrincipal = ?,alarmType = ?," +
                "alarmInterval = ?,updateTime = ? where serverID = ? and port = ?";
        int ret = 0;
        try {

            ret = jdbcTemplate.update(sql, to.getOpenFile(), to.getConnection(), to.getLock(), to.getLockTime(),
                    to.getSlowSql(), to.getMaxFrequency(), to.getMainPrincipal(), to.getBakPrincipal(),
                    to.getAlarmType(), to.getAlarmInterval(), System.currentTimeMillis() / 1000, to.getServerID(), to.getPort());
        }catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return ret;
    }

    public static void main(String[] args) {

        /*List<MysqlMonitorTO> list =
                new MysqlMonitorModuleDao().getMysqlInstanceList("lock", 0, 1, 5, "").getContent();

        for (MysqlMonitorTO to : list) {
            System.out.println();
        }*/

    }

}
