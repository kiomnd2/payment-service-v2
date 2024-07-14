package com.subprj.paymentv2.infrastructure.payment;

import com.subprj.paymentv2.domain.payment.PaymentEvent;
import com.subprj.paymentv2.domain.payment.PaymentEventReader;
import com.subprj.paymentv2.domain.payment.PaymentEventStoreFactory;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import com.subprj.paymentv2.domain.payment.order.PaymentStatusUpdateCommand;
import com.subprj.paymentv2.domain.payment.order.history.PaymentOrderHistory;
import com.subprj.paymentv2.domain.payment.order.history.PaymentOrderHistoryStore;
import com.subprj.paymentv2.domain.payment.order.history.PaymentOrderHistoryStoreFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.jaxb.mapping.marshall.ConstraintModeMarshalling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PaymentEventStoreFactoryImpl implements PaymentEventStoreFactory {
    private final PaymentEventReader paymentEventReader;
    private final PaymentOrderReader paymentOrderReader;
    private final PaymentOrderHistoryStoreFactory paymentOrderHistoryStoreFactory;

    @Transactional
    @Override
    public PaymentEvent store(String orderId, String paymentKey) {
        PaymentEvent paymentEvent = paymentEventReader.read(orderId);
        paymentEvent.changePaymentKey(paymentKey);
        return paymentEvent;
    }

    @Override
    public void updateOrderStatus(PaymentEvent event, PaymentStatusUpdateCommand command) {
        List<PaymentOrder> paymentOrders = paymentOrderReader.readPaymentOrder(event.getOrderId());
        for (PaymentOrder paymentOrder : paymentOrders) {
            switch (command.getStatus()) {
                case SUCCESS -> {
                    paymentOrderHistoryStoreFactory.store(paymentOrder, command.getStatus(), "PAYMENT_CONFIRMATION_DONE");
                    paymentOrder.changeStateByResult(command);
                    event.updateExtraDetail(command);
                }
                case FAILURE -> {
                    paymentOrderHistoryStoreFactory.store(paymentOrder, command.getStatus(), command.getFailure().toString());
                    paymentOrder.changeStateByResult(command);
                }
                case UNKNOWN -> {
                    paymentOrderHistoryStoreFactory.store(paymentOrder, command.getStatus(), "UNKNOWN");
                    paymentOrder.changeStateByResult(command);
                }
                default -> {
                    throw new IllegalArgumentException(String.format("결제 상태 (status: %s 는 올바르지 않은 결제 상태입니다", command.getStatus().name()));
                }
            }
        }
    }
}
