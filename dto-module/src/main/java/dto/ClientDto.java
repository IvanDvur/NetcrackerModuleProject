package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
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

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
