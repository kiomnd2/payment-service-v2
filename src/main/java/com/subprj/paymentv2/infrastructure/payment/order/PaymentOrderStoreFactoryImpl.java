package com.subprj.paymentv2.infrastructure.payment.order;

import com.subprj.paymentv2.domain.payment.PaymentExecutionResult;
import com.subprj.paymentv2.domain.payment.confirm.PaymentConfirmCommand;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderStoreFactory;
import com.subprj.paymentv2.domain.payment.order.PaymentStatusUpdateCommand;
import com.subprj.paymentv2.domain.payment.order.history.PaymentOrderHistoryStoreFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PaymentOrderStoreFactoryImpl implements PaymentOrderStoreFactory {
    private final PaymentOrderHistoryStoreFactory paymentOrderHistoryStoreFactory;
    private final PaymentOrderReader paymentOrderReader;

    @Transactional
    @Override
    public void store(List<PaymentOrder> paymentOrderList, PaymentConfirmCommand command) {
        for (PaymentOrder paymentOrder : paymentOrderList) {
            paymentOrderHistoryStoreFactory.store(paymentOrder, PaymentOrder.PaymentOrderStatus.EXECUTING,
                    "PAYMENT_CONFIRMATION_START");
            paymentOrder.changeState();
        }
    }

}
