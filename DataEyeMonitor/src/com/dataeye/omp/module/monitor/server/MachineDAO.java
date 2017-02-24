package com.dataeye.omp.module.monitor.server;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.annotation.Resource;

import clojure.lang.Cons;
import com.dataeye.common.CachedObjects;
import com.dataeye.omp.module.cmdb.business.BusinessDao;
import com.dataeye.omp.module.cmdb.device.DeviceIP;
import com.google.gson.reflect.TypeToken;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement;
import org.hsqldb.persist.CachedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.KeyValue;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.constant.Constant.ServerCfg;
import com.dataeye.omp.constant.Constant.Table;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.omp.module.cmdb.device.DicValue;
import com.dataeye.utils.DateUtils;
import com.dataeye.utils.HbaseProxyClient;
import com.dataeye.utils.ServerUtils;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;

/**
 * 服务器监控 DAO
 * 
 * @author chenfanglin
 * @date 2016年1月6日 上午10:39:11
 */
@Service
public class MachineDAO {

	@Resource(name = "jdbcTemplateMonitor")
	private JdbcTemplate jdbcTemplateMonitor;
	
	@Resource(name = "jdbcTemplateMonitorStat")
	private JdbcTemplate jdbcTemplateMonitorStat;

	@Autowired
	private BusinessDao businessDao;

