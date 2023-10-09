package com.example.productserviceproject.POJO;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("ProductService")
public class Product implements Serializable {
    @Id
    private String _id;
    private String name;
    private String description;
    private String img_path;
    private double price;
    private String category;
    private double rating;
    private String create_at;
    private String edit_at;
    private List<Review> reviews;

    public Product() {
    }

    public Product(String name, String description, String img_path, double price, String category, String create_at, String edit_at) {
        this(null, name, description, img_path, price, category, new ArrayList<Review>(), create_at, edit_at);
    }

    public Product(String name) {
        this(null, name, null, null, 0.0, null, new ArrayList<>(), null, null);
    }

    public Product(String _id, String name, String description, String img_path, double price, String category, List<Review> reviews, String create_at, String edit_at) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.img_path = img_path;
        this.price = price;
        this.category = category;
        this.reviews = reviews;
        this.create_at = create_at;
        this.edit_at = edit_at;
    }
}
