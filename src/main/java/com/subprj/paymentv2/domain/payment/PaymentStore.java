package com.subprj.paymentv2.domain.payment;

import com.subprj.paymentv2.domain.payment.PaymentEvent;

public interface PaymentStore {
    void store(PaymentEvent paymentEvent);
}
