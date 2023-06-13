package com.netcracker.dataservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dto.SendStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SendStatusPerClient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JsonIgnore
    private SendingOrder order;

    @OneToOne
    private Client client;
    @Enumerated(EnumType.STRING)
    private SendStatus emailStatusPerClient;
    @Enumerated(EnumType.STRING)
    private SendStatus smsStatusPerClient;

    public SendStatusPerClient(SendingOrder order, Client client, SendStatus emailStatusPerClient, SendStatus smsStatusPerClient) {
        this.order = order;
        this.client = client;
        this.emailStatusPerClient = emailStatusPerClient;
        this.smsStatusPerClient = smsStatusPerClient;
    }
}
