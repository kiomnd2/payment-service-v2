package com.subprj.paymentv2.domain.payment.order;

import com.subprj.paymentv2.domain.payment.PaymentExecutionResult;
import com.subprj.paymentv2.domain.payment.confirm.PaymentConfirmCommand;

import java.util.List;

public interface PaymentOrderStoreFactory {
    void store(List<PaymentOrder> paymentOrderList, PaymentConfirmCommand command);
    void storeOrderStatus(List<PaymentOrder> paymentOrderList, PaymentExecutionResult result);
}
