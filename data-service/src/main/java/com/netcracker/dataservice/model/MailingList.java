package com.netcracker.dataservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Client> clients;

    @JsonIgnore
    @ManyToOne
    private Customer customer;

    @Transient
    private int quantityOfClients;

    public MailingList(String name, Set<Client> clients, Customer customer) {
        this.name = name;
        this.clients = clients;
        this.customer = customer;
    }
}
