package com.subprj.paymentv2.infrastructure.payment.order;

import com.subprj.paymentv2.domain.payment.PaymentExecutionResult;
import com.subprj.paymentv2.domain.payment.confirm.PaymentConfirmCommand;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderStoreFactory;
import com.subprj.paymentv2.domain.payment.order.history.PaymentOrderHistoryStore;
import com.subprj.paymentv2.domain.payment.order.history.PaymentOrderHistoryStoreFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PaymentOrderStoreFactoryImpl implements PaymentOrderStoreFactory {
    private final PaymentOrderHistoryStoreFactory paymentOrderHistoryStoreFactory;

    @Transactional
    @Override
    public void store(List<PaymentOrder> paymentOrderList, PaymentConfirmCommand command) {
        for (PaymentOrder paymentOrder : paymentOrderList) {
            paymentOrderHistoryStoreFactory.store(paymentOrder,
                    PaymentOrder.PaymentOrderStatus.EXECUTING,
                    "PAYMENT_CONFIRMATION_START");
            paymentOrder.changeStateExecuting();
        }
    }

    @Transactional
    @Override
    public void storeOrderStatus(List<PaymentOrder> paymentOrderList, PaymentExecutionResult result) {
        for (PaymentOrder paymentOrder : paymentOrderList) {
            paymentOrderHistoryStoreFactory.store(paymentOrder, PaymentOrder.PaymentOrderStatus.SUCCESS,
                    "PAYMENT_CONFIRMATION_DONE");
            paymentOrder.changeStateByResult(result);
        }
    }
}
