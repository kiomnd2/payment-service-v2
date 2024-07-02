package com.subprj.paymentv2.domain.payment.confirm;

import com.subprj.paymentv2.domain.payment.PaymentExecutionResult;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentConfirmationResult {
    private PaymentOrder.PaymentOrderStatus status;
    private PaymentExecutionResult.PaymentExecutionFailure failure;

    @Builder
    public PaymentConfirmationResult(PaymentOrder.PaymentOrderStatus status,
                                     PaymentExecutionResult.PaymentExecutionFailure failure) {
        if (status == PaymentOrder.PaymentOrderStatus.FAILURE && failure == null) {
            throw new IllegalArgumentException("결제상태 FAILURE 일 때 PaymentExecutionFailure 는 null 이 될 수 없습니다");
        }
        this.status = status;
        this.failure = failure;
    }

    public String message() {
        switch (status) {
            case SUCCESS -> {
                return "결제 처리에 성공하였습니다";
            }
            case FAILURE -> {
                return "결제 처리에 실패하였습니다";
            }
            case UNKNOWN -> {
                return "결체 처리 중에 알 수 없는 에러가 발생하였습니다";
            }
            default -> {
                throw new IllegalArgumentException(String.format("현재 결제 상태 (status :%s) 는 올바르지 않은 상태입니다."
                        , status.name()));
            }
        }
    }
}
