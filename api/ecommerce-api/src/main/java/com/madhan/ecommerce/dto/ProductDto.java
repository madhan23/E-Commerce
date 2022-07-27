package com.madhan.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

import com.madhan.ecommerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

	private String id;
	private String title;
	private List<String> images;
	private BigDecimal price;
	private Boolean instock;

}
