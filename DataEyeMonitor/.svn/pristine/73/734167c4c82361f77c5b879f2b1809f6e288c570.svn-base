package com.dataeye.omp.module.cmdb.device;

import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.PageData;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.utils.ValidateUtils;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author wendy
 * @since 2015/12/15 11:26
 */
@Service
public class DeviceDao {

    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplate;

    /**
     * 分页获取所有设备列表
     *
     * @param pageId
     * @param pageSize
     * @param searchKey
     * @return
     * @throws ServerException
     */
    public PageData getDeviceListByAll(int pageId, int pageSize,
                  String searchKey) throws ServerException {

        PageData pageData = new PageData(pageSize, pageId);

        try {
            String sql = "";
            int count =0;

            //没有输入搜索关键词时查询所有
            if (ValidateUtils.isEmpty(searchKey)) {

                sql = "select count(1) from server_list";
                count = jdbcTemplate.queryForInt(sql);

            } else {

                searchKey = "%" + searchKey + "%";
                sql = "select count(1) from server_list where "
                        + "(host_name like ? or id in (select svr_id"
                        + " from server_ip_list where ip like ?))";
                count = jdbcTemplate.queryForInt(sql, searchKey,searchKey);
            }

            pageData.setTotalRecord(count);

            sql = "select sl.id, sl.dev_id, sl.dev_type, sl.idc_id, "
                   + "il.name idcName, sl.cabinet_id, sl.host_name,"
                   + "sl.net_card_num, sl.cpu_num, sl.cpu_type,"
                   + "sl.cpu_physical_cores, sl.cpu_logic_cores, "
                   + "sl.os, sl.kernel, sl.memory,sl.disk_num,"
                   + "sl.disk_size, sl.disk_details, sl.dept_id,"
                   + "sl.admin, sl.backup_admin, sl.descs from server_list sl "
                   + "left join idc_list il on sl.idc_id = il.id ";

            int startIndex = (pageId - 1) * pageSize;

            Object[] objs ;

            if (ValidateUtils.isEmpty(searchKey)) {

                sql += "limit ?, ? ";
                objs = new Object[]{startIndex, pageSize};

            } else {

                sql += "where (sl.host_name like ? or sl.id in (select svr_id "
                     + "from server_ip_list where ip like ?)) limit ?, ? ";
                objs = new Object[]{searchKey, searchKey, startIndex, pageSize};
            }

            List<Device> deviceList = queryDeviceList(sql, objs);

            setBusiAndModuleInfo(deviceList);
            pageData.setContent(deviceList);

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }

        return pageData;
    }

    /**
     * 设置设备业务和模块信息
     * @param deviceList
     */
    private void setBusiAndModuleInfo(List<Device> deviceList) {
        for (Device device : deviceList) {
            List<DeviceBusiness> devBusiList =
                    getDeviceBusiModuleByDevId(device.getId());

            String busiName = "", moduleName = "";
            Set set = new HashSet();
            for (DeviceBusiness devBusi : devBusiList) {
                set.add(devBusi.getBusiName());
                moduleName += devBusi.getModuleName() + ", ";
            }
            busiName = set.toString();
            if (!StringUtil.isEmpty(busiName)) {
                busiName = busiName.substring(1, busiName.length() - 1);
            }

            if (!StringUtil.isEmpty(moduleName)) {
                moduleName = moduleName.substring(0, moduleName.length() - 2);
            }

            device.setBusiName(busiName);
            device.setModuleName(moduleName);
            device.setBusiModuleList(devBusiList);
        }
    }


