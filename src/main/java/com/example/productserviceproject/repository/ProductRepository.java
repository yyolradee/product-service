package com.example.productserviceproject.repository;

import com.example.productserviceproject.POJO.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    @Query(value="{name:'?0'}")
    public List<Product> findByName(String name);

    @Query(value="{category:'?0'}")
    public List<Product> findByCategory(String category);
}
