package com.subprj.paymentv2.interfaces.payment.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class PaymentsDto {

    @Getter
    @Setter
    @ToString
    public static class TossPaymentConfirmRequest {
        private String paymentKey;
        private String orderId;
        private String amount;
    }

    @Getter
    @Builder
    @ToString
    public static class PaymentConfirmResult {

    }
}
