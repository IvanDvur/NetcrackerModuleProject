package com.netcracker.dataservice.controllers.interservice;

import com.netcracker.dataservice.model.Schedule;
import com.netcracker.dataservice.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:8081","http://localhost:8082"})
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
    @PutMapping("/email")
    public void updateEmailStatus(@RequestParam String id,@RequestParam String status){
        logger.info("Updating status for schedule {}",id);
        updateService.updateEmailStatus(id,status);
    }
    @PutMapping("/sms")
    public void updateSmsStatus(@RequestParam String id,@RequestParam String status){
        logger.info("Updating status for schedule {}",id);
        updateService.updateSmsStatus(id,status);
    }
    @PutMapping
    public void updateSchedule(@RequestBody Schedule schedule){
        logger.info("Updating status for schedule {}",schedule);
        updateService.updateSchedule(schedule);
    }

}
