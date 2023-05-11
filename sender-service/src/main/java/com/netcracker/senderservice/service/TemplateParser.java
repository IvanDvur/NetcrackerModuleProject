package com.netcracker.senderservice.service;

import dto.ClientDto;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
public class TemplateParser {

    private Configuration configuration;

    public TemplateParser(Configuration configuration) {
        this.configuration=configuration;
        try {
            File templateDir = new File("src/main/resources/templates/");
            if(templateDir.isDirectory() && !templateDir.exists()){
                templateDir.mkdir();
            }
            configuration.setDirectoryForTemplateLoading(templateDir);
        } catch (IOException e) {
            System.out.println("Некорректная директория для сохранения темплейта");
            e.printStackTrace();
        }
    }

    public String parseTemplate(ClientDto client, Map<String,String> placeholders) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, String> model = placeholders;
        model.put("user", client.getFirstName());
        configuration.getTemplate("template.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    static void writeTemplateFile(String template) {
        File file = new File("src/main/resources/templates/template.ftlh");
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {
            bf.write(template);
        } catch (IOException e) {
            System.err.println("Не удалось записать файл");
            e.printStackTrace();
        }
    }
}
