package com.netcracker.SendManager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.netcracker.SendManager.dto.MessageDto;
import com.netcracker.SendManager.producer.Producer;
import com.netcracker.SendManager.service.schedulers.KafkaTask;
import com.netcracker.SendManager.service.schedulers.OrderScheduler;
import dto.FailedDto;
import dto.GenericDto;
import dto.SendStatus;
import dto.UpdateStatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class KafkaTopicSplitterService {

    @Value("${rest.callback_address}")
    private String url;
    @Value("${rest.callback_generic_address}")
    private String callbackUrl;
    private OrderScheduler orderScheduler;
    private ObjectMapper objectMapper;
    private RestTemplate restTemplate;
    private Producer producer;
    private static final Logger log = LoggerFactory.getLogger(KafkaTopicSplitterService.class);

    @Autowired
    public KafkaTopicSplitterService(OrderScheduler orderScheduler,
                                     ObjectMapper objectMapper,
                                     RestTemplate restTemplate,
                                     Producer producer) {
        this.objectMapper = objectMapper;
        this.orderScheduler = orderScheduler;
        this.restTemplate = restTemplate;
        this.producer = producer;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void splitAdvertisementOnTopic(String orderDto) {
        try {
            List<MessageDto> data = objectMapper.readValue(orderDto, new TypeReference<>() {
            });
            for (MessageDto messageDto : data) {
                messageDto.getSchedule().forEach(schedule -> {
                    orderScheduler.scheduleATask(schedule,
                            new KafkaTask(producer, orderScheduler, messageDto, schedule));
                    if(messageDto.getSendTypes().contains("EMAIL")){
                        schedule.setEmailStatus(SendStatus.PROCESSING);
                    }
                    if(messageDto.getSendTypes().contains("SMS")){
                        schedule.setSmsStatus(SendStatus.PROCESSING);
                    }
                    restTemplate.put(url, schedule, ResponseEntity.class);
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void processFailedMessage(String errorOrderDto) {
        try {
            List<FailedDto> data = objectMapper.readValue(errorOrderDto, new TypeReference<>() {
            });
            for (FailedDto failedDto :data){
                failedDto.getRetryTypes().forEach(type -> {
                    restTemplate.put(GenericDto.prepareStatusUrl(failedDto.getScheduleId(),callbackUrl,"PROCESSED",type.toLowerCase()),ResponseEntity.class);
                    producer.sendMessage(new GenericDto<>(failedDto.getOrderId(),failedDto.getAdvertisment(type),
                            failedDto.getClientsDtos(),failedDto.getScheduleId()),"t."+type.toLowerCase());
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


}