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
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class Producer {

    private final KafkaTemplate<String, GenericDto> kafkaTemplate;
    private RestTemplate restTemplate;
    @Value("${rest.callback_address}")
    private String url;
    @Value("${rest.callback_sms_address}")
    private String smsCallbackUrl;
    @Value("${rest.callback_email_address}")
    private String emailCallbackUrl;

    @Autowired
    public Producer(KafkaTemplate<String, GenericDto> kafkaTemplate, RestTemplate restTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.restTemplate = restTemplate;
    }

    public void sendMessage(GenericDto dto, String topic, Schedule schedule) {
        kafkaTemplate.send(topic, dto).addCallback(new ListenableFutureCallback<SendResult<String, GenericDto>>() {

            @Override
            public void onSuccess(SendResult<String, GenericDto> result) {
                if (topic.equals("t.sms")) {
                    restTemplate.put(prepareProcessedUrl(schedule.getId().toString(), smsCallbackUrl, "PROCESSED"),
                            ResponseEntity.class);
                } else if (topic.equals("t.email")) {
                    restTemplate.put(prepareProcessedUrl(schedule.getId().toString(), emailCallbackUrl, "PROCESSED"),
                            ResponseEntity.class);
                }
            }
            @Override
            public void onFailure(Throwable ex) {
                if (topic.equals("t.sms")) {
                    restTemplate.put(prepareProcessedUrl(schedule.getId().toString(), smsCallbackUrl, "FAILED"),
                            ResponseEntity.class);
                } else if (topic.equals("t.email")) {
                    restTemplate.put(prepareProcessedUrl(schedule.getId().toString(), emailCallbackUrl, "FAILED"),
                            ResponseEntity.class);
                }
                log.info("Exception happeneded {}",ex);
            }
        });
    }

    private static String prepareProcessedUrl(String scheduleId, String baseUrl, String status) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("id", scheduleId)
                .queryParam("status", status)
                .build().toUriString();
    }
}
