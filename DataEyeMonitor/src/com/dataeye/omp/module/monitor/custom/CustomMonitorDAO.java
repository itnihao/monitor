package com.dataeye.omp.module.monitor.custom;


import com.dataeye.exception.ExceptionHandler;
import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.PageData;
import com.dataeye.omp.constant.Constant;
import com.dataeye.omp.constant.StatusCode;
import com.dataeye.utils.HbasePool;
import com.dataeye.utils.HbaseProxyClient;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Repository
public class CustomMonitorDAO {


    @Resource(name = "jdbcTemplateMonitor")
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加自定监控
     * @param monitor 需要添加的monitor对象
     * @return id
     */
    public int add(CustomMonitor monitor) throws ServerException {
        int returnId = 0;
        String sql = "insert into dc_monitor_customize_info (monitorItem,mainPrincipal,bakPrincipal," +
                "business,createTime) values (?, ?, ?, ?, ?)";
        long createTime = System.currentTimeMillis()/1000;
        try {
            returnId = (int) jdbcTemplate.insert(sql, monitor.getMonitorItem(), monitor.getMainPrincipal(),
                    monitor.getBakPrincipal(), monitor.getBusiness(), createTime);

        }catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return returnId;
    }

    /**
     * 检查自定义监控项的唯一性
     * @param monitorItem 监控项
     * @return true：已存在，false：不存在
     */
    public boolean isExists(String monitorItem) throws ServerException {
        int count = 0;
        String sql = "select count(1) from dc_monitor_customize_info where monitorItem = ?";
        try {
            count =jdbcTemplate.queryForInt(sql,monitorItem);
        }catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return (count > 0);
    }


    /**
     * 检查自定义监控项的唯一性
     * @param monitorItem 监控项
     * @return true：已存在，false：不存在
     */
    public boolean isExists(String monitorItem,int monitorId) throws ServerException {
        int count = 0;
        String sql = "select count(1) from dc_monitor_customize_info " +
                "where monitorItem = ? and id <> ? ";
        try {
            count =jdbcTemplate.queryForInt(sql,monitorItem,monitorId);
        }catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return (count > 0);
    }

    /**
     * 修改自定义监控
     * @param monitor 需要修改的monitor对象
     * @return 修改成功记录条数
     */
    public int update (CustomMonitor monitor) throws ServerException {
        int ret = 0;
        String sql = "update dc_monitor_customize_info set monitorItem=?,  mainPrincipal = ?, bakPrincipal = ?," +
                "business = ?, updateTime = ? where id = ?";
        long updateTime = System.currentTimeMillis()/1000;
        try {
            ret = jdbcTemplate.update(sql, monitor.getMonitorItem(), monitor.getMainPrincipal(),
                    monitor.getBakPrincipal(), monitor.getBusiness(),
                    updateTime, monitor.getId());
        }catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return ret;
    }

