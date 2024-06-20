package com.subprj.paymentv2.infrastructure.payment.order;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PaymentOrderReaderImpl implements PaymentOrderReader {
    private final PaymentOrderRepository paymentOrderRepository;

    @Override
    public List<PaymentOrder> readPaymentOrder(String orderId) {
        paymentOrderRepository.findByOrderId(orderId);
        return null;
    }
}
