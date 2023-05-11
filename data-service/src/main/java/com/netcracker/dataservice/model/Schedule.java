package com.netcracker.dataservice.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dto.SendStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность расписания, с внешним ключом на конфиг
 */
@Entity
@Getter
@Setter
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime timeToSend;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    private SendingOrder order;
    @Enumerated(EnumType.STRING)
    private SendStatus emailStatus;
    @Enumerated(EnumType.STRING)
    private SendStatus smsStatus;
    private Integer retriesCount = 0;

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", timeToSend=" + timeToSend +
                ", emailStatus=" + emailStatus +
                ", smsStatus=" + smsStatus +
                ", retriesCount=" + retriesCount +
                '}';
    }


}
