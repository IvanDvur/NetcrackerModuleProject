package com.netcracker.dataservice.controllers.api;


import com.netcracker.dataservice.dto.MailingLIstDto;
import com.netcracker.dataservice.model.Client;
import com.netcracker.dataservice.model.MailingList;
import com.netcracker.dataservice.service.MailingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/lists")
@RequiredArgsConstructor
public class MailingListController {

    private final MailingListService mailingListService;

    @GetMapping
    public ResponseEntity<Set<MailingList>> getMailingLists(@RequestHeader("Authorization") String token){
        return this.mailingListService.getMailingLists(token);
    }

    @PostMapping("/delete")
    public void deleteMailingList(@RequestParam("id") String id,@RequestHeader("Authorization") String token){
        System.out.println("Удаление");
        this.mailingListService.deleteMailingList(id,token);
        System.out.println("Лист удалён");
    }

    @GetMapping("/{id}")
    public ResponseEntity<MailingLIstDto>getMailingListById(@PathVariable String id ){
        return this.mailingListService.getMailingListById(id);
    }
    @PostMapping("/update/client")
    public ResponseEntity<Boolean> updateClient(@RequestBody Client client){
        return this.mailingListService.updateClient(client);

    }
    @PostMapping("/delete/client")
    public void deleteClient(@RequestParam("id") String id){
        this.mailingListService.deleteClient(id);

    }

}
