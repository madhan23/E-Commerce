package com.madhan.ecommerce.delegate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.madhan.ecommerce.dto.OrderInfoDto;
import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.dto.UserAuthentication;
import com.madhan.ecommerce.entity.Address;
import com.madhan.ecommerce.entity.Order;
import com.madhan.ecommerce.entity.OrderStatus;
import com.madhan.ecommerce.entity.Payment;
import com.madhan.ecommerce.exception.InvalidRequestDataException;
import com.madhan.ecommerce.service.OrderService;
import com.madhan.ecommerce.service.PaymentService;
import com.madhan.ecommerce.util.AppUtil;
import com.madhan.ecommerce.util.JwtTokenUtil;

@Component
public class OrderDelegator {

	@Autowired
	OrderService orderService;

	@Autowired
	JwtTokenUtil jwt;

	@Autowired
	PaymentService paymentService;

	public Response getOrder(String id, String authToken) {
		jwt.validateJWTToken(authToken);
		Order order = orderService.getOrder(id);
		if (order == null) {

			return new Response(AppUtil.SUCCESS, "No Order details found", LocalDateTime.now(), null);
		}
		return new Response(AppUtil.SUCCESS, "Record Fetched Successfully", LocalDateTime.now(), Arrays.asList(order));

	}

	public Response createOrder(Order order, String authToken) {
		String data = jwt.validateJWTToken(authToken);
		UserAuthentication auth = jwt.extractUserdetails(data);
		order.setUserId(auth.getEmail());
		order.setOrderTrackingId(UUID.randomUUID().toString());
		Order orderResponse = orderService.createOrder(order);
		Response paymentDetails = paymentService.makeStripePayment(order.getProducts(),
				orderResponse.getOrderTrackingId());
		return paymentDetails;
	}

	public Response updateOrder(OrderInfoDto  orderInfo) {
		String orderId = orderInfo.getOrderId();
		String paymentId = orderInfo.getPaymentId();
		if (orderId == null || orderId.isEmpty()) {
			throw new InvalidRequestDataException("OrderId should not be null or empty");
		}

		Order orderDetails = orderService.getOrder(orderId);

		if (orderDetails == null) {
			return Response.builder().status(AppUtil.SUCCESS).message("No Order details found")
					.dateTime(LocalDateTime.now()).build();
		}
		orderDetails.setOrderTrackingId(orderId);
		
		if (paymentId == null) {
			orderDetails.setStatus(OrderStatus.FAILED);
			Order orderResponse = orderService.updateOrder(orderDetails);
			return new Response(AppUtil.SUCCESS, "Updated Order details", LocalDateTime.now(), orderResponse);
		}
		
		String data = paymentService.getPaymentDetails(paymentId);

		JSONObject json = new JSONObject(data);
		JSONObject addressObj = json.getJSONObject("shipping").getJSONObject("address");
		JSONObject paymentDetails = json.getJSONObject("charges").getJSONArray("data").getJSONObject(0)
				.getJSONObject("payment_method_details");
		JSONObject card = paymentDetails.getJSONObject("card");

		Address address = new Address();
		address.setLine1(addressObj.get("line1").toString());
		address.setLine2(addressObj.get("line2").toString());
		address.setCity(addressObj.get("city").toString());
		address.setState(addressObj.get("state").toString());
		address.setPostalCode(addressObj.get("postal_code").toString());
		address.setCountry(addressObj.get("country").toString());

		Payment payment = new Payment();
		payment.setExpiry(card.get("exp_month").toString() + "/" + card.get("exp_year"));
		payment.setMode(paymentDetails.get("type").toString());
		payment.setId(paymentId);
		payment.setStatus(json.get("status").toString());
		payment.setCardNo(card.get("last4").toString());
		payment.setPartner(card.get("network").toString());

		orderDetails.setBillingAddress(address);
		orderDetails.setPayment(payment);
		orderDetails.setStatus(OrderStatus.PROCESSED);
		

		Order orderResponse = orderService.updateOrder(orderDetails);

		return new Response(AppUtil.SUCCESS, "Updated Order details", LocalDateTime.now(), orderResponse);
	}

}
