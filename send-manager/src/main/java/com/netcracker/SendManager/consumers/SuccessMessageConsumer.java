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
public class SuccessMessageConsumer {

    @Value("${rest.callback_sms_address}")
    private String smsCallbackUrl;
    @Value("${rest.callback_email_address}")
    private String emailCallbackUrl;

    private RestTemplate restTemplate;

    @Autowired
    public SuccessMessageConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = "t.success",groupId = "my_group")
    public void consumeSuccessMessages(UpdateStatusDto dto){
        if(dto.getType().equals(AdTypes.EMAIL)){
            restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(),emailCallbackUrl,"SENT"),ResponseEntity.class);
        } else if (dto.getType().equals(AdTypes.SMS)) {
            restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(),smsCallbackUrl,"SENT"),ResponseEntity.class);
        }
    }

}
