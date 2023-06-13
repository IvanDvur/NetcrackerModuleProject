package com.netcracker.dataservice.configuration;

import com.netcracker.dataservice.model.Schedule;
import com.netcracker.dataservice.repositories.ScheduleRepository;
import dto.SendStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class RestartConfig {

    private ScheduleRepository scheduleRepository;

    @Bean
    public CommandLineRunner commandLineRunner(ScheduleRepository scheduleRepository) {
        return args -> {
            Set<Schedule> expiredSchedules = scheduleRepository.findAllByTimeToSendIsBefore(LocalDateTime.now());
            for (Schedule s : expiredSchedules) {
                if (s.getSmsStatus().equals(SendStatus.WAITING)) {
                    s.setSmsStatus(SendStatus.EXPIRED);
                }
                if (s.getEmailStatus().equals(SendStatus.WAITING)) {
                    s.setEmailStatus(SendStatus.EXPIRED);
                }
            }
            scheduleRepository.saveAll(expiredSchedules);
        };
    }


}
