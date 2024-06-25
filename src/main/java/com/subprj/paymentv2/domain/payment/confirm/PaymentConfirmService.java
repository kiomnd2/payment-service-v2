package com.subprj.paymentv2.domain.payment.confirm;

import com.subprj.paymentv2.domain.payment.PaymentEventStoreFactory;
import com.subprj.paymentv2.domain.payment.PaymentExecutionResult;
import com.subprj.paymentv2.domain.payment.PaymentExecutor;
import com.subprj.paymentv2.domain.payment.PaymentValidator;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderStoreFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        if (paymentValidator.isValid(command.getOrderId(), command.getAmount())) {
            List<PaymentOrder> paymentOrders = paymentReader.readPaymentOrder(command.getOrderId());
            paymentOrderStoreFactory.store(paymentOrders, command);
            paymentEventStoreFactory.store(command.getOrderId(), command.getPaymentKey());
            PaymentExecutionResult result = paymentExecutor.execute(command);
            paymentOrderStoreFactory.storeOrderStatus(paymentOrders, result);
        }

        return null;
    }
}
