package com.example.productserviceproject.POJO;

import java.io.Serializable;

public class Review implements Serializable {
    private String content;
    private double rate;
    private String owner_name;
    private boolean isAnonymous;

    public Review() {
    }

    public Review(String content, double rate, String owner_name, boolean isAnonymous) {
        this.content = content;
        this.rate = rate;
        this.owner_name = owner_name;
        this.isAnonymous = isAnonymous;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }
}
