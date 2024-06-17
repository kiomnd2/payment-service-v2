package com.subprj.paymentv2.payment.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentCommand {
    private String paymentKey;
    private String orderId;
    private Long amount;
}
