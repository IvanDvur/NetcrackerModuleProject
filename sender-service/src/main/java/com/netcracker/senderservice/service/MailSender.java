package com.netcracker.senderservice.service;


import com.netcracker.senderservice.producer.Producer;
import com.sun.mail.util.MailConnectException;
import dto.*;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Base64;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class MailSender {

    private final TemplateParser templateParser;
    private final JavaMailSender javaMailSender;
    private final RestTemplate restTemplate;
    @Value("${mail.user}")
    private String fromUsername;
    private final Producer producer;

    public void send(GenericDto<EmailAdvertisement> dto) {

        EmailAdvertisement ea = dto.getAdvertisement();
        String template = new String(Base64.getDecoder().decode(ea.getTemplate()));
        UpdateStatusDto updateStatusDto = new UpdateStatusDto(dto.getScheduleId(),AdTypes.EMAIL);
        MimeMessage msg = javaMailSender.createMimeMessage();
        try {
            msg.setSentDate(new Date());
            msg.setSubject(ea.getTopic());//топик
            msg.setFrom(fromUsername);
            System.out.println(fromUsername);
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
