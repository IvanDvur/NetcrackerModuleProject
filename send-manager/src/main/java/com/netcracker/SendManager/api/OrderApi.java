package com.netcracker.SendManager.api;

import com.netcracker.SendManager.service.KafkaTopicSplitterService;
import com.netcracker.SendManager.service.schedulers.OrderScheduler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


@Component
@EnableAsync
@EnableScheduling
public class OrderApi {

    @Value("${rest.address}")
    private String url;
    @Value("${rest.retry_address}")
    private String messageForRetryURL;
    @Value("${rest.expired_address}")
    private String expiredMessageURL;
    @Value("${service.medium_load}")
    private Integer mediumLoad;
    @Value("${service.high_load}")
    private Integer highLoad;
    private RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(OrderApi.class);
    private final KafkaTopicSplitterService topicSplitterService;

    @Autowired
    public OrderApi(RestTemplate restTemplate, KafkaTopicSplitterService customerService) {
        this.restTemplate = restTemplate;
        this.topicSplitterService = customerService;
    }

    @Scheduled(fixedRate = 5000)
    @Async
    public void getNormalMessages() {
        System.out.println(url);
        try {
            String dto = restTemplate.getForObject(url, String.class);
            topicSplitterService.splitAdvertisementOnTopic(dto);
            log.info("Processing message {}", dto);
        } catch (HttpClientErrorException e) {
            log.debug("No Messages");
        } catch (ResourceAccessException e) {
            log.warn("No connection to data-service");
        }
    }

    @Scheduled(fixedRate = 10000)
    public void getFailedMessages() {
        if (OrderScheduler.schedulerLoad > highLoad) {
            return;
        }
        try {
            String dto = restTemplate.getForObject(messageForRetryURL, String.class);
            topicSplitterService.processFailedMessage(dto);

        } catch (HttpClientErrorException e) {
            log.debug("No Retry Messages");
        } catch (ResourceAccessException e) {
            log.warn("No connection to data-service");
        }
    }

//    @Scheduled(fixedRate = 10000)
//    public void getExpiredMessages() {
//        if (OrderScheduler.schedulerLoad > mediumLoad) {
//            return;
//        }
//        try {
//            String dto = restTemplate.getForObject(expiredMessageURL, String.class);
//            topicSplitterService.processFailedMessage(dto);
//        } catch (HttpClientErrorException e) {
//            log.debug("No expired Messages");
//        } catch (ResourceAccessException e) {
//            log.warn("No connection to data-service");
//        }
//    }
}
