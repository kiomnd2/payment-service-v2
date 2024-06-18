package com.subprj.paymentv2.domain.payment.checkout;

import reactor.core.publisher.Mono;

public interface CheckoutUseCase {
    CheckoutResult checkout(CheckoutCommand command);

}
