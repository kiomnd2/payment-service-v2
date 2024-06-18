package com.subprj.paymentv2.domain.payment;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.atn.LexerIndexedCustomAction;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payment_events")
public class PaymentEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "is_payment_done", nullable = false)
    private Boolean isPaymentDone = false;

    @Column(name = "payment_key", nullable = false, unique = true)
    private String paymentKey;

    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @Enumerated(EnumType.STRING)
    private PaymentType type = PaymentType.NORMAL;

    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
    @Column(name = "psp_raw_data", nullable = false)
    private String pspRawData;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "paymentEvent")
    private List<PaymentOrder> paymentOrders;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @RequiredArgsConstructor
    public enum PaymentType {
        NORMAL("일반 결제");

        private final String description;
    }

    @RequiredArgsConstructor
    public enum PaymentMethod {
        EASY_PAY("간편결제");

        private final String description;
    }

    @Builder
    public PaymentEvent(Long buyerId, Boolean isPaymentDone, String paymentKey,
                        String orderId, PaymentType type, String orderName,
                        PaymentMethod method, String pspRawData, LocalDateTime approvedAt,
                        LocalDateTime createdAt, List<PaymentOrder> paymentOrders, LocalDateTime updatedAt) {

        this.buyerId = buyerId;
        this.isPaymentDone = isPaymentDone;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.type = type;
        this.orderName = orderName;
        this.method = method;
        this.pspRawData = pspRawData;
        this.approvedAt = approvedAt;
        this.createdAt = createdAt;
        this.paymentOrders = paymentOrders;
        this.updatedAt = updatedAt;
    }

    public Long totalAmount() {
        return this.paymentOrders.stream().mapToLong(order -> order.getAmount().longValue()).sum();
    }
}
