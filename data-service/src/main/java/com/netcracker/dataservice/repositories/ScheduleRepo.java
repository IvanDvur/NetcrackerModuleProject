package com.netcracker.dataservice.repositories;

import com.netcracker.dataservice.model.Schedule;
import com.netcracker.dataservice.model.SendStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepo extends CrudRepository<Schedule, UUID> {

    List<Schedule> findAllByTimeToSendBetweenAndSendStatusOrSendStatus(LocalDateTime dateTimeOfRequest, LocalDateTime endRange, SendStatus waitingStatus,SendStatus failedStatus);

}
