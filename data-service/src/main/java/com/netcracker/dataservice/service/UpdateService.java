package com.netcracker.dataservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcracker.dataservice.model.*;
import com.netcracker.dataservice.repositories.OrderRepository;
import com.netcracker.dataservice.repositories.ScheduleRepository;
import com.netcracker.dataservice.service.converters.CsvParser;
import dto.AdTypes;
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
    private final ObjectMapper mapper;
    private final OrderRepository orderRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public UpdateService(CsvParser csvParser,
                         OrderRepository orderRepository,
                         ScheduleRepository scheduleRepository) {
        this.csvParser = csvParser;
        this.orderRepository = orderRepository;
        this.scheduleRepository = scheduleRepository;
        this.mapper = new ObjectMapper();
    }

    /**
     * Обновляет конфиг и сохраяняет обновлённую версию в дб
     *
     * @param updatedOrder
     * @param id
     * @return
     */
    public ResponseEntity<SendingOrder> updateOrder(String updatedOrder, String id) {
        Optional<SendingOrder> optionalOrder = orderRepository.findById(UUID.fromString(id));
        if (optionalOrder.isPresent()) {
            try {
                SendingOrder presendOrder = optionalOrder.get();
                SendingOrder newOrder = mapper.readValue(updatedOrder, SendingOrder.class);
                if (newOrder.getName() != null) {
                    presendOrder.setName(newOrder.getName());
                }
                if (newOrder.getEmailAdvertisement() != null) {
                    presendOrder.setEmailAdvertisement(newOrder.getEmailAdvertisement());
                }
                if (newOrder.getSmsAdvertisement() != null) {
                    presendOrder.setSmsAdvertisement(newOrder.getSmsAdvertisement());
                }
                if (newOrder.getMessengerAdvertisement() != null) {
                    presendOrder.setMessengerAdvertisement(newOrder.getMessengerAdvertisement());
                }
                if (newOrder.getSendTypes() != null) {
                    presendOrder.setSendTypes(newOrder.getSendTypes());
                }
                orderRepository.save(presendOrder);
                return new ResponseEntity<>(presendOrder, HttpStatus.OK);
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
     * Удаляет всех клиентов из конфига
     *
     * @param id - id конфига
     */
//    public void deleteClientByOrderId(UUID id) {
//        Optional<SendingOrder> optionalOrder = orderRepository.findById(id);
//        if (optionalOrder.isPresent()) {
//            SendingOrder order = optionalOrder.get();
//            MailingList mailingList = order.getMailingList();
//            order.getClients().removeAll(clients);
//            orderRepository.save(order);
//        }
//    }

    /**
     * Удаляет рекламу указанного типа из конфига
     *
     * @param orderId - id конфига
     * @param type    - тип рекламы
     */
    public void deleteAdvertisementByOrderIdAndType(UUID orderId, AdTypes type) {
        Optional<SendingOrder> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            SendingOrder order = optionalOrder.get();
            switch (type) {
                case SMS:
                    order.setSmsAdvertisement(null);
                    orderRepository.save(order);
                    break;
                case EMAIL:
                    order.setEmailAdvertisement(null);
                    orderRepository.save(order);
                    break;
                case MESSENGER:
                    order.setMessengerAdvertisement(null);
                    orderRepository.save(order);
                    break;
                default:
                    System.out.println("Указанного типа не существует");
            }
        }
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
                schedule.setEmailStatus(SendStatus.EXPIRED);
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
                schedule.setSmsStatus(SendStatus.EXPIRED);
            }
            scheduleRepository.save(schedule);
        }
    }
}
