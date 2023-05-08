package com.netcracker.senderservice.service;


import dto.ClientDto;
import dto.EmailAdvertisement;
import dto.GenericDto;
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
import java.io.IOException;
import java.util.Base64;
import java.util.Date;


@Service
public class MailSender {

    private TemplateParser templateParser;
    private JavaMailSender javaMailSender;
    @Value("${mail.user}")
    private String fromUsername;
    @Autowired
    public MailSender(TemplateParser templateParser, JavaMailSender mailSender) {
        this.javaMailSender = mailSender;
        this.templateParser = templateParser;
    }

    public void send(GenericDto<EmailAdvertisement> emailGenericDto) {
        EmailAdvertisement ea = emailGenericDto.getAdvertisement();
        String template = new String(Base64.getDecoder().decode(ea.getTemplate()));
        MimeMessage msg = javaMailSender.createMimeMessage();

        try {
            msg.setSentDate(new Date());
            msg.setSubject(ea.getTopic());//топик
            msg.setFrom(fromUsername);
            TemplateParser.writeTemplateFile(template);
            for (ClientDto clientDto : emailGenericDto.getClientDtoSet()) {
                InternetAddress address = new InternetAddress(clientDto.getEmail());
                msg.setRecipient(Message.RecipientType.TO, address);
                msg.setContent(templateParser.parseTemplate(clientDto,ea.getPlaceholders()), "text/html;charset=utf-8");
                javaMailSender.send(msg);
                System.out.println("Email has been sent to " + clientDto.getEmail());
            }
        }catch (IOException | TemplateException e){
            e.printStackTrace();
        }catch (MessagingException e){
            e.printStackTrace();
        }finally {
            File file = new File("src/main/resources/templates/template.ftlh");
            if (file.delete()) {
                System.out.println("Темплейт удалён");
            } else System.out.println("Файла /src/main/resources/templates/template.ftlh не обнаружено");
        }

    }




}
