package com.madhan.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.madhan.ecommerce.entity.Cart;
import com.madhan.ecommerce.entity.CartItem;
import com.madhan.ecommerce.exception.CartException;
import com.madhan.ecommerce.repository.ICartRepository;

@ExtendWith(MockitoExtension.class)
public class TestCartService {
	@InjectMocks
	CartService service;

	@Mock
	ICartRepository repo;

	@Test
	public void testGetCart() {

		Mockito.when(repo.findByUserId(Mockito.anyString())).thenReturn(getCartObj());
		Cart response = service.getCart("test@gmail.com");
		Mockito.verify(repo, Mockito.times(1)).findByUserId((Mockito.anyString()));
		assertEquals("test@gmail.com", response.getUserId());
		assertEquals(1, response.getProducts().size());
	}

	@Test
	public void testAddCart() {
		Cart cart = getCartObj();
		Mockito.when(repo.save(Mockito.any(Cart.class))).thenReturn(cart);
		Cart response = service.addCart(cart);
		assertEquals("test@gmail.com", response.getUserId());
		assertEquals(1, response.getProducts().size());

	}

	
	@Test
	public void testAddCartException() {
		Cart cart = getCartObj();
		Mockito.when(repo.save(Mockito.any(Cart.class))).thenThrow( new CartException("error Occurred During Cart Details Save"));
		assertThrows(CartException.class, () -> service.addCart(cart));

	}
	
	@Test
	public void testCheckUserIdExist() {
		Mockito.when(repo.existsById(Mockito.anyString())).thenReturn(true);
		assertTrue(service.checkUserIdExist("test@gmail.com"));
	}

	@Test
	public void testDeleteCartRecord() {
		Mockito.doNothing().when(repo).deleteById(Mockito.anyString());
		service.deleteProductFromCart("test@gmail.com");
	}

	private Cart getCartObj() {
		List<CartItem> itemList = new ArrayList<>();
		CartItem item = new CartItem();
		item.setImage("");
		item.setTitle("product");
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
