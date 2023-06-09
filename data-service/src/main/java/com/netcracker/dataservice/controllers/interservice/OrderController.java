package com.netcracker.dataservice.controllers.interservice;


import com.netcracker.dataservice.dto.OrderDto;
import com.netcracker.dataservice.model.SendingOrder;
import com.netcracker.dataservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping
    @CrossOrigin(origins = {"http://localhost:8081","http://localhost:8082"})
    public ResponseEntity<List<OrderDto>> getOrdersByDate() {
        ResponseEntity<List<OrderDto>> orderDtos =  orderService.getOrdersByDate();
        logger.info("Fetching orders {}", orderDtos);
        return orderDtos;
    }

    /**
     * Добавляет новый конфиг в дд
     * @param orderDto
     *
     */
    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<SendingOrder> postOrder(@RequestBody String orderDto,@RequestHeader("Authorization") String token) {
       ResponseEntity<SendingOrder> newOrder = orderService.postOrder(orderDto,token);
       logger.info("Adding new order to database {}",newOrder.getBody());
       return newOrder;
    }


    /**
     * Возвращает конфиг по его id
     *
     * @param uuid
     * @return
     */
    @GetMapping("/active")
    public ResponseEntity<Set<SendingOrder>> getAllOrdersByCustomer(@RequestHeader("Authorization") String token) {
        return orderService.getAllOrdersByCustomer(token);
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
