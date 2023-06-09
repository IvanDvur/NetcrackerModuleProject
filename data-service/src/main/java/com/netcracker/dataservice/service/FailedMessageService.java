package com.netcracker.dataservice.service;

import com.netcracker.dataservice.dto.FailedDto;
import com.netcracker.dataservice.model.Schedule;
import com.netcracker.dataservice.repositories.ScheduleRepository;
import dto.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FailedMessageService {

    private ScheduleRepository scheduleRepository;

    @Autowired
    public FailedMessageService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<FailedDto> getFailedOrders(){
        List<SendStatus> statusCheck = List.of(SendStatus.NOT_SENT,SendStatus.FAILED);
        List<Schedule> failedSchedules = scheduleRepository.findAllBySmsStatusIsInOrEmailStatusIsIn(statusCheck,statusCheck);
        List<FailedDto>failedDtos = new ArrayList<>();

        failedSchedules.forEach(schedule -> {
            SendStatus smsStatus=schedule.getSmsStatus();
            SendStatus emailStatus = schedule.getEmailStatus();
            ArrayList<String> retryTypes = new ArrayList<>();
            if (emailStatus.equals(SendStatus.NOT_SENT)
                    ||emailStatus.equals(SendStatus.FAILED)){
                retryTypes.add("EMAIL");

            }if(smsStatus.equals(SendStatus.NOT_SENT)
                    ||smsStatus.equals(SendStatus.FAILED)){
                retryTypes.add("SMS");
            }
            failedDtos.add(FailedDto.convertToDto(schedule.getOrder(),schedule.getId().toString(),retryTypes));
        });

        return failedDtos;

    }
}
