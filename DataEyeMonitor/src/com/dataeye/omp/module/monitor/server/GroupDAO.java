package com.dataeye.omp.module.monitor.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dataeye.omp.module.cmdb.device.DicValue;
import com.xunlei.jdbc.RowCallbackHandler;
import org.springframework.stereotype.Service;

import com.dataeye.exception.ClientException;
import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.Constant.Table;
import com.dataeye.omp.constant.StatusCode;
import com.xunlei.jdbc.JdbcTemplate;

@Service
public class GroupDAO {

	@Resource(name = "jdbcTemplateMonitor")
	private JdbcTemplate jdbcTemplateMonitor;
	
	@Resource(name = "jdbcTemplateMonitorStat")
	private JdbcTemplate jdbcTemplateMonitorStat;
	
	/**
	 * 查询分组
	 * @param uid
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月12日 下午2:15:05
	 */
	public List<Group> queryGroup(int uid) throws ServerException {
		List<Group> groups = new ArrayList<Group>();
		String sql = "select group_id groupID,groupName,uid from "+Table.SERVER_GROUP_OWNER+" where uid = ? order by group_id desc";
		try {
			groups = jdbcTemplateMonitor.queryForList(sql, Group.class, uid);
			return groups;
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
		return groups;
	}
	
	/**
	 * 删除分组
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月13日 下午4:49:56
	 */
	public void deleteGroup(int groupID) throws ServerException {
		String sql = "delete from "+Table.SERVER_GROUP_OWNER+" where group_id = ?";
		String sql_ser = "delete from "+Table.SERVER_GROUP+" where group_id = ?";
		try {
			jdbcTemplateMonitor.execute(sql, groupID);
			jdbcTemplateMonitor.execute(sql_ser, groupID);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
	}
	
	/**
	 * 添加分组
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @throws ClientException 
	 * @date 2016年1月13日 下午4:50:00
	 */
	public long addGroup(String groupName, int uid) throws ServerException, ClientException {
		String sql_exist = "select count(*) from "+Table.SERVER_GROUP_OWNER+" where uid=? and groupName=?";
		int count = jdbcTemplateMonitor.queryForInt(sql_exist, uid,groupName);
		if (count > 0) {
			ExceptionHandler.throwParameterException(StatusCode.EXISTS);
		} else {
			String sql = "insert into "+Table.SERVER_GROUP_OWNER+"(uid,groupName) values(?,?)";
			try {
				long groupID = jdbcTemplateMonitor.insert(sql,uid,groupName);
				return groupID;
			} catch (Exception e) {
				e.printStackTrace();
				ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
			}
		}
		return 0;
	}

	/**
	 * 获取全部分组
	 * @return
	 * @throws ServerException
	 * @author wendy <br>
	 * @date 2016年1月14日 下午4:22:00
	 */
	public List<Group> getAllGroupList() throws ServerException {
		List<Group> groupList = new ArrayList<>();
		try {
			//获取所有的分组
			String sql = "select group_id groupId,uid, groupName from "
					+ Table.SERVER_GROUP_OWNER;
			groupList = jdbcTemplateMonitor.queryForList(sql, Group.class);
		}catch (Exception e){
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(
					StatusCode.DB_SQL_ERROR, e);
		}
		return groupList;
	}

	/**
	 * 分组ID，名称
	 * @return
	 * @throws ServerException
	 */
	public Map<Integer,String> getGroupIDNames() throws ServerException {
		final Map<Integer, String> map = new HashMap<>();
		try {
			String sql = "select group_id groupId, groupName from server_group_owner";
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					map.put(rs.getInt("groupId"), rs.getString("groupName"));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(
					StatusCode.DB_SQL_ERROR, "数据库异常");
		}
		return map;
	}

	public List<DicValue> getGroupDicListByUID(int uid) throws ServerException {
		final List<DicValue> values = new ArrayList<>();
		try {
			String sql = "select group_id groupId, groupName from server_group_owner where uid = ?";
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					DicValue kv = new DicValue();
					kv.setValue(rs.getInt("groupId"));
					kv.setLabel(rs.getString("groupName"));
					values.add(kv);
				}
			},uid);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(
					StatusCode.DB_SQL_ERROR, "数据库异常");
		}
		return values;
	}

}
