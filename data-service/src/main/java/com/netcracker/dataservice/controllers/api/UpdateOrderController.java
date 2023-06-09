package com.netcracker.dataservice.controllers.api;

import com.netcracker.dataservice.model.SendingOrder;
import com.netcracker.dataservice.service.UpdateService;
import dto.AdTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@CrossOrigin(origins = {"http://localhost:8081","http://localhost:8082"})
@RequestMapping("/update")
public class UpdateOrderController {

    private final UpdateService updateService;

    @Autowired
    public UpdateOrderController(UpdateService updateService) {
        this.updateService = updateService;
    }


    /**
     * Изменяет список клиентов (Добавление новых клиентов к уже существующим)
     * @param file
     * @param id
     * @return
     */
    @PatchMapping("/clients/{id}")
    public ResponseEntity<SendingOrder> updateClients(@RequestBody MultipartFile file,
                                                      @PathVariable("id") String id) {
        return updateService.updateClients(file, id);
    }
}
