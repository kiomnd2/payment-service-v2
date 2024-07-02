package com.subprj.paymentv2.domain.payment;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;

public interface PaymentEventStoreFactory {
    PaymentEvent store(String orderId, String paymentKey);
}
