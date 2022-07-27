package com.madhan.ecommerce.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	@Id
	private String orderTrackingId;
	private String userId;
	private List<CartItem>products;
	private OrderStatus status;
	private BigDecimal totalAmount;
	private Integer totalQty;
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Address billingAddress;
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Payment payment;
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	
}
