package com.subprj.paymentv2.infrastructure.payment;

import com.subprj.paymentv2.domain.payment.PaymentEvent;
import com.subprj.paymentv2.domain.payment.PaymentEventReader;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PaymentEventReaderImpl implements PaymentEventReader {
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentEvent read(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment Event를 찾을 수 없습니다."));
    }

    @Override
    public List<PaymentEvent> readPendingEvent() {

    }
}
