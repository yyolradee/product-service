package com.example.productserviceproject.event;

import com.example.productserviceproject.model.Review;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductCreatedEvent {
    private String _id;
    private String name;
    private String description;
    private MultipartFile image;
    private double price;
    private String category;
    private double rating;
    private String create_at;
    private String edit_at;
    private String shop_id;
    private List<Review> reviews;
}
