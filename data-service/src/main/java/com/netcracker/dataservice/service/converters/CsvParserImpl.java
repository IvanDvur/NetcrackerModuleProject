package com.netcracker.dataservice.service.converters;

import com.netcracker.dataservice.model.Client;
import com.netcracker.dataservice.repositories.ClientRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Парсер списка клиентов из .csv в Set
 */
@Component
public class CsvParserImpl implements CsvParser {

    ClientRepository clientRepository;

    public CsvParserImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Set<Client> parseCsvToList(MultipartFile file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(),"UTF-8"))) {
            List<Client> clientList = new CsvToBeanBuilder(br)
                    .withType(Client.class)
                    .build()
                    .parse();
            return new HashSet<>(clientList);
        } catch (IOException e) {
            System.out.println("Ошибка чтения-записи");
            e.printStackTrace();
        }
        return Set.of();
    }
}
