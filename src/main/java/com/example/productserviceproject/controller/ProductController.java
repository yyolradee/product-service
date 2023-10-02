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

import java.sql.Timestamp;
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
    @RequestMapping(value = "api/products", method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(@RequestBody MultiValueMap<String, String> requestBody) {
        Map<String, String> body = requestBody.toSingleValueMap();
        String name = body.get("name");
        String description = body.get("description");
        String img_path = body.get("img_path");
        double price = Double.parseDouble(body.get("price"));
        String category = body.get("category");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String create_at = now.toString();

        try {
            Product added = productService.addProductService(new Product(name, description, img_path, price, category, null, create_at, create_at));
            return ResponseEntity.ok(added);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    // edit product info
    @RequestMapping(value = "api/products/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> editProduct(@PathVariable("id") String id, @RequestBody MultiValueMap<String, String> requestBody) {
        Optional<Product> out = productService.getProductByIdService(id);
        Product product = out.get();

        Map<String, String> body = requestBody.toSingleValueMap();
        String name = body.get("name");
        String description = body.get("description");
        String img_path = body.get("img_path");
        double price = Double.parseDouble(body.get("price"));
        String category = body.get("category");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String edit_at = now.toString();

        product.setName(name);
        product.setDescription(description);
        product.setImg_path(img_path);
        product.setPrice(price);
        product.setCategory(category);
        product.setEdit_at(edit_at);

        try {
            Product edited = productService.editProductService(product);
            return ResponseEntity.ok(edited);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    // delete product
    @RequestMapping(value = "api/products/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id) {
        boolean deleted = productService.deleteProductService(id);
        if (deleted) {
            return ResponseEntity.ok("Product deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete product");
        }
    }

    // get all product
    @RequestMapping(value = "api/products")
    public ResponseEntity<?> getAllProducts() {
        List<Product> out = this.productService.getAllProductsService();
        return ResponseEntity.ok(out);
    }

    // get product by category
    @RequestMapping(value = "api/products/categories/{category_name}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable("category_name") String category) {
        List<Product> out = this.productService.getProductByCategory(category);
        return ResponseEntity.ok(out);
    }

    // get product by name
    @RequestMapping(value = "api/products/name/{product_name}")
    public ResponseEntity<?> getProductsByName(@PathVariable("product_name") String name) {
        List<Product> out = this.productService.getProductByName(name);
        return ResponseEntity.ok(out);
    }

    // get product by id
    @RequestMapping("api/products/id/{product_id}")
    public ResponseEntity<?> getProductById(@PathVariable("product_id") String id) {
        Optional<Product> out = productService.getProductByIdService(id);
        if (out.isEmpty()) {
            return ResponseEntity.ok("Product not found");
        } else {
            return ResponseEntity.ok(out);
        }
    }

    // add review
    @RequestMapping(value = "api/products/{product_id}/reviews", method = RequestMethod.POST)
    public ResponseEntity<?> addReview(@PathVariable("product_id") String id, @RequestBody MultiValueMap<String, String> requestBody) {
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

        try {
            Product addReview = productService.editProductService(product);
            return ResponseEntity.ok("Review added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
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
