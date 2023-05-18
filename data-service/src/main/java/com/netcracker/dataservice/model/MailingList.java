package com.netcracker.dataservice.model;

import lombok.*;

import javax.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter

public class MailingList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Client> clients;

    @ManyToOne
    private Customer customer;

    public MailingList(String name, Set<Client> clients, Customer customer) {
        this.name = name;
        this.clients = clients;
        this.customer = customer;
    }
}
