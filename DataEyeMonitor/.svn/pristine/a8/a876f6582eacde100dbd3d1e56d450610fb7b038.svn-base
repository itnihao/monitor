package com.dataeye.jobs;

import com.dataeye.exception.ServerException;
import com.dataeye.omp.constant.Constant;
import com.dataeye.utils.ApplicationContextUtil;
import com.dataeye.utils.DateUtils;
import com.dataeye.utils.HbaseProxyClient;
import com.dataeye.utils.HttpClientUtil;
import com.google.gson.annotations.Expose;
import com.qq.jutil.string.StringUtil;
import com.xunlei.jdbc.JdbcTemplate;
import com.xunlei.jdbc.RowCallbackHandler;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author wendy
 * @since 2016/8/3
 */
public class CheckReportInfo implements Job {

    final JdbcTemplate jdbcTemplate = ApplicationContextUtil
            .getBeanJdbcTemplateMonitor();

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
            dojob();
    }

    public Object dojob() {
        List<ReportRecord> list = getServerList();
        getReportData(list);
        List<ReportRecord> mailList = filterUnReportedDataTenMinute(list);
        String mailContent = generateMailContent(mailList);
        sendMail(mailContent);
        return list;
    }

    private String generateMailContent(List<ReportRecord> mailList) {
        StringBuffer mailInfo = new StringBuffer();
        for (ReportRecord rr : mailList) {
            Map<String, String> fmap = rr.getFeatureMap();
            if (!fmap.isEmpty() && fmap.size() > 10) {
                mailInfo.append("\n机器名：" + rr.getHostName() + "\tip: " + rr.getIp() + "\n");
                for (Map.Entry<String, String> entry : fmap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(value)) {
                        long time = Long.parseLong(value);
                        long current = System.currentTimeMillis() / 1000;
                        long lastOneHour = DateUtils.getLastOneHour();
                        if (current - time > 10 * 60 && lastOneHour - time < 0) {
                            String date = DateUtils.getyyyyMMddHHmmssFromTimestamp(
                                    Long.parseLong(value));
                            String fid = key.substring(0, key.indexOf("#"));
                            String obj = key.substring(key.indexOf("#") + 1);
                            if (StringUtil.isNotEmpty(fid) && StringUtil.isNotEmpty(obj)) {
                                mailInfo.append(fid + "\t" + obj + "\t" + date + "\n");
                            }
                        }
                    }
                }
                mailInfo.append("\n--------------------------------\n");
            }
        }
        return mailInfo.toString();
    }

    private List<ReportRecord> filterUnReportedDataTenMinute(
            List<ReportRecord> list) {
        List<ReportRecord> mailList = new ArrayList<>();
        for (ReportRecord rr : list) {
            Map<String, String> fmap = rr.getFeatureMap();
            Map<String, String> tMap = new HashMap<>();
            if (!fmap.isEmpty()) {
                for (Map.Entry<String, String> entry : fmap.entrySet()) {
                    String value = entry.getValue();
                    if (StringUtil.isNotEmpty(value)) {
                        long time = Long.parseLong(value);
                        long current = System.currentTimeMillis() / 1000;
                        long lastOneHour = DateUtils.getLastOneHour();
                        if (current - time > 10 * 60 && lastOneHour - time < 0) {
                            tMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
                if (tMap.size() > 0) {
                    rr.setFeatureMap(tMap);
                    mailList.add(rr);
                }
            }
        }
        return mailList;
    }

    private void getReportData(List<ReportRecord> list) {
        for (ReportRecord report : list) {
            int svrId = report.getSvrId();
            NavigableMap<byte[], byte[]> resultMap = null;
            try {
                Result result = HbaseProxyClient.getOneRecord(
                        Constant.Table.OMP_SERVER_FEATURE_STAT
                        , String.valueOf(svrId));
                if (result != null && !result.isEmpty()) {
                    resultMap = result.getFamilyMap(Bytes
                            .toBytes(Constant.Table.FAMILY));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (resultMap != null) {
                for (Map.Entry<byte[], byte[]> entry : resultMap.entrySet()) {
                    String key = Bytes.toString(entry.getKey());
                    String value = Bytes.toString(entry.getValue());
                    String valueArr[] = value.split("_");
                    report.getFeatureMap().put(key, valueArr[1]);
                }
            }
        }
    }

    private void sendMail(String mailContent) {
        System.out.println("mailContent" + mailContent);
        if (StringUtil.isNotEmpty(mailContent)) {
            String mailto = "wendy@dataeye.com";
            String mailSubject = "agent超过10分钟未上报数据";
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("to", mailto);
            paramMap.put("subject", mailSubject);
            paramMap.put("content", mailContent);
            String url = "http://alarm.dataeye.com/alarm/mail";
            try {
                HttpClientUtil.post(url, paramMap);
            } catch (ServerException e) {
                e.printStackTrace();
            }
        }

    }

    private int getTimes(int svrId, String key, String currentDate) {
        NavigableMap<byte[], byte[]> resultMap = null;
        try {
            String rowkey = svrId + "#" + key + "#" + currentDate;
            Result result = HbaseProxyClient.getOneRecord(
                    Constant.Table.OMP_FEATURE_VALUE_STAT_MINUTE
                    , String.valueOf(rowkey));

            if (result != null && !result.isEmpty()) {
                resultMap = result.getFamilyMap(Bytes.toBytes("stat"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resultMap != null) {
            return resultMap.size();
        }
        return 0;
    }

    public List<ReportRecord> getServerList() {
        final List<ReportRecord> list = new ArrayList<>();
        String sql = "select a.id ,host_name hostName,b.ip from server_list a "
                + "left join server_ip_list b on a.id=b.svr_id where b.type=0;";
        if (jdbcTemplate != null) {
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    int svrId = rs.getInt("id");
                    String hostName = rs.getString("hostName");
                    String ip = rs.getString("ip");
                    ReportRecord rr = new ReportRecord();
                    rr.setSvrId(svrId);
                    rr.setHostName(hostName);
                    rr.setIp(ip);
                    list.add(rr);
                }
            });
        }
        return list;
    }

    private class ReportRecord {
        @Expose
        private int svrId;
        @Expose
        private String ip;
        @Expose
        private String hostName;
        @Expose
        Map<String, String> featureMap = new HashMap<>();

        public Map<String, String> getFeatureMap() {
            return featureMap;
        }

        public void setFeatureMap(Map<String, String> featureMap) {
            this.featureMap = featureMap;
        }

        public int getSvrId() {
            return svrId;
        }

        public void setSvrId(int svrId) {
            this.svrId = svrId;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

    }

}
