package com.madhan.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madhan.ecommerce.entity.Cart;
import com.madhan.ecommerce.exception.CartException;
import com.madhan.ecommerce.repository.ICartRepository;

@Service
public class CartService {

	@Autowired
	ICartRepository repo;

	public Cart getCart(String email) {

		return repo.findByUserId(email);

	}
	
	public Cart getCartDetails(String email, String productId) {

		return repo.getCartDetails(email, productId);

	}

	public Cart addCart(Cart cart) {

		try {
			return repo.save(cart);
		} catch (Exception e) {
			throw new CartException("error Occurred During Cart Details Save");
		}

	}

	public boolean checkUserIdExist(String email) {
		return repo.existsById(email);
	}

	public void deleteProductFromCart(String email) {

		repo.deleteById(email);
	}

}
