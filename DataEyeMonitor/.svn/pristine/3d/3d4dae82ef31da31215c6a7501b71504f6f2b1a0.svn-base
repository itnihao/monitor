package com.dataeye.omp.module.cmdb.employee;


import com.dataeye.omp.constant.StatusCode;
import com.dataeye.omp.module.cmdb.device.DicValue;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther wendy
 * @since 2015/12/27 18:16
 */
@Service
public class DepartmentDao {

    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取所有部门信息
     * @return
     * @throws ServerException
     */
    public List<Object> getAllDepartment()
            throws ServerException {
        final List<Object> deptList = new ArrayList<>();
        try {
            String sql = "select id ,name from department";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    Department dept = new Department();
                    dept.setId(rs.getInt("id"));
                    dept.setName(rs.getString("name"));
                    deptList.add(dept);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return deptList;
    }

    public List<DicValue> getDeptDicList()
            throws ServerException {
        final List<DicValue> valueList = new ArrayList<>();
        try {
            String sql = "select id ,name from department";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    DicValue value = new DicValue();
                    value.setId(rs.getInt("id"));
                    value.setLabel(rs.getString("name"));
                    value.setValue(rs.getInt("id"));
                    valueList.add(value);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return valueList;
    }

    /**
     * 根据部门获取小组信息
     * @param deptId
     * @return
     * @throws ServerException
     */
    public List<Team> getTeamsByDeptId(int deptId)
            throws ServerException {
        final List<Team> teams = new ArrayList<>();
        try {
            String sql = "select id ,name, dept_id from teams "
                    + "where dept_id= ?";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setName(rs.getString("name"));
                    team.setDeptId(rs.getInt("dept_id"));
                    teams.add(team);
                }
            },deptId);
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return teams;
    }


}
