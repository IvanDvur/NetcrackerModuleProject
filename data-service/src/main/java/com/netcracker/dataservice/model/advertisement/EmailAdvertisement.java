package com.netcracker.dataservice.model.advertisement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.File;
import java.util.UUID;

/**
 * Cущность рекламы по Email
 */
@Entity
@Data
@NoArgsConstructor
public class EmailAdvertisement extends Advertisement{

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    //html in base64
    @Column(columnDefinition="TEXT")
    private String template;

    private String topic;

    private File image;

    public EmailAdvertisement(String text) {
        super(text);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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


