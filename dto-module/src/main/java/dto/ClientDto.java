package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDto {

    private String firstName;
    private String email;
    private String phoneNumber;

    public ClientDto(String firstName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public ClientDto() {
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
