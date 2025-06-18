package com.subprj.paymentv2.domain.payment.checkout;

public interface CheckoutUseCase {
    CheckoutResult checkout(CheckoutCommand command);

}
