package com.example.productserviceproject.service.write;

import com.example.productserviceproject.model.ProductModel;
import com.example.productserviceproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class ProductWriteService {
    @Autowired
    private ProductRepository repository;
    public ProductWriteService(ProductRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "productList", allEntries = true)
    public ProductModel addProductService(ProductModel p) {
        return this.repository.insert(p);
    }

    @CacheEvict(value = "productList", allEntries = true)
    public ProductModel editProductService(ProductModel p) {
        return this.repository.save(p);
    }

    @CacheEvict(value = "productList", allEntries = true)
    public boolean deleteProductService(String id) {
        try {
            this.repository.deleteById(id);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
