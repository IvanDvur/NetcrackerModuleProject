package com.netcracker.SendManager.api;

import com.netcracker.SendManager.service.KafkaTopicSplitterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
@EnableAsync
@EnableScheduling
public class OrderApi {

    @Value("${rest.address}")
    private String url;

    private static final Logger log = LoggerFactory.getLogger(OrderApi.class);
    private final KafkaTopicSplitterService customerService;
    @Scheduled(fixedRate = 5000)
    @Async
    public void getDataService()  {
        System.out.println(url);
        RestTemplate restTemplate = new RestTemplate();
        try {
            String dto = restTemplate.getForObject(url, String.class);
            customerService.splitAdvertisementOnTopic(dto);
            log.info("Processing message {}",dto);
        }catch (HttpClientErrorException e){
            log.debug("No Messages");
        }catch (ResourceAccessException e){
            log.warn("No connection to data-service");
        }
    }
}