    /**
     * 根据设备id获取设备对应的业务和模块
     * @param devId
     * @return
     */
    public List<DeviceBusiness> getDeviceBusiModuleByDevId(int devId) {
        final List<DeviceBusiness> deviceBusinessList = new ArrayList<>();

        String sql = "select sb.svr_id, sb.busi_id, b.name busiName, sb.module_id,"
                + "m.name moduleName from server_business sb left join business b "
                + "on sb.busi_id = b.id left join business m "
                + "on sb.module_id = m.id where svr_id = ?";

        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                DeviceBusiness devBusi = new DeviceBusiness();
                devBusi.setSvrId(rs.getInt("svr_id"));
                devBusi.setBusiId(rs.getInt("busi_id"));
                devBusi.setModuleId(rs.getInt("module_id"));
                devBusi.setBusiName(rs.getString("busiName"));
                devBusi.setModuleName(rs.getString("moduleName"));
                deviceBusinessList.add(devBusi);
            }
        }, devId);

        return deviceBusinessList;
    }

    /**
     * 根据机房分页获取所有设备列表
     *
     * @param pageId
     * @param pageSize
     * @param searchKey
     * @return
     * @throws ServerException
     */
    public PageData getDeviceListByIDC(int pageId, int pageSize,
                                       String searchKey) throws ServerException {

        PageData pageData = new PageData(pageSize, pageId);
        try {
            searchKey = "%" + searchKey + "%";
            String sql = "select count(1) from server_list where idc_id in "
                    + "(select id from idc_list where name like ?)";
            int count = jdbcTemplate.queryForInt(sql, searchKey);
            pageData.setTotalRecord(count);

            sql = "select sl.id, sl.dev_id, sl.dev_type, sl.idc_id, "
                    + "il.name idcName, sl.cabinet_id, sl.host_name,"
                    + "sl.net_card_num, sl.cpu_num, sl.cpu_type,"
                    + "sl.cpu_physical_cores, sl.cpu_logic_cores, "
                    + "sl.os, sl.kernel, sl.memory,sl.disk_num,"
                    + "sl.disk_size, sl.disk_details, sl.dept_id,"
                    + "sl.admin, sl.backup_admin, sl.descs from server_list sl "
                    + "left join idc_list il on sl.idc_id = il.id "
                    + "where sl.idc_id in (select id from idc_list "
                    + "where name like ?) limit ?, ? ";

            int startIndex = (pageId - 1) * pageSize;
//            int endIndex =
//                    pageId * pageSize < count ? pageId * pageSize : count;

            Object[] objs = {searchKey, startIndex, pageSize};
            List<Device> deviceList =
                    queryDeviceList(sql, objs);
            setBusiAndModuleInfo(deviceList);

            pageData.setContent(deviceList);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return pageData;
    }

    /**
     * 根据业务分页获取设备列表
     *
     * @param pageId
     * @param pageSize
     * @param searchKey
     * @return
     * @throws ServerException
     */
    public PageData getDeviceListByBusi(int pageId, int pageSize,
                                        String searchKey) throws ServerException {
        PageData pageData = new PageData(pageSize, pageId);
        searchKey = "%" + searchKey + "%";
        try {
            String sql = "select count(1) from server_list where id in"
                    + "(select sb.svr_id from server_business sb left join"
                    + " business s on sb.busi_id=s.id where s.name like ?)";
            int count = jdbcTemplate.queryForInt(sql, searchKey);

            pageData.setTotalRecord(count);

            sql =  "select sl.id, sl.dev_id, sl.dev_type, sl.idc_id, "
                    + "il.name idcName, sl.cabinet_id, sl.host_name,"
                    + "sl.net_card_num, sl.cpu_num, sl.cpu_type,"
                    + "sl.cpu_physical_cores, sl.cpu_logic_cores, "
                    + "sl.os, sl.kernel, sl.memory,sl.disk_num,"
                    + "sl.disk_size, sl.disk_details, sl.dept_id,"
                    + "sl.admin, sl.backup_admin, sl.descs from"
                    + " server_list sl left join idc_list il on"
                    + " sl.idc_id = il.id where sl.id in (select sb.svr_id"
                    + " from server_business sb left join business s on"
                    + " sb.busi_id=s.id where s.name like ?) limit ?, ? ";

            int startIndex = (pageId - 1) * pageSize;

            Object[] objs = {searchKey, startIndex, pageSize};
            List<Device> deviceList = queryDeviceList(sql, objs);
            setBusiAndModuleInfo(deviceList);
            pageData.setContent(deviceList);

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return pageData;
    }


    /**
     * 添加设备
     *
     * @param device
     */
    public long addDevice(Device device) throws ServerException {
        try {
            String sql = "insert into server_list (dev_id, dev_type, idc_id,"
                    + "cabinet_id, host_name, net_card_num, cpu_num, cpu_type, "
                    + "cpu_physical_cores, cpu_logic_cores, os, kernel, memory, "
                    + "disk_num, disk_size, disk_details, dept_id, "
                    + "admin, backup_admin, descs) values(?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Object[] args = getParams(device);
            long id =  jdbcTemplate.insert(sql, args);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return 0;
    }

    /**
     * 检查设备是否已添加
     *
     * @param devId
     * @param hostName
     * @return
     * @throws ServerException
     */
    public boolean checkDeviceExists(int id, String devId, String hostName) throws ServerException {
        try {
            String sql = null;
            int count = 0;
            if (StringUtil.isNotEmpty(devId)) {
                sql = "select count(1) from server_list where id not in (?) and ( dev_id =? or host_name = ?)";
                count = jdbcTemplate.queryForInt(sql, id, devId, hostName);
            } else {
                sql = "select count(1) from server_list where dev_id =? or host_name = ?";
                count = jdbcTemplate.queryForInt(sql, devId, hostName);
            }
            return count > 0 ? true : false;

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return false;
    }

    /**
     * 检查IP是否已经被其它机器使用
     * @param ip
     * @return
     * @throws ServerException
     */
    public boolean checkIpUsed( String ip, int devId)
            throws ServerException {
        try {
            String sql = "select count(1) from server_ip_list where "
                    +   "ip = ? and svr_id not in (?)";
            int count = jdbcTemplate.queryForInt(sql, ip, devId);
            return count > 0 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return false;
    }

    /**
     * 根据ID修改设备信息
     *
     * @param device
     */
    public void updateDevice(Device device) throws ServerException {
        try {
            String sql = "update server_list set dev_id=?, dev_type=?, idc_id=?,"
                    + "cabinet_id=?, host_name=?, net_card_num=?, cpu_num=?,"
                    + "cpu_type=?, cpu_physical_cores=?,cpu_logic_cores=?,os=?,"
                    + "kernel=?, memory=?, disk_num=?, disk_size=?, disk_details=?,"
                    + "dept_id=?, admin=?, backup_admin=?,"
                    + "descs=? where id=?";

            Object[] args =
                    getParams(device);
            jdbcTemplate.update(sql, args);


        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
    }

    /**
     * 删除设备
     * @param id
     */
    public void deleteDevice(int id) throws ServerException {
    try{
        String sql = "delete from server_list where id = ?";
        jdbcTemplate.update(sql, id);

        sql = "delete from server_ip_list where svr_id = ?";
        jdbcTemplate.update(sql, id);
    } catch (Exception e) {
        e.printStackTrace();
        ExceptionHandler.throwDatabaseException(
                StatusCode.DB_SQL_ERROR, "数据库异常");
    }
    }

    /**
     * 检查设备上是否有业务使用
     * @param id
     * @return
     * @throws ServerException
     */
    public boolean checkDeviceUsed(int id) throws ServerException {
        try{
            String sql = "select count(1) from server_business where svr_id = ?";
            int count =jdbcTemplate.queryForInt(sql, id);
            return count > 0 ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return false;
    }

    private Object[] getParams(Device device) {
        if (device == null) {
            return new Object[0];
        }

        Object[] param = new Object[]{
                device.getDevId(),
                device.getDevType(),
                device.getIdcId(),
                device.getCabinetId(),
                device.getHostName(),
                device.getNetCardNum(),
                device.getCpuNum(),
                device.getCpuType(),
                device.getCpuPhysicalCores(),
                device.getCpuLogicCores(),
                device.getOs(),
                device.getKernal(),
                device.getMemory(),
                device.getDiskNum(),
                device.getDiskSize(),
                device.getDiskDetails(),
                device.getDeptId(),
                device.getAdmin(),
                device.getBackupAdmin(),
                device.getDescs()
        };

        if (device.getId() > 0) {
            int length = param.length;
            param = Arrays.copyOf(param, length + 1);
            param[length] = device.getId();
        }
        return param;
    }

    /**
     * 保存设备IP信息
     *
     * @param ipList
     */
    public void batchInsertServerIp(List<DeviceIP> ipList, long devId)
            throws ServerException {
        try{
            String sql = "insert into server_ip_list (svr_id,ip,isp_id,type)" +
                " values(?, ?, ?, ?)";
            for (DeviceIP deviceIp : ipList) {
                jdbcTemplate.update(sql, devId,
                        deviceIp.getIp(),
                        deviceIp.getIspId(),
                        deviceIp.getType()
                );
            }
            } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
    }

    /**
     * 保存设备上的业务信息
     * @param busiModules
     * @param devId
     * @throws ServerException
     */
    public void batchInsertBusiness(List<DeviceBusiness> busiModules,
                                    long devId) throws ServerException {
        try {
            String sql = "insert into server_business (svr_id, busi_id, module_id) "
                    + "values (?, ?, ?)";
            for (DeviceBusiness busiModule : busiModules) {
                jdbcTemplate.insert(sql, devId,
                        busiModule.getBusiId(),
                        busiModule.getModuleId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
    }

    /**
     * 删除设备下的业务模块
     * @param devId
     * @throws ServerException
     */
    public void deleteBusiModuleByDevId(long devId) throws ServerException {
        try {
            String sql = "delete from server_business where svr_id = ?";
            jdbcTemplate.execute(sql, devId);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
    }

    /**
     * 删除设备ip信息
     * @param devId
     * @throws ServerException
     */
    public void deleteServerIpByDevId(long devId) throws ServerException {
        try {
            String sql = "delete from server_ip_list where svr_id = ?";
            jdbcTemplate.execute(sql, devId);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
    }

    /**
     * 查询设备信息列表
     *
     * @param sql
     * @param objs
     * @return
     */
    private List<Device> queryDeviceList(String sql, Object[] objs) throws SQLException {

        final List<Device> deviceList = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Device device = new Device();
                device.setId(rs.getInt("id"));
                device.setDevId(rs.getString("dev_id"));
                device.setDevType(rs.getInt("dev_type"));
                device.setIdcId(rs.getInt("idc_id"));
                device.setIdcName(rs.getString("idcName"));
                device.setCabinetId(rs.getInt("cabinet_id"));
                device.setHostName(rs.getString("host_name"));
                device.setNetCardNum(rs.getInt("net_card_num"));
                device.setCpuNum(rs.getInt("cpu_num"));
                device.setCpuType(rs.getInt("cpu_type"));
                device.setCpuPhysicalCores(rs.getInt("cpu_physical_cores"));
                device.setCpuLogicCores(rs.getInt("cpu_logic_cores"));
                device.setOs(rs.getInt("os"));
                device.setKernal(rs.getInt("kernel"));
                device.setMemory(rs.getInt("memory"));
                device.setDiskNum(rs.getInt("disk_num"));
                device.setDiskSize(rs.getInt("disk_size"));
                device.setDiskDetails(rs.getString("disk_details"));
                device.setDeptId(rs.getInt("dept_id"));
                device.setAdmin(rs.getInt("admin"));
                device.setBackupAdmin(rs.getString("backup_admin"));
                device.setDescs(rs.getString("descs"));
                deviceList.add(device);

            }
        }, objs);
        return deviceList;
    }

    /**
     * 获取字典数据
     *
     * @return
     * @throws ServerException
     */
    public Map<String, List<DicValue>> getDictionaryData() throws ServerException {
        Map<String, List<DicValue>> dictionaryMap = new HashMap<>();
        try {
            final List<DicItem> itemList = new ArrayList<>();
            String sql = "select id,label from dictionary_item";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    DicItem item = new DicItem();
                    item.setId(rs.getInt("id"));
                    item.setLabel(rs.getString("label"));
                    itemList.add(item);
                }
            });

            final List<DicValue> valueList = new ArrayList<>();
            sql = "select id, item_id, label, value from dictionary_value";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    DicValue value = new DicValue();
                    value.setId(rs.getInt("id"));
                    value.setItemId(rs.getInt("item_id"));
                    value.setLabel(rs.getString("label"));
                    value.setValue(rs.getInt("value"));
                    valueList.add(value);
                }
            });

            for (DicItem item : itemList) {
                List<DicValue> subValueList = new ArrayList<>();
                for (DicValue value : valueList) {
                    if (value.getItemId()==item.getId()) {
                        subValueList.add(value);
                    }
                    dictionaryMap.put(item.getLabel(), subValueList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");

        }
        return dictionaryMap;
    }

    /**
     * 获取机房和每个机房的机柜信息
     *
     * @return
     * @throws ServerException
     */
    public List<DicValue> getCabinetDicList() throws ServerException {
        final List<DicValue> valueList = new ArrayList<>();
        try{
            String sql = "select id, idc_id, name from cabinet_list";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    DicValue value = new DicValue();
                    value.setId(rs.getInt("id"));
                    value.setValue(rs.getInt("id"));
                    value.setItemId(rs.getInt("idc_id"));
                    value.setLabel(rs.getString("name"));
                    valueList.add(value);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return valueList;
    }



    public List<DicValue> getIdcDicList() throws ServerException {
        final List<DicValue> valueList = new ArrayList<>();
        try {
            String sql = "select id, name, contacts, address from idc_list";
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
        }catch (Exception e) {
                e.printStackTrace();
                ExceptionHandler.throwDatabaseException(
                        StatusCode.DB_SQL_ERROR, "数据库异常");
            }
            return valueList;
        }

    public List<Idc> getAllIdcList() throws ServerException {
        final List<Idc> idcList = new ArrayList<>();
        try {
            String sql = "select id, name,contacts, address from idc_list";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    Idc idc = new Idc();
                    idc.setId(rs.getInt("id"));
                    idc.setName(rs.getString("name"));
                    idc.setAddress(rs.getString("address"));
                    idc.setContacts(rs.getString("contacts"));
                    idcList.add(idc);
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return idcList;
    }



    public List<DicValue> getIspList() throws ServerException {
        final List<DicValue> valueList = new ArrayList<>();
        try {
            String sql = "select id, name from isp_list";
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
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return valueList;
    }

    /**
     * 获取所有设备IP信息
     * @return
     * @throws ServerException
     */
    public List<DeviceIP> getAllDeviceIp()
            throws ServerException {
        final List<DeviceIP> ipList = new ArrayList<>();
        try {
            String sql = "select sr.id, sr.svr_id, sr.ip, sr.isp_id, sr.type, "
                    + "sp.name from server_ip_list sr left join isp_list sp "
                    + "on sr.isp_id = sp.id";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    DeviceIP ip = new DeviceIP();
                    ip.setDevId(rs.getInt("svr_id"));
                    ip.setIp(rs.getString("ip"));
                    ip.setType(rs.getInt("type"));
                    ip.setIspId(rs.getInt("isp_id"));
                    ip.setIspName(rs.getString("name"));
                    ipList.add(ip);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return ipList;
    }


    /**
     * 获取设备总记录数
     * @return
     * @throws ServerException
     */
    public int getTotalDevcieRecord() throws ServerException {
        int total = 0;
        try {
                String sql = "select count(1) from server_list";
                total = jdbcTemplate.queryForInt(sql);
            } catch (Exception e) {
                e.printStackTrace();
                ExceptionHandler.throwDatabaseException(
                        StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return total;
    }

    /**
     * 获取供选择的设备
     * @return
     * @throws ServerException
     */
    public List<DicValue> getSelectDeviceList() throws ServerException {
        final List<DicValue> values = new ArrayList<>();
        try {
            String sql = "select id, host_name from server_list";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    DicValue kv = new DicValue();
                    kv.setValue(rs.getInt("id"));
                    kv.setLabel(rs.getString("host_name"));
                    values.add(kv);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return values;
    }

    /**
     * 获取所有设备ID和hostName
     * @return
     * @throws ServerException
     */
    public Map<Integer,String> getDeviceIDNames() throws ServerException {
        final  Map<Integer,String> map = new HashMap<Integer, String>();
        try {
            String sql = "select id, host_name from server_list";
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    map.put(rs.getInt("id"),rs.getString("host_name"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(
                    StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return map;
    }

}
