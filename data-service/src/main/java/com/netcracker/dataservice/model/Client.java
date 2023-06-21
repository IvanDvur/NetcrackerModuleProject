package com.netcracker.dataservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Splitter;
import com.opencsv.bean.CsvBindByName;
import dto.ClientDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Сущность клиента которому необходимо отправить рекламу
 */
@Entity
@Data
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CsvBindByName(column = "FIRST_NAME")
    private String firstName;

    @CsvBindByName(column = "LAST_NAME")
    private String lastName;

    @CsvBindByName(column = "EMAIL")
    private String email;

    @CsvBindByName(column = "PHONE_NUMBER")
    private String phoneNumber;

    @CsvBindByName(column = "PROPERTIES")
    private String properties;

    @JsonIgnore
    @OneToOne(mappedBy = "client",cascade = CascadeType.ALL,orphanRemoval = true)
    private SendStatusPerClient status;

    /**
     * Метод equals переопределён для сравнения клиентов по номеру телефона и email, при добавлении новых клиентов
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(email, client.email) && Objects.equals(phoneNumber, client.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, phoneNumber);
    }

    public ClientDto convertToDto() {
        return new ClientDto(String.valueOf(this.id),this.firstName, this.email, this.phoneNumber, parseProperties());
    }

    private Map<String, String> parseProperties() {
        String properties = this.getProperties();
        Map<String, String> propertiesDto = null;
        if (properties != null) {
            propertiesDto = Splitter.on("|")
                    .withKeyValueSeparator(":")
                    .split(properties);
        }
        return propertiesDto;

    }
}
