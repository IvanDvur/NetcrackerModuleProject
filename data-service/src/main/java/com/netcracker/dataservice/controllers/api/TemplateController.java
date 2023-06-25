package com.netcracker.dataservice.controllers.api;

import com.netcracker.dataservice.model.Template;
import com.netcracker.dataservice.service.TemplateService;
import dto.TemplateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Void> saveTemplate(@RequestHeader("Authorization") String token,
                                                   @RequestParam("htmlFile") String htmlFile,
                                                   @RequestParam("jsonFile") String jsonFile) {
        return templateService.saveTemplate(token, htmlFile, jsonFile);
    }

    @GetMapping("/get")
    public ResponseEntity<TemplateDto> getTemplate(@RequestHeader("Authorization") String token, @RequestParam("templateId") String id) {
        return templateService.getUserTemplateById(token, id);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<TemplateDto>> getUserTemplatePreviews(@RequestHeader("Authorization") String token) {
        return templateService.getUserTemplatePreviews(token);
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateTemplateById(@RequestParam("id") String templateId,
                                                      @RequestParam("html") String htmlFile,
                                                      @RequestParam("json") String jsonFile,
                                                      @RequestHeader("Authorization") String token) {
        return templateService.updateTemplateById(templateId, htmlFile, jsonFile, token);
    }

    @PostMapping("/delete/{id}")
    public void deleteTemplateById(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
        this.templateService.deleteTemplateById(id,token);
    }


}
