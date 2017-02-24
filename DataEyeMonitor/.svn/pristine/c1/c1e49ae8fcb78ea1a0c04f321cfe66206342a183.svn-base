package com.dataeye.omp.module.cmdb.employee;


import com.dataeye.omp.common.PageData;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.module.cmdb.device.DicValue;
import com.dataeye.utils.ValidateUtils;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 员工管理接口
 * @auther wendy
 * @since 2015/12/27 13:49
 */
@Service
public class EmployeeDao {

    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplate;

    /**
     * 分页获取员工信息
     * @param searchKey
     * @param pageId
     * @param pageSize
     * @return
     */
    public PageData getAllEmployeeList(String searchKey,
             int pageId, int pageSize)
            throws ServerException {
        PageData pageData = new PageData(pageSize, pageId);
        try {

            String sql ;
            int count ;

            if (ValidateUtils.isEmpty(searchKey)) {
                sql = "select count(1) from employee";
                count = jdbcTemplate.queryForInt(sql);

            } else {
                searchKey = "%" + searchKey + "%";
                sql = "select count(1) from employee where name like ?";
                count = jdbcTemplate.queryForInt(sql, searchKey);
            }

            pageData.setTotalRecord(count);

            int startIndex=(pageId-1)*pageSize;

            sql = "select e.id, e.name as ename, e.email, e.phone, e.team_id,"
              + " t.name as tname, t.dept_id, d.name as dname from employee e"
              + " LEFT JOIN teams t on e.team_id=t.id LEFT JOIN department d"
              + " on t.dept_id=d.id";

            Object[] objs;
            if (ValidateUtils.isEmpty(searchKey)) {

                sql += " limit ?, ?";
                objs = new Object[]{startIndex, pageSize};

            } else {

                sql += " where e.name like ? limit ?, ?";
                objs = new Object[]{searchKey, startIndex, pageSize};
            }

            List<Employee> employees=queryEmployee(sql,objs);
            pageData.setContent(employees);

        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return pageData;
    }

    /**
     * 根据部门获取员工信息列表
     * @param deptId
     * @param searchKey
     * @param pageId
     * @param pageSize
     * @return
     * @throws ServerException
     */
    public PageData getEmployeeListByDepId(int deptId,
         String searchKey, int pageId, int pageSize)
            throws ServerException {

        PageData pageData = new PageData(pageSize, pageId);
        try {

        String sql = "select count(1) from employee where name = ? "
           + "and team_id = (select id from teams where dept_id = ?)";


        int count = jdbcTemplate.queryForInt(sql, searchKey,deptId);

        pageData.setTotalRecord(count);

        int startIndex=(pageId-1)*pageSize;

        sql = "select e.id, e.name as ename, e.email, e.phone, e.team_id,"
                + " t.name as tname, t.dept_id, d.name as dname from employee e"
                + " LEFT JOIN teams t on e.team_id=t.id LEFT JOIN department d"
                + " on t.dept_id=d.id (where e.name = ? and e.team_id = "
                + "(select id from teams where dept_id = ?)) limit ?, ?";

        Object[] objs = {searchKey,deptId, startIndex, pageSize};

        List<Employee> employees=queryEmployee(sql,objs);
        pageData.setContent(employees);

        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return pageData;
    }

    /**
     * 根据小组获取员工信息列表
     * @param teamId
     * @param searchKey
     * @param pageId
     * @param pageSize
     * @return
     * @throws ServerException
     */
    public PageData getEmployeeListByTeamId(int teamId,
          String searchKey, int pageId, int pageSize)
            throws ServerException {

        PageData pageData = new PageData(pageSize, pageId);
        try {

            String sql = "select count(1) from employee where name = ? "
                    + "and team_id = ?";
            int count = jdbcTemplate.queryForInt(sql, searchKey,teamId);
            pageData.setTotalRecord(count);
            sql = "select e.id, e.name as ename, e.email, e.phone, e.team_id,"
                    + " t.name as tname, t.dept_id, d.name as dname from employee e"
                    + " LEFT JOIN teams t on e.team_id=t.id LEFT JOIN department d"
                    + " on t.dept_id=d.id (where e.name = ? and e.team_id = ?)"
                    + " limit ?, ?";
            int startIndex = (pageId - 1) * pageSize;

            Object[] objs = {searchKey,teamId, startIndex, pageSize};

            List<Employee> employees=queryEmployee(sql,objs);
            pageData.setContent(employees);

        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return pageData;
    }


    public List<Employee> queryEmployee(String sql,Object[] objs) {
        final List<Employee> employees = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Employee e = new Employee();
                e.setId(rs.getInt("id"));
                e.setName(rs.getString("ename"));
                e.setEmail(rs.getString("email"));
                e.setPhone(rs.getString("phone"));
                e.setTeamId(rs.getInt("team_id"));
                e.setTeamName(rs.getString("tname"));
                e.setDeptId(rs.getInt("dept_id"));
                e.setDeptName(rs.getString("dname"));
                employees.add(e);
            }
        }, objs);
        return employees;
    }

    public List<DicValue> getEmployeeSelectData() {
        String sql = "select id, name from  employee ";
        final List<DicValue> values = new ArrayList<>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                DicValue e = new DicValue();
                e.setId(rs.getInt("id"));
                e.setLabel(rs.getString("name"));
                e.setValue(rs.getInt("id"));
                values.add(e);
            }
        });
        return values;

    }

}
