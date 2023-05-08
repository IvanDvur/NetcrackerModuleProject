package com.netcracker.SendManager.producer;

import dto.Schedule;
import dto.GenericDto;
import dto.SendStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class Producer{

    private final KafkaTemplate<String, GenericDto> kafkaTemplate;
    private RestTemplate restTemplate;
    @Value("${rest.callback_address}")
    private String url;
    @Autowired
    public Producer(KafkaTemplate<String, GenericDto> kafkaTemplate, RestTemplate restTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.restTemplate = restTemplate;
    }

    public void sendMessage(GenericDto dto, String topic,Schedule schedule){



        kafkaTemplate.send(topic,dto).addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onFailure(Throwable ex) {
                if(topic.equals("t.sms")){
                    schedule.setRetriesCount(schedule.getRetriesCount()+1);
                    schedule.setSmsStatus(SendStatus.FAILED);
                }
                else if(topic.equals("t.email")){
                    schedule.setRetriesCount(dto.getSchedule().getRetriesCount()+1);
                    schedule.setEmailStatus(SendStatus.FAILED);
                }
                restTemplate.put(url, schedule, ResponseEntity.class);
                log.info("Failed to send dto {}", dto);
            }

            @Override
            public void onSuccess(SendResult<String, GenericDto> result) {
                if(topic.equals("t.sms")){
                    schedule.setSmsStatus(SendStatus.PROCESSED);
                }
                else if(topic.equals("t.email")){
                    schedule.setEmailStatus(SendStatus.PROCESSED);
                }
                restTemplate.put(url, schedule, ResponseEntity.class);
                log.info("Successfuly sent message to sender-service {}",dto);
            }
        });

    }
}
