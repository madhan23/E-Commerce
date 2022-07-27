package com.madhan.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.madhan.ecommerce.entity.Address;
import com.madhan.ecommerce.entity.CartItem;
import com.madhan.ecommerce.entity.Order;
import com.madhan.ecommerce.entity.OrderStatus;
import com.madhan.ecommerce.entity.Payment;
import com.madhan.ecommerce.exception.OrderException;
import com.madhan.ecommerce.repository.IOrderRepository;

@ExtendWith(MockitoExtension.class)
public class TestOrderService {
	@InjectMocks
	OrderService service;

	@Mock
	IOrderRepository repo;

	@Test
	public void getOrder() {
		String id = UUID.randomUUID().toString();
		Order order = getOrderData();
		order.setOrderTrackingId(id);
		Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(order));
		Order response = service.getOrder(id);
		assertEquals(id, response.getOrderTrackingId());
		assertEquals("test@gmail.com", response.getUserId());
	}

	@Test
	public void testCreateOrder() {
		Order order = getOrderData();
		Mockito.when(repo.save(Mockito.any(Order.class))).thenReturn(order);
		Order response = service.createOrder(order);
		assertTrue(response.getCreatedAt() != null);
		assertEquals(OrderStatus.PENDING, response.getStatus());
	}

	@Test
	public void testCreateOrderException() {
		Order order = getOrderData();
		Mockito.when(repo.save(Mockito.any(Order.class)))
				.thenThrow(new OrderException("error occurred during Order Details Save"));
		assertThrows(OrderException.class, () -> service.createOrder(order));

	}

	
	@Test
	public void testUpdateOrder() {
		Order order = getOrderData();
		order.setPayment(new Payment("4556", "card", "08", "23", "success", "Visa"));
		Address address = new Address();
		address.setLine1("West street");
		address.setLine2("thiruvanmiyur");
		address.setCity("chennai");
		address.setState("Tamil Nadu");
		address.setPostalCode("600041");
		order.setBillingAddress(address);
		Mockito.when(repo.save(Mockito.any(Order.class))).thenReturn(order);
		Order response = service.updateOrder(order);
		assertEquals("West street",response.getBillingAddress().getLine1());
		assertEquals("Visa",response.getPayment().getPartner());
	}
	
	@Test
	public void testUpdateOrderException() {
		Order order = getOrderData();
		Mockito.when(repo.save(Mockito.any(Order.class)))
				.thenThrow(new OrderException("error occurred during Order Details Update"));
		assertThrows(OrderException.class, () -> service.updateOrder(order));

	}

	private Order getOrderData() {

		CartItem item = new CartItem();
		item.setPid(UUID.randomUUID().toString());
		item.setImage("");
		item.setTitle("test");
		item.setQuantity(2);
		item.setPrice(BigDecimal.valueOf(200));
		List<CartItem> itemList = new ArrayList<>();
		itemList.add(item);
		Order order = new Order();
		order.setUserId("test@gmail.com");
		order.setTotalQty(1);
		order.setProducts(itemList);
		order.setTotalAmount(BigDecimal.valueOf(500));
		return order;

	}
}
