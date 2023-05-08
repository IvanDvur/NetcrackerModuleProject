package dto;

import java.util.Set;

public class GenericDto<T> {

    private T advertisement;
    private Set<ClientDto> clientDtoSet;
    private Schedule schedule;

    public GenericDto(T advertisement, Set<ClientDto> clientDtoSet,Schedule schedule) {
        this.advertisement = advertisement;
        this.clientDtoSet = clientDtoSet;
        this.schedule = schedule;
    }

    public GenericDto() {
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
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
                ", schedule=" + schedule +
                '}';
    }
}
