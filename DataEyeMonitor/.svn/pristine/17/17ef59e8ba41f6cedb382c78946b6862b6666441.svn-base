package com.dataeye.omp.module.alarm.customize;

import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.PageData;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.utils.HbaseProxyClient;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.jruby.RubyProcess;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by rodbate on 2016/3/4.
 */

@Repository
public class CustomizeAlarmDAO {

    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加自定义告警
     *
     * @param customizeAlarm 自定义告警对象
     * @return ID
     * @throws ServerException server异常
     */
    public Long add(CustomizeAlarm customizeAlarm, CustomizeAlarmRule rule) throws ServerException {
        long returnId = 0;
        try {
            String sql = "insert into dc_alarm_customize_info (businessID,alarmItem,mainEmployee,others,createTime,status,remark) values (?, ?, ?, ?, ?, ?, ?)";
            long alarmId = jdbcTemplate.insert(sql, customizeAlarm.getBusinessID(), customizeAlarm.getAlarmItem(), customizeAlarm.getMainEmployee(),
                    customizeAlarm.getOthers(), System.currentTimeMillis() / 1000, customizeAlarm.getStatus(), customizeAlarm.getRemark());

            String sqlRule = "insert into dc_alarm_rule_customize (customizeID,alarmInterval,maxFrequency,restoreType,restoreInterval,alarmType,createTime) values (?, ?, ?, ?, ?, ?, ?)";
            returnId = jdbcTemplate.insert(sqlRule, alarmId, rule.getAlarmInterval(), rule.getMaxFrequency(),
                    rule.getRestoreType(), rule.getRestoreInterval(), rule.getAlarmType(), System.currentTimeMillis() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return returnId;
    }

    /**
     * 条件分页查询自定义告警
     *
     * @param pageId    当前页
     * @param pageSize  页面大小
     * @param searchKey 搜索关键字
     * @param orderBy   排序关键字
     * @param order     排序方式
     * @return 每页数据
     * @throws ServerException ServerException
     */
    public PageData<CustomizeAlarm> queryByQualifiers(int pageId, int pageSize, String searchKey,
                                                      String orderBy, int order) throws ServerException {
        Map<String, Object> map = new HashMap<>();
        PageData<CustomizeAlarm> pageData = new PageData<>(pageSize, pageId);
        pageData.setOrder(order);
        pageData.setOrderBy(orderBy);
        String orderStr = order == 0 ? "ASC" : "DESC";
        String sql = "";
        String sqlCount = "";
        final String sqlCountMonth = "select count(1) from dc_alarm_customize_every_day where alarmItem = ? and alarmTime between " + (System.currentTimeMillis() / 1000 - 30 * 3600 * 24) + " and " + System.currentTimeMillis() / 1000;
        ;
        long alarmTimes = 0;
        final String sqlBusiness = "select name from business where id = ?";
        final String sqlEmployee = "select name from employee where id = ?";
        try {
            final List<CustomizeAlarm> list = new ArrayList<>();
            int count = 0;
            if (searchKey == null) {
                sql = "select id,businessId,alarmItem,mainEmployee,others,createTime,updateTime,status,remark from " +
                        "dc_alarm_customize_info order by ? " + orderStr + " limit ?,?";
                sqlCount = "select count(1) from dc_alarm_customize_info";
                count = jdbcTemplate.queryForInt(sqlCount);
                jdbcTemplate.query(sql, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        CustomizeAlarm customizeAlarm = new CustomizeAlarm();
                        String alarmItem = rs.getString("alarmItem");
                        customizeAlarm.setId(rs.getLong("id"));
                        customizeAlarm.setBusinessID(rs.getLong("businessID"));
                        customizeAlarm.setBusinessName(jdbcTemplate.queryForString(sqlBusiness, rs.getLong("businessID")));
                        customizeAlarm.setAlarmItem(rs.getString("alarmItem"));
                        customizeAlarm.setAlarmObject(getAlarmObjFromHbase(alarmItem));
                        customizeAlarm.setMainEmployee(rs.getInt("mainEmployee"));
                        customizeAlarm.setMainEmployeeName(jdbcTemplate.queryForString(sqlEmployee, rs.getInt("mainEmployee")));
                        customizeAlarm.setOthers(rs.getString("others"));
                        customizeAlarm.setOthersName(getOthersName(rs.getString("others")));
                        customizeAlarm.setUpdateTime(rs.getLong("updateTime"));
                        customizeAlarm.setCreateTime(rs.getLong("createTime"));
                        customizeAlarm.setStatus(rs.getInt("status"));
                        customizeAlarm.setRemark(rs.getString("remark"));
                        customizeAlarm.setAlarmTimesLatestMonth(jdbcTemplate.queryForLong(sqlCountMonth, alarmItem));
                        list.add(customizeAlarm);
                    }
                }, orderBy, (pageId - 1) * pageSize, pageSize);
            } else {
                searchKey = "%" + searchKey + "%";
                sql = "select id,businessId,alarmItem,mainEmployee,others,createTime,updateTime,status,remark from " +
                        "dc_alarm_customize_info where alarmItem like ? order by ? " + orderStr + " limit ?,?";
                sqlCount = "select count(1) from dc_alarm_customize_info where alarmItem like ?";
                count = jdbcTemplate.queryForInt(sqlCount, searchKey);

                jdbcTemplate.query(sql, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        CustomizeAlarm customizeAlarm = new CustomizeAlarm();
                        String alarmItem = rs.getString("alarmItem");
                        customizeAlarm.setId(rs.getLong("id"));
                        customizeAlarm.setBusinessID(rs.getLong("businessID"));
                        customizeAlarm.setBusinessName(jdbcTemplate.queryForString(sqlBusiness, rs.getLong("businessID")));
                        customizeAlarm.setAlarmItem(rs.getString("alarmItem"));
                        customizeAlarm.setAlarmObject(getAlarmObjFromHbase(alarmItem));
                        customizeAlarm.setMainEmployee(rs.getInt("mainEmployee"));
                        customizeAlarm.setMainEmployeeName(jdbcTemplate.queryForString(sqlEmployee, rs.getInt("mainEmployee")));
                        customizeAlarm.setOthers(rs.getString("others"));
                        customizeAlarm.setOthersName(getOthersName(rs.getString("others")));
                        customizeAlarm.setUpdateTime(rs.getLong("updateTime"));
                        customizeAlarm.setCreateTime(rs.getLong("createTime"));
                        customizeAlarm.setStatus(rs.getInt("status"));
                        customizeAlarm.setRemark(rs.getString("remark"));
                        customizeAlarm.setAlarmTimesLatestMonth(jdbcTemplate.queryForLong(sqlCountMonth, alarmItem));
                        list.add(customizeAlarm);
                    }


                }, searchKey, orderBy, (pageId - 1) * pageSize, pageSize);
            }

