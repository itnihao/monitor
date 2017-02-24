package com.dataeye.omp.custom.monitor.consume;


import com.dataeye.omp.common.Constant;
import com.dataeye.omp.dbproxy.hbase.HbasePool;
import com.dataeye.omp.dbproxy.hbase.HbaseProxyClient;
import com.dataeye.omp.utils.DateUtils;
import com.dataeye.util.log.DELogger;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

/**
 * @author wendy
 * @since 2016/3/17 11:38
 */
public class CustomMonitorHandler implements Runnable {
    private KafkaStream<String, String> stream;
    private final static Logger monitorLog = DELogger.getLogger("monitor_log");
    public final static String SEPARATOR = "#";
    public  CustomMonitorHandler (KafkaStream<String, String> stream) {
        this.stream = stream;
    }
    public static final int SUCC = 0;
    public static final String COSTSUCC = "costSucc";
    public static final String COSTFAIL = "costFail";

    public static final String SUCCTIMES = "succTimes";

    public static final String FAILTIMES = "failTimes";



    @Override
    public void run() {
        try {
            for (MessageAndMetadata<String, String> kafkaMsg : stream) {
                String message = kafkaMsg.message();

                monitorLog.info(Thread.currentThread().getName() +
                        ": partition[" + kafkaMsg.partition() + "],"
                        + "offset[" + kafkaMsg.offset() + "], "
                        + kafkaMsg.message());
                CustomMonitorItem item =
                        new CustomMonitorItem(message);

                String caller = item.getCaller();
                String receiver = item.getReceiver();
                if (StringUtils.isEmpty(caller) ||
                        StringUtils.isEmpty(receiver)) {
                    monitorLog.info("invalid monitor call, do nothing");
                    return;
                }

                /**保存自定义监控调用参数*/
                saveMonitorParam(item);

                /**计算并保存每五分钟内，
                 * 监控项调用的耗时和次数*/
                saveMonitorState(item);
            }
        } catch (Exception e) {
            monitorLog.error("custom monitor error", e);
        }
    }


    /**
     *  保存自定义监控调用参数
     *  rowKey:monitorItem
     *  qualifier:caller#receiver#ext1#ext2
     * @param item CustomMonitorItem
     */
    private void saveMonitorParam(CustomMonitorItem item) throws IOException {
        String monitorItem = item.getMonitorItem();
        String caller = item.getCaller();
        String receiver = item.getReceiver();
        String ext1 = item.getExt1();
        String ext2 = item.getExt2();

        String qualifier = caller + SEPARATOR + receiver + SEPARATOR
                + ext1 + SEPARATOR + ext2;

        HbaseProxyClient.addRecord(Constant.TABLE_CUSTOM_MONITOR_OBJECT, monitorItem,
                Constant.COLUMNFAMILY, qualifier, item.getTime());
    }


    /**
     * 保存每五分钟内，监控项调用的耗时和次数
     * @param item CustomMonitorItem
     */
    private void saveMonitorState(CustomMonitorItem item)
            throws ParseException, IOException {

        String caller = item.getCaller();
        String receiver = item.getReceiver();
        String ext1 = item.getExt1();
        String ext2 = item.getExt2();

        if (StringUtils.isEmpty(caller) ||
                StringUtils.isEmpty(receiver)) {
            monitorLog.info("invalid monitor call, do nothing");
        } else {
            if ((StringUtils.isNotEmpty(ext1)
                    && StringUtils.isEmpty(ext2))) {
                /**caller|receiver|ext1*/
                updateRow0ToRow3(item);
                updateRow4ToRow7(item);

            } else if (StringUtils.isEmpty(ext1)
                    && StringUtils.isNotEmpty(ext2)) {
                /**caller|receiver|null|ext2*/
                updateRow0ToRow3(item);
                updateRow8ToRow11(item);

            } else if (StringUtils.isNotEmpty(ext1)
                    && StringUtils.isNotEmpty(ext2)) {
                /**caller|receiver|ext1|ext2*/
                updateRow0ToRow3(item);
                updateRow4ToRow7(item);
                updateRow8ToRow11(item);
                updateRow12ToRow15(item);
            }
        }
    }

