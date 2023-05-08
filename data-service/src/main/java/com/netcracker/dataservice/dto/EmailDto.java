package com.netcracker.dataservice.dto;

import com.google.common.base.Splitter;
import com.netcracker.dataservice.model.advertisement.EmailAdvertisement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    private String template;
    private String topic;
    private Map<String, String> placeholders;
    private File image;


    public static EmailDto convertToDto(EmailAdvertisement ea) {
        String placeholders = ea.getPlaceholders();
        Map<String, String> placeholdersDto = Splitter.on(",")
                .withKeyValueSeparator(":")
                .split(placeholders);
        return new EmailDto(ea.getTemplate(), ea.getTopic(), placeholdersDto, ea.getImage());
    }
}
