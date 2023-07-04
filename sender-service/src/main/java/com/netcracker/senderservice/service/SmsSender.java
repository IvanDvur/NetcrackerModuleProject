package com.netcracker.senderservice.service;


import com.netcracker.senderservice.producer.Producer;
import dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Component
public class SmsSender {

    private Producer producer;

    public SmsSender(Producer producer) {
        this.producer = producer;
    }

    public void send(GenericDto<SmsAdvertisement> dto) {
        SmsAdvertisement smsAdvertisement = dto.getAdvertisement();
        UpdateStatusDto updateStatusDto = new UpdateStatusDto(dto.getScheduleId(), AdTypes.SMS);
        try {
            // Задаем параметры запроса
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("login", "IvanDvur");
            requestBody.add("psw", "fAa-7EY-3fv-G5z");
            requestBody.add("time", "0");
            requestBody.add("mes", smsAdvertisement.getText());

            for (ClientDto clientDto : dto.getClientDtoSet()) {
                if(clientDto.getPhoneNumber()==null){
                    continue;
                }
                requestBody.add("phones", clientDto.getPhoneNumber() + ";");
            }
            if(requestBody.get("phones")==null){
                producer.sendMessage("t.error",updateStatusDto);
                return;
            }
            // Формируем заголовки запроса
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            // Создаем объект RestTemplate для отправки запроса
            RestTemplate restTemplate = new RestTemplate();
            // Создаем объект HttpEntity с телом запроса и заголовками
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);
            // Отправляем запрос на указанный URL с заданными параметрами и получаем ответ в виде строки
            ResponseEntity<String> response = restTemplate.exchange("https://smsc.ru/sys/send.php", HttpMethod.POST, entity, String.class);
            System.out.println("Response: " + response.getBody());
            if(response.getBody().contains("ERROR")){
                producer.sendMessage("t.error",updateStatusDto);
            }else{
                producer.sendMessage("t.success",updateStatusDto);
            }
        } catch (RestClientException e) {
            producer.sendMessage("t.error",updateStatusDto);
            e.printStackTrace();
        }


    }
}

