package com.netcracker.dataservice.controllers.api;

import com.netcracker.dataservice.model.Template;
import com.netcracker.dataservice.service.TemplateService;
import dto.TemplateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/template")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping("/save")
    public void saveTemplate(@RequestHeader("Authorization") String token,
                             @RequestParam("htmlFile") String htmlFile,
                             @RequestParam("jsonFile") String jsonFile){
        templateService.saveTemplate(token,htmlFile,jsonFile);
    }

    @GetMapping("/get")
    public ResponseEntity<Template> getTemplate(@RequestHeader("Authorization") String token,
                                                @RequestParam String htmlFile,
                                                @RequestParam String jsonFile){
//            TODO: доделать получение темплейта
        return null;
    }


    @GetMapping("/findAll")
    public ResponseEntity<List<TemplateDto>> getUserTemplatePreviews(@RequestHeader("Authorization") String token){
        return templateService.getUserTemplatePreviews(token);
    }


}
