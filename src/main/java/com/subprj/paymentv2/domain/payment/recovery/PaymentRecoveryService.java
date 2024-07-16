package com.subprj.paymentv2.domain.payment.recovery;

import com.subprj.paymentv2.domain.payment.PaymentEventReader;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class PaymentRecoveryService implements PaymentRecoveryUseCase {
    private final PaymentEventReader paymentEventReader;

    @Scheduled(fixedDelay = 180, timeUnit = TimeUnit.SECONDS)
    @Override
    public void recovery() {

    }
}
