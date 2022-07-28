package com.madhan.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.madhan.ecommerce.dto.ProductDto;
import com.madhan.ecommerce.entity.Product;


public interface IInventoryRepository extends MongoRepository<Product, String> {

    Page<ProductDto> findAllBy(Pageable pageable);

}
