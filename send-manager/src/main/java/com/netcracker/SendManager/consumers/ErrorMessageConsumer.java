package com.netcracker.SendManager.consumers;

import dto.AdTypes;
import dto.GenericDto;
import dto.Schedule;
import dto.UpdateStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ErrorMessageConsumer {

    private RestTemplate restTemplate;
    @Value("${rest.callback_generic_address}")
    private String callbackUrl;

    @Autowired
    public ErrorMessageConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = "t.error",groupId = "my_group")
    public void consumeErrorMessages(UpdateStatusDto dto){
        if(dto.getType().equals(AdTypes.EMAIL)){
            restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(),callbackUrl,"NOT_SENT","email"),ResponseEntity.class);
        } else if (dto.getType().equals(AdTypes.SMS)) {
            restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(),callbackUrl,"NOT_SENT","sms"),ResponseEntity.class);
        }
    }

}
