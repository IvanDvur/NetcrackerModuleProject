package com.netcracker.senderservice.consumers;

import com.netcracker.senderservice.service.MailSender;
import com.netcracker.senderservice.service.SmsSender;
import dto.GenericDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;

@Service
public class Consumer {

    private SmsSender smsSender;
    private MailSender mailSend;

    @Autowired
    public Consumer(MailSender mailSend, SmsSender smsSender) {
        this.smsSender = smsSender;
        this.mailSend = mailSend;
    }

    @KafkaListener(topics = "t.sms", groupId = "my_group")
    public void listenSms(GenericDto json) {
        if (json == null) {
            System.out.println("SMS Null check");
        } else {
            try {
                smsSender.send(json);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @KafkaListener(topics = "t.email", groupId = "my_group")
    public void listenEmail(GenericDto json) {
        if (json == null) {
            System.out.println("Null Skip");
        } else {
            mailSend.send(json);
        }
    }

}