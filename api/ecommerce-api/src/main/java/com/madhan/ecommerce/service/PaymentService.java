package com.madhan.ecommerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.entity.CartItem;
import com.madhan.ecommerce.exception.PaymentException;
import com.madhan.ecommerce.util.AppUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.AdjustableQuantity;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import com.stripe.param.checkout.SessionCreateParams.ShippingAddressCollection;
import com.stripe.param.checkout.SessionCreateParams.ShippingAddressCollection.AllowedCountry;

@Service
public class PaymentService {

	@Value("${stripe.apikey}")
	private String stripe_api_key;

	@Value("${domain.url}")
	private String domain_url;

	private Session session = null;

	@Value("${stripe.paymentIntentUrl}")
	private String payment_intent_url;

	@Autowired
	RestTemplate restTemplate;

	public Response makeStripePayment(List<CartItem> cartItem, String orderId) {

		Stripe.apiKey = stripe_api_key;
		List<LineItem> lineItemList = new ArrayList<>();

		cartItem.stream().forEach(item -> {
			ProductData productData = ProductData.builder().addImage(item.getImage()).setName(item.getTitle()).build();
			PriceData priceData = PriceData.builder().setCurrency("inr")
					.setUnitAmount(Long.valueOf(item.getPrice().toString()) * 100).setProductData(productData).build();
			AdjustableQuantity aq = AdjustableQuantity.builder().setEnabled(true).setMinimum(1L).build();
			LineItem lineItem = SessionCreateParams.LineItem.builder().setPriceData(priceData)
					.setQuantity(Long.valueOf(item.getQuantity())).setAdjustableQuantity(aq).build();

			lineItemList.add(lineItem);
		});

		ShippingAddressCollection shippingaddress = ShippingAddressCollection.builder()
				.addAllowedCountry(AllowedCountry.IN).build();

		SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl(domain_url + "/success").setCancelUrl(domain_url + "/canceled")
				.addAllLineItem(lineItemList).setShippingAddressCollection(shippingaddress)
				.addShippingRate("shr_1KvrzZSHTuE0aIU0C7dIp6BD").build();

		try {
			session = Session.create(params);
			if (session == null) {
				return Response.builder().status(AppUtil.FAILURE).message("Something went wrong during payment Process")
						.dateTime(LocalDateTime.now()).build();
			}

		} catch (StripeException e) {
			throw new PaymentException(e.getMessage());
		}

		Map<String, String> payment = new HashMap<>();
		payment.put("orderId", orderId);
		payment.put("PaymentId", session.getPaymentIntent());
		payment.put("PaymentURL", session.getUrl());

		return new Response(AppUtil.SUCCESS, "Order In Process..", LocalDateTime.now(), payment);

	}

	public String getPaymentDetails(String id) {

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + stripe_api_key);
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(payment_intent_url + id, HttpMethod.GET, requestEntity,
				String.class);

		return response.getBody();

	}

}
