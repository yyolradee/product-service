package com.example.productserviceproject.service.read;

import com.example.productserviceproject.model.ProductRequest;
import com.example.productserviceproject.model.query.ProductQuery;
import com.example.productserviceproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductReadService {
    @Autowired
    private ProductRepository repository;
    public ProductReadService(ProductRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "productList")
    public List<ProductQuery> getAllProductsService() {
        return this.repository.findAll();
    }

    @Cacheable(value = "productList")
    public Optional<ProductQuery> getProductByIdService(String id) {
        return this.repository.findById(id);
    }

    @Cacheable(value = "productList")
    public List<ProductQuery> getProductByName(String name) {
        return this.repository.findByName(name);
    }

    @Cacheable(value = "productList")
    public List<ProductQuery> getProductByCategory(String category) {
        return this.repository.findByCategory(category);
    }

    @Cacheable(value = "productList")
    public List<ProductQuery> getProductByShopId(String shop_id) {return  this.repository.findByShop(shop_id);};

    @Cacheable
    public List<ProductQuery> getProductByShopIdAndProductId(String shop_id, String product_id) {return this.repository.findByShopAndProductId(shop_id, product_id);}
}
