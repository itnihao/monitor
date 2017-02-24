package com.dataeye.omp.custom.monitor.consume;

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
 * @author wendy
 * @since 2016/3/17 11:37
 */
@Service
public class CustomMonitorConsumer {
    private final static Logger logger = DELogger.getLogger("monitor_log");
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;

    /**自定义监控topic*/
    public static final String MONITOR_TOPIC = "kafka.custom.monitor.topic";

    public CustomMonitorConsumer() {
        ConsumerConfig config = createConsumerConfig();
        consumer = Consumer.createJavaConsumerConnector(config);
        topic = ServerUtils.getMonitorConsumerProperties()
            .getProperty(MONITOR_TOPIC);
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
        logger.info("开始消费kafka自定义监控消息，topic:" + topic);
        Map<String, Integer> topicMap = new HashMap<>();
        topicMap.put(topic, numThreads);
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
        Map<String, List<KafkaStream<String, String>>> consumerMap =
                consumer.createMessageStreams(topicMap,keyDecoder,valueDecoder);
        List<KafkaStream<String, String>> streams = consumerMap.get(topic);

        executor = Executors.newFixedThreadPool(numThreads);

        for (final KafkaStream<String, String> stream : streams) {
            executor.submit(new CustomMonitorHandler(stream));
        }
    }

    private static ConsumerConfig createConsumerConfig() {
        Properties props = ServerUtils.getMonitorConsumerProperties();
        return new ConsumerConfig(props);
    }
}
