package dto;

import java.util.Set;

public class GenericDto<T> {

    public T advertisement;
    public Set<ClientDto> clientDtoSet;
    public String scheduleId;

    public GenericDto(T advertisement, Set<ClientDto> clientDtoSet,String scheduleId) {
        this.advertisement = advertisement;
        this.clientDtoSet = clientDtoSet;
        this.scheduleId = scheduleId;
    }

    public GenericDto() {
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
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

    @Override
    public String toString() {
        return "GenericDto{" +
                "advertisement=" + advertisement +
                ", clientDtoSet=" + clientDtoSet +
                '}';
    }
}
