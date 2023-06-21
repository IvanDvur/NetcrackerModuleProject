package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
@EnableScheduling
public class AccountCleanupTask {

//    private final CustomerRepository customerRepository;
//
//    @Scheduled(cron = "0 * * * * *") // Запускать задачу каждый первый день месяца в полночь
//    public void deleteInactiveAccounts() {
//        System.out.println("Удаление неактивных");
//        LocalDateTime threeMinutesAgo = LocalDateTime.now().minusMinutes(3);
//        List<Customer> inactiveUsers = customerRepository.findAllByLastLoginBefore(threeMinutesAgo);
//        System.out.println(inactiveUsers);
//        for (Customer c:inactiveUsers) {
//            customerRepository.delete(c);
//        }
//    }
}
