package com.example.productserviceproject.controller;

import com.example.productserviceproject.POJO.ErrorResponse;
import com.example.productserviceproject.POJO.Product;
import com.example.productserviceproject.POJO.ProductRequest;
import com.example.productserviceproject.POJO.Review;
import com.example.productserviceproject.repository.FileService;
import com.example.productserviceproject.repository.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;

    public ProductController(ProductService productService, FileService fileService) {
        this.productService = productService;
        this.fileService = fileService;
    }

    // add new product
    @RequestMapping(value = "api/products", method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(@ModelAttribute ProductRequest requestBody) {
        try {
            this.validateProductData(requestBody); //validate data

            String name = requestBody.getName();
            String description = requestBody.getDescription();
            double price = requestBody.getPrice();
            String category = requestBody.getCategory();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String create_at = now.toString();

            // ---------- Upload image in to firebase -----------
            MultipartFile image = requestBody.getImage();
            String img_path;
            if (!image.isEmpty()) {
                File file = fileService.convertMultiPartToFile(image);
                img_path = fileService.uploadFile(file, file.getName());
            } else {
                img_path = null;
            }
            // ---------- End Upload image in to firebase -----------

            Product added = productService.addProductService(new Product(name, description, img_path, price, category, create_at, create_at));
            return ResponseEntity.ok(added);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Adding new product fail", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Adding new product fail cause server", e.getMessage()));
        }
    }

    // edit product info
    @RequestMapping(value = "api/products/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> editProduct(@ModelAttribute ProductRequest requestBody, @PathVariable("id") String id) {
        try {
            this.validateProductData(requestBody); //validate data
            Optional<Product> out = productService.getProductByIdService(id);
            Product product = out.get();

            String name = requestBody.getName();
            String description = requestBody.getDescription();
            double price = requestBody.getPrice();
            String category = requestBody.getCategory();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String edit_at = now.toString();

            // ---------- Upload image in to firebase -----------
            MultipartFile image = requestBody.getImage();
            String img_path;
            if (!image.isEmpty()) {
                // Remove the old image first, if it exists
                Optional<Product> getProductById = productService.getProductByIdService(id);
                if (getProductById.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Product not found", "Product ID not exist"));
                } else {
                    fileService.deleteFile(getProductById.get().getImg_path());
                }

                File file = fileService.convertMultiPartToFile(image);
                img_path = fileService.uploadFile(file, file.getName());
                product.setImg_path(img_path);
            }
            // ---------- End Upload image in to firebase -----------

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCategory(category);
            product.setEdit_at(edit_at);

            Product edited = productService.editProductService(product);
            return ResponseEntity.ok(edited);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Updating product fail", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Updating new product fail cause server", e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Product not found", "Product ID not exist"));
        } else {
            return ResponseEntity.ok(out);
        }
    }

    // add review
    @RequestMapping(value = "api/products/{product_id}/reviews", method = RequestMethod.POST)
    public ResponseEntity<?> addReview(@PathVariable("product_id") String id, @ModelAttribute @Valid Review review) {
        try {
            Optional<Product> out = productService.getProductByIdService(id);
            if (out.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse("Cannot add review", "Product not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            this.validateReviewData(review);
            Product product = out.get();
            List<Review> reviews = product.getReviews();

            reviews.add(review);
            double rating = calculateRating(reviews);
            product.setRating(rating);

            Timestamp now = new Timestamp(System.currentTimeMillis());
            product.setEdit_at(now.toString());

            Product addReview = productService.editProductService(product);
            return ResponseEntity.ok("Review added successfully");
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Cannot add review", ie.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Cannot add review", e.getMessage()));
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

    public void validateProductData(ProductRequest productRequest) throws Exception {
        if (productRequest.getPrice() < 0) {
            throw new IllegalArgumentException("Price must be greater than or equal to Zero");
        }
        if (productRequest.getName() == null || productRequest.getName().isBlank()) {
            throw new IllegalArgumentException("Name is Required");
        }
        if (productRequest.getDescription() == null || productRequest.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description is Required");
        }
        if (productRequest.getCategory() == null || productRequest.getCategory().isBlank()) {
            throw new IllegalArgumentException("Category is Required");
        }
    }

    public void validateReviewData(Review review) throws Exception {
        if (review.getContent() == null || review.getContent().isBlank()) {
            throw new IllegalArgumentException("Content is Required");
        }
        if (review.getOwner_name() == null || review.getOwner_name().isBlank()) {
            throw new IllegalArgumentException("Owner name is Required");
        }
        if (review.getRate() > 5 || review.getRate() <= 0) {
            throw new IllegalArgumentException("Rating must be greater than zero and less than 5.");
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
