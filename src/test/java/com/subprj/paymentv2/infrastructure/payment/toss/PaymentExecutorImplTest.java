package com.subprj.paymentv2.infrastructure.payment.toss;

import com.subprj.paymentv2.common.exception.PSPConfirmationException;
import com.subprj.paymentv2.domain.payment.PaymentExecutor;
import com.subprj.paymentv2.domain.payment.confirm.PaymentConfirmCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Import(PSPTestWebClientConfiguration.class)
@SpringBootTest
class PaymentExecutorImplTest {

    @Autowired
    private PSPTestWebClientConfiguration pspTestWebClientConfiguration;

    @Test
    void should_handle_correctly_various_TossPaymentError_senarios() {
        generatorErrorScenarios().forEach(errorScenario -> {
            System.out.println(errorScenario.errorCode);
            PaymentConfirmCommand command = PaymentConfirmCommand.builder()
                    .paymentKey(UUID.randomUUID().toString())
                    .orderId(UUID.randomUUID().toString())
                    .amount(10000L)
                    .build();

            PaymentExecutorImpl paymentExecutor = new PaymentExecutorImpl(pspTestWebClientConfiguration
                    .createTestTossWebClient(Pair.of("TossPayments-Test-Code", errorScenario.errorCode)));
            paymentExecutor.setUrl("/v1/payments/key-in");

            try{
                paymentExecutor.execute(command);
            } catch (PSPConfirmationException e) {
                Assertions.assertThat(e.getIsSuccess()).isEqualTo(errorScenario.isSuccess);
                Assertions.assertThat(e.getIsFailure()).isEqualTo(errorScenario.isFailure);
                Assertions.assertThat(e.getIsUnknown()).isEqualTo(errorScenario.isUnknown);

            }
        });
    }

    private List<ErrorScenario> generatorErrorScenarios() {
        return Arrays.stream(TossPaymentError.values()).map( tossPaymentError ->
                new ErrorScenario(tossPaymentError.name(), tossPaymentError.isFailure()
                , tossPaymentError.isUnknown(), tossPaymentError.isSuccess()))
                .collect(Collectors.toList());

    }


    public class ErrorScenario {
        String errorCode;
        Boolean isFailure;
        Boolean isUnknown;
        Boolean isSuccess;

        public ErrorScenario(String errorCode, Boolean isFailure, Boolean isUnknown, Boolean isSuccess) {
            this.errorCode = errorCode;
            this.isFailure = isFailure;
            this.isUnknown = isUnknown;
            this.isSuccess = isSuccess;
        }
    }
}