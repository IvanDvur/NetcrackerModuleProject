package com.netcracker.SendManager.service.schedulers;

import dto.Schedule;
import dto.GenericDto;
import com.netcracker.SendManager.dto.MessageDto;
import com.netcracker.SendManager.producer.Producer;


public class KafkaTask implements Runnable {

    private final Producer producer;
    private final OrderScheduler orderScheduler;
    private final MessageDto messageDto;
    private final Schedule schedule;

    public KafkaTask(Producer producer, OrderScheduler orderScheduler, MessageDto messageDto, Schedule schedule) {
        this.producer = producer;
        this.orderScheduler = orderScheduler;
        this.messageDto = messageDto;
        this.schedule = schedule;
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
                            new GenericDto(messageDto.getOrderId(),
                                    messageDto.getSmsAdvertisement(),
                                    messageDto.getClientsDtos(),
                                    schedule.getId().toString()),
                            "t.sms");
                    break;
                case "EMAIL":
                    producer.sendMessage(
                            new GenericDto(messageDto.getOrderId(),
                                    messageDto.getEmailAdvertisement(),
                                    messageDto.getClientsDtos(),
                                    schedule.getId().toString()),
                            "t.email");
                    break;
                case "MESSENGER":
                    producer.sendMessage(
                            new GenericDto(messageDto.getOrderId(),
                                    messageDto.getMessengerAdvertisement(),
                                    messageDto.getClientsDtos(),
                                    schedule.getId().toString()),
                            "t.messenger");
                    break;
            }
        }
    }
}
