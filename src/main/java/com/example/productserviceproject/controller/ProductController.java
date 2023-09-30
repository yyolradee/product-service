package com.example.productserviceproject.controller;

import com.example.productserviceproject.POJO.Product;
import com.example.productserviceproject.POJO.Review;
import com.example.productserviceproject.repository.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // add new product
    @RequestMapping(value = "api/product/addProduct", method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(@RequestBody MultiValueMap<String, String> requestBody) {
        Map<String, String> body = requestBody.toSingleValueMap();
        String name = body.get("name");
        String description = body.get("description");
        String img_path = body.get("img_path");
        double price = Double.parseDouble(body.get("price"));
        String category = body.get("category");
        String create_at = body.get("create_at");
        boolean added = productService.addProductService(new Product(name, description, img_path, price, category, null, create_at, create_at));
        if (added) {
            return ResponseEntity.ok("Product added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product");
        }
    }

    // edit product info
    @RequestMapping(value = "api/product/editProduct/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> editProduct(@PathVariable("id") String id, @RequestBody MultiValueMap<String, String> requestBody) {
        Optional<Product> out = productService.getProductByIdService(id);
        Product product = out.get();

        Map<String, String> body = requestBody.toSingleValueMap();
        String name = body.get("name");
        String description = body.get("description");
        String img_path = body.get("img_path");
        double price = Double.parseDouble(body.get("price"));
        String category = body.get("category");
        String edit_at = body.get("edit_at");

        product.setName(name);
        product.setDescription(description);
        product.setImg_path(img_path);
        product.setPrice(price);
        product.setCategory(category);
        product.setEdit_at(edit_at);

        boolean edited = productService.editProductService(product);

        if (edited) {
            return ResponseEntity.ok("Product edited successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to edit product");
        }
    }

    // delete product
    @RequestMapping(value = "api/product/deleteProduct/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id) {
        boolean deleted = productService.deleteProductService(id);
        if (deleted) {
            return ResponseEntity.ok("Product deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete product");
        }
    }

    // get all product
    @RequestMapping(value = "api/getAllProducts")
    public ResponseEntity<?> getAllProducts() {
        List<Product> out = this.productService.getAllProductsService();
        return ResponseEntity.ok(out);
    }

    // get product by category
    @RequestMapping(value = "api/getProductsByCategory/{category}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable("category") String category) {
        List<Product> out = this.productService.getProductByCategory(category);
        return ResponseEntity.ok(out);
    }

    // get product by name
    @RequestMapping(value = "api/getProductsByName/{name}")
    public ResponseEntity<?> getProductsByName(@PathVariable("name") String name) {

        List<Product> out = this.productService.getProductByName(name);
        return ResponseEntity.ok(out);
    }

    // get product by id
    @RequestMapping("api/getProductById/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") String id) {
        Optional<Product> out = productService.getProductByIdService(id);
        if (out.isEmpty()) {
            return ResponseEntity.ok("Product not found");
        } else {
            return ResponseEntity.ok(out);
        }
    }

    // add review
    @RequestMapping(value = "api/review/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addReview(@PathVariable("id") String id, @RequestBody MultiValueMap<String, String> requestBody) {
        Optional<Product> out = productService.getProductByIdService(id);
        Product product = out.get();
        List<Review> reviews = product.getReviews();

        Map<String, String> body = requestBody.toSingleValueMap();
        String content = body.get("content");
        double rate = Double.parseDouble(body.get("rate"));
        boolean isAnonymous = Boolean.parseBoolean(body.get("isAnonymous"));

        reviews.add(new Review(content, rate, "AiKAZE", isAnonymous));

        double rating = calculateRating(reviews);
        product.setRating(rating);

        boolean addReview = productService.editProductService(product);
        if (addReview) {
            return ResponseEntity.ok("Review added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add review");
        }
    }

    // calculate rating
    public double calculateRating(List<Review> reviews) {
        if (reviews.size() == 0){
            return  0;
        }
        double totalRate = 0.0;
        for (Review r : reviews) {
            totalRate += r.getRate();
        }
        double rating = totalRate / reviews.size();
        return rating;
    }
}
