package com.example.productserviceproject.POJO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class ProductRequest implements Serializable {
    private String name;
    private String description;
    private double price;
    private String category;
    private MultipartFile image;

    public ProductRequest() {
    }

    public ProductRequest(String name, String description, double price, String category, MultipartFile image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
    }
}
