package com.dataeye.omp.module.cmdb.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.Constant.Table;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.omp.module.cmdb.employee.Employee;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;

@Service
public class UserDAO {

	 @Resource(name = "jdbcTemplateMonitor")
	 private JdbcTemplate jdbcTemplateMonitor;
	 
	 /**
	  * 根据登录邮箱获取用户信息
	  * @param email
	  * @return
	  * @throws ServerException
	  * @author chenfanglin <br>
	  * @date 2016年1月21日 下午6:02:33
	  */
	 public Employee getEmployeeByEmail(final String email) throws ServerException{
		String sql = "select name,nameEN,password,email,id,phone from " + Table.EMPLOYEE + " where email=?";
		try {
			final Employee e = new Employee();
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				
				public void processRow(ResultSet rs) throws SQLException {
					e.setId(rs.getInt("id"));
					e.setName(rs.getString("name"));
					e.setPassword(rs.getString("password"));
					e.setPhone(rs.getString("phone"));
					e.setEmail(email);
					e.setNameEN(rs.getString("nameEN"));
				}
			}, email);
			return e;
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
		return null; 
	 }
}
