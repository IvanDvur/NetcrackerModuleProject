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
    @Value("${service.max_retries_count}")
    private Integer maxRetriesCount;
    private Producer producer;

    public SmsSender(Producer producer) {
        this.producer = producer;
    }

    public void send(GenericDto<SmsAdvertisement> advertisment) {
        SmsAdvertisement smsAdvertisement = advertisment.getAdvertisement();
        Schedule schedule = advertisment.getSchedule();
        try {
            // Задаем параметры запроса
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("login", "Sotlint");
            requestBody.add("psw", "6qJLab3b");
            requestBody.add("time", "0");
            requestBody.add("mes", smsAdvertisement.getText());

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
            schedule.setSmsStatus(SendStatus.SENT);
            producer.sendMessage("t.success",schedule);

        } catch (RestClientException e) {
            if (schedule.getRetriesCount() < maxRetriesCount) {
                schedule.setSmsStatus(SendStatus.NOT_SENT);
                schedule.setRetriesCount(schedule.getRetriesCount() + 1);
                producer.sendMessage("t.error", schedule);

            }else{
                schedule.setSmsStatus(SendStatus.EXPIRED);
                producer.sendMessage("t.error", schedule);
            }
            e.printStackTrace();
        }


    }
}

