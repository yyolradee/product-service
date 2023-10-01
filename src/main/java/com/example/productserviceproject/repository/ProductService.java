package com.example.productserviceproject.repository;

import com.example.productserviceproject.POJO.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = "productList", allEntries = true)
    public Product addProductService(Product p) {
        return this.repository.insert(p);
    }

    @CacheEvict(value = "productList", allEntries = true)
    public Product editProductService(Product p) {
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

    @Cacheable(value = "productList")
    public List<Product> getAllProductsService() {
        return this.repository.findAll();
    }

    @Cacheable(value = "productList")
    public Optional<Product> getProductByIdService(String id) {
        return this.repository.findById(id);
    }

    @Cacheable(value = "productList")
    public List<Product> getProductByName(String name) {
        return this.repository.findByName(name);
    }

    @Cacheable(value = "productList")
    public List<Product> getProductByCategory(String category) {
        return this.repository.findByCategory(category);
    }
}
