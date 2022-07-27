package com.madhan.ecommerce.delegate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.dto.UserAuthentication;
import com.madhan.ecommerce.entity.Cart;
import com.madhan.ecommerce.entity.CartItem;
import com.madhan.ecommerce.service.CartService;
import com.madhan.ecommerce.util.AppUtil;
import com.madhan.ecommerce.util.JwtTokenUtil;

@ExtendWith(MockitoExtension.class)
public class TestCartDelegate {
	@InjectMocks
	CartDelegate delegate;

	@Mock
	CartService service;
	@Mock
	JwtTokenUtil jwt;

	@Test
	public void testGetCart() {
		UserAuthentication auth = new UserAuthentication("test@gmail.com", true);
		Mockito.when(jwt.validateJWTToken(Mockito.anyString())).thenReturn("");
		Mockito.when(jwt.extractUserdetails(Mockito.anyString())).thenReturn(auth);
		Mockito.when(service.getCart(Mockito.anyString())).thenReturn(getCartObj());
		Response data = delegate.getCart("Bearer token");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Record Fetched Successfully", data.getMessage());
	}

	@Test
	public void testGetCartForEmpty() {
		UserAuthentication auth = new UserAuthentication("test@gmail.com", true);
		Mockito.when(jwt.validateJWTToken(Mockito.anyString())).thenReturn("");
		Mockito.when(jwt.extractUserdetails(Mockito.anyString())).thenReturn(auth);
		Mockito.when(service.getCart(Mockito.anyString())).thenReturn(null);
		Response data = delegate.getCart("Bearer token");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("No Cart Items Found", data.getMessage());
	}

	@Test
	public void testAddCart() {
		UserAuthentication auth = new UserAuthentication("test@gmail.com", true);
		Mockito.when(jwt.validateJWTToken(Mockito.anyString())).thenReturn("");
		Mockito.when(jwt.extractUserdetails(Mockito.anyString())).thenReturn(auth);
		Mockito.when(service.checkUserIdExist(Mockito.anyString())).thenReturn(false);
		Mockito.when(service.addCart(Mockito.any(Cart.class))).thenReturn(getCartObj());
		Response data = delegate.addCart(getCartObj(), "Bearer token");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Cart Data Updated Successfully", data.getMessage());
	}

	@Test
	public void testUpdateCart() {
		UserAuthentication auth = new UserAuthentication("test@gmail.com", true);
		Mockito.when(jwt.validateJWTToken(Mockito.anyString())).thenReturn("");
		Mockito.when(jwt.extractUserdetails(Mockito.anyString())).thenReturn(auth);
		Mockito.when(service.checkUserIdExist(Mockito.anyString())).thenReturn(true);
		Mockito.when(service.addCart(Mockito.any(Cart.class))).thenReturn(getCartObj());
		Response data = delegate.addCart(getCartObj(), "Bearer token");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Cart Data Updated Successfully", data.getMessage());
	}

	@Test
	public void testDeleteItem() {
		UserAuthentication auth = new UserAuthentication("test@gmail.com", true);
		Mockito.when(jwt.validateJWTToken(Mockito.anyString())).thenReturn("");
		Mockito.when(jwt.extractUserdetails(Mockito.anyString())).thenReturn(auth);
		Mockito.when(service.getCartDetails(Mockito.anyString(),Mockito.anyString())).thenReturn(getCartObj());
		Mockito.when(service.addCart(Mockito.any(Cart.class))).thenReturn(getCartObj());
		Response data = delegate.deleteItem(UUID.randomUUID().toString(), "Bearer token");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Product Removed From Cart Successfully", data.getMessage());
	}

	
	@Test
	public void testDeleteCartData() {
		List<CartItem> itemList = new ArrayList<>();
		Cart cart = new Cart();
		cart.setProducts(itemList);
		cart.setUserId("test@gmail.com");
		cart.setCreatedAt(LocalDateTime.now());
		UserAuthentication auth = new UserAuthentication("test@gmail.com", true);
		Mockito.when(jwt.validateJWTToken(Mockito.anyString())).thenReturn("");
		Mockito.when(jwt.extractUserdetails(Mockito.anyString())).thenReturn(auth);
		Mockito.when(service.getCartDetails(Mockito.anyString(),Mockito.anyString())).thenReturn(cart);
		Mockito.doNothing().when(service).deleteProductFromCart(Mockito.anyString());
		Response data = delegate.deleteItem(UUID.randomUUID().toString(), "Bearer token");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Product Removed From Cart Successfully", data.getMessage());
	}
	private Cart getCartObj() {
		List<CartItem> itemList = new ArrayList<>();
		CartItem item = new CartItem();
		item.setPid(UUID.randomUUID().toString());
		item.setImage("");
		item.setTitle("test");
		item.setQuantity(2);
		item.setPrice(BigDecimal.valueOf(200));
		itemList.add(item);
		Cart cart = new Cart();
		cart.setCreatedAt(LocalDateTime.now());
		cart.setProducts(itemList);
		return cart;
	}
}
