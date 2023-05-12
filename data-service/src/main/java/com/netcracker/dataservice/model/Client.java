package com.netcracker.dataservice.model;

import com.google.common.base.Splitter;
import com.opencsv.bean.CsvBindByName;
import dto.ClientDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @CsvBindByName(column = "GENDER")
    private Boolean gender;

    @CsvBindByName(column = "PHONE_NUMBER")
    private String phoneNumber;

    @CsvBindByName(column = "BIRTHDAY")
    private String birthDay;

    @CsvBindByName(column = "COUNTRY_CODE")
    private Integer countryCode;

    @CsvBindByName(column = "CITY")
    private String city;

    @CsvBindByName(column = "PROPERTIES")
    private String properties;

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
        return new ClientDto(this.firstName, this.email, this.phoneNumber, parseProperties());
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
