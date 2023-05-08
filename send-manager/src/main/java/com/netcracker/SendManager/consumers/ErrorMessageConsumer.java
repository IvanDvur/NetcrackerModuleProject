package com.netcracker.SendManager.consumers;

import dto.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ErrorMessageConsumer {

    @Value("${rest.callback_address}")
    private String callbackUrl;
    private RestTemplate restTemplate;

    @Autowired
    public ErrorMessageConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = "t.error",groupId = "my_group")
    public void consumeErrorMessages(Schedule schedule){
        restTemplate.put(callbackUrl,schedule, ResponseEntity.class);
    }

}