    /**
     * rowKey1 ${monitorItem}#{time}#${caller}###
     * rowKey2 ${monitorItem}#{time}##${receive}##
     * rowKey3 ${monitorItem}#{time}#${caller}#${receive}##
     *
     * @param item CustomMonitorItem
     * @throws IOException
     * @throws ParseException
     */
    private void updateRow0ToRow3(CustomMonitorItem item)
            throws IOException, ParseException {

        String monitorItem = item.getMonitorItem();
        String caller = item.getCaller();
        String receiver = item.getReceiver();
        long cost = item.getCost();
        int isSucc = item.getIsSucc();

        String time = item.getTime();
        int minute = DateUtils.getMinuteBetweenDay(time);
        int columnSuffix = (minute / 5 + 1) * 5;
        String currentTime = time.substring(0, 10);

        String rowPrefix = monitorItem + SEPARATOR +
                currentTime + SEPARATOR;

        String rowKey0 = rowPrefix + SEPARATOR +
                SEPARATOR + SEPARATOR;
        updateOneRow(rowKey0, cost, isSucc, columnSuffix);

        String rowKey1 = rowPrefix + caller + SEPARATOR +
                SEPARATOR + SEPARATOR;
        updateOneRow(rowKey1, cost, isSucc, columnSuffix);

        String rowKey2 = rowPrefix + SEPARATOR +
                receiver + SEPARATOR + SEPARATOR;
        updateOneRow(rowKey2, cost, isSucc, columnSuffix);

        String rowKey3 = rowPrefix + caller + SEPARATOR +
                receiver + SEPARATOR + SEPARATOR;
        updateOneRow(rowKey3, cost, isSucc, columnSuffix);
    }

    /**
     * rowKey4 ${monitorItem}#{time}#${caller}##${ext1}#
     * rowKey5 ${monitorItem}#{time}##${receive}#${ext1}#
     * rowKey6 ${monitorItem}#{time}###${ext1}#
     * rowKey7 ${monitorItem}#{time}#${caller}#${receive}#${ext1}#
     *
     * @param item CustomMonitorItem
     * @throws IOException
     * @throws ParseException
     */
    private void updateRow4ToRow7(CustomMonitorItem item)
            throws IOException, ParseException {

        String monitorItem = item.getMonitorItem();
        String caller = item.getCaller();
        String receiver = item.getReceiver();
        String ext1 = item.getExt1();
        long cost = item.getCost();
        int isSucc = item.getIsSucc();

        String time = item.getTime();
        int minute = DateUtils.getMinuteBetweenDay(time);
        int columnSuffix = (minute / 5 + 1) * 5;
        String currentTime = time.substring(0, 10);

        String rowPrefix = monitorItem + SEPARATOR +
                currentTime + SEPARATOR;

        String rowKey4 = rowPrefix + caller + SEPARATOR +
                SEPARATOR + ext1 + SEPARATOR;
        updateOneRow(rowKey4, cost, isSucc, columnSuffix);

        String rowKey5 = rowPrefix + SEPARATOR +
                receiver + SEPARATOR + ext1 + SEPARATOR;
        updateOneRow(rowKey5, cost, isSucc, columnSuffix);

        String rowKey6 = rowPrefix + SEPARATOR +
                SEPARATOR + ext1 + SEPARATOR;
        updateOneRow(rowKey6, cost, isSucc, columnSuffix);

        String rowKey7 = rowPrefix + caller + SEPARATOR +
                receiver + SEPARATOR + ext1 + SEPARATOR;
        updateOneRow(rowKey7, cost, isSucc, columnSuffix);

    }

