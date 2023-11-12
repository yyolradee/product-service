package com.example.productserviceproject.handler.event;

import com.example.productserviceproject.controller.rest.WriteProductController;
import com.example.productserviceproject.event.ProductCreatedEvent;
import com.example.productserviceproject.model.ErrorResponse;
import com.example.productserviceproject.model.ProductModel;
import com.example.productserviceproject.model.Shop;
import com.example.productserviceproject.model.command.ProductCreateModel;
import com.example.productserviceproject.repository.ProductRepository;
import com.example.productserviceproject.service.FileService;
import com.example.productserviceproject.service.read.ProductReadService;
import com.example.productserviceproject.service.write.ProductWriteService;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

@Component
public class ProductEventHandler {
    @Autowired
    private final ProductRepository repository;
    @Autowired
    private WriteProductController writeProductController;

    public ProductEventHandler(ProductRepository repository, WriteProductController writeProductController) {
        this.repository = repository;
        this.writeProductController = writeProductController;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        System.out.println(event);
        ProductCreateModel model = new ProductCreateModel();
        BeanUtils.copyProperties(event, model);
//        ResponseEntity<?> test =  writeProductController.addProduct(model);
//        System.out.println(test);
    }

}
