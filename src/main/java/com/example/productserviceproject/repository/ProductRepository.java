package com.example.productserviceproject.repository;

import com.example.productserviceproject.model.ProductModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends MongoRepository<ProductModel, String> {
    @Query(value="{name:'?0'}")
    public List<ProductModel> findByName(String name);

    @Query(value="{category:'?0'}")
    public List<ProductModel> findByCategory(String category);

    @Query(value="{shop_id:'?0'}")
    public List<ProductModel> findByShop(String shop_id);

    @Query(value = "{ 'shop_id' : ?0, '_id' : ?1 }")
    public List<ProductModel> findByShopAndProductId(String shop_id, String _id);
}