    /**
     * 根据id查询自定义监控项
      * @param id 监控id
     * @return monitor对象
     */
    public CustomMonitor loadById(int id) throws ServerException {
        final CustomMonitor monitor = new CustomMonitor();
        String sql = "select * from dc_monitor_customize_info where id = ?";
        try {

            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {

                    try {
                        monitor.setId(rs.getInt("id"));
                        monitor.setMonitorItem(rs.getString("monitorItem"));
                        monitor.setMainPrincipal(rs.getInt("mainPrincipal"));
                        monitor.setMainPrincipalName(getMainPrincipalNameById(rs.getInt("mainPrincipal")));
                        monitor.setBakPrincipal(rs.getString("bakPrincipal"));
                        monitor.setBakPrincipalNames(getBakPrincipalNames(rs.getString("bakPrincipal")));
                        monitor.setBusiness(rs.getInt("business"));
                        monitor.setBusinessName(getBusinessNameById(rs.getInt("business")));
                        monitor.setCreateTime(rs.getLong("createTime"));
                        monitor.setUpdateTime(rs.getLong("updateTime"));
                    } catch (ServerException e) {
                        e.printStackTrace();
                    }
                }
            }, id);
        }catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return monitor;
    }

    /**
     * 分页条件查询
     * @param pageId 当前页
     * @param pageSize 每页记录的条数
     * @param searchKey 查询关键字 默认按monitorItem进行模糊查询
     * @param orderBy 排序关键字
     * @param order 排序方式 0-->ASC , 1-->DESC
     * @return pageData
     */
    public PageData<CustomMonitor> getByPage(int pageId, int pageSize, String searchKey,
                                             String orderBy, int order) throws ServerException {
        PageData<CustomMonitor> pageData = new PageData<>();
        pageData.setPageID(pageId);
        pageData.setPageSize(pageSize);
        pageData.setOrderBy(orderBy);
        pageData.setOrder(order);
        String orderStr = order == 0 ? "ASC" : "DESC";
        String sql = "";
        String sqlCount = "";
        int count;
        final List<CustomMonitor> list = new ArrayList<>();
        try {
            if (searchKey == null || "".equals(searchKey)) {
                sql = "select * from dc_monitor_customize_info order by " + orderBy + " "+ orderStr +" limit ?, ?";
                sqlCount = "select count(1) from dc_monitor_customize_info";
                count = jdbcTemplate.queryForInt(sqlCount);
                jdbcTemplate.query(sql, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        CustomMonitor monitor = new CustomMonitor();
                        try {
                            monitor.setId(rs.getInt("id"));
                            monitor.setMonitorItem(rs.getString("monitorItem"));
                            monitor.setMainPrincipal(rs.getInt("mainPrincipal"));
                            monitor.setMainPrincipalName(getMainPrincipalNameById(rs.getInt("mainPrincipal")));
                            monitor.setBakPrincipal(rs.getString("bakPrincipal"));
                            monitor.setBakPrincipalNames(getBakPrincipalNames(rs.getString("bakPrincipal")));
                            monitor.setBusiness(rs.getInt("business"));
                            monitor.setBusinessName(getBusinessNameById(rs.getInt("business")));
                            monitor.setCreateTime(rs.getLong("createTime"));
                            monitor.setUpdateTime(rs.getLong("updateTime"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        list.add(monitor);
                    }
                }, (pageId - 1) * pageSize, pageSize);
            } else {
                searchKey = "%" + searchKey + "%";
                sql = "select * from dc_monitor_customize_info where monitorItem like ? order by "+ orderBy + " " + orderStr + " limit ?, ?";
                sqlCount = "select count(1) from dc_monitor_customize_info where monitorItem like ?";
                count = jdbcTemplate.queryForInt(sqlCount, searchKey);
                jdbcTemplate.query(sql, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        CustomMonitor monitor = new CustomMonitor();
                        try {
                            monitor.setId(rs.getInt("id"));
                            monitor.setMonitorItem(rs.getString("monitorItem"));
                            monitor.setMainPrincipal(rs.getInt("mainPrincipal"));
                            monitor.setMainPrincipalName(getMainPrincipalNameById(rs.getInt("mainPrincipal")));
                            monitor.setBakPrincipal(rs.getString("bakPrincipal"));
                            monitor.setBakPrincipalNames(getBakPrincipalNames(rs.getString("bakPrincipal")));
                            monitor.setBusiness(rs.getInt("business"));
                            monitor.setBusinessName(getBusinessNameById(rs.getInt("business")));
                            monitor.setCreateTime(rs.getLong("createTime"));
                            monitor.setUpdateTime(rs.getLong("updateTime"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        list.add(monitor);
                    }
                }, searchKey, (pageId - 1) * pageSize, pageSize);
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


    private String getBusinessNameById(int id) throws ServerException {
        String sql = "select name from business where id = ?";
        String name = "";
        try {
           name = jdbcTemplate.queryForString(sql, id);
        }catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return name;
    }

    private String getMainPrincipalNameById(int id) throws ServerException {
        String sql = "select name from employee where id = ?";
        String name = "";
        try {
            name = jdbcTemplate.queryForString(sql, id);
        }catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
        }
        return name;
    }


    private List<Map<String, String>> getBakPrincipalNames(String others){
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


    /**
     * 获取自定义监控对象
     * @param monitorItem
     * @param items
     * @return
     */
    public List<Map<String, String>> getSelectorByMonitorItem(String monitorItem, String... items){
        List<Map<String, String>> list = new ArrayList<>();
        HTableInterface table = null;
        StringBuffer reg = new StringBuffer("^"+monitorItem);
        for (int i = 0; i < items.length; i++) {
            reg.append("[#]"+items[i]);
        }
        reg.append("#[^#]*$");
        try {
            table = HbasePool.getConnection().getTable(Bytes.toBytes(
                    Constant.Table.OMP_CUSTOM_MONITOR_OBJECT));
            Scan scan = new Scan();
            Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,
                    new RegexStringComparator(reg.toString()));
            scan.setFilter(filter);
            ResultScanner resultScanner = table.getScanner(scan);
            for (Result rs : resultScanner) {
                KeyValue[] kvs = rs.raw();
                for (int i = 0; i < kvs.length; i++) {
                    Map<String, String> map = new HashMap<>();
                    map.put("label", Bytes.toString(kvs[i].getValue()));
                    map.put("value", Bytes.toString(kvs[i].getValue()));
                    list.add(map);
                    //System.out.println(Bytes.toString(kvs[i].getValue()));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            HbasePool.close(table);
        }
        return list;
    }

    /**
     * 根据监控项模糊查询列表信息
     * @param item
     * @return
     */
    public List<Map<String, Object>> getMonitorItemByFuzzy(String item) throws ServerException {
        final List<Map<String, Object>> list = new ArrayList<>();
        String sql = "";
        if (item == null || "".equals(item)) {
        	sql = "select id, monitorItem from dc_monitor_customize_info";
            try {
                jdbcTemplate.query(sql, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", rs.getInt("id"));
                        map.put("monitorItem", rs.getString("monitorItem"));
                        list.add(map);
                    }
                });
            }catch (Exception e) {
                e.printStackTrace();
                ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
            }
        } else {
        	sql = "select id, monitorItem from dc_monitor_customize_info where monitorItem like ?";
            String key = "%" + item + "%";
            try {
                jdbcTemplate.query(sql, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", rs.getInt("id"));
                        map.put("monitorItem", rs.getString("monitorItem"));
                        list.add(map);
                    }
                }, key);
            }catch (Exception e) {
                e.printStackTrace();
                ExceptionHandler.throwDatabaseException(StatusCode.DB_SQL_ERROR, "数据库异常");
            }
        }
        
        return list;
    }


    public Object getMonitorSelectItem(int type, String searchKey,String monitorItem,
                                       String caller, String receiver,
                                       String ext1, String ext2) throws IOException {
        Result rs = HbaseProxyClient.getOneRecord(Constant.Table.OMP_CUSTOM_MONITOR_OBJECT, monitorItem);
        NavigableMap<byte[], byte[]> dataMap = rs.getFamilyMap(Bytes.toBytes(Constant.Table.FAMILY));
        if (searchKey == null) {
            searchKey = "";
        }
        Map<String, String> map = new HashMap<>();
        if (dataMap != null) {
            for (Map.Entry<byte[], byte[]> data : dataMap.entrySet()) {
                String key = Bytes.toString(data.getKey());

                String[] params = key.split(Constant.ServerCfg.ROWKEY_SPLITER);
                /**type: 1 调用端  2 接收端  3 扩展属性1  4 扩展属性2 */
                switch (type) {
                    case 1: {
                        queryCaller(searchKey, receiver, ext1, ext2, params, map);
                        break;
                    }

                    case 2: {
                        queryReceiver(caller, searchKey, ext1, ext2, params, map);
                        break;
                    }

                    case 3: {
                        queryExt1(caller, receiver, searchKey, ext2, params, map);
                        break;
                    }

                    case 4: {
                        queryExt2(caller, receiver, ext1, searchKey, params, map);
                        break;
                    }

                    default: {
                        break;
                    }
                }
            }
        }


        List<Map<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, String> item : map.entrySet()) {
            Map<String, String> rMap = new HashMap<>();
            rMap.put("label", item.getKey());
            rMap.put("value", item.getValue());
            list.add(rMap);
        }

        return list;
    }

    private void queryExt2(String caller, String receiver,
                           String ext1, String ext2,
                           String[] params,
                           Map<String, String> map) {
        if (StringUtil.isEmpty(caller)
                && StringUtil.isEmpty(receiver)
                && StringUtil.isEmpty(ext1)) {
            if (params[3].indexOf(ext2) >= 0) {
                map.put(params[3], params[3]);
            }
        }

        if (StringUtil.isNotEmpty(caller)
                && StringUtil.isEmpty(receiver)
                && StringUtil.isEmpty(ext1)) {
            if (params[0].equals(caller)
                    && params[3].indexOf(ext2) >= 0) {
                map.put(params[3], params[3]);
            }
        }

        if (StringUtil.isEmpty(caller)
                && StringUtil.isNotEmpty(receiver)
                && StringUtil.isEmpty(ext1)) {
            if (params[1].equals(receiver)
                    && params[3].indexOf(ext2) >= 0) {
                map.put(params[3], params[3]);
            }
        }


        if (StringUtil.isEmpty(caller)
                && StringUtil.isEmpty(receiver)
                && StringUtil.isNotEmpty(ext1)) {
            if (params[2].equals(ext1)
                    && params[3].indexOf(ext2) >= 0) {
                map.put(params[3], params[3]);
            }
        }

        if (StringUtil.isNotEmpty(caller)
                && StringUtil.isNotEmpty(receiver)
                && StringUtil.isEmpty(ext1)) {
            if (params[0].equals(caller)
                    && params[1].equals(receiver)
                    && params[3].indexOf(ext2) >= 0) {
                map.put(params[3], params[3]);
            }
        }

        if (StringUtil.isNotEmpty(caller)
                && StringUtil.isEmpty(receiver)
                && StringUtil.isNotEmpty(ext1)) {
            if (params[0].equals(caller)
                    && params[2].equals(ext1)
                    && params[3].indexOf(ext2) >= 0) {
                map.put(params[3], params[3]);
            }
        }


        if (StringUtil.isEmpty(caller)
                && StringUtil.isNotEmpty(receiver)
                && StringUtil.isNotEmpty(ext1)) {
            if (params[1].equals(receiver)
                    && params[2].equals(ext1)
                    && params[3].indexOf(ext2) >= 0) {
                map.put(params[3], params[3]);
            }
        }

        if (StringUtil.isNotEmpty(caller)
                && StringUtil.isNotEmpty(receiver)
                && StringUtil.isNotEmpty(ext1)) {
            if ((params[0].equals(caller)
                    && params[1].equals(receiver)
                    && params[2].equals(ext1)
                    && params[3].indexOf(ext2) >= 0)) {
                map.put(params[3], params[3]);
            }
        }
    }

    private void queryExt1(String caller, String receiver,
                           String ext1, String ext2,
                           String[] params,
                           Map<String, String> map) {

        if (StringUtil.isEmpty(caller)
                && StringUtil.isEmpty(receiver)
                && StringUtil.isEmpty(ext2)) {
            if (params[2].indexOf(ext1) >= 0) {
                map.put(params[2], params[2]);
            }
        }

        if (StringUtil.isNotEmpty(caller)
                && StringUtil.isEmpty(receiver)
                && StringUtil.isEmpty(ext2)) {
            if (params[0].equals(caller)
                    && params[2].indexOf(ext1) >= 0) {
                map.put(params[2], params[2]);
            }
        }

        if (StringUtil.isEmpty(caller)
                && StringUtil.isNotEmpty(receiver)
                && StringUtil.isEmpty(ext2)) {
            if (params[1].equals(receiver)
                    && params[2].indexOf(ext1) >= 0) {
                map.put(params[2], params[2]);
            }
        }


        if (StringUtil.isEmpty(caller)
                && StringUtil.isEmpty(receiver)
                && StringUtil.isNotEmpty(ext2)) {
            if (params[3].equals(ext2)
                    && params[2].indexOf(ext1) >= 0) {
                map.put(params[2], params[2]);
            }
        }

        if (StringUtil.isNotEmpty(caller)
                && StringUtil.isNotEmpty(receiver)
                && StringUtil.isEmpty(ext2)) {
            if (params[0].equals(caller)
                    && params[1].equals(receiver)
                    && params[2].indexOf(ext1) >= 0) {
                map.put(params[2], params[2]);
            }
        }

        if (StringUtil.isNotEmpty(caller)
                && StringUtil.isEmpty(receiver)
                && StringUtil.isNotEmpty(ext2)) {
            if (params[0].equals(caller)
                    && params[3].equals(ext2)
                    && params[2].indexOf(ext1) >= 0) {
                map.put(params[2], params[2]);
            }
        }


        if (StringUtil.isEmpty(caller)
                && StringUtil.isNotEmpty(receiver)
                && StringUtil.isNotEmpty(ext2)) {
            if (params[1].equals(receiver)
                    && params[3].equals(ext2)
                    && params[2].indexOf(ext1) >= 0) {
                map.put(params[2], params[2]);
            }
        }

        if (StringUtil.isNotEmpty(caller)
                && StringUtil.isNotEmpty(receiver)
                && StringUtil.isNotEmpty(ext2)) {
            if ((params[0].equals(caller)
                    && params[1].equals(receiver)
                    && params[3].equals(ext2)
                    && params[2].indexOf(ext1) >= 0)) {
                map.put(params[2], params[2]);
            }
        }


    }

    private void queryReceiver(String caller, String receiver,
                               String ext1, String ext2,
                               String[] params,
                               Map<String, String> map) {
        if (StringUtil.isEmpty(caller)
                && StringUtil.isEmpty(ext1)
                && StringUtil.isEmpty(ext2)) {
            if (params[1].indexOf(receiver) >= 0) {
                map.put(params[1], params[1]);
            }
        }

        if (StringUtil.isNotEmpty(caller)
                && StringUtil.isEmpty(ext1)
                && StringUtil.isEmpty(ext2)) {
            if (params[0].equals(caller)
                    && params[1].indexOf(receiver) >= 0) {
                map.put(params[1], params[1]);
            }
        }

        if (StringUtil.isEmpty(caller)
                && StringUtil.isNotEmpty(ext1)
                && StringUtil.isEmpty(ext2)) {
            if (params[2].equals(ext1)
                    && params[1].indexOf(receiver) >= 0) {
                map.put(params[1], params[1]);
            }
        }


        if (StringUtil.isEmpty(caller)
                && StringUtil.isEmpty(ext1)
                && StringUtil.isNotEmpty(ext2)) {
            if (params[3].equals(ext2)
                    && params[1].indexOf(receiver) >= 0) {
                map.put(params[1], params[1]);
            }
        }

        if (StringUtil.isNotEmpty(caller)
                && StringUtil.isNotEmpty(ext1)
                && StringUtil.isEmpty(ext2)) {
            if (params[0].equals(caller)
                    && params[2].equals(ext1)
                    && params[1].indexOf(receiver) >= 0) {
                map.put(params[1], params[1]);
            }
        }

        if (StringUtil.isNotEmpty(caller)
                && StringUtil.isEmpty(ext1)
                && StringUtil.isNotEmpty(ext2)) {
            if (params[0].equals(caller)
                    && params[3].equals(ext2)
                    && params[1].indexOf(receiver) >= 0) {
                map.put(params[1], params[1]);
            }
        }


        if (StringUtil.isEmpty(caller)
                && StringUtil.isNotEmpty(ext1)
                && StringUtil.isNotEmpty(ext2)) {
            if (params[2].equals(ext1)
                    && params[3].equals(ext2)
                    && params[1].indexOf(receiver) >= 0) {
                map.put(params[1], params[1]);
            }
        }

        if (StringUtil.isNotEmpty(caller)
                && StringUtil.isNotEmpty(ext1)
                && StringUtil.isNotEmpty(ext2)) {
            if ((params[0].equals(caller)
                    && params[2].equals(ext1)
                    && params[3].equals(ext2)
                    && params[1].indexOf(receiver) >= 0)) {
                map.put(params[1], params[1]);
            }
        }
    }

    private void queryCaller(String caller, String receiver,
                             String ext1, String ext2,
                             String[] params,
                             Map<String, String> map) {

        if (StringUtil.isEmpty(receiver)
                && StringUtil.isEmpty(ext1)
                && StringUtil.isEmpty(ext2)) {
            if (params[0].indexOf(caller) >= 0) {
                map.put(params[0], params[0]);
            }
        }

        if (StringUtil.isNotEmpty(receiver)
                && StringUtil.isEmpty(ext1)
                && StringUtil.isEmpty(ext2)) {
            if (params[1].equals(receiver) &&
                    params[0].indexOf(caller) >= 0) {
                map.put(params[0], params[0]);
            }
        }

        if (StringUtil.isEmpty(receiver)
                && StringUtil.isNotEmpty(ext1)
                && StringUtil.isEmpty(ext2)) {
            if (params[2].equals(ext1)
                    && params[0].indexOf(caller) >= 0) {
                map.put(params[0], params[0]);
            }
        }

        if (StringUtil.isEmpty(receiver)
                && StringUtil.isEmpty(ext1)
                && StringUtil.isNotEmpty(ext2)) {
            if (params[3].equals(ext2)
                    && params[0].indexOf(caller) >= 0) {
                map.put(params[0], params[0]);
            }
        }

        if (StringUtil.isNotEmpty(receiver)
                && StringUtil.isNotEmpty(ext1)
                && StringUtil.isEmpty(ext2)) {
            if (params[1].equals(receiver) && params[2].equals(ext1)
                    && params[0].indexOf(caller) >= 0) {
                map.put(params[0], params[0]);
            }
        }

        if (StringUtil.isNotEmpty(receiver)
                && StringUtil.isEmpty(ext1)
                && StringUtil.isNotEmpty(ext2)) {
            if (params[1].equals(receiver) && params[3].equals(ext2)
                    && params[0].indexOf(caller) >= 0) {
                map.put(params[0], params[0]);
            }
        }

        if (StringUtil.isEmpty(receiver)
                && StringUtil.isNotEmpty(ext1)
                && StringUtil.isNotEmpty(ext2)) {
            if (params[2].equals(ext1) && params[3].equals(ext2)
                    && params[0].indexOf(caller) >= 0) {
                map.put(params[0], params[0]);
            }
        }

        if (StringUtil.isNotEmpty(receiver)
                && StringUtil.isNotEmpty(ext1)
                && StringUtil.isNotEmpty(ext2)) {
            if (params[1].equals(receiver)
                    && params[2].equals(ext1)
                    && params[3].equals(ext2)
                    && params[0].indexOf(caller) >= 0) {
                map.put(params[0], params[0]);
            }
        }
    }


    public static void main(String[] args) {
        String value = "caller####";
        String[] params = value.split("#");
        for (String param : params) {
            System.out.println(param.trim());
        }
    }
}
