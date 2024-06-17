package com.subprj.paymentv2.payment.domain.order;

import com.subprj.paymentv2.payment.domain.PaymentEvent;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "payment_orders")
public class PaymentOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(unique = true, name = "payment_event_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentEvent paymentEvent;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_order_status", nullable = false, columnDefinition = "")
    private PaymentOrderStatus paymentOrderStatus = PaymentOrderStatus.NOT_STARTED;

    @Column(name = "ledger_updated", nullable = false)
    private Boolean ledgerUpdated = false;

    @Column(name = "wallet_updated", nullable = false)
    private Boolean walletUpdated = false;

    @Column(name = "failed_count", nullable = false)
    private Long failedCount = 0L;

    @Column(name = "threshold", nullable = false)
    private Long threshold = 5L;
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum PaymentOrderStatus {
        NOT_STARTED, EXECUTING, SUCCESS, FAILURE, UNKNOWN
    }
}
