package com.netcracker.dataservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class MailingList {

    @Id
    private UUID id;

    private String name;

    @OneToMany
    private Set<Client> clients;

    @ManyToOne
    private Customer customer;

}
