package com.madhan.ecommerce.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.madhan.ecommerce.delegate.CartDelegate;
import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.entity.Cart;
import com.madhan.ecommerce.entity.CartItem;
import com.madhan.ecommerce.util.AppUtil;

@ExtendWith(MockitoExtension.class)
public class TestCartController {

	@InjectMocks
	private CartController controller;

	@Mock
	private CartDelegate cartdelegate;

	@Spy
	private MockHttpServletRequest httpServletRequest;

	@BeforeEach
	public void init() {
		httpServletRequest.addHeader(AppUtil.AUTHORIZATION, "Bearer GHFDGHJK");
	}

	@Test
	public void testGetCart() {

		Response data = new Response(AppUtil.SUCCESS, "Record Fetched Successfully", LocalDateTime.now(), getCartObj());
		Mockito.when(cartdelegate.getCart(Mockito.anyString())).thenReturn(data);
		ResponseEntity<Response> response = controller.getCart(httpServletRequest);
		Mockito.verify(cartdelegate).getCart(Mockito.anyString());
		assertEquals(AppUtil.SUCCESS, response.getBody().getStatus());
		assertEquals("Record Fetched Successfully", response.getBody().getMessage());

	}

	@Test
	public void testAddCart() {
		Response data = new Response(AppUtil.SUCCESS, "Cart Data Updated Successfully", LocalDateTime.now(),
				getCartObj());
		Mockito.when(cartdelegate.addCart(Mockito.any(Cart.class), Mockito.any())).thenReturn(data);
		ResponseEntity<Response> response = controller.addCart(getCartObj(), httpServletRequest);
		Mockito.verify(cartdelegate, Mockito.times(1)).addCart(Mockito.any(Cart.class), Mockito.any());
		assertEquals(AppUtil.SUCCESS, response.getBody().getStatus());
		assertEquals("Cart Data Updated Successfully", response.getBody().getMessage());
	}

	@Test
	public void testdeleteCartItem() {

		Response data = Response.builder().status(AppUtil.SUCCESS).message("Cart Item Removed Successfully")
				.dateTime(LocalDateTime.now()).build();
		Mockito.when(cartdelegate.deleteItem(Mockito.anyString(), Mockito.anyString())).thenReturn(data);
		ResponseEntity<Response> response = controller.deleteItemFromCart(UUID.randomUUID().toString(),
				httpServletRequest);
		Mockito.verify(cartdelegate, Mockito.times(1)).deleteItem(Mockito.anyString(), Mockito.anyString());
		assertEquals(AppUtil.SUCCESS, response.getBody().getStatus());
		assertEquals("Cart Item Removed Successfully", response.getBody().getMessage());
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
		cart.setUserId("test@gmail.com");
		cart.setCreatedAt(LocalDateTime.now());
		cart.setProducts(itemList);
		return cart;
	}
}
