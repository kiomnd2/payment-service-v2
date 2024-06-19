package com.subprj.paymentv2.infrastructure.payment;

import com.subprj.paymentv2.domain.payment.PaymentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEvent, Long> {
    List<PaymentEvent> findByOrderId(String orderId);
}
