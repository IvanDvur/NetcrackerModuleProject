package com.netcracker.dataservice.dto;

import com.netcracker.dataservice.model.Client;
import com.netcracker.dataservice.model.MailingList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailingLIstDto {
    private UUID id;

    private String name;
    private Set<Client> clients;


    public static MailingLIstDto convertMailingListToDto(MailingList mailingList){
       return new MailingLIstDto(mailingList.getId(),mailingList.getName(),mailingList.getClients());
    }

}
