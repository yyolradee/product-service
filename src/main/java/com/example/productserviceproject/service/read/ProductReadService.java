package com.example.productserviceproject.service.read;

import com.example.productserviceproject.model.ProductModel;
import com.example.productserviceproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ProductModel> getAllProductsService() {
        return this.repository.findAll();
    }

    @Cacheable(value = "productList")
    public Optional<ProductModel> getProductByIdService(String id) {
        return this.repository.findById(id);
    }

    @Cacheable(value = "productList")
    public List<ProductModel> getProductByName(String name) {
        return this.repository.findByName(name);
    }

    @Cacheable(value = "productList")
    public List<ProductModel> getProductByCategory(String category) {
        return this.repository.findByCategory(category);
    }

    @Cacheable(value = "productList")
    public List<ProductModel> getProductByShopId(String shop_id) {return  this.repository.findByShop(shop_id);};

    @Cacheable
    public List<ProductModel> getProductByShopIdAndProductId(String shop_id, String product_id) {return this.repository.findByShopAndProductId(shop_id, product_id);}
}
