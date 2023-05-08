package com.netcracker.SendManager.producer;

import dto.Schedule;
import dto.GenericDto;
import dto.SendStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class Producer{

    private final KafkaTemplate<String, GenericDto> kafkaTemplate;


    public void sendMessage(GenericDto dto, String topic, Schedule schedule,String url,RestTemplate restTemplate){
        kafkaTemplate.send(topic,dto).addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                schedule.setSendStatus(SendStatus.FAILED);
                restTemplate.put(url, schedule, ResponseEntity.class);
                log.info("Failed to send dto {}", dto);
            }

            @Override
            public void onSuccess(SendResult<String, GenericDto> result) {
                schedule.setSendStatus(SendStatus.PROCESSED);
                restTemplate.put(url, schedule, ResponseEntity.class);
                log.info("Successfuly sent message to sender-service {}",dto);
            }
        });

    }
}
