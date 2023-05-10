package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsAdvertisement implements AdvertismentInterface{

    private String text;

    public SmsAdvertisement(String text) {
        this.text = text;
    }

    public SmsAdvertisement() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