    /**
     * rowKey8 ${monitorItem}#{time}#${caller}###${ext2}
     * rowKey9 ${monitorItem}#{time}##${receive}##${ext2}
     * rowKey10 ${monitorItem}#{time}####${ext2}
     * rowKey11 ${monitorItem}#{time}#${caller}#${receive}##${ext2}
     *
     * @param item CustomMonitorItem
     * @throws IOException
     * @throws ParseException
     */
    private void updateRow8ToRow11(CustomMonitorItem item)
            throws IOException, ParseException {
        String monitorItem = item.getMonitorItem();
        String caller = item.getCaller();
        String receiver = item.getReceiver();
        String ext2 = item.getExt2();
        long cost = item.getCost();
        int isSucc = item.getIsSucc();

        String time = item.getTime();
        int minute = DateUtils.getMinuteBetweenDay(time);
        int columnSuffix = (minute / 5 + 1) * 5;
        String currentTime = time.substring(0, 10);

        String rowPrefix = monitorItem + SEPARATOR +
                currentTime + SEPARATOR;

        String rowKey8 = rowPrefix + caller + SEPARATOR +
                SEPARATOR + SEPARATOR + ext2;
        updateOneRow(rowKey8, cost, isSucc, columnSuffix);

        String rowKey9 = rowPrefix + SEPARATOR +
                receiver + SEPARATOR + SEPARATOR + ext2;
        updateOneRow(rowKey9, cost, isSucc, columnSuffix);

        String rowKey10 = rowPrefix + SEPARATOR +
                SEPARATOR + SEPARATOR + ext2;
        updateOneRow(rowKey10, cost, isSucc, columnSuffix);

        String rowKey11 = rowPrefix + caller + SEPARATOR +
                receiver + SEPARATOR + SEPARATOR + ext2;
        updateOneRow(rowKey11, cost, isSucc, columnSuffix);
    }

    /**
     * rowKey12 ${monitorItem}#{time}#${caller}##${ext1}#${ext2}
     * rowKey13 ${monitorItem}#{time}##${receive}#${ext1}#${ext2}
     * rowKey14 ${monitorItem}#{time}###${ext1}#${ext2}
     * rowKey15 ${monitorItem}#{time}#${caller}#${receive}#${ext1}#${ext2}
     *
     * @param item CustomMonitorItem
     * @throws IOException
     * @throws ParseException
     */
    private void updateRow12ToRow15(CustomMonitorItem item)
            throws IOException, ParseException {

        String monitorItem = item.getMonitorItem();
        String caller = item.getCaller();
        String receiver = item.getReceiver();
        String ext1 = item.getExt1();
        String ext2 = item.getExt2();
        long cost = item.getCost();
        int isSucc = item.getIsSucc();

        String time = item.getTime();
        int minute = DateUtils.getMinuteBetweenDay(time);
        int columnSuffix = (minute / 5 + 1) * 5;
        String currentTime = time.substring(0, 10);

        String rowPrefix = monitorItem + SEPARATOR +
                currentTime + SEPARATOR;


        String rowKey12 = rowPrefix + caller + SEPARATOR +
                SEPARATOR + ext1 + SEPARATOR + ext2;
        updateOneRow(rowKey12, cost, isSucc, columnSuffix);

        String rowKey13 = rowPrefix + SEPARATOR +
                receiver + SEPARATOR + ext1 + SEPARATOR + ext2;
        updateOneRow(rowKey13, cost, isSucc, columnSuffix);

        String rowKey14 = rowPrefix + SEPARATOR +
                SEPARATOR + ext1 + SEPARATOR + ext2;
        updateOneRow(rowKey14, cost, isSucc, columnSuffix);

        String rowKey15 = rowPrefix + caller + SEPARATOR +
                receiver + SEPARATOR + ext1 + SEPARATOR + ext2;
        updateOneRow(rowKey15, cost, isSucc, columnSuffix);
    }


