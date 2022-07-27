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

import com.madhan.ecommerce.dto.ProductDto;
import com.madhan.ecommerce.dto.ProductResponse;
import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.entity.Product;
import com.madhan.ecommerce.service.InventoryService;
import com.madhan.ecommerce.util.AppUtil;
import com.madhan.ecommerce.util.JwtTokenUtil;

@ExtendWith(MockitoExtension.class)
public class TestInventoryDelegate {
	@InjectMocks
	InventoryDelegate delegate;

	@Mock
	InventoryService service;
	@Mock
	JwtTokenUtil jwt;

	@Test
	public void testGetAllProducts() {
		ProductDto product = new ProductDto();
		product.setId(UUID.randomUUID().toString());
		product.setTitle("Trends Collections");
		product.setPrice(BigDecimal.valueOf(499));
		List<ProductDto> products = new ArrayList<>();
		products.add(product);
		ProductResponse response = new ProductResponse();
		response.setPage(1);
		response.setLimit(5);
		response.setProducts(products);
		response.setTotalRecords(100);
		Mockito.when(service.getAllProducts(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(response);
		Response data = delegate.getAllProducts("0", "5", "T-Shirt", "S", "price", "desc");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Record Fetched Successfully", data.getMessage());

	}

	@Test
	public void testSaveproduct() {
		Mockito.doNothing().when(jwt).authorizeRequest(Mockito.anyString());
		Mockito.when(service.saveproduct(Mockito.any(Product.class))).thenReturn(getProduct());
		Response data = delegate.saveproduct(getProduct(), "Bearer Token");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Product Saved Successfully", data.getMessage());
	}

	@Test
	public void testUpdateProduct() {
		Mockito.doNothing().when(jwt).authorizeRequest(Mockito.anyString());	
		Mockito.when(service.updateProduct(Mockito.any(Product.class))).thenReturn(getProduct());
		Response data = delegate.updateProduct(getProduct(), "Bearer Token");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Product Updated Successfully", data.getMessage());
	}
	
	@Test
	public void testDeleteProduct() {
		Mockito.doNothing().when(jwt).authorizeRequest(Mockito.anyString());
		Mockito.when(service.findProduct(Mockito.anyString())).thenReturn(getProduct());
		Mockito.doNothing().when(service).deleteProduct(Mockito.anyString());
		Response data = delegate.deleteProduct(UUID.randomUUID().toString(), "Bearer Token");
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Product Deleted Successfully", data.getMessage());
	}
	
	@Test
	public void testGetProduct() {
		Mockito.when(service.findProduct(Mockito.anyString())).thenReturn(getProduct());
		Response data = delegate.getProduct(UUID.randomUUID().toString());
		assertEquals(AppUtil.SUCCESS, data.getStatus());
		assertEquals("Record Fetched Successfully", data.getMessage());
	}
	
	private Product getProduct() {
		Product product = new Product();
		product.setId(UUID.randomUUID().toString());
		product.setCategories(List.of("T-Shirt", "Men"));
		product.setTitle("Trends Collections");
		product.setSize(List.of("S", "M", "L"));
		product.setPrice(BigDecimal.valueOf(499));
		product.setCreatedAt(LocalDateTime.now());
		return product;
	}
	
	
}
