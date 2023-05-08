package com.netcracker.dataservice.model;

import com.netcracker.dataservice.model.advertisement.EmailAdvertisement;
import com.netcracker.dataservice.model.advertisement.MessengerAdvertisement;
import com.netcracker.dataservice.model.advertisement.SmsAdvertisement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * Сущность конфигурации рассылки
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class SendingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;

    private String name;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private EmailAdvertisement emailAdvertisement;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private SmsAdvertisement smsAdvertisement;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private MessengerAdvertisement messengerAdvertisement;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private Set<Client> clients;

    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Schedule> schedule;

    private String sendTypes;

    /**
     * Метод toString() переопределён для избежания циклических ссылок между Schedule и SendingOrder
     * @return
     */
    @Override
    public String toString() {
        return "SendingOrder{" +
                "id=" + id +
                ", customer=" + customer +
                ", name='" + name + '\'' +
                ", emailAdvertisement=" + emailAdvertisement +
                ", smsAdvertisement=" + smsAdvertisement +
                ", messengerAdvertisement=" + messengerAdvertisement +
                ", clients=" + clients +
                ", sendTypes='" + sendTypes + '\'' +
                '}';
    }
}
