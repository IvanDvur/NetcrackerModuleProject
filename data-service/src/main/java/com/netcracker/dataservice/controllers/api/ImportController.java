package com.netcracker.dataservice.controllers.api;

import com.netcracker.dataservice.model.MailingList;
import com.netcracker.dataservice.service.ImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/import")
public class ImportController {


    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping
    public ResponseEntity<String> importClients(@RequestParam(value = "file",required = true) MultipartFile file,
                                                     @RequestParam("name") String name,
                                                     @RequestHeader("Authorization") String token) {
        return importService.createMailingList(name,file,token);
    }
}
