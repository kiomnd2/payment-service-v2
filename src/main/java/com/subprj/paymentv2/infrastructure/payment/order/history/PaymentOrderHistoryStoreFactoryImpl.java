package com.subprj.paymentv2.infrastructure.payment.order.history;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.history.PaymentOrderHistory;
import com.subprj.paymentv2.domain.payment.order.history.PaymentOrderHistoryStoreFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentOrderHistoryStoreFactoryImpl implements PaymentOrderHistoryStoreFactory {

    @Override
    public void store(PaymentOrder paymentOrder, PaymentOrder.PaymentOrderStatus status, String reason) {
        PaymentOrderHistory history = PaymentOrderHistory.builder()
                .previousStatus(paymentOrder.getPaymentOrderStatus())
                .new_status(status)
                .reason(reason)
                .build();
        paymentOrder.addHistory(history);
    }
}
