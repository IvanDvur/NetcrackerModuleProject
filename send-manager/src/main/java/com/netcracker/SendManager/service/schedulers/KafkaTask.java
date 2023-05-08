package com.netcracker.SendManager.service.schedulers;

import dto.Schedule;
import dto.GenericDto;
import com.netcracker.SendManager.dto.MessageDto;
import com.netcracker.SendManager.producer.Producer;
import org.springframework.web.client.RestTemplate;


public class KafkaTask implements Runnable {

    private String url;
    private final Producer producer;
    private final OrderScheduler orderScheduler;
    private final MessageDto messageDto;
    private final Schedule schedule;
    private RestTemplate restTemplate;

    public KafkaTask(String url,
                     RestTemplate restTemplate,
                     Producer producer,
                     OrderScheduler orderScheduler,
                     MessageDto messageDto,
                     Schedule schedule) {
        this.producer = producer;
        this.restTemplate = restTemplate;
        this.orderScheduler = orderScheduler;
        this.messageDto = messageDto;
        this.schedule = schedule;
        this.url = url;
    }

    @Override
    public void run() {
        addInTopic();
    }

    public void addInTopic() {
        checkAndSendToTopic(messageDto, "EMAIL");
        checkAndSendToTopic(messageDto, "SMS");
        checkAndSendToTopic(messageDto, "MESSENGER");
        orderScheduler.removeScheduledTask(schedule.getId().toString());
    }

    public void checkAndSendToTopic(MessageDto messageDto, String sendType) {
        if (messageDto.getSendTypes().contains(sendType)) {
            switch (sendType) {
                case "SMS":
                    producer.sendMessage(
                            new GenericDto(messageDto.getSmsAdvertisement(), messageDto.getClientsDtos(),schedule),
                            "t.sms",
                            schedule,
                            url,
                            restTemplate);
                    break;
                case "EMAIL":
                    producer.sendMessage(
                            new GenericDto(messageDto.getEmailAdvertisement(), messageDto.getClientsDtos(),schedule),
                            "t.email",
                            schedule,
                            url,
                            restTemplate);
                    break;
                case "MESSENGER":
                    producer.sendMessage(
                            new GenericDto(messageDto.getMessengerAdvertisement(), messageDto.getClientsDtos(),schedule),
                            "t.messenger",
                            schedule,
                            url,
                            restTemplate);
                    break;
            }
        }
    }
}
