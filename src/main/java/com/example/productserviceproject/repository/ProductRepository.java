package com.example.productserviceproject.repository;

import com.example.productserviceproject.model.query.ProductQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends MongoRepository<ProductQuery, String> {
    @Query(value="{name:'?0'}")
    public List<ProductQuery> findByName(String name);

    @Query(value="{category:'?0'}")
    public List<ProductQuery> findByCategory(String category);

    @Query(value="{shop_id:'?0'}")
    public List<ProductQuery> findByShop(String shop_id);

    @Query(value = "{ 'shop_id' : ?0, '_id' : ?1 }")
    public List<ProductQuery> findByShopAndProductId(String shop_id, String _id);
}
