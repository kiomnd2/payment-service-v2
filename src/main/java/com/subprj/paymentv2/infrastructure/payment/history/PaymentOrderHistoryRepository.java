package com.subprj.paymentv2.infrastructure.payment.history;

import com.subprj.paymentv2.domain.payment.history.PaymentOrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderHistoryRepository extends JpaRepository<PaymentOrderHistory, Long> {
}
