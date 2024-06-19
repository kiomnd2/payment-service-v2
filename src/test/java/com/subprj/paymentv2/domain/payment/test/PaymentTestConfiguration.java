package com.subprj.paymentv2.domain.payment.test;

import com.subprj.paymentv2.infrastructure.payment.PaymentRepository;
import com.subprj.paymentv2.infrastructure.payment.history.PaymentOrderHistoryRepository;
import com.subprj.paymentv2.infrastructure.payment.order.PaymentOrderRepository;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class PaymentTestConfiguration {
    public PaymentDatabaseHelper paymentDatabaseHelper(PaymentRepository paymentRepository,
                                                       PaymentOrderRepository paymentOrderRepository,
                                                       PaymentOrderHistoryRepository paymentOrderHistoryRepository) {
        return new JpaPaymentDatabaseHelper(paymentRepository, paymentOrderRepository, paymentOrderHistoryRepository);
    }
}
