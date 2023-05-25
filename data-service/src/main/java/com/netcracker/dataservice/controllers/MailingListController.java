package com.netcracker.dataservice.controllers;


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

}