	/**
	 * 查询 机器列表
	 * @param idcId
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月7日 下午5:30:55
	 */
	public List<Machine> queryRoomServerList(int idcId) throws ServerException {
		final List<Machine> machineList = new ArrayList<Machine>();
		String sql = "select a.id serverID,a.host_name hostName,a.disk_num,b.name machineRoom,c.name business from " + Table.SERVER_LIST + " a left join "
				+Table.IDC_LIST+" b on a.idc_id = b.id left join (select distinct a.name,b.svr_id from  "+Table.BUSINESS+" a,"+Table.SERVER_BUSINESS+" b"
				+ " where a.id = b.busi_id) c on a.id = c.svr_id";
		if (idcId != ServerCfg.ALL) {
			sql = sql + " where b.id = "+idcId;
		} 
		sql = sql + " order by a.id desc";
		try {
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					Machine machine = new Machine();
					Integer serverID = rs.getInt("serverID");
					String hostName = rs.getString("hostName");
					String machineRoom = rs.getString("machineRoom");
					String business = rs.getString("business");
					machine.setServerID(serverID);
					machine.setDiskNum(rs.getInt("disk_num"));
					machine.setBusiness(business);
					machine.setHostName(hostName);
					machine.setMachineRoom(machineRoom);
					machine.setIp(getMachineIP(serverID));
					try {
						machine.setCpuUsage(getCurrentCpuUsage(serverID));
						machine.setStatus(getCurrentStatus(serverID));
						machine.setFiveLoad(getCurrentFiveMinLoad(serverID));
					} catch (ServerException e) {
						e.printStackTrace();
					}
					machineList.add(machine);
				}
			});
			return machineList;
		} catch (Exception e) {
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
		return null;
	}

	/**
	 * 格式化机器IP
	 * @param serverID
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException
	 * @date 2016年1月6日 下午5:37:09
	 */
	public List<KeyValue<String,Integer>> getMachineIP(Integer serverID) {
		final List<KeyValue<String,Integer>> list = new ArrayList<KeyValue<String,Integer>>();
		String sql = "select ip,type from "+Table.SERVER_IP_LIST+" where svr_id = ?";
		jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				KeyValue<String,Integer> kv = new KeyValue<String, Integer>();
				kv.setKey(rs.getString("ip"));
				kv.setValue(rs.getInt("type"));
				list.add(kv);
			}
		}, serverID);
		return list;
	}
	
	/**
	 * 查询机器当前的CPU使用率
	 * @param serverID
	 * @return
	 * @author chenfanglin <br>
	 * @throws ServerException 
	 * @date 2016年1月7日 下午5:54:38
	 */
	private String getCurrentCpuUsage(Integer serverID) throws ServerException{
		String dbKey = serverID.toString();
		String column = Constant.CPUUSAGE + ServerCfg.ROWKEY_SPLITER + Constant.CPU;
		try {
			Result hbaseResult = HbaseProxyClient.getOneRecord(Table.OMP_SERVER_FEATURE_STAT, dbKey);
			if (hbaseResult != null && !hbaseResult.isEmpty()) { 
				byte[] cpu = hbaseResult.getValue(Bytes.toBytes("stat"), Bytes.toBytes(column));
				if (cpu != null) {
					double value = Double.parseDouble(Bytes.toString(cpu).split("_")[0])/100;
					return ServerUtils.precent(value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.HBASE_ERROR, e);
		}
		return null;
	}
	
	/**
	 * 查询机器当前的CPU5分钟负载
	 * @param serverID
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月8日 下午3:03:59
	 */
	private double getCurrentFiveMinLoad(Integer serverID) throws ServerException{
		String dbKey = serverID.toString();
		String column = Constant.FIVEMIN + ServerCfg.ROWKEY_SPLITER + Constant.CPU;
		try {
			Result hbaseResult = HbaseProxyClient.getOneRecord(Table.OMP_SERVER_FEATURE_STAT, dbKey);
			if (hbaseResult != null && !hbaseResult.isEmpty()) { 
				byte[] cpu = hbaseResult.getValue(Bytes.toBytes("stat"), Bytes.toBytes(column));
				if (cpu != null) {
					double value = Double.parseDouble(Bytes.toString(cpu).split("_")[0])/100;
					return value;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.HBASE_ERROR, e);
		}
		return 0;
	}
	
	/**
	 * 查询机器当前的健康状态
	 * 当上次心跳时间<=5分钟，正常；>5分钟，不正常
	 * @param serverID
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月7日 下午6:47:08
	 */
	private int getCurrentStatus(Integer serverID) throws ServerException{
		String sql = "select UNIX_TIMESTAMP(hb_time) hb_time from "+Table.SERVER_LIST+" where id = ?";
		// 上次心跳时间
		try {
			long lastHeartbeatTime = jdbcTemplateMonitorStat.queryForLong(sql, serverID);
			if (lastHeartbeatTime > -1) {
				int currentTime = DateUtils.unixTimestamp();
				if (currentTime - lastHeartbeatTime > Constant.SECONDS_TEN_MIN) {
					// 不正常
					return 1;
				} else {
					// 正常
					return 0;
				}
			} else {
				// 不正常
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
		return 0;
	}
	
	/**
	 * 业务视图
	 * @param busineID
	 * @param moduleId
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月13日 下午3:57:34
	 */
	public List<Machine> queryBusinessServerList(final int busineID,int moduleId) throws ServerException{
		final List<Machine> machineList = new ArrayList<Machine>();
//		String sql = "select a.id serverID,a.host_name hostName,b.name machineRoom,c.name business from " + Table.SERVER_LIST + " a left join "
//				+ Table.IDC_LIST+" b on a.idc_id = b.id left join (SELECT DISTINCT a.name,b.svr_id FROM  "+Table.BUSINESS+" a,"+Table.SERVER_BUSINESS+" b "
//				+ "WHERE a.id = b.busi_id AND b.busi_id = ?";
//		if (ServerCfg.ALL != moduleId) {
//			sql = sql + " AND b.module_id = " + moduleId;
//		}
//		sql = sql + ") c ON a.id = c.svr_id ORDER BY a.id DESC";

		String sql = "select a.id serverID,a.host_name hostName,a.disk_num,b.name machineRoom  "
				+ "from server_list a left join idc_list b on a.idc_id = b.id  "
				+ "where a.id in (select svr_id from server_business where busi_id = ? ";

		if (ServerCfg.ALL != moduleId) {
			sql = sql + " and module_id =  " + moduleId;
		}

		sql = sql + ")";

		try {
			final Map<Integer, String> busiIdNames = businessDao.getBusiIDNames();
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					Machine machine = new Machine();
					Integer serverID = rs.getInt("serverID");
					String hostName = rs.getString("hostName");
					String machineRoom = rs.getString("machineRoom");
					machine.setServerID(serverID);
					machine.setBusiness(busiIdNames.get(busineID));
					machine.setHostName(hostName);
					machine.setDiskNum(rs.getInt("disk_num"));
					machine.setMachineRoom(machineRoom);
					machine.setIp(getMachineIP(serverID));
					try {
						machine.setCpuUsage(getCurrentCpuUsage(serverID));
						machine.setStatus(getCurrentStatus(serverID));
						machine.setFiveLoad(getCurrentFiveMinLoad(serverID));
					} catch (ServerException e) {
						e.printStackTrace();
					}
					machineList.add(machine);
				}
			}, busineID);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
		return machineList;
	}
	
	/**
	 * 分组视图
	 * @param groupID
	 * @return
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月13日 下午4:17:21
	 */
	public List<Machine> queryGroupServerList(int groupID) throws ServerException{
		final List<Machine> machineList = new ArrayList<Machine>();
		String sql = "select a.id serverID,a.host_name hostName,a.disk_num,b.name machineRoom,c.name business from " + Table.SERVER_LIST + " a left join "
				+ Table.IDC_LIST+" b on a.idc_id = b.id left join (select distinct a.name,b.svr_id from  "+Table.BUSINESS+" a,"+Table.SERVER_BUSINESS+" b"
				+ " where a.id = b.busi_id) c on a.id = c.svr_id,"+Table.SERVER_GROUP+" d where a.id = d.svr_id"
				+ " and d.group_id = ? order by a.id desc";
		try {
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					Machine machine = new Machine();
					Integer serverID = rs.getInt("serverID");
					String hostName = rs.getString("hostName");
					String machineRoom = rs.getString("machineRoom");
					String business = rs.getString("business");
					machine.setServerID(serverID);
					machine.setBusiness(business);
					machine.setDiskNum(rs.getInt("disk_num"));
					machine.setHostName(hostName);
					machine.setMachineRoom(machineRoom);
					machine.setIp(getMachineIP(serverID));
					try {
						machine.setCpuUsage(getCurrentCpuUsage(serverID));
						machine.setStatus(getCurrentStatus(serverID));
						machine.setFiveLoad(getCurrentFiveMinLoad(serverID));
					} catch (ServerException e) {
						e.printStackTrace();
					}
					machineList.add(machine);
				}
			}, groupID);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
		return machineList;
	}
	
	/**
	 * 根据业务查询模块
	 * @param busiId
	 * @return
	 * @author chenfanglin <br>
	 * @date 2016年1月12日 下午5:00:44
	 */
	public List<DicValue> queryModuleByBusiness(int busiId) throws ServerException{
		String sql = "select id value, name item from "+Table.BUSINESS+" where pid = ?";
		try {
			final List<DicValue> valueItems = new ArrayList<DicValue>();
			DicValue all = new DicValue(ServerCfg.ALL,"全部");
			valueItems.add(all);
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					DicValue v = new DicValue();
					v.setValue(rs.getInt("value"));
					v.setLabel(rs.getString("item"));
					valueItems.add(v);
				}
			}, busiId);
			return valueItems;
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
		return null;
	}
	
	/**
	 * 给分组添加机器
	 * @param groupID
	 * @param serverID
	 * @throws ServerException
	 * @author chenfanglin <br>
	 * @date 2016年1月13日 下午5:12:41
	 */
	public long addMachineForGroup(int groupID, int serverID) throws ServerException  {
		String sql = "insert into "+Table.SERVER_GROUP+"(group_id,svr_id) values(?,?)";
		try {
			return jdbcTemplateMonitor.insert(sql, groupID,serverID);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
		return 0;
	}

	/**
	 * 获取服务器基础数据
	 * @param serverID
	 * @author wendy
	 * @return
	 * @throws ServerException
	 */
	public Map<String,Object> getServerBasisData(final int serverID) throws ServerException {
		String sql = "select id, host_name, net_card_num, cpu_num, cpu_type," +
				"cpu_physical_cores, cpu_logic_cores, os, kernel, memory, " +
				"disk_num, disk_size, disk_details, disk_partition from server_list " +
				"where id= ? ";

		final Map<String, Object> map = new HashMap<>();
		try {
			 jdbcTemplateMonitorStat.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					map.put("hostName", rs.getString("host_name"));
					map.put("netCardNum", rs.getInt("net_card_num"));
					map.put("cpuNum", rs.getInt("cpu_num"));
					map.put("cpuType", rs.getString("cpu_type"));
					map.put("cpuPhysicalCores", rs.getLong("cpu_physical_cores"));
					map.put("cpuLogicCores", rs.getLong("cpu_logic_cores"));
					map.put("kernel", rs.getString("kernel"));
					map.put("os", rs.getString("os"));
					Long memory = rs.getLong("memory");
					map.put("memory", memory.doubleValue() / 1024 * 1024);
					map.put("diskNum", rs.getLong("disk_num"));
					map.put("diskSize", rs.getLong("disk_size"));
					String diskDetails = rs.getString("disk_details");
					List<DiskDetail> disk = CachedObjects.GSON.fromJson(diskDetails, new TypeToken<List<DiskDetail>>() {
					}.getType());
					map.put("diskDetails", disk);

					String diskPartitions = rs.getString("disk_partition");

					List<DiskPartition> diskPartition = CachedObjects.GSON.
							fromJson(diskPartitions, new TypeToken<List<DiskPartition>>() {
							}.getType());

					setDiskPartionUsage(diskPartition, serverID);
					map.put("diskPartitions", diskPartition);
				}
			}, serverID);

			sql = "select sl.idc_id, sl.cabinet_id, il.name idcName, cl.name cabinetName "
					+ "from server_list sl left join idc_list il on sl.idc_id = il.id "
					+ "left join cabinet_list cl on sl.cabinet_id = cl.id where sl.id = ? ";

			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					map.put("idcName", rs.getString("idcName"));
					map.put("cabinetName", rs.getString("cabinetName"));
				}
			},serverID);

			sql = "select sil.id,sil.ip,sil.isp_id,sil.type,il.name ispName"
					+ " from server_ip_list sil left join isp_list il " +
					"on sil.isp_id = il.id where sil.svr_id = ?";
			final List<DeviceIP> ipList = new ArrayList<DeviceIP>();
			jdbcTemplateMonitor.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					DeviceIP ip = new DeviceIP();
					ip.setIp(rs.getString("ip"));
					ip.setIspId(rs.getLong("isp_id"));
					ip.setType(rs.getLong("type"));
					ip.setIspName(rs.getString("ispName"));
					ipList.add(ip);
				}
			}, serverID);

			map.put("ipList", ipList);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, e);
		}
		return map;
	}

	/**
	 * 设置磁盘分区使用信息
	 * @param diskPartition
	 * @param serverID
	 */
	public void setDiskPartionUsage(List<DiskPartition> diskPartition,int serverID) {
		try {
			Result result =
					HbaseProxyClient.getOneRecord(Table.OMP_SERVER_FEATURE_STAT, String.valueOf(serverID));
			NavigableMap<byte[], byte[]> resultMap = result.getFamilyMap(Bytes.toBytes("stat"));

			for (DiskPartition partition : diskPartition) {
				String key1 = Constant.FREEPARTITION + ServerCfg.ROWKEY_SPLITER
						+ partition.getPartition();
				String key2 = Constant.TOTALPARTITION + ServerCfg.ROWKEY_SPLITER
						+ partition.getPartition();

				Long free = Bytes.toLong(resultMap.get(Bytes.toBytes(key1)));
				Long total = Bytes.toLong(resultMap.get(Bytes.toBytes(key2)));

				Long used = (total - free) / (1024 * 1024 * 1024);

				double usage = new BigDecimal(total - free).divide(new BigDecimal(total), 4,
						BigDecimal.ROUND_HALF_EVEN).doubleValue();
				partition.setUsed(used);
				partition.setUsage(usage);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
