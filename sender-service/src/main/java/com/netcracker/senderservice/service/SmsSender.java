package com.netcracker.senderservice.service;


import dto.ClientDto;
import dto.GenericDto;
import dto.SmsAdvertisement;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Component
public class SmsSender {

    public static boolean isNotFailed = true;

    public void send(GenericDto<SmsAdvertisement> advertisment) {

        SmsAdvertisement smsAdvertisement = advertisment.getAdvertisement();
        try {
            // Задаем параметры запроса
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("login", "Sotlint");
            requestBody.add("psw", "6qJLab3b");
            requestBody.add("time", "0");
            requestBody.add("mes", smsAdvertisement.getText());
            // SmsAdvertisement a = advertisment.getAdvertisement();

            for (ClientDto clientDto : advertisment.getClientDtoSet()) {
                requestBody.add("phones", clientDto.getPhoneNumber() + ";");
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
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

