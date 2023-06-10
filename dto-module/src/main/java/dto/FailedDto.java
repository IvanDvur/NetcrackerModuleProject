package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FailedDto {

    private String orderId;
    private EmailAdvertisement emailAdvertisement;
    private SmsAdvertisement smsAdvertisement;
    private MessengerAdvertisement messengerAdvertisement;
    private Set<ClientDto> clientsDtos;
    private List<String> retryTypes;
    private String scheduleId;


    public AdvertismentInterface getAdvertisment(String type){
        if(type.equals("SMS")){
            return smsAdvertisement;
        }
        if(type.equals("EMAIL")){
            return emailAdvertisement;
        }
        else {
            return messengerAdvertisement;
        }
    }

}
