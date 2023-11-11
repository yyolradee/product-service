package com.example.productserviceproject.model.query;

import com.example.productserviceproject.model.Review;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("ProductService")
public class ProductModel implements Serializable {
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
    private String shop_id;
    private List<Review> reviews;

    public ProductModel() {
    }

    public ProductModel(String name, String description, String img_path, double price, String category, String shop_id, String create_at, String edit_at) {
        this(null, name, description, img_path, price, category, new ArrayList<Review>(), shop_id, create_at, edit_at);
    }

    public ProductModel(String name) {
        this(null, name, null, null, 0.0, null, new ArrayList<>(),null ,null, null);
    }

    public ProductModel(String _id, String name, String description, String img_path, double price, String category, List<Review> reviews, String shop_id, String create_at, String edit_at) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.img_path = img_path;
        this.price = price;
        this.category = category;
        this.reviews = reviews;
        this.shop_id = shop_id;
        this.create_at = create_at;
        this.edit_at = edit_at;
    }
}
