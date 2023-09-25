package com.example.productserviceproject.repository;

import com.example.productserviceproject.POJO.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public boolean addProductService(Product p) {
        try {
            this.repository.insert(p);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean editProductService(Product p) {
        try {
            this.repository.save(p);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean deleteProductService(String id) {
        try {
            this.repository.deleteById(id);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public List<Product> getAllProductsService() {
        return this.repository.findAll();
    }

    public Optional<Product> getProductByIdService(String id) {
        return this.repository.findById(id);
    }
}
