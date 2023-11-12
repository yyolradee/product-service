package com.example.productserviceproject;

import com.example.productserviceproject.config.AxonXstreamConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

//@EnableCaching
@SpringBootApplication
@Import({ AxonXstreamConfig.class })
public class ProductServiceProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceProjectApplication.class, args);
        System.out.println("ProductService - Project");
    }

}
