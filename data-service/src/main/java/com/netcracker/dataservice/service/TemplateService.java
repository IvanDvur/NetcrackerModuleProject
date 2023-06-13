package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.model.Template;
import com.netcracker.dataservice.repositories.CustomerRepository;
import com.netcracker.dataservice.repositories.TemplateRepository;
import com.netcracker.dataservice.security.JwtService;
import dto.TemplateDto;
import gui.ava.html.image.generator.HtmlImageGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final CustomerRepository customerRepository;
    private String basedir = "D:/templates";
    private final JwtService jwtService;

    public ResponseEntity<List<TemplateDto>> getUserTemplatePreviews(String token) {
        return null;
    }

    public void saveTemplate(String token, String htmlFile, String jsonFile) {
        Customer customer = getCustomerFromJwt(token);
        if (customer == null) {
            return;
        }
        Template template = new Template(UUID.randomUUID(), customer.getId());
        templateRepository.save(template);
        File dir = new File(basedir + File.separator + customer.getId() + File.separator + template.getId());
        dir.mkdirs();
        File htmlSaveFile = new File(dir + "/" + template.getId() + ".html");
        File jsonSaveFile = new File(dir + "/" + template.getId() + ".json");
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        imageGenerator.setSize(new Dimension(800,1080));
        try (BufferedWriter htmlBw = new BufferedWriter(new FileWriter(htmlSaveFile));
             BufferedWriter jsonBw = new BufferedWriter(new FileWriter(jsonSaveFile));) {
            htmlBw.write(htmlFile);
            jsonBw.write(jsonFile);
            imageGenerator.loadHtml(htmlFile);
            imageGenerator.saveAsImage(new File(dir + "/" + template.getId() + ".png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }


    private Customer getCustomerFromJwt(String token) {
        String jwt = token.substring(7);
        String customerUsername = jwtService.extractUsername(jwt);
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(customerUsername);
        return optionalCustomer.orElse(null);
    }
}
