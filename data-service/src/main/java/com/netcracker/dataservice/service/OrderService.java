package com.netcracker.dataservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcracker.dataservice.dto.OrderDto;
import com.netcracker.dataservice.model.*;
import com.netcracker.dataservice.repositories.ClientRepository;
import com.netcracker.dataservice.repositories.OrderRepository;
import com.netcracker.dataservice.repositories.ScheduleRepo;
import com.netcracker.dataservice.service.converters.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ObjectMapper mapper;
    private final CsvParser csvParser;
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ScheduleRepo scheduleRepo;

    @Autowired
    public OrderService(ObjectMapper mapper,
                        CsvParser csvParser,
                        OrderRepository orderRepository,
                        ClientRepository clientRepository,
                        ScheduleRepo scheduleRepo) {
        this.mapper = mapper;
        this.csvParser = csvParser;
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.scheduleRepo = scheduleRepo;
    }

    /**
     * Метод сохраняющий конфиг в дб
     *
     * @param orderDto - тело конфига
     * @param file      - .csv файл с клиентами
     * @return
     */
    public ResponseEntity<SendingOrder> postOrder(String orderDto, MultipartFile file) {
        try {
            SendingOrder order = mapper.readValue(orderDto, SendingOrder.class);
            Customer customer = new Customer("NikePR", "nikePR@gmail.com", "password");
            order.setCustomer(customer);
            order.setClients(csvParser.parseCsvToList(file));
            orderRepository.save(order);
            for (Schedule s : order.getSchedule()) {
                s.setOrder(order);
                scheduleRepo.save(s);
            }
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Метод возвращает всех клиентов по id конфига
     *
     * @param uuid
     * @return
     */
    public ResponseEntity<List<Client>> getClientsByOrderId(UUID uuid) {
        List<Client> customersById = clientRepository.getAllByOrderId(uuid);
        return new ResponseEntity<>(customersById, HttpStatus.OK);
    }

    /**
     * Возвращает конфиг по его id
     *
     * @param uuid
     * @return
     */
    public ResponseEntity<SendingOrder> getOrderById(UUID uuid) {
        Optional<SendingOrder> messageOrder = orderRepository.findById(uuid);
        if (messageOrder.isPresent()) {
            return new ResponseEntity<>(messageOrder.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Удаляет Order и все принадлежащие ему сущности из дб
     *
     * @param id
     */
    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }

    /**
     * Метод возвращающий список OrderDto в временном диапазоне в 15 минут(вперёд) с момента запроса
     *
     * @return
     */
    public ResponseEntity<List<OrderDto>> getOrdersByDate() {
        LocalDateTime dt = LocalDateTime.now();
        LocalDateTime timeOfRequest = LocalDateTime.of(
                dt.getYear(),
                dt.getMonthValue(),
                dt.getDayOfMonth(),
                dt.getHour(),
                dt.getMinute());
        List<Schedule> schedules = scheduleRepo.findAllByTimeToSendBetweenAndSendStatusOrSendStatus(dt, timeOfRequest.plusMinutes(30),
                SendStatus.WAITING,
                SendStatus.FAILED);
        Set<SendingOrder> orders = schedules.stream().map(x -> x.getOrder()).collect(Collectors.toSet());
        List<OrderDto> orderDtos = new ArrayList<>();

        for (SendingOrder c : orders) {
            Set<Schedule> timeToSend = c.getSchedule()
                    .stream()
                    .filter(s -> s.getTimeToSend().isBefore(timeOfRequest.plusMinutes(30))
                            && s.getTimeToSend().isAfter(timeOfRequest) || s.getSendStatus().equals(SendStatus.FAILED))
                    .collect(Collectors.toSet());
            orderDtos.add(OrderDto.convertToDto(c, timeToSend));
        }
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

//        Orders.forEach(Order -> orderDtos.add(OrderDto.convertToDto(Order)));
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }
}
