package com.madhan.ecommerce.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madhan.ecommerce.entity.Order;
import com.madhan.ecommerce.entity.OrderStatus;
import com.madhan.ecommerce.exception.OrderException;
import com.madhan.ecommerce.repository.IOrderRepository;
import com.madhan.ecommerce.util.AppUtil;

@Service
public class OrderService {

	@Autowired
	IOrderRepository orderRepository;

	public Order getOrder(String id) {

		return orderRepository.findById(id).orElse(null);
	}

	public Order createOrder(Order order) {
		try {
			order.setOrderTrackingId(UUID.randomUUID().toString());
			order.setCreatedAt(AppUtil.getDateTime());
			order.setStatus(OrderStatus.PENDING);
			return orderRepository.save(order);
		} catch (Exception e) {
			throw new OrderException("error occurred during Order Details Save");
		}

	}

	public Order updateOrder(Order order) {

		try {
			order.setUpdatedAt(AppUtil.getDateTime());
			return orderRepository.save(order);
		} catch (Exception e) {
			throw new OrderException("error occurred during Order Details Update");
		}

	}

}
