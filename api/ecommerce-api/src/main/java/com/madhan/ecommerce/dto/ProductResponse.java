package com.madhan.ecommerce.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

	private List<ProductDto> products;
	private int page;
	private int limit;
	private Integer TotalRecords;
}
