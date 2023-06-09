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
public class MailingListDto {
    private UUID id;

    private String name;
    private Set<Client> clients;


    public static MailingListDto convertMailingListToDto(MailingList mailingList){
       return new MailingListDto(mailingList.getId(),mailingList.getName(),mailingList.getClients());
    }

}
