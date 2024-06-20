package com.subprj.paymentv2.domain.payment.order;

import com.subprj.paymentv2.domain.payment.order.PaymentOrder;

import java.util.List;

public interface PaymentOrderReader {
    List<PaymentOrder> readPaymentOrder(String orderId);
}
