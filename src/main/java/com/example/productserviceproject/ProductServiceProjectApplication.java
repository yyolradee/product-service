package com.example.productserviceproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

//@EnableCaching
@SpringBootApplication
public class ProductServiceProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceProjectApplication.class, args);
        System.out.println("ProductService - Project");
    }

}
