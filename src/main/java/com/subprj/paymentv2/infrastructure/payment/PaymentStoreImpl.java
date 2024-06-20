package com.subprj.paymentv2.infrastructure.payment;

import com.subprj.paymentv2.domain.payment.PaymentEvent;
import com.subprj.paymentv2.domain.payment.PaymentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentStoreImpl implements PaymentStore {
    private final PaymentRepository paymentRepository;

    @Override
    public void store(PaymentEvent paymentEvent) {
        paymentRepository.save(paymentEvent);
    }
}
