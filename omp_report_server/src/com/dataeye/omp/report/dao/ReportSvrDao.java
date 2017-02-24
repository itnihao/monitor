package com.dataeye.omp.report.dao;

import com.dataeye.omp.report.bean.Device;
import com.dataeye.omp.report.bean.Feature;
import com.dataeye.omp.report.dbproxy.hbase.HbaseBatchPutHandler;
import com.dataeye.omp.report.dbproxy.hbase.PutItem;
import com.dataeye.omp.report.utils.Constant;
import com.dataeye.omp.report.utils.DateUtils;
import com.dataeye.omp.report.utils.ServerIDCache;
import com.xunlei.jdbc.JdbcTemplate;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Objects;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wendy
 * @since 2016/7/29
 */
@Service
public class ReportSvrDao {

    @Resource(name = "jdbcTemplateMonitorStat")
    private JdbcTemplate jdbcTemplateMonitorStat;

    public void saveData2Hbase(List<Feature> params)
            throws IOException {
        String tableNameMinute = Constant.TABLE_FEATURE_VALUE_MINUTE;
        String tableNameHour = Constant.TABLE_FEATURE_VALUE_HOUR;
        String tableNameCurrent = Constant.TABLE_SERVER_FEATURE;
        Put put = null;
        for (Feature param : params) {
            //1. 以分钟的形式保存数据到hbase(omp_feature_value_stat_minute)表中,
            //   以 serverid#featrue_id#object#date 为行名
            //   以每分钟为列，全天共1440分钟，列名范围 [0,1439]
            String rowKeyMinute = getRowKey(param);
            int minute = DateUtils.getMinuteBetweenDay(param.getTime());
            String columnMinute = String.valueOf(minute);
            String valueMinute = param.getValue() + "_" + DateUtils.unixTimestamp();
            put = new Put(Bytes.toBytes(rowKeyMinute));
            put.add(Bytes.toBytes(Constant.COLUMN_FAMILY)
                    , Bytes.toBytes(columnMinute)
                    , Bytes.toBytes(valueMinute));
            HbaseBatchPutHandler.saveItem(new PutItem(tableNameMinute, put));

            //2. 以小时的形式保存数据到hbase(omp_feature_value_stat_hour)表中，
            //   以 serverid#featrue_id#object#date  为行名称
            //   每小时一列，列名范围 [0,23]. 每小时存的是60分钟内的峰值
            String rowKeyHour = getRowKey(param);
            String columnHour = DateUtils.formatKK(param.getTime());
            String perHour = DateUtils.formatyyyyMMddHH(param.getTime());
            long valueHour = param.getValue().longValue();
            if (isMaxCacheValue(perHour, rowKeyHour, valueHour)) {
                put = new Put(Bytes.toBytes(rowKeyHour));
                put.add(Bytes.toBytes(Constant.COLUMN_FAMILY),
                        Bytes.toBytes(columnHour),
                        Bytes.toBytes(valueHour));
                HbaseBatchPutHandler.saveItem(new PutItem(tableNameHour, put));
            }

            //3. 统计服务器系统特性 保存当前上报最新值到hbase(omp_server_feature_stat)表中
            //   以serverid为行名称， 以fid#object为列
            int srvId = ServerIDCache.getServerId(param.getClient());
            String rowKeyCurrent = String.valueOf(srvId);
            String columnCurrent = param.getFid() + "#" + param.getObject();
            String valueCurrent = param.getValue() + "_" + DateUtils.unixTimestamp();
            put = new Put(Bytes.toBytes(rowKeyCurrent));
            put.add(Bytes.toBytes(Constant.COLUMN_FAMILY),
                    Bytes.toBytes(columnCurrent),
                    Bytes.toBytes(valueCurrent));
            HbaseBatchPutHandler.saveItem(new PutItem(tableNameCurrent, put));

                }
    }

    /**
     * 获取行键:serverid#featrue_id#object#date
     * @param feature
     * @return
     * @throws ParseException
     */
    private String getRowKey(Feature feature) {
        Integer srvId = ServerIDCache.getServerId(feature.getClient());
        String currentDate = DateUtils.formatyyyyMMdd(feature.getTime());
        StringBuffer sb = new StringBuffer();
        sb.append(srvId)
                .append("#")
                .append(feature.getFid())
                .append("#")
                .append(feature.getObject())
                .append("#")
                .append(currentDate);
        return sb.toString();
    }

    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Long>>
            maxValuePerHourCache = new ConcurrentHashMap<>();

    public boolean isMaxCacheValue(String date, String rowKey, long value) {
        ConcurrentHashMap<String, Long> map
                = maxValuePerHourCache.get(date);
        if (null == map) {
            synchronized (Objects.class) {
                if (null == map) {
                    maxValuePerHourCache = null;
                    maxValuePerHourCache = new ConcurrentHashMap<>();
                    map = new ConcurrentHashMap<String, Long>();
                    maxValuePerHourCache.put(date, map);
                }
            }
        }

        Long maxValue = map.get(rowKey);
        if (null == maxValue) {
            map.put(rowKey, value);
            return true;
        }

        if (maxValue < value) {
            map.put(rowKey, value);
            return true;
        }
        return false;
    }


    /**
     * 保存上报设备信息
     * @param device
     * @throws ParseException
     */
    public void addServer(Device device) throws ParseException {
        try {
            String sql = "select count(1) from server_list where id = ?";
            int count = jdbcTemplateMonitorStat.queryForInt(sql, device.getId());

            if (device.getId() == -1) {
                return ;
            }

            if (count > 0) {
                sql = "update server_list set host_name = ?, net_card_num = ?,"
                        + "cpu_num = ?, cpu_type = ?, cpu_physical_cores = ?,"
                        + "cpu_logic_cores = ?, os = ?, kernel = ?, memory = ?,"
                        + "disk_num = ?, disk_size =?, disk_details = ?,"
                        +  "disk_partition = ?, hb_time = ? where id = ?";
            }else{
                sql = "insert into server_list(host_name, net_card_num,"
                        + "cpu_num, cpu_type, cpu_physical_cores, cpu_logic_cores,"
                        + "os, kernel, memory, disk_num, disk_size, disk_details,"
                        + "disk_partition, hb_time, id) values (?, ?, ?, ?, ?, ?, ?, ?,"
                        + " ?, ?, ?, ?, ?, ?, ?)";
            }
            Object[] objs = getParams(device);
            jdbcTemplateMonitorStat.execute(sql, objs);


        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public Object[] getParams(Device device)  {
        Date date = DateUtils.parseDate(device.getDateTime());
        Object[] objs = {
                device.getHostName(),
                device.getNetCardNum(),
                device.getCpuNum(),
                device.getCpuType(),
                device.getCpuPhysicalCores(),
                device.getCpuLogicCores(),
                device.getOs(),
                device.getKernal(),
                device.getMemory().longValue(),
                device.getDiskNum(),
                device.getDiskSize().longValue(),
                device.getDiskDetailsString(),
                device.getDiskPartitionsString(),
                date,
                device.getId()
        };

        return objs;
    }

}
