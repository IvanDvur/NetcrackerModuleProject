package com.netcracker.dataservice.controllers;


import com.netcracker.dataservice.dto.OrderDto;
import com.netcracker.dataservice.model.Client;
import com.netcracker.dataservice.model.SendingOrder;
import com.netcracker.dataservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ModelAttribute("order")
    public SendingOrder messageOrder() {
        return new SendingOrder();
    }


    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrdersByDate() {
        ResponseEntity<List<OrderDto>> orderDtos =  orderService.getOrdersByDate();
        logger.info("Fetching orders {}", orderDtos);
        return orderDtos;
    }

    /**
     * Добавляет новый конфиг в дб
     *
     * @param orderDto
     * @param file
     */
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<SendingOrder> postOrder(@RequestParam("model") String orderDto) {
       ResponseEntity<SendingOrder> newOrder = orderService.postOrder(orderDto);
       logger.info("Adding new order to database {}",newOrder.getBody());
       return newOrder;
    }

    /**
     * Получаем клиентов по id конфигурации
     *
     * @param uuid
     * @return
     */
    @GetMapping("/getClientsByOrder/{id}")
    public ResponseEntity<List<Client>> getClientsByOrderId(@PathVariable("id") UUID uuid) {
        ResponseEntity<List<Client>> clients = orderService.getClientsByOrderId(uuid);
        logger.info("Fetching client from order {}",uuid);
        return clients;
    }

    /**
     * Возвращает конфиг по его id
     *
     * @param uuid
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<SendingOrder> getOrderById(@PathVariable("id") UUID uuid) {
        return orderService.getOrderById(uuid);
    }


    /**
     * Удаляет конфиг по его id
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public void deleteOrderById(@PathVariable("id") UUID id) {
        orderService.deleteOrder(id);
    }

}
