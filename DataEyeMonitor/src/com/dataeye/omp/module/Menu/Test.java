package com.dataeye.omp.module.Menu;

import com.dataeye.utils.HbaseProxyClient;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @auther wendy
 * @since 2016/1/12 14:38
 */
public class Test {

    public static void main(String[] args) throws IOException {
        String table = "omp_feature_value_stat_hour";
        String rowKey = "28#22#mem_pri#2016-01-14";
        Result rs = HbaseProxyClient.getOneRecord(table, rowKey);
        System.out.println(rs);

       byte[] b= rs.getValue(Bytes.toBytes("stat"),
                Bytes.toBytes("14"));

        System.out.println(Bytes.toInt(b));


    }

}
