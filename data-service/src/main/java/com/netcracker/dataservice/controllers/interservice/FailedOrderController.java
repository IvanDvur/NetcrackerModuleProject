package com.netcracker.dataservice.controllers.interservice;


import com.netcracker.dataservice.dto.FailedDto;
import com.netcracker.dataservice.service.FailedMessageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:8081","http://localhost:8082"})
@RequestMapping("/failedOrder")
public class FailedOrderController {

    private final FailedMessageService failedMessageService;

    public FailedOrderController(FailedMessageService failedMessageService) {
        this.failedMessageService = failedMessageService;
    }

    @GetMapping
    public List<FailedDto> getOrdersByDate() {
        return failedMessageService.getFailedOrders();
    }
}
