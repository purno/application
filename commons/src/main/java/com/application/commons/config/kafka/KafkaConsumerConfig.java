package com.application.commons.config.kafka;

import com.application.commons.constants.AppConstants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;


@EnableKafka
@Configuration
@PropertySources({
        @PropertySource(value = {"classpath:commons-${spring.profiles.active}.properties"}),
        /*
         * the below file is added so as to provide a consolidated application properties file to the QA
         */
        @PropertySource(value = "classpath:application-configuration.properties", ignoreResourceNotFound = true)
})
public class KafkaConsumerConfig implements InitializingBean {

    @Value(value = "${" + AppConstants.KAFKA_CONSUMER_BOOTSTRAP_ADDRESS + "}")
    protected String bootstrapAddress;

    @Value(value = "${" + AppConstants.KAFKA_DEFAULT_GROUP_ID + "}")
    protected String defaultGroupId;

    @Value(value = "${" + AppConstants.KAFKA_AUTO_OFFSET_RESET + "}")
    protected String autoOffsetStrategy;

    @Value(value = "${" + AppConstants.KAFKA_CONSUMER_CONCURRENCY + "}")
    protected int concurrency;


    public ConsumerFactory<String, String> consumerFactory(String groupId, boolean isAckAuto) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetStrategy);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, isAckAuto);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> defaultListenerAutoAckContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(defaultGroupId, true));
        if (concurrency != 0) {
            factory.setConcurrency(concurrency);
        }
        return factory;
    }

//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> defaultListenerManualAckContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory(defaultGroupId,false));
//        if(concurrency != 0){
//    		factory.setConcurrency(concurrency);
//    	}
//        factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
//        return factory;
//    }


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("bootstrapAddress = " + bootstrapAddress);
        System.out.println("defaultGroupId = " + defaultGroupId);
        System.out.println("autoOffsetStrategy = " + autoOffsetStrategy);
        System.out.println("concurrency = " + concurrency);

    }

}
