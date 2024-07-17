package com.subprj.paymentv2.domain.payment;

import com.subprj.paymentv2.domain.payment.order.PaymentStatusUpdateCommand;

public interface PaymentEventStoreFactory {
    PaymentEvent store(String orderId, String paymentKey);

    PaymentEvent updateOrderStatus(PaymentStatusUpdateCommand command);
}
