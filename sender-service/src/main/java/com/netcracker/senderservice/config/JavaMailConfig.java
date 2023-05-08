package com.netcracker.senderservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailConfig {

    @Value("${mail.user}")
    private String USER;
    @Value("${mail.password}")
    private String PASSWORD;
    @Value("${spring.mail.properties.mail.smtp.host}")
    private String HOST_SMTP;
    @Value("${spring.mail.properties.mail.smtp.port}")
    private Integer PORT;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private Boolean enableAuth;
    @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
    private Boolean enableSSL;

    @Bean
    public JavaMailSender javaMailSender(){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(HOST_SMTP);
        mailSender.setPort(PORT);
        mailSender.setUsername(USER);
        mailSender.setPassword(PASSWORD);
        mailSender.setDefaultEncoding("UTF-8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth",enableAuth);
        javaMailProperties.put("mail.smtp.ssl.enable",enableSSL);
        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }
}
