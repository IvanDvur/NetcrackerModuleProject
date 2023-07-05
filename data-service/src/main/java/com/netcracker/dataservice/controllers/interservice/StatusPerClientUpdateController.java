package com.netcracker.dataservice.controllers.interservice;

import com.netcracker.dataservice.model.SendStatusPerClient;
import com.netcracker.dataservice.service.StatusPerClientUpdateService;
import dto.StatusPerClientUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/statusPerClient")
public class StatusPerClientUpdateController {

    private final StatusPerClientUpdateService service;

    @PostMapping("/emailPerClientStatus")
    public void updateEmailStatus(@RequestBody StatusPerClientUpdateRequest request){
        service.updateEmailStatuses(request);
    }

    @PostMapping("/smsPerClientStatus")
    public void updateSmsStatus(@RequestBody StatusPerClientUpdateRequest request){
        service.updateSmsStatuses(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<SendStatusPerClient>> getStatusesByOrderId(@PathVariable("id") String id){
        return service.getStatusesByOrderId(id);
    }

}
