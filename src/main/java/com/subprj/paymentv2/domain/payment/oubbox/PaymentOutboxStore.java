package com.subprj.paymentv2.domain.payment.oubbox;

import com.subprj.paymentv2.domain.payment.PaymentEventMessage;
import com.subprj.paymentv2.domain.payment.order.PaymentStatusUpdateCommand;

import java.io.IOException;

public interface PaymentOutboxStore {
    PaymentEventMessage store(PaymentStatusUpdateCommand command) throws IOException;
}
