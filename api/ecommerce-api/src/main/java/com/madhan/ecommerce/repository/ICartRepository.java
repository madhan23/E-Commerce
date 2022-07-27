package com.madhan.ecommerce.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.madhan.ecommerce.entity.Cart;

public interface ICartRepository extends MongoRepository<Cart, String> {

	Cart findByUserId(String email);
	
	@Query("{'_id' : ?0, 'products.pid' : ?1}")
	Cart getCartDetails(String email,String productId);
}
