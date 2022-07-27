package com.madhan.ecommerce.delegate;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.madhan.ecommerce.dto.ProductResponse;
import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.entity.Product;
import com.madhan.ecommerce.service.InventoryService;
import com.madhan.ecommerce.util.AppUtil;
import com.madhan.ecommerce.util.JwtTokenUtil;

@Component
public class InventoryDelegate {

	@Autowired
	InventoryService inventoryService;

	@Autowired
	JwtTokenUtil jwt;

	public Response getAllProducts(String page, String limit, String categories, String size, String sortBy,
			String orderBy) {

		ProductResponse product = inventoryService.getAllProducts(page, limit, categories, size, sortBy, orderBy);

		return new Response(AppUtil.SUCCESS, "Record Fetched Successfully", LocalDateTime.now(), product);
	}

	public Response saveproduct(Product request, String authHeader) {

		jwt.authorizeRequest(authHeader);
		Product product = inventoryService.saveproduct(request);
		return new Response(AppUtil.SUCCESS, "Product Saved Successfully", LocalDateTime.now(), product);
	}

	public Response updateProduct(Product request, String authHeader) {
		jwt.authorizeRequest(authHeader);
		Product product = inventoryService.updateProduct(request);
		return new Response(AppUtil.SUCCESS, "Product Updated Successfully", LocalDateTime.now(), product);
	}

	public Response deleteProduct(String productId, String authHeader) {
		jwt.authorizeRequest(authHeader);
		inventoryService.findProduct(productId);
		inventoryService.deleteProduct(productId);
		return Response.builder().status(AppUtil.SUCCESS).message("Product Deleted Successfully")
				.dateTime(LocalDateTime.now()).build();

	}

	public Response getProduct(String productId) {
		Product product = inventoryService.findProduct(productId);
		return new Response(AppUtil.SUCCESS, "Record Fetched Successfully", LocalDateTime.now(), product);

	}

}
