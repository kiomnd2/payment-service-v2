package com.subprj.paymentv2.infrastructure.payment.order;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    List<PaymentOrder> findByOrderId(String orderId);
}
