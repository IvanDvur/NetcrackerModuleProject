package com.netcracker.senderservice.producer;

import dto.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class Producer {

    @Value("${rest.callback.address}")
    private String url;
    private RestTemplate restTemplate;
    private final KafkaTemplate<String, Schedule> kafkaTemplate;

    public Producer(KafkaTemplate<String, Schedule> kafkaTemplate) {
        this.restTemplate = new RestTemplate();
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Schedule schedule) {
        kafkaTemplate.send(topic,schedule).addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {

            }

            @Override
            public void onSuccess(SendResult<String, Schedule> result) {

            }
        });

    }

}
