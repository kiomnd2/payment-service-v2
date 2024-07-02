package com.subprj.paymentv2.domain.payment.confirm;

import com.subprj.paymentv2.domain.payment.*;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderStoreFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentConfirmService implements PaymentConfirmUseCase {
    private final PaymentOrderReader paymentReader;
    private final PaymentOrderStoreFactory paymentOrderStoreFactory;
    private final PaymentEventStoreFactory paymentEventStoreFactory;
    private final PaymentValidator paymentValidator;
    private final PaymentExecutor paymentExecutor;

    @Transactional
    @Override
    public PaymentConfirmationResult confirm(PaymentConfirmCommand command) {
            List<PaymentOrder> paymentOrders = paymentReader.readPaymentOrder(command.getOrderId()).stream()
                .filter(paymentOrder -> paymentValidator.isValid(paymentOrder.getOrderId(), command.getAmount()))
                .toList();
            paymentOrderStoreFactory.store(paymentOrders, command);
            PaymentEvent event = paymentEventStoreFactory.store(command.getOrderId(), command.getPaymentKey());
            PaymentExecutionResult result = paymentExecutor.execute(command);
            paymentOrderStoreFactory.storeOrderStatus(paymentOrders, result);
            event.updateExtraDetail(result);
            return PaymentConfirmationResult.builder()
                    .status(result.paymentOrderStatus())
                    .failure(result.getFailure())
                    .build();

    }
}
