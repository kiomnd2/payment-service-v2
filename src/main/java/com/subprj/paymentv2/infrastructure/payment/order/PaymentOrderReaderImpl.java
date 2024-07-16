package com.subprj.paymentv2.infrastructure.payment.order;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class PaymentOrderReaderImpl implements PaymentOrderReader {
    private final PaymentOrderRepository paymentOrderRepository;

    @Override
    public List<PaymentOrder> readPaymentOrder(String orderId) {
        return paymentOrderRepository.findByOrderId(orderId);
    }

    @Override
    public List<PaymentOrder> readPendingPayment() {
        return paymentOrderRepository.findPendingPayment(LocalDateTime.now().minusMinutes(3L), PageRequest.of(0,10 ));
    }
}
