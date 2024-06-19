package com.subprj.paymentv2.domain.payment.test;

import com.subprj.paymentv2.domain.payment.PaymentEvent;

public interface PaymentDatabaseHelper {
    PaymentEvent getPayment(String orderId);

    void clear();

}
