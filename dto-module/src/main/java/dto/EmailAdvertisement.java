package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailAdvertisement implements AdvertismentInterface{

    private String template;
    private String topic;
    private File image;

    public EmailAdvertisement(String text, String template, String topic, File image) {
        this.template = template;
        this.topic = topic;
        this.image = image;
    }

    public EmailAdvertisement() {
    }


    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
