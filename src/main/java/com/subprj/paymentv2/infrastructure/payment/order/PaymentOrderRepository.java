package com.subprj.paymentv2.infrastructure.payment.order;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
}
