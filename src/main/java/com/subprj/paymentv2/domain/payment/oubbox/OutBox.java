package com.subprj.paymentv2.domain.payment.oubbox;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "outboxes")
public class OutBox {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    private State state;

    private String type;
    private Long partitionKey;
    private String payload;
    private String metadata;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum State{
        INIT, FAILURE, SUCCESS
    }

    @Builder
    public OutBox(String idempotencyKey, State state, String type, Long partitionKey, String payload, String metadata) {
        this.idempotencyKey = idempotencyKey;
        this.state = state;
        this.type = type;
        this.partitionKey = partitionKey;
        this.payload = payload;
        this.metadata = metadata;
    }
}
