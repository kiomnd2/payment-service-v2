package com.subprj.paymentv2.domain.payment.history;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "payment_order_histories")
public class PaymentOrderHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "payment_order_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private PaymentOrder paymentOrder;

    @Column(name = "previous_status")
    @Enumerated(EnumType.STRING)
    private PaymentOrder.PaymentOrderStatus previousStatus;

    @Column(name = "new_status")
    @Enumerated(EnumType.STRING)
    private PaymentOrder.PaymentOrderStatus new_status;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "reason")
    private String reason;


}
