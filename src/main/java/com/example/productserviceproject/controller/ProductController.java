package com.example.productserviceproject.controller;

import com.example.productserviceproject.controller.rest.ReadProductController;
import com.example.productserviceproject.controller.rest.WriteProductController;
import com.example.productserviceproject.controller.write.CreateProductCommand;
import com.example.productserviceproject.model.command.ProductCreateModel;
import com.example.productserviceproject.model.command.ProductUpdateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final WriteProductController writeProductController;
    private final ReadProductController readProductController;

    @Autowired
    public ProductController(WriteProductController writeProductController, ReadProductController readProductController) {
        this.writeProductController = writeProductController;
        this.readProductController = readProductController;
    }

//     Write Operations
    @PostMapping
    public String addProduct(@RequestBody ProductCreateModel model, @RequestHeader(value = "Authorization", required = true) String token) {
        CreateProductCommand command = CreateProductCommand.builder()
                .name(model.getName())
                .description(model.getDescription())
                .price(model.getPrice())
                .category(model.getCategory())
                .image(model.getImage())
//                .token(model.getToken())
                .build();

        String result;
        try {
            result = String.valueOf(writeProductController.addProduct(command));
        }
        catch (Exception e) {
            result = e.getLocalizedMessage();
        }

        return result;
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> editProduct(@PathVariable String id, @RequestBody ProductUpdateModel model) {
//        return writeProductController.editProduct(id, command);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id, @RequestHeader(value = "Authorization", required = true) String token) {
        return writeProductController.deleteProduct(id, token);
    }

//     Read Operations

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return readProductController.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        return readProductController.getProductById(id);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable String name) {
        return readProductController.getProductsByName(name);
    }

    @GetMapping("/categories/{category_name}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String category_name) {
        return readProductController.getProductsByCategory(category_name);
    }

    @GetMapping("/shopId/{shop_id}")
    public ResponseEntity<?> getProductsByShopId(@PathVariable String shop_id) {
        return readProductController.getProductsByShopId(shop_id);
    }

    @GetMapping("/shopId/{shop_id}/{product_id}")
    public ResponseEntity<?> getProductsByShopIdAndProductId(@PathVariable String shop_id, @PathVariable String product_id) {
        return readProductController.getProductsByShopIdAndProductId(shop_id, product_id);
    }
}
