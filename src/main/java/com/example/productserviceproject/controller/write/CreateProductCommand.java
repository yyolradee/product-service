package com.example.productserviceproject.controller.write;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class CreateProductCommand {
    @TargetAggregateIdentifier
    private String _id;
    private String name;
    private String description;
    private MultipartFile image;
    private double price;
    private String category;
}
