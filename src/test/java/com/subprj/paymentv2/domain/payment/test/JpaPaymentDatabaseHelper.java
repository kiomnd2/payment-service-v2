package com.subprj.paymentv2.domain.payment.test;

import com.subprj.paymentv2.domain.payment.PaymentEvent;
import com.subprj.paymentv2.infrastructure.payment.PaymentRepository;
import com.subprj.paymentv2.infrastructure.payment.order.history.PaymentOrderHistoryRepository;
import com.subprj.paymentv2.infrastructure.payment.order.PaymentOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JpaPaymentDatabaseHelper implements PaymentDatabaseHelper {

    private final PaymentRepository paymentRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final PaymentOrderHistoryRepository paymentOrderHistoryRepository;

    @Autowired
    public JpaPaymentDatabaseHelper(PaymentRepository paymentRepository, PaymentOrderRepository paymentOrderRepository
    , PaymentOrderHistoryRepository paymentOrderHistoryRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentOrderRepository = paymentOrderRepository;
        this.paymentOrderHistoryRepository = paymentOrderHistoryRepository;
    }

    @Override
    public PaymentEvent getPayment(String orderId) {
        return paymentRepository.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("찾을 수 없는 Entity"));
    }

    @Transactional
    @Override
    public void clear() {
        paymentOrderHistoryRepository.deleteAll();
        paymentOrderRepository.deleteAll();
        paymentRepository.deleteAll();
    }
}
