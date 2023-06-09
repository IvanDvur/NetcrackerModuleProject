package com.netcracker.SendManager.producer;

import dto.Schedule;
import dto.GenericDto;
import dto.SendStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@Component
public class Producer {
    @Value("${rest.callback_generic_address}")
    private String callbackUrl;
    private final KafkaTemplate<String, GenericDto> kafkaTemplate;
    private RestTemplate restTemplate;
    private Logger log = LoggerFactory.getLogger(Producer.class);
    @Autowired
    public Producer(KafkaTemplate<String, GenericDto> kafkaTemplate, RestTemplate restTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.restTemplate = restTemplate;
    }
    public void sendMessage(GenericDto dto, String topic) {
        kafkaTemplate.send(topic, dto).addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<String, GenericDto> result) {
                if (topic.equals("t.sms")) {
                    restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(), callbackUrl, "PROCESSED","sms"),
                            ResponseEntity.class);
                    log.info("Updating status for sms to PROCESSED {}",dto.getScheduleId());
                } else if (topic.equals("t.email")) {
                    restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(), callbackUrl, "PROCESSED","email"),
                            ResponseEntity.class);
                    log.info("Updating status for email to PROCESSED {}",dto.getScheduleId());
                }
            }
            @Override
            public void onFailure(Throwable ex) {
                if (topic.equals("t.sms")) {
                    restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(), callbackUrl, "FAILED","sms"),
                            ResponseEntity.class);
                    log.info("Updating status for sms to FAILED {}",dto.getScheduleId());
                } else if (topic.equals("t.email")) {
                    restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(), callbackUrl, "FAILED","email"),
                            ResponseEntity.class);
                    log.info("Updating status for email to FAILED {}",dto.getScheduleId());
                }
                log.info("Exception happeneded {}",ex);
            }
        });
    }
}
