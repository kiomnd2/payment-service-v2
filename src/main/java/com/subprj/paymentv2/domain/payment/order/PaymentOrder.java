package com.subprj.paymentv2.domain.payment.order;

import com.subprj.paymentv2.common.exception.PaymentAlreadyProcessedException;
import com.subprj.paymentv2.domain.payment.PaymentEvent;
import com.subprj.paymentv2.domain.payment.PaymentExecutionResult;
import com.subprj.paymentv2.domain.payment.order.history.PaymentOrderHistory;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payment_orders")
public class PaymentOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentEvent paymentEvent;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "order_id", unique = true)
    private String orderId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_order_status")
    private PaymentOrderStatus paymentOrderStatus;

    @Column(name = "ledger_updated")
    private Boolean ledgerUpdated;

    @Column(name = "wallet_updated")
    private Boolean walletUpdated;

    @Column(name = "failed_count", nullable = false)
    private Long failedCount;

    @Column(name = "threshold", nullable = false)
    private Long threshold;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "paymentOrder", cascade = CascadeType.PERSIST)
    private List<PaymentOrderHistory> paymentOrderHistories;

    public void setPaymentEvent(PaymentEvent paymentEvent) {
        this.paymentEvent = paymentEvent;
    }

    public void addHistory(PaymentOrderHistory history) {
        this.paymentOrderHistories.add(history);
        history.setPaymentOrder(this);
    }


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
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.paymentEvent = paymentEvent;
        this.sellerId = sellerId;
        this.productId = productId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentOrderStatus = paymentOrderStatus;
        this.ledgerUpdated = false;
        this.walletUpdated = false;
        this.failedCount = 0L;
        this.threshold = 5L;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void changeStateExecuting() {
        // payment 상태가 NOT_STARTED, UNKNOWN, EXECUTING 중 어떤것도 아닐땐 제한
        if (this.paymentOrderStatus == PaymentOrderStatus.SUCCESS) {
            throw new PaymentAlreadyProcessedException(PaymentOrderStatus.SUCCESS, "이미 처리 성공한 결제 입니다.");
        } else if (this.paymentOrderStatus == PaymentOrderStatus.FAILURE) {
            throw new PaymentAlreadyProcessedException(PaymentOrderStatus.FAILURE, "이미 처리 실패한 결제 입니다.");
        }
        this.paymentOrderStatus = PaymentOrderStatus.EXECUTING;
    }


    public void changeStateByResult(PaymentExecutionResult result) {
        if (!(result.getIsFailure() || result.getIsSuccess() || result.getIsUnknown())) {
            throw new IllegalArgumentException(String.format("결제 상태 (status : %s) 는 올바르지 않은 상태입니다.",
                    result.paymentOrderStatus().name()));
        }
        this.paymentOrderStatus = result.paymentOrderStatus();
    }
}