    /**
     * ${costSucc}#每五分钟      ${value}
     * ${costFail}#每五分钟	    ${value}
     * ${succTimes}#每五分钟	    ${value}
     * ${failTimes}#每五分钟	    ${value}
     *
     * @param rowKey       行
     * @param cost         耗时
     * @param isSucc       0 成功 1 失败
     * @param columnSuffix 列前缀
     * @throws IOException
     */
    public void updateOneRow(String rowKey, long cost,
                             int isSucc, int columnSuffix) throws IOException {
        Result rs = HbaseProxyClient.getOneRecord(Constant.TABLE_CUSTOM_MONITOR_STATE,
                rowKey);
        NavigableMap<byte[], byte[]> navigableMap =
                rs.getFamilyMap(Bytes.toBytes(Constant.COLUMNFAMILY));

        List<Put> puts = new ArrayList<>();
        Put put = new Put(Bytes.toBytes(rowKey));
        if (SUCC == isSucc) {
            String column1 = COSTSUCC + SEPARATOR + columnSuffix;
            String column3 = SUCCTIMES + SEPARATOR + columnSuffix;
            if (navigableMap == null) {
                put.add(Bytes.toBytes(Constant.COLUMNFAMILY),
                        Bytes.toBytes(column1), Bytes.toBytes(cost));
                put.add(Bytes.toBytes(Constant.COLUMNFAMILY),
                        Bytes.toBytes(column3), Bytes.toBytes(1));
            } else {
                byte[] succCost = navigableMap.get(Bytes.toBytes(column1));
                if (succCost == null) {
                    put.add(Bytes.toBytes(Constant.COLUMNFAMILY),
                            Bytes.toBytes(column1), Bytes.toBytes(cost));
                } else {
                    //TODO 会不会存在并发问题？
                    put.add(Bytes.toBytes(Constant.COLUMNFAMILY),
                            Bytes.toBytes(column1),
                            Bytes.toBytes(Bytes.toLong(succCost) + cost));
                }

                byte[] succTimes = navigableMap.get(Bytes.toBytes(column3));
                if (succTimes == null) {
                    put.add(Bytes.toBytes(Constant.COLUMNFAMILY),
                            Bytes.toBytes(column3), Bytes.toBytes(1));
                } else {
                    put.add(Bytes.toBytes(Constant.COLUMNFAMILY),
                            Bytes.toBytes(column3),
                            Bytes.toBytes(Bytes.toInt(succTimes) + 1));
                }
            }
        } else {
            String column2 = COSTFAIL + SEPARATOR + columnSuffix;
            String column4 = FAILTIMES + SEPARATOR + columnSuffix;
            if (navigableMap == null) {
                put.add(Bytes.toBytes(Constant.COLUMNFAMILY),
                        Bytes.toBytes(column2), Bytes.toBytes(cost));

                put.add(Bytes.toBytes(Constant.COLUMNFAMILY),
                        Bytes.toBytes(column4), Bytes.toBytes(1));
                puts.add(put);
            } else {

                byte[] failCosts = navigableMap.get(Bytes.toBytes(column2));
                if (failCosts == null) {
                    put.add(Bytes.toBytes(Constant.COLUMNFAMILY),
                            Bytes.toBytes(column2), Bytes.toBytes(cost));
                } else {
                    put.add(Bytes.toBytes(Constant.COLUMNFAMILY),
                            Bytes.toBytes(column2),
                            Bytes.toBytes(Bytes.toLong(failCosts) + cost));
                }

                byte[] failTimes = navigableMap.get(Bytes.toBytes(column4));
                if (failTimes == null) {
                    put.add(Bytes.toBytes(Constant.COLUMNFAMILY),
                            Bytes.toBytes(column4), Bytes.toBytes(1));
                } else {
                    put.add(Bytes.toBytes(Constant.COLUMNFAMILY),
                            Bytes.toBytes(column4),
                            Bytes.toBytes(Bytes.toInt(failTimes) + 1));
                }
            }
        }
        puts.add(put);
        saveMonitorData(puts, Constant.TABLE_CUSTOM_MONITOR_STATE);
    }

    private void saveMonitorData(List<Put> puts
            , String tableName) {
        HTableInterface table = null;
        try {
            table = HbasePool.getConnection().getTable(tableName);
            table.setAutoFlush(false);
            table.batch(puts);
        } catch (Exception e) {
            monitorLog.error("save custom Monitor to habse error"
                    + ", table:" + tableName, e);
        } finally {
            HbasePool.close(table);
        }
    }


    public static void main(String[] args) {
        byte[] b = {0, 0, 0, 10};
        Integer v = Bytes.toInt(b);
        System.out.println(v);
    }
}
