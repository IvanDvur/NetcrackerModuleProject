package com.netcracker.senderservice.service;

import dto.ClientDto;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class TemplateParser {

    private Configuration configuration;

    @Autowired
    public TemplateParser(Configuration configuration) {
        this.configuration=configuration;
        try {
            File templateDir = new File("sender-service/src/main/resources/templates");
            if(!templateDir.exists()){
                templateDir.mkdirs();
                System.out.println("Директория создана");
            }
            configuration.setDirectoryForTemplateLoading(templateDir);
        } catch (IOException e) {
            System.out.println("Некорректная директория для сохранения темплейта");
            e.printStackTrace();
        }
    }

    public String parseTemplate(ClientDto client) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, ClientDto> model = new HashMap<>();
        model.put("user", client);
        configuration.getTemplate("template.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    public static void writeTemplateFile(String template) {
        File file = new File("sender-service/src/main/resources/templates/template.ftlh");
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {
            bf.write(template);
        } catch (IOException e) {
            System.err.println("Не удалось записать файл");
            e.printStackTrace();
        }
    }
}
