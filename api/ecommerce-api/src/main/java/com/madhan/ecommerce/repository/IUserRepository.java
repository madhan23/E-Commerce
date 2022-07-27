package com.madhan.ecommerce.repository;

import com.madhan.ecommerce.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface IUserRepository extends MongoRepository<User,String> {

	User findByEmail(String email);

	User findByUsername(String username);
	
	@Query("{'email' : ?0, 'password' : ?1}")
	User Login(String emailId,String password);


	User findByEmailAndProvider(String emailId,String provider);
}
