package com.subprj.paymentv2.domain.payment.order;

import com.subprj.paymentv2.domain.payment.PaymentEvent;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
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

    @Getter
    @RequiredArgsConstructor
    public enum PaymentOrderStatus {
        NOT_STARTED(" 결제 시작 전"),
        EXECUTING("결제 중"),
        SUCCESS("결제 승인 완료"),
        FAILURE("결제 승인 실패"),
        UNKNOWN("결제 승인 할 수 없는 상태");

        private final String description;
    }

    @Builder
    public PaymentOrder(PaymentEvent paymentEvent, Long sellerId, Long productId,
                        String orderId, BigDecimal amount, PaymentOrderStatus paymentOrderStatus,
                        Boolean ledgerUpdated, Boolean walletUpdated, Long failedCount, Long threshold,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.paymentEvent = paymentEvent;
        this.sellerId = sellerId;
        this.productId = productId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentOrderStatus = paymentOrderStatus;
        this.ledgerUpdated = ledgerUpdated;
        this.walletUpdated = walletUpdated;
        this.failedCount = failedCount;
        this.threshold = threshold;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
