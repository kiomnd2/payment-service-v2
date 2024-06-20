package com.subprj.paymentv2.domain.payment.confirm;

public interface PaymentConfirmUseCase {
    PaymentConfirmationResult confirm(PaymentConfirmCommand paymentConfirmCommand);
}
