package com.subprj.paymentv2.domain.payment.order;

import com.subprj.paymentv2.domain.payment.PaymentExecutionResult;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;


@NoArgsConstructor
@Getter
public class PaymentStatusUpdateCommand {
    private String paymentKey;
    private String orderId;
    private PaymentOrder.PaymentOrderStatus status;
    private PaymentExecutionResult.PaymentExtraDetails paymentExtraDetails;
    private PaymentExecutionResult.PaymentExecutionFailure failure;


    @Builder
    public PaymentStatusUpdateCommand(String paymentKey, String orderId, PaymentOrder.PaymentOrderStatus status,
                                      PaymentExecutionResult.PaymentExtraDetails paymentExtraDetails, PaymentExecutionResult.PaymentExecutionFailure failure) {

        if (status != PaymentOrder.PaymentOrderStatus.SUCCESS && status != PaymentOrder.PaymentOrderStatus.FAILURE && status != PaymentOrder.PaymentOrderStatus.UNKNOWN) {
            throw new IllegalArgumentException(String.format("결제 상태 (status: %s) 는 올바르지 않은 결제 상태입니다", status.name()));
        }
        if (status == PaymentOrder.PaymentOrderStatus.SUCCESS && Objects.isNull(paymentExtraDetails)) {
            throw new IllegalArgumentException("PaymentStatus 값이 SUCCESS 라면 PaymentExtraDetails 는 null 이 되면 안됩니다.");
        } else if (status == PaymentOrder.PaymentOrderStatus.FAILURE && Objects.isNull(failure)) {
            throw new IllegalArgumentException("PaymentStatus 값이 FAILURE 라면 PaymentExecutionFailure 는 null 이 되면 안됩니다.");
        }

        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.status = status;
        this.paymentExtraDetails = paymentExtraDetails;
        this.failure = failure;
    }

    public static  PaymentStatusUpdateCommand by(PaymentExecutionResult result) {
        return PaymentStatusUpdateCommand.builder()
                .paymentKey(result.getPaymentKey())
                .orderId(result.getOrderId())
                .status(result.paymentOrderStatus())
                .paymentExtraDetails(result.getPaymentExtraDetails())
                .failure(result.getFailure())
                .build();
    }
}
