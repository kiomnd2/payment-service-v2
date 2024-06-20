package com.subprj.paymentv2.domain.payment.order.history;

public interface PaymentOrderHistoryStore {
    void store(PaymentOrderHistory paymentOrderHistory);
}
