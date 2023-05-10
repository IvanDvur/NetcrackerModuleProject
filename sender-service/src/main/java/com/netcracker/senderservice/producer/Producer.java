package com.netcracker.senderservice.producer;

import dto.AdTypes;
import dto.GenericDto;
import dto.Schedule;
import dto.UpdateStatusDto;
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

    @Value("${rest.callback_generic_address}")
    private String callbackUrl;
    private RestTemplate restTemplate;
    private final KafkaTemplate<String, UpdateStatusDto> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    public Producer(KafkaTemplate<String, UpdateStatusDto> kafkaTemplate) {
        this.restTemplate = new RestTemplate();
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, UpdateStatusDto dto) {
        kafkaTemplate.send(topic, dto).addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, UpdateStatusDto> result) {
                log.info("Successfully sent request to update dto status {}", dto);
            }

            @Override
            public void onFailure(Throwable ex) {
                if (topic.equals("t.success") && dto.getType().equals(AdTypes.EMAIL)) {
                    restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(), callbackUrl, "SENT","email"),
                            ResponseEntity.class);
                }
                if (topic.equals("t.success") && dto.getType().equals(AdTypes.SMS)) {
                    restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(), callbackUrl, "SENT","sms"),
                            ResponseEntity.class);
                }
                if (topic.equals("t.error") && dto.getType().equals(AdTypes.EMAIL)) {
                    restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(), callbackUrl, "NOT_SENT","email"),
                            ResponseEntity.class);
                }
                if (topic.equals("t.error") && dto.getType().equals(AdTypes.SMS)) {
                    restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(), callbackUrl, "NOT_SENT","sms"),
                            ResponseEntity.class);
                }
            }
        });

    }

}
