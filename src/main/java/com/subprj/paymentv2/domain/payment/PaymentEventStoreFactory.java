package com.subprj.paymentv2.domain.payment;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;

public interface PaymentEventStoreFactory {
    void store(String orderId, String paymentKey);
}
