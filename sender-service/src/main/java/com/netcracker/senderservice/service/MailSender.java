package com.netcracker.senderservice.service;


import com.netcracker.senderservice.producer.Producer;
import com.sun.mail.util.MailConnectException;
import dto.*;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Base64;
import java.util.Date;


@Service
public class MailSender {

    private TemplateParser templateParser;
    private JavaMailSender javaMailSender;
    @Value("${mail.user}")
    private String fromUsername;
    private Producer producer;

    @Autowired
    public MailSender(TemplateParser templateParser, JavaMailSender mailSender, Producer producer) {
        this.javaMailSender=mailSender;
        this.templateParser = templateParser;
        this.producer = producer;
    }

    public void send(GenericDto<EmailAdvertisement> dto) {
        EmailAdvertisement ea = dto.getAdvertisement();
        String template = new String(Base64.getDecoder().decode(ea.getTemplate()));
        MimeMessage msg = javaMailSender.createMimeMessage();
        UpdateStatusDto updateStatusDto = new UpdateStatusDto(dto.getScheduleId(),AdTypes.EMAIL);
        try {
            msg.setSentDate(new Date());
            msg.setSubject(ea.getTopic());//топик
            msg.setFrom(fromUsername);
            TemplateParser.writeTemplateFile(template);
            for (ClientDto clientDto : dto.getClientDtoSet()) {
                InternetAddress address = new InternetAddress(clientDto.getEmail());
                msg.setRecipient(Message.RecipientType.TO, address);
                msg.setContent(templateParser.parseTemplate(clientDto), "text/html;charset=utf-8");
                javaMailSender.send(msg);
                System.out.println("Email has been sent to " + clientDto.getEmail());
            }
            //Апдейтим статус через кафку
            producer.sendMessage("t.success",updateStatusDto);
        } catch (Exception e) {
            producer.sendMessage("t.error",updateStatusDto);
            e.printStackTrace();
        } finally {
            File file = new File("sender-service/src/main/resources/templates/template.ftlh");
            if (file.delete()) {
                System.out.println("Темплейт удалён");
            } else System.out.println("Файла /src/main/resources/templates/template.ftlh не обнаружено");
        }

    }


}
