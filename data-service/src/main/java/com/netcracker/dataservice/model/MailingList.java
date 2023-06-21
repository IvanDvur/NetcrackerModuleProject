package com.netcracker.dataservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JoinColumn(name = "mailing_list_id")
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

    @Override
    public String toString() {
        return "MailingList{" +
                "name='" + name + '\'' +
                '}';
    }
}
