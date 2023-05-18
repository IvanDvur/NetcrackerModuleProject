package com.netcracker.dataservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcracker.dataservice.dto.OrderDto;
import com.netcracker.dataservice.model.*;
import com.netcracker.dataservice.repositories.*;
import com.netcracker.dataservice.security.JwtService;
import com.netcracker.dataservice.service.converters.CsvParser;
import dto.SendStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ObjectMapper mapper;
    private final OrderRepository orderRepository;
    private final ScheduleRepository scheduleRepository;
    private final MailingListRepository mailingListRepository;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;

    /**
     * Метод сохраняющий конфиг в дб
     *
     * @param orderDto - тело запроса
     * @return
     */
    public ResponseEntity<SendingOrder> postOrder(String orderDto, String token) {
        try {
            String jwt = token.substring(7);
            SendingOrder order = mapper.readValue(orderDto, SendingOrder.class);
            JsonNode jsonNodeRoot = mapper.readTree(orderDto);
            JsonNode jsonNodeListId = jsonNodeRoot.get("mailingListId");
            String listId = jsonNodeListId.asText();
            String username = jwtService.extractUsername(jwt);
            Optional<Customer> customer = customerRepository.findByUsername(username);
            if (customer.isPresent()) {
                MailingList mailingList = mailingListRepository.findById(UUID.fromString(listId)).get();
                order.setMailingList(mailingList);
                order.setCustomer(customer.get());
                orderRepository.save(order);
                for (Schedule s : order.getSchedule()) {
                    setInitialStatus(s, order.getSendTypes());
                    s.setOrder(order);
                    scheduleRepository.save(s);
                }
                return new ResponseEntity<>(order, HttpStatus.CREATED);
            }
            else {
                throw new UsernameNotFoundException("User not found");
            }
        } catch (JsonProcessingException | UsernameNotFoundException e) {
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
//    public ResponseEntity<List<Client>> getClientsByOrderId(UUID uuid) {
//        List<Client> customersById = clientRepository.getAllByOrderId(uuid);
//        return new ResponseEntity<>(customersById, HttpStatus.OK);
//    }

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
        List<Schedule> schedules = scheduleRepository.findAllByTimeToSendBetweenAndEmailStatusOrSmsStatus(dt, timeOfRequest.plusMinutes(30),
                SendStatus.WAITING, SendStatus.WAITING);
        Set<SendingOrder> orders = schedules.stream().map(x -> x.getOrder()).collect(Collectors.toSet());
        List<OrderDto> orderDtos = new ArrayList<>();

        for (SendingOrder c : orders) {
            Set<Schedule> timeToSend = c.getSchedule()
                    .stream()
                    .filter(s -> s.getTimeToSend().isBefore(timeOfRequest.plusMinutes(30))
                            && s.getTimeToSend().isAfter(timeOfRequest))
                    .collect(Collectors.toSet());
            orderDtos.add(OrderDto.convertToDto(c, timeToSend));
        }
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }


    private void setInitialStatus(Schedule schedule, String sendTypes) {
        if (sendTypes.contains("SMS")) {
            schedule.setSmsStatus(SendStatus.WAITING);
        } else {
            schedule.setSmsStatus(SendStatus.NOT_REQUESTED);
        }
        if (sendTypes.contains("EMAIL")) {
            schedule.setEmailStatus(SendStatus.WAITING);
        } else {
            schedule.setEmailStatus(SendStatus.NOT_REQUESTED);
        }
    }
}
