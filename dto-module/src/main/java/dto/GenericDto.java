package dto;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

public class GenericDto<T> {

    private T advertisement;
    private Set<ClientDto> clientDtoSet;
    private String scheduleId;

    public GenericDto(T advertisement, Set<ClientDto> clientDtoSet,String  scheduleId) {
        this.advertisement = advertisement;
        this.clientDtoSet = clientDtoSet;
        this.scheduleId = scheduleId;
    }

    public GenericDto() {
    }

    public String  getScheduleId() {
        return scheduleId;
    }

    public void setSchedule(String  scheduleId) {
        this.scheduleId = scheduleId;
    }

    public T getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(T advertisement) {
        this.advertisement = advertisement;
    }

    public Set<ClientDto> getClientDtoSet() {
        return clientDtoSet;
    }

    public void setClientDtoSet(Set<ClientDto> clientDtoSet) {
        this.clientDtoSet = clientDtoSet;
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
