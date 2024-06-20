package com.subprj.paymentv2.domain.payment.order.history;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;

public interface PaymentOrderHistoryStoreFactory {
    void store(PaymentOrder paymentOrder, PaymentOrder.PaymentOrderStatus status, String reason);
}
