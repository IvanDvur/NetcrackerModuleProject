package com.netcracker.dataservice.repositories;

import com.netcracker.dataservice.model.Schedule;
import dto.SendStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, UUID> {

    List<Schedule> findAllByTimeToSendBetweenAndEmailStatusOrSmsStatus(LocalDateTime dateTimeOfRequest, LocalDateTime endRange, SendStatus waitingStatus, SendStatus smsWaitingStatus);

    List<Schedule> findAllBySmsStatusIsInOrEmailStatusIsIn(Collection<SendStatus> smsStatus, Collection<SendStatus> emailStatus);

    Set<Schedule> findAllByTimeToSendIsBefore(LocalDateTime time);

}
