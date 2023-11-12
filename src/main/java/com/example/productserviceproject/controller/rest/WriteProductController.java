package com.example.productserviceproject.controller.rest;

import com.example.productserviceproject.model.ProductModel;
import com.example.productserviceproject.model.Shop;
import com.example.productserviceproject.model.command.ProductCreateModel;
import com.example.productserviceproject.model.command.ProductUpdateModel;
import com.example.productserviceproject.service.FileService;
import com.example.productserviceproject.model.ErrorResponse;
import com.example.productserviceproject.service.read.ProductReadService;
import com.example.productserviceproject.service.write.ProductWriteService;
import com.example.productserviceproject.model.command.Reviews;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/write/products")
public class WriteProductController {
    @Autowired
    private ProductWriteService productWriteService;
    @Autowired
    private ProductReadService productReadService;
    @Autowired
    private FileService fileService;

    // add new product
    public ResponseEntity<?> addProduct(@ModelAttribute ProductCreateModel requestBody, @RequestHeader(value = "Authorization", required = true) String token) {
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

                    ProductModel added = productWriteService.addProductService(new ProductModel(name, description, img_path, price, category, shop_id, create_at, create_at));
                    return  ResponseEntity.ok(added);
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
    public ResponseEntity<?> editProduct(@ModelAttribute ProductUpdateModel requestBody, @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            String[] tokens = token.split(" ");
            // check token
            if (tokens.length > 1) {
                String bearerToken = tokens[1];
                ResponseEntity<List<Shop>> shopResponse = getShopData(bearerToken);

                // check send api to shop
                if (shopResponse.getStatusCode().is2xxSuccessful()) {
                    String shopId = shopResponse.getBody().get(0).get_id();
                    ResponseEntity<List<ProductModel>> checkOwnProduct = getProductsByShopIdAndProductId(shopId, requestBody.get_id());

                    // check is have shop, own shop, own product
                    if (shopId.length() <= 0 || checkOwnProduct.getBody().size() <= 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Updating product fail", "Shop not found, or you are not the product owner or shop owner."));
                    }

                    this.validateProductData(requestBody); //validate data
                    Optional<ProductModel> out = productReadService.getProductByIdService(requestBody.get_id());
                    ProductModel productModel = out.get();

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
                        Optional<ProductModel> getProductById = productReadService.getProductByIdService(requestBody.get_id());
                        if (getProductById.isEmpty()) {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Product not found", "Product ID not exist"));
                        } else {
                            fileService.deleteFile(getProductById.get().getImg_path());
                        }

                        File file = fileService.convertMultiPartToFile(image);
                        img_path = fileService.uploadFile(file, file.getName());
                        productModel.setImg_path(img_path);
                    }
                    // ---------- End Upload image in to firebase -----------

                    productModel.setName(name);
                    productModel.setDescription(description);
                    productModel.setPrice(price);
                    productModel.setCategory(category);
                    productModel.setEdit_at(edit_at);

                    ProductModel edited = productWriteService.editProductService(productModel);
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
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id, @RequestHeader(value = "Authorization", required = true) String token) {
        String[] tokens = token.split(" ");
        // check token
        if (tokens.length > 1) {
            String bearerToken = tokens[1];
            ResponseEntity<List<Shop>> shopResponse = getShopData(bearerToken);

            // check send api to shop
            if (shopResponse.getStatusCode().is2xxSuccessful()) {
                String shopId = shopResponse.getBody().get(0).get_id();
                ResponseEntity<List<ProductModel>> checkOwnProduct = getProductsByShopIdAndProductId(shopId, id);

                // check is have shop, own shop, own product
                if (shopId.length() <= 0 || checkOwnProduct.getBody().size() <= 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Failed to delete product", "Shop not found, or you are not the product owner or shop owner."));
                }
                boolean deleted = productWriteService.deleteProductService(id);
                if (deleted) {
                    return ResponseEntity.ok("Product deleted successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete product");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Failed to delete product", "Invalid Token"));
    }

    // Add review
    public ResponseEntity<?> addReview(@PathVariable("product_id") String id, @ModelAttribute @Valid Reviews review, @RequestHeader(value = "Authorization", required = true) String token) {
        try {
            Optional<ProductModel> out = productReadService.getProductByIdService(id);
            if (out.isEmpty()) {
                ErrorResponse errorResponse = new ErrorResponse("Cannot add review", "Product not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            this.validateReviewData(review);
            ProductModel product = out.get();
            List<Reviews> reviews = product.getReviews();
            reviews.add(review);
            double rating = calculateRating(reviews);
            product.setRating(rating);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            product.setEdit_at(now.toString());
            ProductModel addReview = productWriteService.editProductService(product);
            return ResponseEntity.ok("Review added successfully");
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Cannot add review", ie.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Cannot add review", e.getMessage()));
        }
    }

    // calculate rating
    public double calculateRating(List<Reviews> reviews) {
        if (reviews.size() == 0){
            return  0;
        }
        double totalRate = 0.0;
        for (Reviews r : reviews) {
            totalRate += r.getRate();
        }
        double rating = totalRate / reviews.size();
        return rating;
    }

    public void validateProductData(ProductCreateModel productRequest) throws Exception {
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

    public void validateProductData(ProductUpdateModel productRequest) throws Exception {
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

    public void validateReviewData(Reviews reviews) throws Exception {
        if (reviews.getContent() == null || reviews.getContent().isBlank()) {
            throw new IllegalArgumentException("Content is Required");
        }
        if (reviews.getOwner_name() == null || reviews.getOwner_name().isBlank()) {
            throw new IllegalArgumentException("Owner name is Required");
        }
        if (reviews.getRate() > 5 || reviews.getRate() <= 0) {
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

    public ResponseEntity<List<ProductModel>> getProductsByShopIdAndProductId(@PathVariable("shop_id") String shopId, @PathVariable("product_id") String productId) {
        List<ProductModel> out = this.productReadService.getProductByShopIdAndProductId(shopId, productId);
        return ResponseEntity.ok(out);
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
