package com.netcracker.dataservice.model.advertisement;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public abstract class Advertisement {

    protected String text;

    public Advertisement(String text) {
        this.text = text;
    }

    public Advertisement() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
