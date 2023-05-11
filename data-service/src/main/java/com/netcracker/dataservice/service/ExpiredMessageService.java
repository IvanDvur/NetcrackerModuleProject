package com.netcracker.dataservice.service;

import com.netcracker.dataservice.dto.FailedDto;
import com.netcracker.dataservice.model.Schedule;
import com.netcracker.dataservice.repositories.ScheduleRepo;
import dto.SendStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpiredMessageService {

    ScheduleRepo scheduleRepo;

    public ExpiredMessageService(ScheduleRepo scheduleRepo) {
        this.scheduleRepo = scheduleRepo;
    }

    public ResponseEntity<List<FailedDto>> getExpiredDtos(){
        List<SendStatus> expiredStatus = List.of(SendStatus.EXPIRED);
        List<Schedule> expiredSchedule = scheduleRepo.findAllBySmsStatusIsInOrEmailStatusIsIn(expiredStatus,expiredStatus);
        List<FailedDto> expiredDtos = new ArrayList<>();
        expiredSchedule.forEach(schedule -> {
            SendStatus smsStatus=schedule.getSmsStatus();
            SendStatus emailStatus = schedule.getEmailStatus();
            ArrayList<String> retryTypes = new ArrayList<>();
            if (emailStatus.equals(SendStatus.EXPIRED)){
                retryTypes.add("EMAIL");
            }if(smsStatus.equals(SendStatus.EXPIRED)){
                retryTypes.add("SMS");
            }
//            else {
//                retryTypes.add("MESSENGER");
//            }
            expiredDtos.add(FailedDto.convertToDto(schedule.getOrder(),schedule.getId().toString(),retryTypes));
        });
        return new ResponseEntity<>(expiredDtos, HttpStatus.OK);
    }
}
