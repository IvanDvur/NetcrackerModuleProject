package com.netcracker.dataservice.controllers.interservice;

import com.netcracker.dataservice.dto.FailedDto;
import com.netcracker.dataservice.service.ExpiredMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:8081","http://localhost:8082"})
@RequestMapping("/expired")
public class ExpiredOrderController {

    ExpiredMessageService expiredMessageService;

    @Autowired
    public ExpiredOrderController(ExpiredMessageService expiredMessageService) {
        this.expiredMessageService = expiredMessageService;
    }

    @GetMapping
    public ResponseEntity<List<FailedDto>> getExpiredDtos(){
        return expiredMessageService.getExpiredDtos();
    }


}
