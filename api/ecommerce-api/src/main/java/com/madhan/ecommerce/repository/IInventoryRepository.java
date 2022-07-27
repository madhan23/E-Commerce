package com.madhan.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.madhan.ecommerce.dto.ProductDto;
import com.madhan.ecommerce.entity.Product;


public interface IInventoryRepository extends MongoRepository<Product, String> {

	//@Query(fields = "{_id:1,title:1,images:1,price:1,instock:1}")
    Page<ProductDto> findAllBy(Pageable pageable);




}
