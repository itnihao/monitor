package com.dataeye.omp.module.monitor.custom;


import com.dataeye.omp.common.DEResponse;
import com.dataeye.omp.constant.Constant;
import com.dataeye.utils.DateUtils;
import com.dataeye.utils.HbaseProxyClient;
import com.dataeye.utils.ServerUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class CustomMonitorReportDAO {

    /**
     * 获取单日失败率
     * @param rowKey
     * @param time
     * @return
     */
    public DEResponse getFailedRateToday(String rowKey, String time){
        DEResponse response = new DEResponse();

        List<Map<String, Object>> list = new ArrayList<>();
        //set name
        Map<String, String> name = new HashMap<>();
        name.put("y0", "失败率");
        name.put("y1", "调用次数");
        response.setName(name);

        try {
            Result rs = HbaseProxyClient.getOneRecord(Constant.Table.OMP_CUSTOM_MONITOR_STAT, rowKey);
            NavigableMap<byte[], byte[]> nMap = rs.getFamilyMap(Bytes.toBytes(Constant.Table.FAMILY));

            int start = DateUtils.getUnixTimestampFromyyyyMMdd(time);
            SimpleDateFormat s = new SimpleDateFormat("HH:mm");
            if (nMap != null) {
                for (int m = 0; m < 1440; m += 5) {
                    String timeStr = s.format(new Date((start + 60 * m) * 1000L));
                    byte[] successTimeQualifier = Bytes.toBytes("succTimes#" + m);
                    byte[] failTimeQualifier = Bytes.toBytes("failTimes#" + m);
                    int st = 0;
                    if (nMap.get(successTimeQualifier) != null) {
                         st = Bytes.toInt(nMap.get(successTimeQualifier));
                    }

                    int ft = 0;
                    if (nMap.get(failTimeQualifier) != null) {
                        ft = Bytes.toInt(nMap.get(failTimeQualifier));
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("x", timeStr);
                    if (st + ft > 0) {
                        double ratio = new Double(ft) / new Double(ft + st);
                        map.put("y0", ServerUtils.format(ratio * 100));
                        map.put("y1", st + ft);
                    } else {
                        map.put("y0", 0);
                        map.put("y1", 0);
                    }
                    list.add(map);
                }
            }
            response.setContent(list);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }


    /**
     * 查询对比失败率
     * @param rowKey1
     * @param rowKey2
     * @param currentDate
     * @param comparedDate
     * @return
     */
    public DEResponse getFailedRateComparedDate(String rowKey1, String rowKey2,
                                                String currentDate, String comparedDate) {
        DEResponse response = new DEResponse();

        List<Map<String, Object>> list = new ArrayList<>();
        //set name
        Map<String, String> name = new HashMap<>();

        name.put("y0", currentDate + "#" + "失败率");
        name.put("y1", comparedDate + "#" + "失败率");
        name.put("y2", currentDate + "#" + "调用次数");
        name.put("y3", comparedDate + "#" + "调用次数");
        response.setName(name);

        try {
            Result rsCurrent =
                    HbaseProxyClient.getOneRecord(Constant.Table.OMP_CUSTOM_MONITOR_STAT, rowKey1);
            Result rsCompared =
                    HbaseProxyClient.getOneRecord(Constant.Table.OMP_CUSTOM_MONITOR_STAT, rowKey2);
            NavigableMap<byte[], byte[]> mapCurrent = rsCurrent.getFamilyMap(Bytes.toBytes(Constant.Table.FAMILY));
            NavigableMap<byte[], byte[]> mapCompared = rsCompared.getFamilyMap(Bytes.toBytes(Constant.Table.FAMILY));

            int start = DateUtils.getUnixTimestampFromyyyyMMdd(currentDate);
            SimpleDateFormat s = new SimpleDateFormat("HH:mm");
            if (mapCurrent != null || mapCompared != null) {
                for (int m = 0; m < 1440; m += 5) {
                    String timeStr = s.format(new Date((start + 60 * m) * 1000L));
                    byte[] successTimeQualifier = Bytes.toBytes("succTimes#" + m);
                    byte[] failTimeQualifier = Bytes.toBytes("failTimes#" + m);

                    int stCurrent = 0;
                    int ftCurrent = 0;
                    if (mapCurrent != null) {
                        if (mapCurrent.get(successTimeQualifier) != null) {
                            stCurrent = Bytes.toInt(mapCurrent.get(successTimeQualifier));
                        }

                        if (mapCurrent.get(failTimeQualifier) != null) {
                            ftCurrent = Bytes.toInt(mapCurrent.get(failTimeQualifier));
                        }
                    }

                    int ftCompared = 0;
                    int stCompared = 0;

                    if (mapCompared != null) {
                        if (mapCompared.get(failTimeQualifier) != null) {
                            ftCompared = Bytes.toInt(mapCompared.get(failTimeQualifier));
                        }

                        if (mapCompared.get(successTimeQualifier) != null) {
                            stCompared = Bytes.toInt(mapCompared.get(successTimeQualifier));
                        }
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("x", timeStr);
                    if (ftCurrent + stCurrent > 0) {
                        double ratioCurrent = new Double(ftCurrent)
                                / new Double((ftCurrent + stCurrent));
                        map.put("y0", ServerUtils.format(ratioCurrent * 100));
                    } else {
                        map.put("y0", 0 );
                    }

                    if (ftCompared + stCompared > 0) {
                        double ratioCompared = new Double(ftCompared)
                                / new Double((ftCompared + stCompared));
                        map.put("y1", ServerUtils.format(ratioCompared * 100));
                    } else {
                        map.put("y1", 0);
                    }

                    map.put("y2", ftCurrent + stCurrent);
                    map.put("y3", ftCompared + stCompared);
                    list.add(map);
                }
            }
            response.setContent(list);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }


    /**
     * 获取单日平均总耗时
     * @param rowKey
     * @param time
     * @return
     */
    public DEResponse getAverageTimeCostToday(String rowKey,String time){
        DEResponse response = new DEResponse();
        List<Map<String, Object>> list = new ArrayList<>();
        //set name
        Map<String, String> name = new HashMap<>();

        name.put("y0","平均总耗时");
        name.put("y1", "调用次数");
        response.setName(name);

        try {
            Result rs = HbaseProxyClient.getOneRecord(Constant.Table.OMP_CUSTOM_MONITOR_STAT, rowKey);
            NavigableMap<byte[], byte[]> nMap = rs.getFamilyMap(Bytes.toBytes(Constant.Table.FAMILY));

            int start = DateUtils.getUnixTimestampFromyyyyMMdd(time);
            SimpleDateFormat s = new SimpleDateFormat("HH:mm");
            if (nMap != null) {
                for (int m = 0; m < 1440; m += 5) {
                    String timeStr = s.format(new Date((start + 60 * m) * 1000L));
                    byte[] successTimeQualifier = Bytes.toBytes("succTimes#" + m);
                    byte[] failTimeQualifier = Bytes.toBytes("failTimes#" + m);
                    byte[] successCostTimeQualifier = Bytes.toBytes("costSucc#" + m);
                    byte[] failCostTimeQualifier = Bytes.toBytes("costFail#" + m);

                    int st = 0;
                    if (nMap.get(successTimeQualifier) != null) {
                        st = Bytes.toInt(nMap.get(successTimeQualifier));
                    }

                    int ft = 0;
                    if (nMap.get(failTimeQualifier) != null) {
                        ft = Bytes.toInt(nMap.get(failTimeQualifier));
                    }

                    long sct = 0;
                    if (nMap.get(successCostTimeQualifier) != null) {
                        sct = Bytes.toLong(nMap.get(successCostTimeQualifier));
                    }

                    long fct = 0;
                    if (nMap.get(failCostTimeQualifier) != null) {
                        fct = Bytes.toLong(nMap.get(failCostTimeQualifier));
                    }

                    Map<String, Object> map = new HashMap<>();

                    map.put("x", timeStr);

                    if ((st + ft) > 0) {
                        double cost = new Double(sct + fct)
                                / new Double(st + ft);
                        map.put("y0", ServerUtils.format(cost));

                    } else {
                        map.put("y0", 0);
                    }

                    map.put("y1", st + ft);
                    list.add(map);
                }

            }
            response.setContent(list);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }


    /**
     * 获取总平均耗时对比图
     * @param rowKey1
     * @param rowKey2
     * @param currentDate
     * @param comparedDate
     * @return
     */
    public DEResponse getAverageTimeCostCompared(String rowKey1, String rowKey2,
                                                 String currentDate, String comparedDate) {
        DEResponse response = new DEResponse();

        List<Map<String, Object>> list = new ArrayList<>();
        //set name
        Map<String, String> name = new HashMap<>();

        name.put("y0", currentDate+"#"+"平均耗时");
        name.put("y1", comparedDate + "#" + "平均耗时");
        name.put("y2", currentDate+"#"+"调用次数");
        name.put("y3", comparedDate + "#" + "调用次数");

        response.setName(name);

        try {
            Result rsCurrent = HbaseProxyClient.getOneRecord(Constant.Table.OMP_CUSTOM_MONITOR_STAT, rowKey1);
            Result rsCompared = HbaseProxyClient.getOneRecord(Constant.Table.OMP_CUSTOM_MONITOR_STAT, rowKey2);

            NavigableMap<byte[], byte[]> mapCurrent = rsCurrent.getFamilyMap(Bytes.toBytes(Constant.Table.FAMILY));
            NavigableMap<byte[], byte[]> mapCompared = rsCompared.getFamilyMap(Bytes.toBytes(Constant.Table.FAMILY));

            int start = DateUtils.getUnixTimestampFromyyyyMMdd(currentDate);
            SimpleDateFormat s = new SimpleDateFormat("HH:mm");
            if (mapCompared != null || mapCurrent != null) {
                for (int m = 0; m < 1440; m += 5) {
                    String timeStr = s.format(new Date((start + 60 * m) * 1000L));
                    byte[] successTimeQualifier = Bytes.toBytes("succTimes#" + m);
                    byte[] failTimeQualifier = Bytes.toBytes("failTimes#" + m);
                    byte[] successCostTimeQualifier = Bytes.toBytes("costSucc#" + m);
                    byte[] failCostTimeQualifier = Bytes.toBytes("costFail#" + m);

                    int stCurrent = 0;
                    int ftCurrent = 0;
                    long sctCurrent = 0;
                    long fctCurrent = 0;
                    if (mapCurrent != null) {
                        if (mapCurrent.get(successTimeQualifier) != null) {
                            stCurrent = Bytes.toInt(mapCurrent.get(successTimeQualifier));
                        }

                        if (mapCurrent.get(failTimeQualifier) != null) {
                            ftCurrent = Bytes.toInt(mapCurrent.get(failTimeQualifier));
                        }

                        if (mapCurrent.get(successCostTimeQualifier) != null) {
                            sctCurrent = Bytes.toLong(mapCurrent.get(successCostTimeQualifier));
                        }

                        if (mapCurrent.get(failCostTimeQualifier) != null) {
                            fctCurrent = Bytes.toLong(mapCurrent.get(failCostTimeQualifier));
                        }
                    }

                    int stCompared = 0;
                    int ftCompared = 0;
                    long sctCompared = 0;
                    long fctCompared = 0;
                    if (mapCompared != null) {
                        if (mapCompared.get(successTimeQualifier) != null) {
                            stCompared = Bytes.toInt(mapCompared.get(successTimeQualifier));
                        }

                        if (mapCompared.get(failTimeQualifier) != null) {
                            ftCompared = Bytes.toInt(mapCompared.get(failTimeQualifier));
                        }

                        if (mapCompared.get(successCostTimeQualifier) != null) {
                            sctCompared = Bytes.toLong(mapCompared.get(successCostTimeQualifier));
                        }

                        if (mapCompared.get(failCostTimeQualifier) != null) {
                            fctCompared = Bytes.toLong(mapCompared.get(failCostTimeQualifier));
                        }
                    }

                    Map<String, Object> map = new HashMap<>();

                    if (stCurrent + ftCurrent > 0) {
                        double costCurrent = new Double(sctCurrent + fctCurrent)
                                / new Double(stCurrent + ftCurrent);
                        map.put("y0", ServerUtils.format(costCurrent));
                    } else {
                        map.put("y0", 0);
                    }

                    if (stCompared + ftCompared > 0) {
                        double costCompared = new Double(sctCompared + fctCompared)
                                / new Double(stCompared + ftCompared);
                        map.put("y1", ServerUtils.format(costCompared));
                    } else {
                        map.put("y1", 0);
                    }

                    map.put("x", timeStr);
                    map.put("y2", stCurrent + ftCurrent);
                    map.put("y3", stCompared + ftCompared);
                    list.add(map);
                }
            }
            response.setContent(list);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
