package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.Schedule;
import com.netcracker.dataservice.model.SendingOrder;
import com.netcracker.dataservice.repositories.ScheduleRepo;
import dto.GenericDto;
import dto.SendStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class FailedMessageService {

    private ScheduleRepo scheduleRepo;


    public ResponseEntity<List<GenericDto>> getFailedOrders(){
        List<SendStatus> statusCheck = List.of(SendStatus.NOT_SENT,SendStatus.FAILED);
        List<Schedule> failedSchedules = scheduleRepo.findAllBySmsStatusIsInOrEmailStatusIsIn(statusCheck,statusCheck);

    }
}
