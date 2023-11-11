package com.example.productserviceproject.controller;

import com.example.productserviceproject.POJO.*;
import com.example.productserviceproject.repository.FileService;
import com.example.productserviceproject.repository.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

@CrossOrigin
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    // add new product
    @RequestMapping(value = "api/products", method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(@ModelAttribute ProductRequest requestBody, @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            String[] tokens = token.split(" ");
            // check token
            if (tokens.length > 1) {
                String bearerToken = tokens[1];
                ResponseEntity<List<Shop>> shopResponse = getShopData(bearerToken);

                // check send api to shop
                if (shopResponse.getStatusCode().is2xxSuccessful()) {
                    String shopId = shopResponse.getBody().get(0).get_id();

                    // check is have shop
                    if (shopId.length() <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Adding new product fail", "Shop not found"));
                    }

                    this.validateProductData(requestBody); //validate data

                    String name = requestBody.getName();
                    String description = requestBody.getDescription();
                    double price = requestBody.getPrice();
                    String category = requestBody.getCategory();
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    String create_at = now.toString();
                    String shop_id = shopId;

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

                    Product added = productService.addProductService(new Product(name, description, img_path, price, category, shop_id, create_at, create_at));
                    return ResponseEntity.ok(added);
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Adding new product fail", "Invalid Token"));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Adding new product fail", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Adding new product fail cause server", e.getMessage()));
        }
    }

    // edit product info
    @RequestMapping(value = "api/products/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> editProduct(@ModelAttribute ProductRequest requestBody, @PathVariable("id") String id, @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            String[] tokens = token.split(" ");
            // check token
            if (tokens.length > 1) {
                String bearerToken = tokens[1];
                ResponseEntity<List<Shop>> shopResponse = getShopData(bearerToken);

                // check send api to shop
                if (shopResponse.getStatusCode().is2xxSuccessful()) {
                    String shopId = shopResponse.getBody().get(0).get_id();
                    ResponseEntity<List<Product>> checkOwnProduct = getProductsByShopIdAndProductId(shopId, id);

                    // check is have shop, own shop, own product
                    if (shopId.length() <= 0 || checkOwnProduct.getBody().size() <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Updating product fail", "Shop not found, or you are not the product owner or shop owner."));
                    }

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
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Updating product fail", "Invalid Token"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Updating product fail", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Updating new product fail cause server", e.getMessage()));
        }
    }


    // delete product
    @RequestMapping(value = "api/products/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id, @RequestHeader(value = "Authorization", required = true) String token) {
        String[] tokens = token.split(" ");
        // check token
        if (tokens.length > 1) {
            String bearerToken = tokens[1];
            ResponseEntity<List<Shop>> shopResponse = getShopData(bearerToken);

            // check send api to shop
            if (shopResponse.getStatusCode().is2xxSuccessful()) {
                String shopId = shopResponse.getBody().get(0).get_id();
                ResponseEntity<List<Product>> checkOwnProduct = getProductsByShopIdAndProductId(shopId, id);

                // check is have shop, own shop, own product
                if (shopId.length() <= 0 || checkOwnProduct.getBody().size() <= 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Failed to delete product", "Shop not found, or you are not the product owner or shop owner."));
                }
                boolean deleted = productService.deleteProductService(id);
                if (deleted) {
                    return ResponseEntity.ok("Product deleted successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete product");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Failed to delete product", "Invalid Token"));
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

    // get product by shop_id
    @RequestMapping(value = "api/products/shopId/{shop_id}")
    public ResponseEntity<List<Product>> getProductsByShopId(@PathVariable("shop_id") String shopId) {
        List<Product> out = this.productService.getProductByShopId(shopId);
        return ResponseEntity.ok(out);
    }

    // get product by shop_id and product_id
    @RequestMapping(value = "api/products/shopId/{shop_id}/{product_id}")
    public ResponseEntity<List<Product>> getProductsByShopIdAndProductId(@PathVariable("shop_id") String shopId, @PathVariable("product_id") String productId) {
        List<Product> out = this.productService.getProductByShopIdAndProductId(shopId, productId);
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
    public ResponseEntity<?> addReview(@PathVariable("product_id") String id, @ModelAttribute @Valid Review review, @RequestHeader(value = "Authorization", required = true) String token) {
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

    private ResponseEntity<List<Shop>> getShopData(String bearerToken) {
        String apiUrl = "https://shop-908649839259189283.rcf2.deploys.app/api/shops/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);

        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpEntity<String> requestEntity = new org.springframework.http.HttpEntity<>(headers);

        return restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Shop>>() {});
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
