package com.netcracker.dataservice.controllers;

import com.netcracker.dataservice.model.Schedule;
import com.netcracker.dataservice.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final UpdateService updateService;
    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    public ScheduleController(UpdateService updateService) {
        this.updateService = updateService;
    }

    /**
     * Api для SendManager для изменения статуса конфига
     * @param schedule
     * @return
     */
    @PutMapping
    public void updateSchedule(@RequestBody Schedule schedule){
        logger.info("Updating status for schedule {}",schedule);
        updateService.updateSchedule(schedule);
    }

}
