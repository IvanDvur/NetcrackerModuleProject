package com.netcracker.senderservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.EmailAdvertisement;
import dto.GenericDto;
import dto.SmsAdvertisement;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class DtoDeserializer implements Deserializer<GenericDto<?>> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public GenericDto<?> deserialize(String s, byte[] bytes) {
        return null;
    }

    @Override
    public GenericDto<?> deserialize(String topic, Headers headers, byte[] data) {
        objectMapper.registerModule(new JavaTimeModule());
        try {
            if (topic.equals("t.email")) {
                return objectMapper.readValue(new String(data, "UTF-8"), new TypeReference<GenericDto<EmailAdvertisement>>() {
                });
            }else if(topic.equals("t.sms")){
                return objectMapper.readValue(new String(data, "UTF-8"), new TypeReference<GenericDto<SmsAdvertisement>>() {
                });
            }
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("Ошибка парсинга сообщения");
        }
        return Deserializer.super.deserialize(topic, headers, data);
    }

    @Override
    public void close() {

    }
}
