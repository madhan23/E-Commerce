package com.madhan.ecommerce.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.madhan.ecommerce.delegate.OrderDelegator;
import com.madhan.ecommerce.dto.OrderInfoDto;
import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.entity.Address;
import com.madhan.ecommerce.entity.CartItem;
import com.madhan.ecommerce.entity.Order;
import com.madhan.ecommerce.entity.Payment;
import com.madhan.ecommerce.util.AppUtil;

@ExtendWith(MockitoExtension.class)
public class TestOrderController {
	@InjectMocks
	private OrderController controller;
	@Mock
	private OrderDelegator orderDelegator;

	@Spy
	private MockHttpServletRequest httpServletRequest;

	@BeforeEach
	public void init() {
		httpServletRequest.addHeader(AppUtil.AUTHORIZATION, "Bearer GHFDGHJK");
	}

	@Test
	public void testGetOrder() {

		List<Order> orders = List.of(getOrderData());
		Response data = new Response(AppUtil.SUCCESS, "Record Fetched Successfully", LocalDateTime.now(), orders);
		Mockito.when(orderDelegator.getOrder(Mockito.anyString(), Mockito.anyString())).thenReturn(data);
		ResponseEntity<Response> response = controller.getOrder(UUID.randomUUID().toString(), httpServletRequest);
		Mockito.verify(orderDelegator, Mockito.times(1)).getOrder(Mockito.anyString(), Mockito.anyString());
		assertEquals(AppUtil.SUCCESS, response.getBody().getStatus());
		assertEquals("Record Fetched Successfully", response.getBody().getMessage());
	}

	@Test
	public void testCreateOrder() {

		Map<String, String> payment = new HashMap<>();
		payment.put("orderId", "546787");
		payment.put("PaymentId", "HGJKLIKJHGC");
		payment.put("PaymentURL", "http://test.com");

		Response data = new Response(AppUtil.SUCCESS, "Order In Process..", LocalDateTime.now(), payment);

		Mockito.when(orderDelegator.createOrder(Mockito.any(Order.class), Mockito.anyString())).thenReturn(data);
		ResponseEntity<Response> response = controller.createOrder(getOrderData(), httpServletRequest);
		Mockito.verify(orderDelegator, Mockito.times(1)).createOrder(Mockito.any(Order.class), Mockito.anyString());
		assertEquals(AppUtil.SUCCESS, response.getBody().getStatus());
		assertEquals("Order In Process..", response.getBody().getMessage());
	}

	@Test
	public void testUpdateOrder() {

		OrderInfoDto orderInfo = new OrderInfoDto(UUID.randomUUID().toString(), "HJKKHGFHJK");

		Order order = getOrderData();
		order.setPayment(new Payment("4556", "card", "08", "23", "success", "Visa"));
		Address address = new Address();
		address.setLine1("West street");
		address.setLine2("thiruvanmiyur");
		address.setCity("chennai");
		address.setState("Tamil Nadu");
		address.setPostalCode("600041");
		order.setBillingAddress(address);
		Response data = new Response(AppUtil.SUCCESS, "Updated Order details", LocalDateTime.now(), order);
		Mockito.when(orderDelegator.updateOrder(Mockito.any(OrderInfoDto.class))).thenReturn(data);
		ResponseEntity<Response> response = controller.updateOrder(orderInfo);
		Mockito.verify(orderDelegator, Mockito.times(1)).updateOrder(Mockito.any(OrderInfoDto.class));
		assertEquals(AppUtil.SUCCESS, response.getBody().getStatus());
		assertEquals("Updated Order details", response.getBody().getMessage());
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
		order.setOrderTrackingId(UUID.randomUUID().toString());
		order.setTotalQty(1);
		order.setProducts(itemList);
		order.setCreatedAt(LocalDateTime.now());
		order.setTotalAmount(BigDecimal.valueOf(500));
		return order;

	}
}
