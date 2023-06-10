package com.netcracker.SendManager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dto.Schedule;
import dto.ClientDto;
import dto.EmailAdvertisement;
import dto.MessengerAdvertisement;
import dto.SmsAdvertisement;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDto {

    private String orderId;
    private EmailAdvertisement emailAdvertisement;
    private SmsAdvertisement smsAdvertisement;
    private MessengerAdvertisement messengerAdvertisement;
    private Set<ClientDto> clientsDtos;
    private Set<Schedule> schedule;
    private String sendTypes;

    public MessageDto(
            String orderId,
            EmailAdvertisement emailAdvertisement,
            SmsAdvertisement smsAdvertisement,
            MessengerAdvertisement messengerAdvertisement,
            Set<ClientDto> clientsDtos,
            Set<Schedule> schedule,
            String sendTypes) {
        this.orderId = orderId;
        this.emailAdvertisement = emailAdvertisement;
        this.smsAdvertisement = smsAdvertisement;
        this.messengerAdvertisement = messengerAdvertisement;
        this.clientsDtos = clientsDtos;
        this.schedule = schedule;
        this.sendTypes = sendTypes;
    }

    public MessageDto() {

    }


}
