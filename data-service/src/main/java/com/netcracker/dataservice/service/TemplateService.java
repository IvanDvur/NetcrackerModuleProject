package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.model.TariffConfig;
import com.netcracker.dataservice.model.Template;
import com.netcracker.dataservice.repositories.CustomerRepository;
import com.netcracker.dataservice.repositories.TemplateRepository;
import com.netcracker.dataservice.security.JwtService;
import com.netcracker.dataservice.service.converters.HtmlToImageConverter;
import dto.TemplateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final CustomerRepository customerRepository;
    private String basedir = "/app/templates";
    private final JwtService jwtService;
    private final HtmlToImageConverter htmlConverter;

    public ResponseEntity<List<TemplateDto>> getUserTemplatePreviews(String token) {
        List<TemplateDto> clientTemplates = new ArrayList<>();
        Customer customer = getCustomerFromJwt(token);
        List<Template> templates = templateRepository.findAllByUserId(customer.getId());
        templates.forEach(x -> {
            String jsonFilePath = x.getFilePath() + "/" + x.getId() + ".json";
            String htmlFilePath = x.getFilePath() + "/" + x.getId() + ".html";
            String imageFilePath = x.getFilePath() + "/" + x.getId() + ".png";
            try {
                byte[] html = Files.readAllBytes(new File(htmlFilePath).toPath());
                byte[] json = Files.readAllBytes(new File(jsonFilePath).toPath());
                byte[] image = Files.readAllBytes(new File(imageFilePath).toPath());
                clientTemplates.add(new TemplateDto(String.valueOf(x.getId()), html, json, image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return new ResponseEntity<>(clientTemplates, HttpStatus.OK);
    }

    public ResponseEntity<Void> saveTemplate(String token, String htmlFile, String jsonFile) {
        Customer customer = getCustomerFromJwt(token);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(!checkTariff(customer)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UUID templateId = UUID.randomUUID();
        File saveDir = new File(basedir + File.separator + customer.getId() + File.separator + templateId);
        Template template = new Template(templateId, customer.getId(), saveDir.toString());
        templateRepository.save(template);
        saveDir.mkdirs();
        File htmlSaveFile = new File(saveDir + "/" + template.getId() + ".html");
        File jsonSaveFile = new File(saveDir + "/" + template.getId() + ".json");
        String imageFilePath = template.getFilePath() + "/" + template.getId() + ".png";
        try (BufferedWriter htmlBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlSaveFile), StandardCharsets.UTF_8));
             BufferedWriter jsonBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jsonSaveFile), StandardCharsets.UTF_8))) {
            htmlConverter.convertToImage(htmlFile, imageFilePath);
            htmlBw.write(htmlFile);
            jsonBw.write(jsonFile);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    private Customer getCustomerFromJwt(String token) {
        String jwt = token.substring(7);
        String customerUsername = jwtService.extractUsername(jwt);
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(customerUsername);
        return optionalCustomer.orElse(null);
    }

    public ResponseEntity<TemplateDto> getUserTemplateById(String token, String id) {
        Customer customer = getCustomerFromJwt(token);
        Optional<Template> fileData = templateRepository.findById(UUID.fromString(id));
        if (fileData.isPresent()) {
            String templatePath = fileData.get().getFilePath();
            String jsonFilePath = templatePath + "/" + id + ".json";
            String htmlFilePath = templatePath + "/" + id + ".html";
            String imageFilePath = templatePath + "/" + id + ".png";
            try {
                byte[] html = Files.readAllBytes(new File(htmlFilePath).toPath());
                byte[] json = Files.readAllBytes(new File(jsonFilePath).toPath());
                byte[] image = Files.readAllBytes(new File(imageFilePath).toPath());
                return new ResponseEntity<>(new TemplateDto(id, html, json, image), HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Boolean> updateTemplateById(String id, String htmlFile, String jsonFile, String token) {
        if (getCustomerFromJwt(token) == null) {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
        }
        Optional<Template> optionalTemplate = templateRepository.findById(UUID.fromString(id));
        if (optionalTemplate.isPresent()) {
            Template template = optionalTemplate.get();
            String jsonFilePath = template.getFilePath() + "/" + template.getId() + ".json";
            String htmlFilePath = template.getFilePath() + "/" + template.getId() + ".html";
            try (BufferedWriter htmlBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFilePath), StandardCharsets.UTF_8));
                 BufferedWriter jsonBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jsonFilePath), StandardCharsets.UTF_8))) {
                htmlBw.write(htmlFile);
                jsonBw.write(jsonFile);
                return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
    }

    public void deleteTemplateById(String id, String token) {
        Customer customer = getCustomerFromJwt(token);
        if (customer != null) {
            Optional<Template> templateToDelete = templateRepository.findById(UUID.fromString(id));
            if (!templateToDelete.isPresent()) {
                return;
            }
            String path = templateToDelete.get().getFilePath();
            deleteDirectory(new File(path));
            templateRepository.deleteById(UUID.fromString(id));
        }
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    private boolean checkTariff(Customer customer) {
        int nbOfTemlates = templateRepository.findAllByUserId(customer.getId()).size();
        switch (customer.getRole()) {
            case USER:
                return nbOfTemlates < TariffConfig.FREE_MAX_TEMPLATES;
            case USER_PLUS:
                return nbOfTemlates < TariffConfig.PLUS_MAX_TEMPLATES;
            case USER_PREMIUM:
                return nbOfTemlates < TariffConfig.PREMIUM_MAX_TEMPLATES;
        }
        return false;
    }
}
