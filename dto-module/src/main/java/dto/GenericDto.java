package dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Getter
@Setter
public class GenericDto<T> {

    private String orderId;
    private T advertisement;
    private Set<ClientDto> clientDtoSet;
    private String scheduleId;

    public GenericDto(String orderId,T advertisement, Set<ClientDto> clientDtoSet,String  scheduleId) {
        this.advertisement = advertisement;
        this.clientDtoSet = clientDtoSet;
        this.scheduleId = scheduleId;
        this.orderId=orderId;
    }

    public GenericDto() {
    }

    public static String prepareStatusUrl(String scheduleId, String baseUrl, String status,String type) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl).pathSegment(type)
                .queryParam("id", scheduleId)
                .queryParam("status", status)
                .build().toUriString();
    }
    @Override
    public String toString() {
        return "GenericDto{" +
                "advertisement=" + advertisement +
                ", clientDtoSet=" + clientDtoSet +
                ", schedule=" + scheduleId +
                '}';
    }
}
