package com.netcracker.dataservice.dto;

import com.netcracker.dataservice.model.Client;
import com.netcracker.dataservice.model.Schedule;
import com.netcracker.dataservice.model.SendingOrder;
import com.netcracker.dataservice.model.advertisement.MessengerAdvertisement;
import com.netcracker.dataservice.model.advertisement.SmsAdvertisement;
import dto.ClientDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private String orderId;
    private EmailDto emailAdvertisement;
    private SmsAdvertisement smsAdvertisement;
    private MessengerAdvertisement messengerAdvertisement;
    private Set<ClientDto> clientsDtos;
    private String sendTypes;
    private Set<Schedule> schedule;

    /**
     * Подготавливаем OrderDto для отправки в SendManager
     *
     * @param order SendingOrder для преобразования в dto
     * @return
     */
    public static OrderDto convertToDto(SendingOrder order, Set<Schedule> timeToSend) {

        Set<ClientDto> clientDto = order.getMailingList().getClients().stream()
                .map(Client::convertToDto)
                .collect(Collectors.toSet());

        return new OrderDto(String.valueOf(order.getId()),EmailDto.convertToDto(order.getEmailAdvertisement()),
                order.getSmsAdvertisement(),
                order.getMessengerAdvertisement(),
                clientDto, order.getSendTypes(), timeToSend);
    }


}