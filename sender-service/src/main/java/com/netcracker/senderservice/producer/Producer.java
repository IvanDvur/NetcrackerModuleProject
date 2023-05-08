package com.netcracker.senderservice.producer;

import dto.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.RestTemplate;

@Service
public class Producer {

    @Value("${rest.callback.address}")
    private String url;
    private RestTemplate restTemplate;
    private final KafkaTemplate<String, Schedule> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    public Producer(KafkaTemplate<String, Schedule> kafkaTemplate) {
        this.restTemplate = new RestTemplate();
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Schedule schedule) {
        kafkaTemplate.send(topic,schedule).addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                restTemplate.put(url,schedule, ResponseEntity.class);
            }

            @Override
            public void onSuccess(SendResult<String, Schedule> result) {
                log.info("Updating status for schedule {}", schedule);
            }
        });

    }

}
