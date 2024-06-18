package com.subprj.paymentv2.domain.payment;

public interface PaymentExecutor {

    String execute(PaymentCommand command);
}
