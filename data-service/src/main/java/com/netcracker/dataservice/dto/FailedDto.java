package com.netcracker.dataservice.dto;

import com.netcracker.dataservice.model.Schedule;
import com.netcracker.dataservice.model.SendingOrder;
import com.netcracker.dataservice.model.advertisement.MessengerAdvertisement;
import com.netcracker.dataservice.model.advertisement.SmsAdvertisement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailedDto {


    private EmailDto emailAdvertisement;
    private SmsAdvertisement smsAdvertisement;
    private MessengerAdvertisement messengerAdvertisement;
    private Set<ClientDto> clientsDtos;
    private List<String> retryTypes;
    private String scheduleId;



    public static FailedDto convertToDto(SendingOrder order, String scheduleId,List<String> listTypes) {
        /**
         * Преобразуем полную информацию о клиентах в dto
         */
        Set<ClientDto> clientDto = order.getClients().stream()
                .map(client -> new ClientDto(client.getFirstName(),
                        client.getEmail(),
                        client.getPhoneNumber()))
                .collect(Collectors.toSet());

        /**
         * Возвращаем конфиг dto на основе информации из тела конфига и dto клиентов
         */


        return new FailedDto(EmailDto.convertToDto(order.getEmailAdvertisement()),
                order.getSmsAdvertisement(),
                order.getMessengerAdvertisement(),
                clientDto, listTypes,scheduleId);
        }



}
