package com.example.productserviceproject.model.command;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.io.Serializable;

@Data
public class ProductCreateModel implements Serializable {
    private String _id;
    private String name;
    private String description;
    private MultipartFile image;
    private double price;
    private String category;

    public ProductCreateModel() {}

    public ProductCreateModel(String _id, String name, String description, double price, MultipartFile image, String category) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.category = category;
    }
}
