package com.subprj.paymentv2.domain.payment.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@ToString
public class Product {
    private Long id;
    private BigDecimal amount;
    private Integer quantity;
    private String name;
    private Long sellerId;
}
