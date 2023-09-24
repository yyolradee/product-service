package com.example.productserviceproject.POJO;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Document("ProductService")
public class Product implements Serializable {
    private String _id;
    private String name;
    private String description;
    private String img_path;
    private double price;
    private String category;
    private String shop_id;
    private double rating;
    private List<Review> reviews;

    public Product() {
    }

    public Product(String _id, String name, String description, String img_path, double price, String category, String shop_id, List<Review> reviews) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.img_path = img_path;
        this.price = price;
        this.category = category;
        this.shop_id = shop_id;
        this.reviews = reviews;
    }
}
