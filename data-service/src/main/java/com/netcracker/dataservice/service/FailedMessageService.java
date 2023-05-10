package com.netcracker.dataservice.service;

import com.netcracker.dataservice.dto.FailedDto;
import com.netcracker.dataservice.model.Schedule;
import com.netcracker.dataservice.model.SendingOrder;
import com.netcracker.dataservice.repositories.ScheduleRepo;
import dto.AdTypes;
import dto.EmailAdvertisement;
import dto.GenericDto;
import dto.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FailedMessageService {

    private ScheduleRepo scheduleRepo;

    @Autowired
    public FailedMessageService(ScheduleRepo scheduleRepo) {
        this.scheduleRepo = scheduleRepo;
    }

    public List<FailedDto> getFailedOrders(){
        List<SendStatus> statusCheck = List.of(SendStatus.NOT_SENT,SendStatus.FAILED);
        List<Schedule> failedSchedules = scheduleRepo.findAllBySmsStatusIsInOrEmailStatusIsIn(statusCheck,statusCheck);
        List<FailedDto>failedDtos = new ArrayList<>();


        failedSchedules.forEach(schedule -> {
            SendStatus smsStatus=schedule.getSmsStatus();
            SendStatus emailStatus = schedule.getEmailStatus();
            ArrayList<String> retryTypes = new ArrayList<>();
            if (emailStatus.equals(SendStatus.NOT_SENT)
                    ||emailStatus.equals(SendStatus.FAILED)){
                retryTypes.add("EMAIL");

            } if(smsStatus.equals(SendStatus.NOT_SENT)
                    ||smsStatus.equals(SendStatus.FAILED)){
                retryTypes.add("SMS");
            }else {
                retryTypes.add("MESSENGER");
            }
            failedDtos.add(FailedDto.convertToDto(schedule.getOrder(),schedule.getId().toString(),retryTypes));
        });

        return failedDtos;

    }
}
