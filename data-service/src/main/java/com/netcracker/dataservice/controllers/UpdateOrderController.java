package com.netcracker.dataservice.controllers;

import com.netcracker.dataservice.model.SendingOrder;
import com.netcracker.dataservice.service.UpdateService;
import dto.AdTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/update")
public class UpdateOrderController {

    private final UpdateService updateService;

    @Autowired
    public UpdateOrderController(UpdateService updateService) {
        this.updateService = updateService;
    }

    /**
     * Изменяет тело конфига
     * @param updatedOrder - изменённый конфиг
     * @param id id изменяемого конфига
     * @return
     */
    @PatchMapping("/order/{id}")
    public ResponseEntity<SendingOrder> updateConfig(@RequestBody String updatedOrder,
                                                     @PathVariable("id") String id) {
        return updateService.updateOrder(updatedOrder, id);
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

    /**
     * Очищает список клиентов для конфига и удаляет их из дб
     * @param id - id конфига, клиентов которого требуется удалить
     */
    @DeleteMapping("/deleteClients/{id}")
    public void deleteClientsByConfigId(@PathVariable("id") UUID id){
        updateService.deleteClientByOrderId(id);
    }

    /**
     * Удаляет рекламу заданного типа из конфига и из дб
     * @param orderId - id конфига, рекламу которого нужно удалить
     * @param type - тип рекламы (SMS,EMAIL,MESSENGER)
     */
    @DeleteMapping("/deleteAd/{orderId}/{type}")
    public void deleteAdvetisementByConfigId(@PathVariable("orderId") UUID orderId,
                                             @PathVariable("type") AdTypes type){
        updateService.deleteAdvertisementByOrderIdAndType(orderId,type);
    }


}
