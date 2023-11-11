package com.example.productserviceproject.model.command;

import com.example.productserviceproject.model.Review;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("ProductService")
public class ProductCreateModel implements Serializable {
    private String name;
    private String description;
    private MultipartFile image;
    private double price;
    private String category;
    private String token;

    public ProductCreateModel(String name, String description, MultipartFile image, double price, String category, String token) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.category = category;
        this.token = token;
    }
}
