package com.netcracker.senderservice.service;


import com.netcracker.senderservice.producer.Producer;
import dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
@RequiredArgsConstructor
public class SmsSender {

    private final Producer producer;
    private final RestTemplate restTemplate;


    public void send(GenericDto<SmsAdvertisement> dto) {
        SmsAdvertisement smsAdvertisement = dto.getAdvertisement();
        UpdateStatusDto updateStatusDto = new UpdateStatusDto(dto.getScheduleId(), AdTypes.SMS);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (ClientDto clientDto : dto.getClientDtoSet()) {
                if (clientDto.getPhoneNumber().trim().length()==0) {
                    continue;
                }
                stringBuilder.append(clientDto.getPhoneNumber() + ";");
            }

            if(stringBuilder.toString().length() == 0){
                restTemplate.put(GenericDto.prepareStatusUrl(dto.getScheduleId(),"http://data-service:8080","Fatal","sms"),ResponseEntity.class);
            }

            Smsc sd = new Smsc("IvanDvur", "fAa-7EY-3fv-G5z");
            String[] response = sd.send_sms(stringBuilder.toString(), smsAdvertisement.getText(), 0, "", "", 0, "", "");

            if (response[0].equals("Ошибка соединения")) {
                producer.sendMessage("t.error", updateStatusDto);
            }

            List<String> successSms = new ArrayList<>();
            for (ClientDto clientDto: dto.getClientDtoSet()){
                String[] report = sd.get_status(Integer.parseInt(response[0]),clientDto.getPhoneNumber(),0);
                System.out.println(Arrays.toString(report));
                if(Integer.parseInt(report[0]) == -1){
                    successSms.add(clientDto.getId());
                }
            }
            restTemplate.postForObject("http://data-service:8080/statusPerClient/smsPerClientStatus",
                    new StatusPerClientUpdateRequest(dto.getOrderId(), successSms), ResponseEntity.class);

            producer.sendMessage("t.success", updateStatusDto);
        } catch (RestClientException e) {
            producer.sendMessage("t.error", updateStatusDto);
            e.printStackTrace();
        }

    }
}

