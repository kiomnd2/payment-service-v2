package com.subprj.paymentv2.domain.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@Builder
public class PaymentEventMessage {
    private PaymentEventMessageType type;
    private Map<String, Object> payload;
    private Map<String, Object> metadata;

    @RequiredArgsConstructor
    @Getter
    public enum PaymentEventMessageType {
        PAYMENT_CONFIRMATION_SUCCESS("결제승인 완료 이벤트");

        private final String description;
    }
}
