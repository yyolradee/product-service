package com.example.productserviceproject.controller;

import com.example.productserviceproject.controller.rest.ReadProductController;
import com.example.productserviceproject.controller.rest.WriteProductController;
import com.example.productserviceproject.controller.write.CreateProductCommand;
import com.example.productserviceproject.model.command.ProductCreateModel;
import com.example.productserviceproject.model.command.ProductUpdateModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final WriteProductController writeProductController;
    private final ReadProductController readProductController;
    private final CommandGateway commandGateway;

    @Autowired
    public ProductController(WriteProductController writeProductController, ReadProductController readProductController, CommandGateway commandGateway) {
        this.writeProductController = writeProductController;
        this.readProductController = readProductController;
        this.commandGateway = commandGateway;
    }

//     Write Operations
    @PostMapping()
    public ResponseEntity<?> addProduct(@ModelAttribute ProductCreateModel model, @RequestHeader(value = "Authorization", required = true) String token) {
        CreateProductCommand command = CreateProductCommand.builder()
                ._id(UUID.randomUUID().toString())
                .name(model.getName())
                .description(model.getDescription())
                .price(model.getPrice())
                .category(model.getCategory())
                .image(model.getImage())
                .token(token)
                .build();

        model.setToken(token);
        try {
            ResponseEntity<?> writeProductResponse = writeProductController.addProduct(model);

            if (writeProductResponse.getStatusCode().is2xxSuccessful()) {
                System.out.println("😊");
                String result = commandGateway.sendAndWait(command);

                return writeProductResponse;
            } else {
                // If writeProductController response is not successful, return its response
                return writeProductResponse;
            }

        } catch (Exception e) {
            // Handle the exception
            return ResponseEntity.status(500).body("Failed to add product");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editProduct(@PathVariable String id, @ModelAttribute ProductUpdateModel model, @RequestHeader(value = "Authorization", required = true) String token) {
        CreateProductCommand command = CreateProductCommand.builder()
                ._id(UUID.randomUUID().toString())
                .name(model.getName())
                .description(model.getDescription())
                .price(model.getPrice())
                .category(model.getCategory())
                .image(model.getImage())
                .token(token)
                .build();
        model.setToken(token);
        model.set_id(id);
        try {
            ResponseEntity<?> writeProductResponse = writeProductController.editProduct(model);

            if (writeProductResponse.getStatusCode().is2xxSuccessful()) {
                System.out.println("😊");
                String result = commandGateway.sendAndWait(command);

                return writeProductResponse;
            } else {
                // If writeProductController response is not successful, return its response
                return writeProductResponse;
            }

        } catch (Exception e) {
            // Handle the exception
            return ResponseEntity.status(500).body("Failed to update product");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id, @RequestHeader(value = "Authorization", required = true) String token) {
        CreateProductCommand command = CreateProductCommand.builder()
                ._id(UUID.randomUUID().toString())
                .build();
        try {
            ResponseEntity<?> writeProductResponse = writeProductController.deleteProduct(id, token);;

            if (writeProductResponse.getStatusCode().is2xxSuccessful()) {
                System.out.println("😊");
                String result = commandGateway.sendAndWait(command);

                return writeProductResponse;
            } else {
                // If writeProductController response is not successful, return its response
                return writeProductResponse;
            }

        } catch (Exception e) {
            // Handle the exception
            return ResponseEntity.status(500).body("Failed to delete product");
        }
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
