package dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Schedule {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime timeToSend;
    private UUID id;
    private SendStatus emailStatus = SendStatus.WAITING;
    private SendStatus smsStatus = SendStatus.WAITING;
    private Integer retriesCount;
    public Schedule(LocalDateTime timeToSend, UUID id, SendStatus sendStatus,SendStatus smsStatus,Integer retriesCount) {
        this.timeToSend = timeToSend;
        this.id = id;
        this.emailStatus = sendStatus;
        this.smsStatus = smsStatus;
        this.retriesCount = retriesCount;
    }

    public Schedule() {
    }
}
