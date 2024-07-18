package com.subprj.paymentv2.domain.payment.recovery;

import com.subprj.paymentv2.domain.payment.*;
import com.subprj.paymentv2.domain.payment.confirm.PaymentConfirmCommand;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import com.subprj.paymentv2.domain.payment.order.PaymentStatusUpdateCommand;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class PaymentRecoveryService implements PaymentRecoveryUseCase {
    private final PaymentOrderReader paymentOrderReader;
    private final PaymentValidator paymentValidator;
    private final PaymentExecutor paymentExecutor;
    private final PaymentEventStoreFactory paymentEventStoreFactory;
    private final Bulkhead bulkhead;

    @Scheduled(fixedDelay = 180, timeUnit = TimeUnit.SECONDS)
    @Override
    public void recovery() {
        paymentOrderReader.readPendingPayment()
                .stream().map(paymentOrder -> {
                    PaymentEvent event = paymentOrder.getPaymentEvent();
                    return PaymentConfirmCommand.builder()
                            .paymentKey(event.getPaymentKey())
                            .orderId(event.getOrderId())
                            .amount(event.totalAmount())
                            .build();
                }).forEach(command -> {
                    bulkhead.executeRunnable(() -> {
                        boolean valid = paymentValidator.isValid(command.getOrderId(), command.getAmount());
                        if (valid) {
                            PaymentExecutionResult result = paymentExecutor.execute(command);
                            paymentEventStoreFactory.updateOrderStatus(PaymentStatusUpdateCommand.by(result));
                        }
                    });
                });
    }
}
