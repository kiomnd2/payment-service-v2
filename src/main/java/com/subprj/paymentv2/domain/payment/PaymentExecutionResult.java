package com.subprj.paymentv2.domain.payment;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderStoreFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.stream.Stream;


@NoArgsConstructor
@Getter
public class PaymentExecutionResult {
    private String paymentKey;
    private String orderId;
    private PaymentExtraDetails paymentExtraDetails;
    private PaymentExecutionFailure failure;
    private Boolean isSuccess;
    private Boolean isFailure;
    private Boolean isUnknown;
    private Boolean isRetryable;

    @Builder
    public PaymentExecutionResult(String paymentKey, String orderId, PaymentExtraDetails paymentExtraDetails,
                                  PaymentExecutionFailure failure,
                                  Boolean isSuccess, Boolean isFailure, Boolean isUnknown, Boolean isRetryable) {
        long count = Stream.of(isSuccess, isFailure, isUnknown).filter(v -> v).count();
        if (count != 1) {
            throw new IllegalArgumentException(String.format("결제 (orderId : %s) 는 올바르지 않는 상태입니다.", orderId));
        }

        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.paymentExtraDetails = paymentExtraDetails;
        this.failure = failure;
        this.isSuccess = isSuccess;
        this.isFailure = isFailure;
        this.isUnknown = isUnknown;
        this.isRetryable = isRetryable;
    }

    @Getter
    public static class PaymentExtraDetails {
        private final PaymentEvent.PaymentType paymentType;
        private final PaymentEvent.PaymentMethod paymentMethod;
        private final LocalDateTime approvedAt;
        private final String orderName;
        private final PSPConfirmationStatus pspConfirmationStatus;
        private final Long totalAmount;
        private final String pspRawData;

        @Builder
        public PaymentExtraDetails(PaymentEvent.PaymentType paymentType, PaymentEvent.PaymentMethod paymentMethod,
                                   LocalDateTime approvedAt, String orderName,
                                   PSPConfirmationStatus pspConfirmationStatus, Long totalAmount, String pspRawData) {
            this.paymentType = paymentType;
            this.paymentMethod = paymentMethod;
            this.approvedAt = approvedAt;
            this.orderName = orderName;
            this.pspConfirmationStatus = pspConfirmationStatus;
            this.totalAmount = totalAmount;
            this.pspRawData = pspRawData;
        }
    }

    public static class PaymentExecutionFailure {
        private String errorCode;
        private String message;
    }

    public PaymentOrder.PaymentOrderStatus paymentOrderStatus() {
        if (isSuccess) return PaymentOrder.PaymentOrderStatus.SUCCESS;
        else if (isFailure) return PaymentOrder.PaymentOrderStatus.FAILURE;
        else if (isUnknown) return PaymentOrder.PaymentOrderStatus.UNKNOWN;
        else {
            throw new IllegalArgumentException("결제 는 올바르지 않은 결제 상태입니다.");
        }
    }
}