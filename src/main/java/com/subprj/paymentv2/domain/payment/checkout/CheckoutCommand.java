package com.subprj.paymentv2.domain.payment.checkout;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CheckoutCommand {
    private Long cartId;
    private List<Long> productIds ;
    private Long buyerId;
    private String idempotencyKey; // 물건에 대한 요청을 구분

}

