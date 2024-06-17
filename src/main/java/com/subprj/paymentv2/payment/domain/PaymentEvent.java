package com.subprj.paymentv2.payment.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    enum PaymentType {
        NORMAL
    }

    enum PaymentMethod {
        EASY_PAY
    }
}
