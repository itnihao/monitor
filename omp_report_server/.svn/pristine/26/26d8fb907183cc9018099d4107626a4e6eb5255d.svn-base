package com.dataeye.omp.report.cmds;

import com.dataeye.omp.report.dbproxy.hbase.HbaseBatchPutHandler;
import com.dataeye.omp.report.dbproxy.hbase.HbaseProxyClient;
import com.dataeye.omp.report.dbproxy.hbase.PutItem;
import com.dataeye.omp.report.dbproxy.kafka.KafkaProducerAgent;
import com.dataeye.omp.report.utils.Constant;
import com.dataeye.omp.report.utils.DateUtils;
import com.dataeye.omp.report.utils.ServerIDCache;
import com.dataeye.util.log.DELogger;
import com.google.gson.Gson;
import com.qq.jutil.string.StringUtil;
import com.xunlei.netty.httpserver.cmd.BaseCmd;
import com.xunlei.netty.httpserver.cmd.CmdMapper;
import com.xunlei.netty.httpserver.component.XLHttpRequest;
import com.xunlei.netty.httpserver.component.XLHttpResponse;
import com.xunlei.util.Log;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author wendy
 * @since 2016/3/31 14:06
 */
@Service
public class ReportMysqlCmd extends BaseCmd {

    private Logger logger = DELogger.getLogger("mysql_log");
    private final static Logger testLog = Log.getLogger("test_log");

    private static final String SEPERATER = "_";

    private static final String DEFAULT_SEPERATER = ",";


    @CmdMapper("/report/mysql")
    public Object mysqlReport(XLHttpRequest request
            , XLHttpResponse response) {
        String content = request.getContentString().trim();
        if (StringUtil.isEmpty(content)) {
            logger.error("unsupported empty message!" + content);
            return "fail";
        }

        MysqlMsg mysqlMsgs = null;
        try {
            mysqlMsgs = new Gson().fromJson(content, MysqlMsg.class);
        } catch (Exception e) {
            logger.error("parse content error, content is : " + content);
            return "fail";
        }

        List<MysqlMsg> featureList = mysqlMsgs.getFeature_list();
        if (null == featureList || featureList.size() == 0) {
            logger.error(" featureList is empty! " + content);
            return "fail";
        }

        MysqlMsg param = featureList.get(0);

        if ("192.168.1.209".equals(param.getClient())) {
            testLog.info(content);
        }

        //将消息发到kafka,用于告警
        KafkaProducerAgent.pushMassage2Kafka(content, Constant.MYSQL_TOPIC);

        for (MysqlMsg msg : featureList) {
            Integer fid = msg.getFid();
            //锁时长 求最大值，平均数，中位数
            if (fid != Constant.LOCKTIME) {
                saveOneMinuteState(msg);
                saveOneHourState(msg);
                saveCurrentState(msg);
            } else {
                String value = msg.getValue();
                if (StringUtil.isEmpty(value)) {
                    msg.setValue(String.valueOf(0));
                } else {
                    String[] vs = value.split(DEFAULT_SEPERATER);
                    int lockTimeTop = 0;
                    int lockTimeTotal = 0;

                    for (String v : vs) {
                        if (Integer.parseInt(v) > lockTimeTop) {
                            lockTimeTop = Integer.parseInt(v);
                        }
                        lockTimeTotal += Integer.parseInt(v);
                    }

                    //保存锁时长最大值
                    msg.setObject(Constant.LOCK_TIME_TOP);
                    msg.setValue(String.valueOf(lockTimeTop));
                    saveOneMinuteState(msg);
                    saveOneHourState(msg);
                    saveCurrentState(msg);

                    //保存锁时长平均值
                    int lockTimeAverage = lockTimeTotal / vs.length;
                    msg.setObject(Constant.LOCK_TIME_AVERAGE);
                    msg.setValue(String.valueOf(lockTimeAverage));
                    saveOneMinuteState(msg);
                    saveOneHourState(msg);
                    saveCurrentState(msg);
                }
            }
        }
        return "succ";
    }

