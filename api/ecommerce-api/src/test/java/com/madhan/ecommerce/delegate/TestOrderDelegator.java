package com.madhan.ecommerce.delegate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.madhan.ecommerce.dto.OrderInfoDto;
import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.dto.UserAuthentication;
import com.madhan.ecommerce.entity.CartItem;
import com.madhan.ecommerce.entity.Order;
import com.madhan.ecommerce.exception.InvalidRequestDataException;
import com.madhan.ecommerce.service.OrderService;
import com.madhan.ecommerce.service.PaymentService;
import com.madhan.ecommerce.util.AppUtil;
import com.madhan.ecommerce.util.JwtTokenUtil;

@ExtendWith(MockitoExtension.class)
public class TestOrderDelegator {

	@InjectMocks
	private OrderDelegator orderDelegator;
	@Mock
	OrderService orderService;

	@Mock
	JwtTokenUtil jwt;

	@Mock
	PaymentService paymentService;

	@Test
	public void testGetOrder() {
		Mockito.when(jwt.validateJWTToken(Mockito.anyString())).thenReturn("");
		Mockito.when(orderService.getOrder(Mockito.anyString())).thenReturn(getOrderData());
		Response data = orderDelegator.getOrder(UUID.randomUUID().toString(), "Bearer token");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Record Fetched Successfully", data.getMessage());
	}

	@Test
	public void testGetOrderForNoOrderData() {
		Mockito.when(jwt.validateJWTToken(Mockito.anyString())).thenReturn("");
		Mockito.when(orderService.getOrder(Mockito.anyString())).thenReturn(null);
		Response data = orderDelegator.getOrder(UUID.randomUUID().toString(), "Bearer token");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("No Order details found", data.getMessage());
	}

	@Test
	public void testCreateOrder() {
		Order order = getOrderData();
		UserAuthentication auth = new UserAuthentication("test@gmail.com", true);
		Mockito.when(jwt.validateJWTToken(Mockito.anyString())).thenReturn("");
		Mockito.when(jwt.extractUserdetails(Mockito.anyString())).thenReturn(auth);
		Mockito.when(orderService.createOrder(Mockito.any(Order.class))).thenReturn(order);
		Map<String, String> payment = new HashMap<>();
		payment.put("orderId", UUID.randomUUID().toString());
		Response reponse = new Response(AppUtil.SUCCESS, "Order In Process..", LocalDateTime.now(), payment);
		Mockito.when(paymentService.makeStripePayment(Mockito.any(), Mockito.any())).thenReturn(reponse);
		Response data = orderDelegator.createOrder(order, "Bearer GFHJKH");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Order In Process..", data.getMessage());
	}

	@Test
	public void testUpdateOrder() {

		String paymentDetails = "{\"status\":\"succeed\",\"shipping\":{\"address\":{\"city\":\"test\",\"country\":\"IN\",\"line1\":\"test\",\"line2\":\"test\",\"postal_code\":\"600004\",\"state\":\"Karnataka\"},\"carrier\":null,\"name\":\"madhan\",\"phone\":null,\"tracking_number\":null},\"charges\":{\"data\":[{\"payment_method_details\":{\"card\":{\"brand\":\"visa\",\"checks\":{\"address_line1_check\":\"pass\",\"address_postal_code_check\":\"pass\",\"cvc_check\":\"pass\"},\"country\":\"US\",\"exp_month\":9,\"exp_year\":2023,\"fingerprint\":\"BHSCnOE3AYhxuEYm\",\"funding\":\"credit\",\"installments\":null,\"last4\":\"4242\",\"mandate\":null,\"network\":\"visa\",\"three_d_secure\":{\"authentication_flow\":null,\"result\":\"attempt_acknowledged\",\"result_reason\":null,\"version\":\"1.0.2\"},\"wallet\":null},\"type\":\"card\"}}]}}";
		Mockito.when(orderService.getOrder(Mockito.anyString())).thenReturn(getOrderData());
		Mockito.when(paymentService.getPaymentDetails(Mockito.anyString())).thenReturn(paymentDetails);
		Mockito.when(orderService.updateOrder(Mockito.any(Order.class))).thenReturn(getOrderData());
		OrderInfoDto request = new OrderInfoDto("orderId45", "paymentId2345");
		Response data = orderDelegator.updateOrder(request);
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Updated Order details", data.getMessage());

	}

	@Test
	public void testUpdateOrderForInvalidOrderId() {
		OrderInfoDto request = new OrderInfoDto(null, "paymentId2345");
		try {
			orderDelegator.updateOrder(request);
		} catch (InvalidRequestDataException e) {
			assertEquals("OrderId should not be null or empty", e.getMessage());
		}

	}

	@Test
	public void testUpdateOrderForNoOrderFound() {
		OrderInfoDto request = new OrderInfoDto("orderId45", "paymentId2345");
		Mockito.when(orderService.getOrder(Mockito.anyString())).thenReturn(null);
		Response data = orderDelegator.updateOrder(request);
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("No Order details found", data.getMessage());
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
		order.setCreatedAt(LocalDateTime.now());
		order.setTotalAmount(BigDecimal.valueOf(500));
		return order;

	}
}
