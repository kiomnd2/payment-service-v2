package com.subprj.paymentv2.payment.domain;

public interface PaymentExecutor {

    String execute(PaymentCommand command);
}
