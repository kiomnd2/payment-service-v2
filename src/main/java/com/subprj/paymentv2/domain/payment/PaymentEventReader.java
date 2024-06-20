package com.subprj.paymentv2.domain.payment;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;

import java.util.List;

public interface PaymentEventReader {
    PaymentEvent read(String orderId);
}
