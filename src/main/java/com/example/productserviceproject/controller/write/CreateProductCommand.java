package com.example.productserviceproject.controller.write;

import com.example.productserviceproject.model.Review;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Data
public class CreateProductCommand {
    @TargetAggregateIdentifier
    private String name;
    private String description;
    private MultipartFile image;
    private double price;
    private String category;
    private String token;
}
