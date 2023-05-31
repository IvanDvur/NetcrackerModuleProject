package com.netcracker.dataservice.service.converters;

import com.netcracker.dataservice.model.Client;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface CsvParser {
   Set<Client> parseCsvToList(MultipartFile file);
}
