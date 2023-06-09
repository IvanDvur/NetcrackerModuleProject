package com.netcracker.dataservice.model;

import dto.SendStatus;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class SendStatusPerClient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  UUID id;

    @OneToOne
    private SendingOrder order;

    @OneToOne
    private Client client;
    @Enumerated(EnumType.STRING)
    private SendStatus emailStatusPerClient;
    @Enumerated(EnumType.STRING)
    private SendStatus smsSendStatus;

    public SendStatusPerClient(SendingOrder order, Client client, SendStatus emailStatusPerClient, SendStatus smsSendStatus) {
        this.order = order;
        this.client = client;
        this.emailStatusPerClient = emailStatusPerClient;
        this.smsSendStatus = smsSendStatus;
    }
}
