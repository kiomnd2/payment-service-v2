package com.subprj.paymentv2.domain.payment;

import com.subprj.paymentv2.domain.payment.confirm.PaymentConfirmCommand;

public interface PaymentExecutor {
    PaymentExecutionResult execute(PaymentConfirmCommand command);
}
