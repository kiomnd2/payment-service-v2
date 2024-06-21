package com.subprj.paymentv2.domain.payment;

import java.math.BigDecimal;

public interface PaymentValidator {
    boolean isValid(String orderId, Long amount);
}
