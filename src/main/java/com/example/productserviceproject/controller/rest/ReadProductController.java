package com.example.productserviceproject.controller.rest;

import com.example.productserviceproject.model.query.ProductQuery;
import com.example.productserviceproject.model.ErrorResponse;
import com.example.productserviceproject.service.read.ProductReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/read/products")
public class ReadProductController {
    @Autowired
    private ProductReadService productReadService;

    public ReadProductController(ProductReadService productReadService) {
        this.productReadService = productReadService;
    }

    // get all product
    public ResponseEntity<?> getAllProducts() {
        List<ProductQuery> out = this.productReadService.getAllProductsService();
        return ResponseEntity.ok(out);
    }

    // get product by category
    public ResponseEntity<?> getProductsByCategory(@PathVariable("category_name") String category) {
        List<ProductQuery> out = this.productReadService.getProductByCategory(category);
        return ResponseEntity.ok(out);
    }

    // get product by shop_id
    public ResponseEntity<List<ProductQuery>> getProductsByShopId(@PathVariable("shop_id") String shopId) {
        List<ProductQuery> out = this.productReadService.getProductByShopId(shopId);
        return ResponseEntity.ok(out);
    }

    // get product by shop_id and product_id
    public ResponseEntity<List<ProductQuery>> getProductsByShopIdAndProductId(@PathVariable("shop_id") String shopId, @PathVariable("product_id") String productId) {
        List<ProductQuery> out = this.productReadService.getProductByShopIdAndProductId(shopId, productId);
        return ResponseEntity.ok(out);
    }

    // get product by name
    public ResponseEntity<?> getProductsByName(@PathVariable("product_name") String name) {
        List<ProductQuery> out = this.productReadService.getProductByName(name);
        return ResponseEntity.ok(out);
    }

    // get product by id
    public ResponseEntity<?> getProductById(@PathVariable("product_id") String id) {
        Optional<ProductQuery> out = productReadService.getProductByIdService(id);
        if (out.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Product not found", "Product ID not exist"));
        } else {
            return ResponseEntity.ok(out);
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();
        List<String> errorMessages = new ArrayList<>();

        for (ObjectError error : errors) {
            errorMessages.add(error.getDefaultMessage());
        }

        ErrorResponse response = new ErrorResponse("Error", "Request Body has Invalid data type " + String.valueOf(errorMessages));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
