package com.netcracker.dataservice.controllers;

import com.netcracker.dataservice.model.MailingList;
import com.netcracker.dataservice.service.ImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/import")
public class ImportController {


    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

//    @PostMapping
//    public ResponseEntity<MailingList> importClients(MultipartFile file) {
//        return importService.createMailingList(file);
    //TODO: Доделать importservice и раскоментить
//    }
}
