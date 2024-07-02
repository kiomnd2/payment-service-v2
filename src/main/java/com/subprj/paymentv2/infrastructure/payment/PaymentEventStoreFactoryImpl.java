package com.subprj.paymentv2.infrastructure.payment;

import com.subprj.paymentv2.domain.payment.PaymentEvent;
import com.subprj.paymentv2.domain.payment.PaymentEventReader;
import com.subprj.paymentv2.domain.payment.PaymentEventStoreFactory;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class PaymentEventStoreFactoryImpl implements PaymentEventStoreFactory {
    private final PaymentEventReader paymentEventReader;

    @Transactional
    @Override
    public PaymentEvent store(String orderId, String paymentKey) {
        PaymentEvent paymentEvent = paymentEventReader.read(orderId);
        paymentEvent.changePaymentKey(paymentKey);
        return paymentEvent;
    }
}
