package com.madhan.ecommerce.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.madhan.ecommerce.delegate.InventoryDelegate;
import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.entity.Product;
import com.madhan.ecommerce.util.AppUtil;

@ExtendWith(MockitoExtension.class)
public class TestInventoryController {

	@InjectMocks
	private InventoryController controller;

	@Mock
	private InventoryDelegate delegate;

	@Spy
	private MockHttpServletRequest httpServletRequest;

	@Test
	public void testGetAllProducts() throws UnsupportedEncodingException {

		Response data = new Response(AppUtil.SUCCESS, "Record Fetched Successfully", LocalDateTime.now(), getProduct());
		Mockito.when(delegate.getAllProducts(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(data);
		ResponseEntity<Response> response = controller.getAllProducts("1", "5", "T-Shirt", "L", "Price", "desc");
		Mockito.verify(delegate).getAllProducts(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		assertEquals(AppUtil.SUCCESS, response.getBody().getStatus());
		assertEquals("Record Fetched Successfully", response.getBody().getMessage());
	}

	@Test
	public void testGetProduct() {
		Response data = new Response(AppUtil.SUCCESS, "Record Fetched Successfully", LocalDateTime.now(), getProduct());
		Mockito.when(delegate.getProduct(Mockito.anyString())).thenReturn(data);
		ResponseEntity<Response> response = controller.getproduct(UUID.randomUUID().toString());
		Mockito.verify(delegate).getProduct(Mockito.anyString());
		assertEquals(AppUtil.SUCCESS, response.getBody().getStatus());
		assertEquals("Record Fetched Successfully", response.getBody().getMessage());
	}

	@Test
	public void testSaveProduct() {
		httpServletRequest.addHeader(AppUtil.AUTHORIZATION, "Bearer GHFDGHJK");
		Response data = new Response(AppUtil.SUCCESS, "Product Saved Successfully", LocalDateTime.now(), getProduct());
		Mockito.when(delegate.saveproduct(Mockito.any(Product.class), Mockito.anyString())).thenReturn(data);
		ResponseEntity<Response> response = controller.saveproduct(getProduct(), httpServletRequest);
		Mockito.verify(delegate).saveproduct(Mockito.any(Product.class), Mockito.anyString());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Product Saved Successfully", response.getBody().getMessage());
	}

	@Test
	public void testUpdateProduct() {
		httpServletRequest.addHeader(AppUtil.AUTHORIZATION, "Bearer GHFDGHJK");
		Response data = new Response(AppUtil.SUCCESS, "Product Updated Successfully", LocalDateTime.now(), getProduct());
		Mockito.when(delegate.updateProduct(Mockito.any(Product.class), Mockito.anyString())).thenReturn(data);
		ResponseEntity<Response> response = controller.updateProduct(getProduct(), httpServletRequest);
		Mockito.verify(delegate).updateProduct(Mockito.any(Product.class), Mockito.anyString());
		assertEquals(AppUtil.SUCCESS, response.getBody().getStatus());
		assertEquals("Product Updated Successfully", response.getBody().getMessage());
	}

	@Test
	public void deleteProduct() {
		httpServletRequest.addHeader(AppUtil.AUTHORIZATION, "Bearer GHFDGHJK");
		Response data = Response.builder().status(AppUtil.SUCCESS).message("Product deleted Successfully")
				.dateTime(LocalDateTime.now()).build();
		Mockito.when(delegate.deleteProduct(Mockito.anyString(), Mockito.anyString())).thenReturn(data);
		ResponseEntity<Response> response = controller.deleteProduct(UUID.randomUUID().toString(), httpServletRequest);
		Mockito.verify(delegate).deleteProduct(Mockito.anyString(), Mockito.anyString());
		assertEquals(AppUtil.SUCCESS, response.getBody().getStatus());
		assertEquals("Product deleted Successfully", response.getBody().getMessage());
	}

	private Product getProduct() {
		Product product = new Product();
		product.setId(UUID.randomUUID().toString());
		product.setCategories(List.of("T-Shirt", "Men"));
		product.setTitle("Trends Collections");
		// product.setColor(null); Need to work
		product.setSize(List.of("S", "M", "L"));
		product.setPrice(BigDecimal.valueOf(499));
		product.setCreatedAt(LocalDateTime.now());
		return product;
	}

}
