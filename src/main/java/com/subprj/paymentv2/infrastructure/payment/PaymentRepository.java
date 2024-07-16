package com.subprj.paymentv2.infrastructure.payment;

import com.subprj.paymentv2.domain.payment.PaymentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEvent, Long> {
    Optional<PaymentEvent> findByOrderId(String orderId);

}
