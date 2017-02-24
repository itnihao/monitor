package com.dataeye.omp.module.monitor.mysql;

import com.dataeye.exception.ServerException;
import com.dataeye.omp.common.DEResponse;
import com.dataeye.omp.module.monitor.server.ReportHbaseUtils;
import com.dataeye.utils.DateUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class MysqlMonitorReportDao {



    /**
     * 打开文件数(flag=1)|连接数(flag=2)|慢SQL数(flag=3)|锁数量(flag=4)  每分钟
     * @param serverId 机器id
     * @param port port
     * @param date 当前日期
     * @param flag 标志位
     * @return DEResponse
     * @throws ServerException
     */
    public DEResponse getMysqlFeatureByMinute(int serverId, int port, String date, int flag) throws ServerException {
        DEResponse deResponse = new DEResponse();

        //DEFINE THE CONTENT IN THE DE RESPONSE
        List<Map<String, Object>> list = new ArrayList<>();

        //set name
        Map<String, String> name = new HashMap<>();

        //row key
        String rowKey = "";
        try {
            if (flag == 4) {
                name.put("y0", "锁数量");
                name.put("y1", "最大锁时长");
                name.put("y2", "平均锁时长");
                //锁数量HBase行健
                String rowKey_lockCount = serverId + "#" + port + "#83#lock_num#" + date;
                //最大锁时长HBase行健
                String rowKey_maxLockTime = serverId + "#" + port + "#84#lock_top#" + date;
                //平均锁时长HBase行健
                String rowKey_avgLockTime = serverId + "#" + port + "#84#lock_average#" + date;

                NavigableMap<byte[], byte[]> resultMapLockCount =
                        ReportHbaseUtils.getRowFromHbase("omp_mysql_stat_minute", rowKey_lockCount);

                NavigableMap<byte[], byte[]> resultMapMaxLockTime =
                        ReportHbaseUtils.getRowFromHbase("omp_mysql_stat_minute", rowKey_maxLockTime);

                NavigableMap<byte[], byte[]> resultMapAvgLockTime =
                        ReportHbaseUtils.getRowFromHbase("omp_mysql_stat_minute", rowKey_avgLockTime);

                int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                for (int m = 0; m < 1440; m++) {
                    long time = (start + m * 60) * 1000L;
                    String t = sdf.format(new Date(time));
                    Map<String, Object> map = new HashMap<>();
                    byte[] lockCount = null;
                    byte[] maxLockTime = null;
                    byte[] avgLockTime = null;
                    if (resultMapLockCount != null &&  (lockCount = resultMapLockCount.get(Bytes.toBytes(m + ""))) != null) {
                        map.put("y0", extractValue(Bytes.toString(lockCount)));
                    }
                    if (resultMapMaxLockTime != null && (maxLockTime = resultMapMaxLockTime.get(Bytes.toBytes(m + ""))) != null) {
                        map.put("y1", extractValue(Bytes.toString(maxLockTime)));
                    }
                    if (resultMapAvgLockTime != null && (avgLockTime = resultMapAvgLockTime.get(Bytes.toBytes(m + ""))) != null) {
                        map.put("y2", extractValue(Bytes.toString(avgLockTime)));
                    }
                    if (lockCount != null || maxLockTime != null || avgLockTime != null) {
                        map.put("x", t);
                        list.add(map);
                    }
                    
                }


            } else {

                if (flag == 1) {
                    name.put("y0", "打开文件数");
                    rowKey = serverId + "#" + port + "#80#open_files#" + date;
                }

                if (flag == 2) {
                    name.put("y0", "连接数");
                    rowKey = serverId + "#" + port + "#82#mysql_conn#" + date;
                }

                if (flag == 3) {
                    name.put("y0", "慢SQL数");
                    rowKey = serverId + "#" + port + "#85#slow_sql#" + date;
                }

                NavigableMap<byte[], byte[]> resultMap =
                        ReportHbaseUtils.getRowFromHbase("omp_mysql_stat_minute", rowKey);

                int start = DateUtils.getUnixTimestampFromyyyyMMdd(date);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                for (int m = 0; m < 1440; m++) {
                    long time = (start + m * 60) * 1000L;
                    String t = sdf.format(new Date(time));
                    Map<String, Object> map = new HashMap<>();
                    byte[] result;
                    if (resultMap != null && (result = resultMap.get(Bytes.toBytes(m + ""))) != null) {
                        map.put("y0", extractValue(Bytes.toString(result)));
                        map.put("x", t);
                        list.add(map);
                    }

                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        deResponse.setName(name);
        deResponse.setContent(list);
        return deResponse;
    }

    /**
     * 打开文件数(flag=1)|连接数(flag=2)|慢SQL数(flag=3)|锁数量(flag=4)  每小时
     * @param serverId 机器id
     * @param port port
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param flag 标志位
     * @return DEResponse
     * @throws ServerException
     */
    public DEResponse getMysqlFeatureByHour(int serverId, int port, String startDate,
                                            String endDate, int flag) throws ServerException {
        DEResponse response = new DEResponse();

        //DEFINE THE CONTENT IN THE DE RESPONSE
        List<Map<String, Object>> list = new ArrayList<>();

        //set name
        Map<String, String> name = new HashMap<>();

        //row key
        String rowKey = "";

        int start = DateUtils.getUnixTimestampFromyyyyMMdd(startDate);
        int end = DateUtils.getUnixTimestampFromyyyyMMdd(endDate);

        try {
            if (flag == 4) {
                name.put("y0", "锁数量");
                name.put("y1", "最大锁时长");
                name.put("y2", "平均锁时长");

                for (int time = start; time <= end; time += 24 * 60 * 60) {
                    String current = DateUtils.getyyyyMMddFromTimestamp(time);

                    //锁数量HBase行健
                    String rowKey_lockCount = serverId + "#" + port + "#83#lock_num#" + current;
                    //最大锁时长HBase行健
                    String rowKey_maxLockTime = serverId + "#" + port + "#84#lock_top#" + current;
                    //平均锁时长HBase行健
                    String rowKey_avgLockTime = serverId + "#" + port + "#84#lock_average#" + current;

                    NavigableMap<byte[], byte[]> resultMapLockCount =
                            ReportHbaseUtils.getRowFromHbase("omp_mysql_stat_hour", rowKey_lockCount);

                    NavigableMap<byte[], byte[]> resultMapMaxLockTime =
                            ReportHbaseUtils.getRowFromHbase("omp_mysql_stat_hour", rowKey_maxLockTime);

                    NavigableMap<byte[], byte[]> resultMapAvgLockTime =
                            ReportHbaseUtils.getRowFromHbase("omp_mysql_stat_hour", rowKey_avgLockTime);

                    for (int hour = 0; hour < 24; hour++) {
                        String paddingHour = String.format("%02d:00", hour);
                        Map<String, Object> map = new HashMap<String, Object>();

                        byte[] lockCount = null;
                        byte[] maxLockTime = null;
                        byte[] avgLockTime = null;
                        if (resultMapLockCount != null &&  (lockCount = resultMapLockCount.get(Bytes.toBytes(hour + ""))) != null) {
                            map.put("y0", extractValue(Bytes.toString(lockCount)));
                        }
                        if (resultMapMaxLockTime != null && (maxLockTime = resultMapMaxLockTime.get(Bytes.toBytes(hour + ""))) != null) {
                            map.put("y1", extractValue(Bytes.toString(maxLockTime)));
                        }
                        if (resultMapAvgLockTime != null && (avgLockTime = resultMapAvgLockTime.get(Bytes.toBytes(hour + ""))) != null) {
                            map.put("y2", extractValue(Bytes.toString(avgLockTime)));
                        }
                        if (lockCount != null || maxLockTime != null || avgLockTime != null) {
                            map.put("x", current + " " + paddingHour);
                            list.add(map);
                        }
                        
                    }


                }


            } else {

                for (int time = start; time <= end; time += 24 * 60 * 60) {
                    String current = DateUtils.getyyyyMMddFromTimestamp(time);

                    if (flag == 1) {
                        name.put("y0", "打开文件数");
                        rowKey = serverId + "#" + port + "#80#open_files#" + current;
                    }

                    if (flag == 2) {
                        name.put("y0", "连接数");
                        rowKey = serverId + "#" + port + "#82#mysql_conn#" + current;
                    }

                    if (flag == 3) {
                        name.put("y0", "慢SQL数");
                        rowKey = serverId + "#" + port + "#85#slow_sql#" + current;
                    }

                    NavigableMap<byte[], byte[]> resultMap =
                            ReportHbaseUtils.getRowFromHbase("omp_mysql_stat_hour", rowKey);


                    for (int hour = 0; hour < 24; hour++) {
                        String paddingHour = String.format("%02d:00", hour);
                        Map<String, Object> map = new HashMap<String, Object>();
                        byte[] result;
                        if (resultMap != null && (result = resultMap.get(Bytes.toBytes(hour + ""))) != null) {
                            map.put("y0", extractValue(Bytes.toString(result)));
                            map.put("x", current + " " + paddingHour);
                            list.add(map);
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setName(name);
        response.setContent(list);

        return response;
    }

    private int extractValue(String src){
        int dest = 0;

        if (src != null && !"".equals(src)){
            String[] values = src.split("_");
            if (values.length >= 2) {
                dest = Integer.parseInt(values[0]);
            }
        }
        return dest;
    }
}
