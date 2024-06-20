package com.subprj.paymentv2.common.exception;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import lombok.Getter;

@Getter
public class PaymentAlreadyProcessedException extends RuntimeException {
    private PaymentOrder.PaymentOrderStatus paymentOrderStatus;
    private String message;

    public PaymentAlreadyProcessedException(PaymentOrder.PaymentOrderStatus paymentOrderStatus, String message) {
        this.paymentOrderStatus = paymentOrderStatus;
        this.message = message;
    }
}
