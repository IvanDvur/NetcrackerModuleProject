package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.Client;
import com.netcracker.dataservice.model.MailingList;
import com.netcracker.dataservice.repositories.MailingListRepository;
import com.netcracker.dataservice.service.converters.CsvParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
public class ImportService {

    private MailingListRepository mailingListRepository;
    private CsvParser csvParser;
    public ImportService(MailingListRepository mailingListRepository, CsvParser csvParser) {
        this.mailingListRepository = mailingListRepository;
        this.csvParser = csvParser;
    }

//    public ResponseEntity<MailingList> createMailingList(MultipartFile file) {
//        Set<Client> clients = csvParser.parseCsvToList(file);
//        MailingList list = new MailingList();
////        mailingListRepository.save()
//        //TODO: заимплементить привязку листа к клиенту
//    }
}
