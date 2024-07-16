package com.subprj.paymentv2.infrastructure.payment.order;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    List<PaymentOrder> findByOrderId(String orderId);

    @Query("select po from PaymentOrder po inner join po.paymentEvent pe " +
            "where po.paymentOrderStatus ='UNKNOWN'" +
            "OR (po.paymentOrderStatus = 'EXECUTING' AND po.updatedAt = :updatedAt) " +
            "AND po.failedCount < po.threshold")
    List<PaymentOrder> findPendingPayment(LocalDateTime updatedAt, Pageable pageable);
}
