package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ClientDto {

    private String id;
    private String firstName;
    private String email;
    private String phoneNumber;
    private Map<String,String> properties;


    public ClientDto(String id,String firstName, String email, String phoneNumber,Map<String,String> properties) {
        this.id = id;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.properties = properties;
    }

    public ClientDto() {
    }



}
