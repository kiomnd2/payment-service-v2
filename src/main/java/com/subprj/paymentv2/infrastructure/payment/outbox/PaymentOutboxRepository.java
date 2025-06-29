package com.subprj.paymentv2.infrastructure.payment.outbox;

import com.subprj.paymentv2.domain.payment.oubbox.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOutboxRepository extends JpaRepository<OutBox, Long> {
}
