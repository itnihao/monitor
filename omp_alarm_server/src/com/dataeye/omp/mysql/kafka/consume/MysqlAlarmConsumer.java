package com.dataeye.omp.mysql.kafka.consume;

import com.dataeye.omp.utils.ServerUtils;
import com.dataeye.util.log.DELogger;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wendywang
 * @since 2016/4/4 13:30
 */
@Service
public class MysqlAlarmConsumer {

    private final static Logger logger = DELogger.getLogger("mysql_log");
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;

    public MysqlAlarmConsumer() {
        ConsumerConfig config = createConsumerConfig();
        consumer = Consumer.createJavaConsumerConnector(config);
        topic = ServerUtils.getMysqlAlarmProperties().getProperty("kafka.mysql.alarm.topic");
    }

    public void shutdown() {
        if (consumer != null) {
            consumer.shutdown();
        }
        if (executor != null) {
            executor.shutdown();
        }
    }

    public void consume(int numThreads) {
        logger.info("topic:" + topic);
        System.out.println("topic:" + topic);
        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        topicMap.put("mysqlReportTopic", numThreads);

        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

        Map<String, List<KafkaStream<String, String>>> consumerMap =
                consumer.createMessageStreams(topicMap, keyDecoder, valueDecoder);
        List<KafkaStream<String, String>> streams = consumerMap.get(topic);

        executor = Executors.newFixedThreadPool(numThreads);

        for (final KafkaStream<String, String> stream : streams) {
            executor.submit(new MysqlAlarmHandler(stream));
        }
    }


    private static ConsumerConfig createConsumerConfig() {
         Properties props = ServerUtils.getMysqlAlarmProperties();
        return new ConsumerConfig(props);
    }
}
