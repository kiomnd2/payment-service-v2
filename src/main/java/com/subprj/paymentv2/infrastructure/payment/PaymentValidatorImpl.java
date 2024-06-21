package com.subprj.paymentv2.infrastructure.payment;

import com.subprj.paymentv2.common.exception.PaymentValidationException;
import com.subprj.paymentv2.domain.payment.PaymentValidator;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PaymentValidatorImpl implements PaymentValidator {
    private final PaymentOrderReader paymentOrderReader;

    @Override
    public boolean isValid(String orderId, Long amount) {
        List<PaymentOrder> paymentOrders = paymentOrderReader.readPaymentOrder(orderId);
        long totalAmount = paymentOrders.stream().mapToLong(order -> order.getAmount().longValue()).sum();
        if (totalAmount == amount) {
            return true;
        }
        throw new PaymentValidationException(String.format("결제 (orderId: %s) 에서 금액 %d이 올바르지 않습니다.", orderId, amount));
    }
}
