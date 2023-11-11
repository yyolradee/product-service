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
    private String shop_id;

    public ProductRequest() {
    }

    public ProductRequest(String name, String description, double price, String category, MultipartFile image, String shop_id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.shop_id = shop_id;
    }
}
