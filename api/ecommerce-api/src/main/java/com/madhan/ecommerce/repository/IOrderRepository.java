package com.madhan.ecommerce.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.madhan.ecommerce.entity.Order;

public interface IOrderRepository extends MongoRepository<Order, String> {

}
