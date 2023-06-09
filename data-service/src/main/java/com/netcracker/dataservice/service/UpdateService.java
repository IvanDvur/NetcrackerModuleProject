package com.netcracker.dataservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcracker.dataservice.model.*;
import com.netcracker.dataservice.repositories.OrderRepository;
import com.netcracker.dataservice.repositories.ScheduleRepository;
import com.netcracker.dataservice.service.converters.CsvParser;
import dto.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UpdateService {
    @Value("${service.max_retries_count}")
    private Integer maxRetriesCount;
    private final CsvParser csvParser;
    private final OrderRepository orderRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public UpdateService(CsvParser csvParser,
                         OrderRepository orderRepository,
                         ScheduleRepository scheduleRepository) {
        this.csvParser = csvParser;
        this.orderRepository = orderRepository;
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * Добавляет новых клиентов к уже имеющимся и сохраняет их в дб
     *
     * @param file
     * @param id
     * @return
     */
    public ResponseEntity<SendingOrder> updateClients(MultipartFile file, String id) {
        if (file != null && id != null) {
            Optional<SendingOrder> orderToUpdate = orderRepository.findById(UUID.fromString(id));
            if (orderToUpdate.isPresent()) {
                SendingOrder order = orderToUpdate.get();
                Set<Client> clientsToUpdate = new HashSet<>(order.getMailingList().getClients());
                Set<Client> newClients = csvParser.parseCsvToList(file);
                clientsToUpdate.addAll(newClients);
                order.getMailingList().setClients(clientsToUpdate);
                orderRepository.save(order);
                return new ResponseEntity<>(order, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    /**
     * Обновляет статус конфига (WAITING,PROCESSED,FAILED)
     *
     * @param
     * @return
     */
    public void updateSchedule(Schedule schedule) {
        Schedule scheduleToUpdate = scheduleRepository.findById(schedule.getId()).get();
        scheduleToUpdate.setEmailStatus(schedule.getEmailStatus());
        scheduleToUpdate.setSmsStatus(schedule.getSmsStatus());
        scheduleToUpdate.setRetriesCount(schedule.getRetriesCount());
        scheduleRepository.save(scheduleToUpdate);
    }

    public void updateEmailStatus(String id, String status) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(UUID.fromString(id));
        if (optionalSchedule.isPresent()) {
            Schedule schedule = optionalSchedule.get();
            if (status.equals("SENT") || status.equals("PROCESSED")) {
                schedule.setEmailStatus(SendStatus.valueOf(status));
            } else if (schedule.getRetriesCount() < maxRetriesCount && (status.equals("FAILED") || status.equals("NOT_SENT"))) {
                schedule.setEmailStatus(SendStatus.valueOf(status));
                schedule.setRetriesCount(schedule.getRetriesCount() + 1);
            } else {
                schedule.setEmailStatus(SendStatus.FATAL);
            }
            scheduleRepository.save(schedule);
        }
    }

    public  void updateSmsStatus(String id, String status) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(UUID.fromString(id));
        if (optionalSchedule.isPresent()) {
            Schedule schedule = optionalSchedule.get();
            if (status.equals("SENT") || status.equals("PROCESSED")) {
                schedule.setSmsStatus(SendStatus.valueOf(status));
            }else if (schedule.getRetriesCount() < maxRetriesCount && (status.equals("FAILED") || status.equals("NOT_SENT"))) {
                schedule.setSmsStatus(SendStatus.valueOf(status));
                schedule.setRetriesCount(schedule.getRetriesCount() + 1);
            } else {
                schedule.setSmsStatus(SendStatus.FATAL);
            }
            scheduleRepository.save(schedule);
        }
    }
}
