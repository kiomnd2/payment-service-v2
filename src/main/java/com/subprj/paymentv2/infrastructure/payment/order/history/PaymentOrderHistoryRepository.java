package com.subprj.paymentv2.infrastructure.payment.order.history;

import com.subprj.paymentv2.domain.payment.order.history.PaymentOrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderHistoryRepository extends JpaRepository<PaymentOrderHistory, Long> {
}
