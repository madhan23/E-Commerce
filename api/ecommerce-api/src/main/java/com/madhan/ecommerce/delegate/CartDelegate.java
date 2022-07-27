package com.madhan.ecommerce.delegate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.dto.UserAuthentication;
import com.madhan.ecommerce.entity.Cart;
import com.madhan.ecommerce.entity.CartItem;
import com.madhan.ecommerce.exception.CartException;
import com.madhan.ecommerce.service.CartService;
import com.madhan.ecommerce.util.AppUtil;
import com.madhan.ecommerce.util.JwtTokenUtil;

@Component
public class CartDelegate {

	@Autowired
	CartService cartService;

	@Autowired
	JwtTokenUtil jwt;

	@Autowired
	ObjectMapper mapper;

	public Response getCart(String authHeader) {

		String data = jwt.validateJWTToken(authHeader);
		UserAuthentication auth = jwt.extractUserdetails(data);
		Cart cart = cartService.getCart(auth.getEmail());
		if (cart == null) {
			return Response.builder().status(AppUtil.SUCCESS).message("No Cart Items Found")
					.dateTime(LocalDateTime.now()).build();
		}

		return new Response(AppUtil.SUCCESS, "Record Fetched Successfully", LocalDateTime.now(), cart);
	}

	public Response addCart(Cart cartRequest, String authHeader) {
		String data = jwt.validateJWTToken(authHeader);
		UserAuthentication auth = jwt.extractUserdetails(data);

		cartRequest.setUserId(auth.getEmail());

		if (cartService.checkUserIdExist(auth.getEmail())) {
			cartRequest.setUpdatedAt(AppUtil.getDateTime());
		} else {
			cartRequest.setCreatedAt(AppUtil.getDateTime());
		}

		Cart cart = cartService.addCart(cartRequest);

		return new Response(AppUtil.SUCCESS, "Cart Data Updated Successfully", LocalDateTime.now(), cart);
	}

	public Response deleteItem(String productId, String authHeader) {

		String data = jwt.validateJWTToken(authHeader);
		UserAuthentication auth = jwt.extractUserdetails(data);
		Cart cart = cartService.getCartDetails(auth.getEmail(), productId);

		if (cart == null) {
			throw new CartException("No Product Details Found");
		}
		List<CartItem> dbCartItems = cart.getProducts();
		List<CartItem> cartItems = dbCartItems.stream().filter(item -> !item.getPid().equals(productId))
				.collect(Collectors.toList());

		if (cartItems.isEmpty()) {
			cartService.deleteProductFromCart(cart.getUserId());
		} else {

			cart.setProducts(cartItems);
			cart.setTotalQty(cartItems.size());
			BigDecimal sum = cartItems.stream()
					.map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			cart.setTotalAmount(sum);
			cart.setUpdatedAt(LocalDateTime.now());
			cartService.addCart(cart);
		}

		return Response.builder().status(AppUtil.SUCCESS).message("Product Removed From Cart Successfully")
				.dateTime(LocalDateTime.now()).build();
	}

}
