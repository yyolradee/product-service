package com.example.productserviceproject.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Review implements Serializable {
    private String content;
    private double rate;
    private String owner_name;
    private boolean isAnonymous;

    public Review() {
    }

    public Review(String content, double rate, String owner_name) {
        this(content, rate, owner_name, false);
    }

    public Review(String content, double rate, String owner_name, boolean isAnonymous) {
        this.content = content;
        this.rate = rate;
        this.owner_name = owner_name;
        this.isAnonymous = isAnonymous;
    }
}
