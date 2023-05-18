package com.netcracker.dataservice.controllers;


import com.netcracker.dataservice.dto.FailedDto;
import com.netcracker.dataservice.dto.OrderDto;
import com.netcracker.dataservice.service.FailedMessageService;
import dto.GenericDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/failedOrder")
public class FailedOrderController {

    FailedMessageService failedMessageService;

    public FailedOrderController(FailedMessageService failedMessageService) {
        this.failedMessageService = failedMessageService;
    }

    @GetMapping
    public List<FailedDto> getOrdersByDate() {
        List<FailedDto> orderDtos =  failedMessageService.getFailedOrders();
        return orderDtos;
    }
}