    /**
     * mysql特性一分钟统计
     *
     * @param msg
     * @throws ParseException
     * @throws IOException
     */
    private void saveOneMinuteState(MysqlMsg msg) {
        Integer fid = msg.getFid();
        String obj = msg.getObject();
        String client = msg.getClient();
        String time = msg.getTime();
        Integer port = msg.getPort();
        int devId = ServerIDCache.getServerId(client);
        int currentMinute = DateUtils.getMinuteBetweenDay(time);

        String tableName = Constant.TABLE_MYSQL_MONITOR_MINUTE;
        String value = msg.getValue() + SEPERATER + DateUtils.unixTimestamp();
        String rowKey = devId + Constant.SEPARATER
                + port + Constant.SEPARATER
                + fid + Constant.SEPARATER
                + obj + Constant.SEPARATER
                + DateUtils.formatyyyyMMdd(time);

        Put put = new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(Constant.COLUMN_FAMILY)
                , Bytes.toBytes(String.valueOf(currentMinute))
                , Bytes.toBytes(value));
        HbaseBatchPutHandler.saveItem(new PutItem(tableName, put));
    }

    /**
     * mysql特性一小时统计
     *
     * @param msg
     * @throws IOException
     * @throws ParseException
     */
    private void saveOneHourState(MysqlMsg msg) {
        Integer fid = msg.getFid();
        String obj = msg.getObject();
        String client = msg.getClient();
        String time = msg.getTime();
        Integer port = msg.getPort();

        int devId = ServerIDCache.getServerId(client);

        String rowKey = devId + Constant.SEPARATER + port +
                Constant.SEPARATER + fid + Constant.SEPARATER + obj + Constant.SEPARATER
                + DateUtils.formatyyyyMMdd(time);

        Result rs = null;
        try {
            rs = HbaseProxyClient.getOneRecord(Constant.TABLE_MYSQL_MONITOR_HOUR,
                    rowKey);
        } catch (IOException e) {
            logger.error("get One Record from omp_mysql_hour_stat error! ", e.getMessage());
            throw new RuntimeException(e);
        }
        String tableName = Constant.TABLE_MYSQL_MONITOR_HOUR;
        int currentHour = DateUtils.getHourBetweenDay(time);
        byte[] b = rs.getValue(Bytes.toBytes(Constant.COLUMN_FAMILY),
                Bytes.toBytes(currentHour));
        String value = msg.getValue() + SEPERATER + DateUtils.unixTimestamp();

        if (null == b) {

            Put put = new Put(Bytes.toBytes(rowKey));
            put.add(Bytes.toBytes(Constant.COLUMN_FAMILY)
                    , Bytes.toBytes(String.valueOf(currentHour))
                    , Bytes.toBytes(value));
            HbaseBatchPutHandler.saveItem(new PutItem(tableName, put));
        } else {
            long v = Long.parseLong(Bytes.toString(b).split(SEPERATER)[0]);
            if (Long.parseLong(msg.getValue()) > v) {
                Put put = new Put(Bytes.toBytes(rowKey));
                put.add(Bytes.toBytes(Constant.COLUMN_FAMILY)
                        , Bytes.toBytes(String.valueOf(currentHour))
                        , Bytes.toBytes(value));
                HbaseBatchPutHandler.saveItem(new PutItem(tableName, put));
            }
        }
    }

    /**
     * mysql特性当前值统计
     *
     * @param msg
     * @throws ParseException
     * @throws IOException
     */
    private void saveCurrentState(MysqlMsg msg) {
        Integer fid = msg.getFid();
        String obj = msg.getObject();
        String client = msg.getClient();
        int port = msg.getPort();
        int devId = ServerIDCache.getServerId(client);
        String tableName = Constant.TABLE_MYSQL_MONITOR_CURRENT;
        String rowKey = devId + Constant.SEPARATER + port;
        String value = msg.getValue() + SEPERATER + DateUtils.unixTimestamp();
        String currQuaifier = fid + Constant.SEPARATER + obj;
        Put put = new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(Constant.COLUMN_FAMILY)
                , Bytes.toBytes(String.valueOf(currQuaifier))
                , Bytes.toBytes(value));
        HbaseBatchPutHandler.saveItem(new PutItem(tableName, put));
    }

    class MysqlMsg {

        private List<MysqlMsg> feature_list;

        /**
         * 特性id
         */
        private int fid;

        /**
         * 监控的对象
         */
        private String object;

        /**
         * mysql实例唯一标识
         */
        private String server_id;

        /**
         * 端口
         */
        private int port;

        /**
         * 所在的server内网IP
         */
        private String client;

        /**
         * 值
         */
        private String value;

        /**
         * 时间戳
         */
        private String time;

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getServer_id() {
            return server_id;
        }

        public void setServer_id(String server_id) {
            this.server_id = server_id;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public List<MysqlMsg> getFeature_list() {
            return feature_list;
        }

        public void setFeature_list(List<MysqlMsg> feature_list) {
            this.feature_list = feature_list;
        }

    }


}
