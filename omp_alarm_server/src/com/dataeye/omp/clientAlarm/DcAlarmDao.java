package com.dataeye.omp.clientAlarm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.dataeye.omp.clientAlarm.request.GetAlarmRequest;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
/**
 * 
 * <pre>
 * 客户端上报告警数据的数据表dao层
 * @author stan zhang          <br>
 * @date 2015-10-20 上午10:27:56 <br>
 * @version 1.0
 * <br>
 */
/**
 * 
 * <pre>
 * 客户端上报告警数据的数据表dao层
 * @author stan zhang          <br>
 * @date 2015-10-20 上午10:27:56 <br>
 * @version 1.0
 * <br>
 */
@Repository
public class DcAlarmDao {

	@Resource(name = "jdbcTemplateDcBusinessUser")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 插入告警数据到数据库中
	 * @param dcAlarmDomain
	 */
	public long insertDcAlarmData(DcAlarmDomain dcAlarmDomain){
		String sql = "insert into dc_client_alarm (title,type,content,closeUrl,others,c_time,m_time) values (?,?,?,?,?,now(),now())";
		
		return jdbcTemplate.insert(sql, dcAlarmDomain.getTitle(),dcAlarmDomain.getType(),dcAlarmDomain.getContent(),dcAlarmDomain.getCloseUrl(),dcAlarmDomain.getOthers());
	}
	/**
	 * 向告警备份数据表中插入数据
	 * @param dcAlarmDomain
	 */
	public void insertDcAlarmBakData(DcAlarmDomain dcAlarmDomain,long insertId){
		String sql = "insert into dc_client_alarm_bak (normal_alarm_id,title,type,content,closeUrl,others,c_time,m_time) values (?,?,?,?,?,?,now(),now())";
						
		jdbcTemplate.insert(sql, insertId,dcAlarmDomain.getTitle(),dcAlarmDomain.getType(),dcAlarmDomain.getContent(),dcAlarmDomain.getCloseUrl(),dcAlarmDomain.getOthers());
	}
	
	/**
	 * 根据id批量更新备份数据表中的关闭告警的用户closeUser
	 * @param dcAlarmDomain
	 */
	public void batchUpdateDcAlarmBakDataByNormalAlarmId(String closeUser,List<Integer> idList){
		String sql = "update dc_client_alarm_bak set close_user=?,finish_label=? where normal_alarm_id=?";
		if(null!=idList && 0<idList.size()){
			List<Object[]> args = new ArrayList<Object[]>();
			for(Integer id : idList){
				Object[] e = new Object[3];
				e[0] = closeUser;
				e[1] = DcAlarmDomain.SLOVE_FINISH_LABEL;
				e[2] = id;
				args.add(e);
			}
			jdbcTemplate.batchUpdate(sql, args);
		}
	}
	/**
	 * 根据告警类型和告警接收人查询告警数据
	 * @param dcAlarmDomain
	 */
	public List<DcAlarmDomain> queryDcAlarmDataByTypeAndOthers(DcAlarmDomain dcAlarmDomain){
		final List<DcAlarmDomain> resultList = new ArrayList<DcAlarmDomain>();
		String sql = "";
		if(dcAlarmDomain.getType().trim().equals(GetAlarmRequest.QUERY_ALL_TYPE)){
			sql = "select id,title,type,content,closeUrl,others from dc_client_alarm where others like ? ";
	
		}else{
			StringBuilder types = new StringBuilder();
			for(String type : dcAlarmDomain.getType().trim().split(",")){
				types.append("'").append(type).append("'").append(",");			
			}
			types.replace(types.lastIndexOf(","), types.length(), "");
			sql = "select id,title,type,content,closeUrl,others from dc_client_alarm where type in ("+ types.toString() + ") and others like ? ";		
		}
		jdbcTemplate.query(sql, new RowCallbackHandler() {			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				DcAlarmDomain domain = new DcAlarmDomain();
				domain.setId(rs.getInt("ID"));
				domain.setTitle(rs.getString("TITLE"));
				domain.setType(rs.getString("TYPE"));
				domain.setContent(rs.getString("CONTENT"));
				domain.setCloseUrl(rs.getString("CLOSEURL"));
				domain.setOthers(rs.getString("OTHERS"));	
				resultList.add(domain);
				
			}
		},"%"+dcAlarmDomain.getOthers().trim()+"%");
			
		
		
		return resultList;
	}	
	/**
	 * 根据告警id批量删除指定的告警
	 * @param id
	 */
	public void batchDeleteDcAlarmById(List<Integer> idList){
		String sql = "delete from dc_client_alarm where id=?";		
		if(null!=idList && 0<idList.size()){
			List<Object[]> args = new ArrayList<Object[]>();
			for(Integer id : idList){
				Object[] e = new Object[1];
				e[0] = id;
				args.add(e);
			}
			jdbcTemplate.batchUpdate(sql, args);
		}	
	}
}