            pageData.setTotalRecord(count);
            pageData.setTotalPage();

            pageData.setContent(list);


        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return pageData;
    }

    private String getAlarmObjFromHbase(String alarmItem) {
        StringBuilder sb = new StringBuilder();
        try {
            Result rs = HbaseProxyClient.getOneRecord("omp_customize_alarm_object", alarmItem);

            if (rs != null && !rs.isEmpty()) {
                KeyValue[] kv = rs.raw();
                for (int i = 0; i < kv.length; i++) {
                    if (i == kv.length - 1) {
                        sb.append(Bytes.toString(kv[i].getValue()));
                    } else {
                        sb.append(Bytes.toString(kv[i].getValue()) + ", ");
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private List<Map<String, String>> getAlarmObjMapFromHbase(String alarmItem) {

    	List<Map<String, String>> list = new ArrayList<>();

        try {
            Result rs = HbaseProxyClient.getOneRecord("omp_customize_alarm_object", alarmItem);

            if (rs != null && !rs.isEmpty()) {
                KeyValue[] kv = rs.raw();
                for (int i = 0; i < kv.length; i++) {
                	Map<String, String> map = new HashMap<>();
                    map.put("key", Bytes.toString(kv[i].getQualifier()));
                    map.put("value", Bytes.toString(kv[i].getValue()));
                    list.add(map);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 更新自定义告警
     *
     * @param alarm 需要更新的自定义告警对象
     * @return 更新记录条数
     * @throws ServerException server异常
     */
    public int update(CustomizeAlarm alarm, CustomizeAlarmRule rule) throws ServerException {
        String sql = "update dc_alarm_customize_info set businessID = ?, mainEmployee = ?, others = ?, " +
                "updateTime = ?, remark = ? where id = ?";
        String sqlRule = "update dc_alarm_rule_customize set alarmInterval = ?, maxFrequency = ?, restoreType = ?, " +
                "restoreInterval = ?, alarmType = ?, updateTime = ? where customizeID = ?";
        int retVal = 0;
        try {
            jdbcTemplate.update(sql, alarm.getBusinessID(), alarm.getMainEmployee(), alarm.getOthers(),
                    System.currentTimeMillis() / 1000, alarm.getRemark(), alarm.getId());

            retVal = jdbcTemplate.update(sqlRule, rule.getAlarmInterval(), rule.getMaxFrequency(), rule.getRestoreType(),
                    rule.getRestoreInterval(), rule.getAlarmType(), System.currentTimeMillis() / 1000, alarm.getId());
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return retVal;
    }


    /**
     * 自定义告警的启停切换
     *
     * @param id     自定义告警id
     * @param status 自定义告警启停状态
     * @return 更新记录条数
     * @throws ServerException server异常
     */
    public int switchByStatus(long id, int status) throws ServerException {
        String sql = "update dc_alarm_customize_info set updateTime = ?, status = ? where id = ?";
        int retVal = 0;
        try {
            retVal = jdbcTemplate.update(sql, System.currentTimeMillis() / 1000, status, id);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return retVal;
    }

    /**
     * 根据id查询
     *
     * @param customizeAlarmId
     * @return
     */
    public Object getById(long customizeAlarmId) throws ServerException {
        String sql = "select info.id,info.businessId,info.alarmItem,info.mainEmployee,info.others,info.createTime,info.updateTime," +
                "info.status,info.remark,rule.id,rule.alarmInterval,rule.maxFrequency,rule.restoreType,rule.restoreInterval," +
                "rule.alarmType,rule.createTime,rule.updateTime from dc_alarm_customize_info info left join dc_alarm_rule_customize rule on info.id = rule.customizeId where info.id = ?";
        final String sqlCountMonth = "select count(1) from dc_alarm_customize_every_day where alarmItem = ? and alarmTime between " + (System.currentTimeMillis() / 1000 - 30 * 3600 * 24) + " and " + System.currentTimeMillis() / 1000;
        
        long alarmTimes = 0;
        final String sqlBusiness = "select name from business where id = ?";
        final String sqlEmployee = "select name from employee where id = ?";
        final CustomizeAlarm alarm = new CustomizeAlarm();
        try {
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    alarm.setId(rs.getLong(1));
                    alarm.setBusinessID(rs.getLong(2));
                    alarm.setBusinessName(jdbcTemplate.queryForString(sqlBusiness, rs.getLong(2)));
                    alarm.setAlarmItem(rs.getString(3));
                    alarm.setAlarmObjectMap(getAlarmObjMapFromHbase(rs.getString(3)));
                    alarm.setMainEmployee(rs.getInt(4));
                    alarm.setMainEmployeeName(jdbcTemplate.queryForString(sqlEmployee, rs.getInt(4)));
                    alarm.setOthers(rs.getString(5));
                    alarm.setOthersName(getOthersName(rs.getString(5)));
                    alarm.setCreateTime(rs.getLong(6));
                    alarm.setUpdateTime(rs.getLong(7));
                    alarm.setStatus(rs.getInt(8));
                    alarm.setRemark(rs.getString(9));
                    alarm.setAlarmTimesLatestMonth(jdbcTemplate.queryForLong(sqlCountMonth, rs.getString(3)));
                    CustomizeAlarmRule rule = new CustomizeAlarmRule();
                    rule.setId(rs.getLong(10));
                    rule.setAlarmInterval(rs.getInt(11));
                    rule.setMaxFrequency(rs.getInt(12));
                    rule.setRestoreType(rs.getInt(13));
                    rule.setRestoreInterval(rs.getInt(14));
                    rule.setAlarmType(rs.getString(15));
                    rule.setCreateTime(rs.getLong(16));
                    rule.setUpdateTime(rs.getLong(17));
                    alarm.setRule(rule);
                }
            }, customizeAlarmId);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }

        return alarm;
    }
    
    private List<Map<String, String>> getOthersName(String others){
    	String sql = "select name from employee where id = ?";
    	List<Map<String, String>> list = new ArrayList<>();
    	if(others.trim() != null & others.trim().length() > 0) {
    		String[] strIds = others.split(",");
    		for (int i = 0; i < strIds.length; i++) {
    			String idS = strIds[i].trim();
    			int id = Integer.valueOf(idS);
    			String name = jdbcTemplate.queryForString(sql, id);
    			Map<String, String> map =new HashMap<>();
    			map.put("id", idS);
    			map.put("name", name);
    			list.add(map);
			}
    	}
    	return list;
    }
    
    public int getRecordByAlarmItem(String alarmItem){
    	String sql = "select count(1) from dc_alarm_customize_info where alarmItem = ?";
    	return jdbcTemplate.queryForInt(sql, alarmItem);
    }

}
