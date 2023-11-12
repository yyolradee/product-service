package com.example.productserviceproject.controller.write;

import com.example.productserviceproject.event.ProductCreatedEvent;
import com.example.productserviceproject.model.command.Reviews;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Aggregate
public class ProductCommandHandler {
    @AggregateIdentifier
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
    private List<Reviews> reviews;

    public ProductCommandHandler() {}

    @CommandHandler
    public ProductCommandHandler(CreateProductCommand createProductCommand) {
        ProductCreatedEvent productCreatedEvent =  new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
        AggregateLifecycle.apply(productCreatedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        System.out.println("ON AGGREGATE");
        this._id = productCreatedEvent.get_id();
        this.name = productCreatedEvent.getName();
        this.description = productCreatedEvent.getDescription();
        this.image = productCreatedEvent.getImage();
        this.price = productCreatedEvent.getPrice();
        this.category = productCreatedEvent.getCategory();
        this.rating = productCreatedEvent.getRating();
        this.create_at = productCreatedEvent.getCreate_at();
        this.edit_at = productCreatedEvent.getEdit_at();
        this.shop_id = productCreatedEvent.getShop_id();
        this.reviews = productCreatedEvent.getReviews();
    }
}
