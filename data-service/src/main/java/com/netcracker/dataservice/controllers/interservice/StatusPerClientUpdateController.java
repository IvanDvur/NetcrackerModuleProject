package com.netcracker.dataservice.controllers.interservice;

import com.netcracker.dataservice.service.StatusPerClientUpdateService;
import dto.StatusPerClientUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statusPerClient")
public class StatusPerClientUpdateController {

    private final StatusPerClientUpdateService service;

    @PostMapping()
    public void updateStatus(@RequestBody StatusPerClientUpdateRequest request){
        service.updateStatuses(request);
    }

}
