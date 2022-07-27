package com.madhan.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.madhan.ecommerce.entity.CartItem;
import com.madhan.ecommerce.exception.PaymentException;
import com.stripe.exception.StripeException;

@ExtendWith(MockitoExtension.class)
public class TestPaymentService {

	@InjectMocks
	PaymentService service;

	@Mock
	RestTemplate restTemplate;

	@Test
	public void testMakeStripePaymentForPaymentException() throws StripeException {
		List<CartItem> itemList = new ArrayList<>();
		CartItem item = new CartItem();
		item.setImage("");
		item.setTitle("product");
		item.setQuantity(2);
		item.setPrice(BigDecimal.valueOf(200));
		itemList.add(item);
		try {
			service.makeStripePayment(itemList, UUID.randomUUID().toString());
		} catch (PaymentException e) {
			assertTrue(e.getMessage() != null);
		}

	}

	@Test
	public void testGetPaymentDetails() throws StripeException {

		ResponseEntity<String> entity = new ResponseEntity<String>("response data", HttpStatus.OK);
		Mockito.when(restTemplate.exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(HttpEntity.class), ArgumentMatchers.<Class<String>>any())).thenReturn(entity);
		String response = service.getPaymentDetails(UUID.randomUUID().toString());
		assertEquals("response data", response);

	}

}
