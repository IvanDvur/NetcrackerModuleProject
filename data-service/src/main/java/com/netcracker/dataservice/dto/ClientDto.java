package com.netcracker.dataservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Dto для отправки только необходимой информации на SendManager
 */
@AllArgsConstructor
@Data
public class ClientDto {

    private String firstName;
    private String email;
    private String phoneNumber;

}
