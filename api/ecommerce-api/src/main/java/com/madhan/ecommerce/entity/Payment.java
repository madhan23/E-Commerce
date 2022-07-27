package com.madhan.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	private String id;
	private String mode;
	private String expiry;
	private String cardNo;
	private String status;
	private String partner;

}
