package com.subprj.paymentv2.domain.payment.confirm;

import com.subprj.paymentv2.domain.payment.PaymentEventStoreFactory;
import com.subprj.paymentv2.domain.payment.PaymentExecutor;
import com.subprj.paymentv2.domain.payment.PaymentValidator;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import com.subprj.paymentv2.domain.payment.order.history.PaymentOrderHistoryStoreFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentConfirmService implements PaymentConfirmUseCase {
    private final PaymentOrderReader paymentReader;
    private final PaymentOrderHistoryStoreFactory paymentOrderHistoryStoreFactory;
    private final PaymentEventStoreFactory paymentEventStoreFactory;
    private final PaymentValidator paymentValidator;
    private final PaymentExecutor paymentExecutor;

    @Transactional
    @Override
    public PaymentConfirmationResult confirm(PaymentConfirmCommand command) {
        List<PaymentOrder> paymentOrders = paymentReader.readPaymentOrder(command.getOrderId());

        if (paymentValidator.isValid(command.getOrderId(), command.getAmount())) {
            for (PaymentOrder paymentOrder : paymentOrders) {
                // 이력 저장
                paymentOrderHistoryStoreFactory.store(paymentOrder,
                        PaymentOrder.PaymentOrderStatus.EXECUTING,
                        "PAYMENT_CONFIRMATION_START");
                paymentOrder.changeState(PaymentOrder.PaymentOrderStatus.EXECUTING);
                paymentEventStoreFactory.store(command.getOrderId(), command.getPaymentKey());
            }
        }

        return null;
    }
}
