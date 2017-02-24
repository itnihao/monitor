package com.dataeye.omp.module.cmdb.business;

import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.PageData;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.omp.module.cmdb.device.DicValue;
import com.dataeye.utils.DateUtils;
import com.dataeye.utils.ValidateUtils;
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
 * 业务/模块接口
 * @auther wendy
 * @since 2015/12/27 15:01
 */
@Service
public class BusinessDao {


    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取所有业务
     * @return
     */
    public List<Business> getBusinessList() throws ServerException {
        final List<Business> busiList = new ArrayList<>();
        try {
            String sql = "select b.id, b.name, pid, om_person, time, e.name as omPersonName from business b "
                    +  "left join employee e on b.om_person = e.id  where pid = 0";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    Business busi = new Business();
                    busi.setId(rs.getInt("id"));
                    busi.setName(rs.getString("name"));
                    busi.setPid(rs.getInt("pid"));
                    busi.setOmPerson(rs.getString("om_person"));
                    busi.setOmPersonName(rs.getString("omPersonName"));
                    busiList.add(busi);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return busiList;
    }

    /**
     * 添加业务
     * @param name
     * @param omPerson
     * @throws ServerException
     */
    public long addBusiness(String name, String omPerson)
            throws ServerException {
        long id = 0;
        try{
            String sql = "insert into business(name, pid, om_person, time) "
                    + "values (?, ?, ?, ?)";

            int pid = 0;
            id= jdbcTemplate.insert(sql, name, pid, omPerson, DateUtils.now());
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return id;
    }


    public boolean checkBusinessExits(String name) throws ServerException {
        try{
            String sql = "select count(1) from business where name = ?";
            int count = jdbcTemplate.queryForInt(sql, name);
            return count>0?true:false;
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return false;
    }

    /**
     * 添加模块
     *
     * @param name
     * @param omPerson
     * @param busiId
     * @throws ServerException
     */
    public long addModule(String name, String omPerson,
                          String bakPrincipal, int busiId)
            throws ServerException {
        long id = 0;
        try {
            String sql = "insert into business(name, pid, om_person, bakPrincipal, time) "
                    + "values (?, ?, ?, ?, ?)";
            id = jdbcTemplate.insert(sql, name, busiId,
                    omPerson, bakPrincipal == null ? "" :bakPrincipal, DateUtils.now());
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return id;
    }

    /**
     * 添加模块
     * @param name
     * @param omPerson
     * @param busiId
     * @throws ServerException
     */
    public long updateModule(String name, String omPerson, String bakPrincipal, int busiId, int moduleId)
            throws ServerException {
        long id = 0;
        try {
            String sql = "update business set name = ?, pid = ?, om_person = ? , bakPrincipal = ?, "
                    + "time = ? where id = ? ";
            id = jdbcTemplate.update(sql, name, busiId, omPerson,
                    bakPrincipal == null ? "" : bakPrincipal, DateUtils.now(), moduleId);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return id;
    }

    /**
     * 同一业务下模块不能相同
     * @param name
     * @return
     * @throws ServerException
     */
    public boolean checkModuleExits(String name, int pid) throws ServerException {
        try{
            String sql = "select count(1) from business where name = ? and pid = ?";
            int count = jdbcTemplate.queryForInt(sql, name, pid);
            return count > 0 ? true : false;
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return false;
    }

    /**
     * 同一业务下模块不能相同
     * @param name
     * @return
     * @throws ServerException
     */
    public boolean checkModuleExits(String name, int pid, int moduleId) throws ServerException {
        try{
            String sql = "select count(1) from business where name = ? "
                    + "and pid = ? and id not in (?)";
            int count = jdbcTemplate.queryForInt(sql, name, pid, moduleId);
            return count > 0 ? true : false;
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return false;
    }


    /**
     * 删除业务和业务下的模块
     * @param id
     * @throws ServerException
     */
    public void deleteBusiness(int id) throws ServerException {
        try {
            String sql = "delete from business where id= ? or pid = ?";
            jdbcTemplate.execute(sql, id, id);
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
    }

    /**
     * 删除业务
     * @param id
     * @throws ServerException
     */
    public void deleteModule(int id) throws ServerException {
        try {
            String sql = "delete from business where id= ?";
            jdbcTemplate.execute(sql, id);
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
    }

    /**
     * 根据业务Id获取模块信息
     * @param busiId
     * @return
     * @throws ServerException
     */
    public List<Business> getModuleListByBusiId(final int busiId)
            throws ServerException {
        final List<Business> moduleList = new ArrayList<>();
        try {
            String sql = "select id, name, pid, om_person, time from "
                    + "business where pid = ?";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    Business business = new Business();
                    business.setName(rs.getString("name"));
                    business.setPid(rs.getInt("pid"));
                    business.setId(rs.getInt("id"));
                    business.setOmPerson(rs.getString("om_person"));
                    business.setTime(rs.getDate("time"));
                    moduleList.add(business);
                }
            }, busiId);

        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return moduleList;
    }

    /**
     * 获取所有模块信息
     * @return
     * @throws ServerException
     */
    public List<Business> getModuleList() throws ServerException {

        final List<Business> moduleList = new ArrayList<>();
        try {
            String sql = "select b.id, b.name bname,e.name ename, pid, om_person from business b "
                    + "left join employee e on b.om_person = e.id where b.id not in "
                    + "(select id from business where pid = 0)";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    Business module = new Business();
                    module.setId(rs.getInt("id"));
                    module.setName(rs.getString("bname"));
                    module.setPid(rs.getInt("pid"));
                    module.setOmPerson(rs.getString("om_person"));
                    module.setOmPersonName(rs.getString("ename"));
                    moduleList.add(module);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return moduleList;
    }


    /**
     *获取业务字典数据
     * @return
     */
    public List<DicValue> getBusiDicList() throws ServerException {
        List<DicValue> valueList = new ArrayList<>();
        List<Business> businessList= getBusinessList();
        for(Business busi:businessList){
            DicValue value = new DicValue();
            value.setId(busi.getId());
            value.setLabel(busi.getName());
            value.setValue(busi.getId());
            valueList.add(value);
        }
        return valueList;
    }

    /**
     *获取模块字典数据
     * @return
     */
    public List<DicValue> getModuleDicList() throws ServerException {
        List<DicValue> valueList = new ArrayList<>();
        List<Business> moduleList= getModuleList();
        for(Business busi:moduleList){
            DicValue value = new DicValue();
            value.setId(busi.getId());
            value.setItemId(busi.getPid());
            value.setLabel(busi.getName());
            value.setValue(busi.getId());
            valueList.add(value);
        }
        return valueList;
    }

    /**
     * 获取业务ID和名称
     * @return
     * @throws ServerException
     */
    public Map<Integer, String> getBusiIDNames() throws ServerException {
        final Map<Integer, String> map = new HashMap<>();
        try {
            String sql = "select id, name from business where pid = 0";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    map.put(rs.getInt("id"), rs.getString("name"));
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return map;
    }

    /**
     * 获取模块对应的机器信息
     * @return
     * @throws ServerException
     */
    public List<DicValue> getMonitorServerList() throws ServerException {
        final List<DicValue> list = new ArrayList<>();
        try {
            String sql = "select module_id, svr_id, sl.host_name from server_business sb"
                    + " left join server_list sl on sb.svr_id = sl.id";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    DicValue dicValue = new DicValue();
                    dicValue.setLabel(rs.getString("host_name"));
                    dicValue.setValue(rs.getInt("svr_id"));
                    dicValue.setItemId(rs.getInt("module_id"));
                    list.add(dicValue);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return list;
    }


}

