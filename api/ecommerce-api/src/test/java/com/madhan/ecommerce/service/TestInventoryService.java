package com.madhan.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;

import com.madhan.ecommerce.entity.Product;
import com.madhan.ecommerce.exception.ProductException;
import com.madhan.ecommerce.exception.ProductNotFoundException;
import com.madhan.ecommerce.repository.IInventoryRepository;

@ExtendWith(MockitoExtension.class)
public class TestInventoryService {
	@InjectMocks
	InventoryService service;

	@Mock
	IInventoryRepository repo;

	@Mock
	MongoTemplate mongoTemplate;

	@Test
	public void testFindProduct() {
		Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(getProduct()));
		Product product = service.findProduct(UUID.randomUUID().toString());
		assertEquals("Trends Collections", product.getTitle());
		assertEquals(3, product.getSize().size());
	}

	@Test
	public void testFindProductForProductNotFoundException() {
		Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
		assertThrows(ProductNotFoundException.class, () -> service.findProduct(UUID.randomUUID().toString()));

	}

	@Test
	public void testDeleteProduct() {
		Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(getProduct()));
		Mockito.doNothing().when(repo).deleteById(Mockito.anyString());
		service.deleteProduct(UUID.randomUUID().toString());
	}

	@Test
	public void testUpdateProduct() {
		Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(getProduct()));
		Mockito.when(repo.save(Mockito.any(Product.class))).thenReturn(getProduct());
		Product product = service.updateProduct(getProduct());
		assertEquals("Trends Collections", product.getTitle());
		assertEquals(3, product.getSize().size());
	}

	@Test
	public void testUpdateProductForException() {
		Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(getProduct()));
		Mockito.when(repo.save(Mockito.any(Product.class))).thenThrow(
				new ProductException("Error Occurred during Product Update", HttpStatus.INTERNAL_SERVER_ERROR));
		assertThrows(ProductException.class, () -> service.updateProduct(getProduct()));

	}

	@Test
	public void testSaveProduct() {
		Mockito.when(repo.save(Mockito.any(Product.class))).thenReturn(getProduct());
		Product product = service.saveproduct(getProduct());
		assertEquals("Trends Collections", product.getTitle());
		assertEquals(3, product.getSize().size());
	}

	@Test
	public void testSaveProductForException() {
		Mockito.when(repo.save(Mockito.any(Product.class))).thenThrow(
				new ProductException("Error Occurred during Product Update", HttpStatus.INTERNAL_SERVER_ERROR));
		assertThrows(ProductException.class, () -> service.saveproduct(getProduct()));

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
