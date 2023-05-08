package com.netcracker.SendManager.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class TopicConfig {

    @Bean
    public NewTopic topicEmail() {
        return TopicBuilder
                .name("t.email")
                .build();
    }

    @Bean
    public NewTopic topicSms() {
        return TopicBuilder
                .name("t.sms")
                .build();
    }

    @Bean
    public NewTopic topicWhatsApp() {
        return TopicBuilder
                .name("t.messenger")
                .build();
    }

    @Bean
    public NewTopic topicError(){
        return TopicBuilder
                .name("t.error")
                .build();
    }

    @Bean NewTopic topicSuccess(){
        return TopicBuilder
                .name("t.success")
                .build();
    }
}
