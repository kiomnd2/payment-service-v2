package com.subprj.paymentv2.domain.payment;

public interface PaymentValidator {
    boolean isValid(String orderId, Long amount);
}
