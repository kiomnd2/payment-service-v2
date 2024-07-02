package com.subprj.paymentv2.domain.payment.confirm;

import com.subprj.paymentv2.domain.payment.*;
import com.subprj.paymentv2.domain.payment.checkout.CheckoutCommand;
import com.subprj.paymentv2.domain.payment.checkout.CheckoutResult;
import com.subprj.paymentv2.domain.payment.checkout.CheckoutUseCase;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderStoreFactory;
import com.subprj.paymentv2.domain.payment.test.PaymentDatabaseHelper;
import com.subprj.paymentv2.domain.payment.test.PaymentTestConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(PaymentTestConfiguration.class)
class PaymentConfirmServiceTest {
    @Autowired
    private CheckoutUseCase checkoutUseCase;

    @Autowired
    PaymentValidator paymentValidator;

    @Autowired
    PaymentDatabaseHelper paymentDatabaseHelper;

    @Autowired
    PaymentOrderStoreFactory paymentOrderStoreFactory;

    @Autowired
    PaymentOrderReader paymentOrderReader;

    @Autowired
    PaymentEventStoreFactory paymentEventStoreFactory;



    @Mock
    PaymentExecutor paymentExecutor;

    @Test
    void shouldBeMarkedAsSUCCESSIfPaymentConfirmationSuccessInPSP() {
        String orderId = UUID.randomUUID().toString();

        CheckoutCommand command = CheckoutCommand.builder()
                .cartId(1L)
                .buyerId(1L)
                .productIds(List.of(1L, 2L, 3L))
                .idempotencyKey(orderId)
                .build();

        CheckoutResult checkoutResult = checkoutUseCase.checkout(command);

        PaymentConfirmCommand paymentConfirmCommand = PaymentConfirmCommand.builder()
                .paymentKey(UUID.randomUUID().toString())
                .orderId(orderId)
                .amount(checkoutResult.getAmount())
                .build();

        PaymentConfirmService paymentConfirmService =
                new PaymentConfirmService(paymentOrderReader, paymentOrderStoreFactory, paymentEventStoreFactory, paymentValidator, paymentExecutor);

        Mockito.when(paymentExecutor.execute(ArgumentMatchers.any()))
                .thenReturn(PaymentExecutionResult.builder()
                        .paymentKey(paymentConfirmCommand.getPaymentKey())
                        .orderId(paymentConfirmCommand.getOrderId())
                        .paymentExtraDetails( PaymentExecutionResult.PaymentExtraDetails.builder()
                                .paymentType(PaymentEvent.PaymentType.NORMAL)
                                .paymentMethod(PaymentEvent.PaymentMethod.EASY_PAY)
                                .totalAmount(paymentConfirmCommand.getAmount())
                                .orderName("test Order")
                                .pspConfirmationStatus(PSPConfirmationStatus.DONE)
                                .approvedAt(LocalDateTime.now())
                                .pspRawData("{}")
                                .build())
                        .isSuccess(true)
                        .isFailure(false)
                        .isUnknown(false)
                        .isRetryable(false)
                        .build());

        PaymentConfirmationResult result = paymentConfirmService.confirm(paymentConfirmCommand);

        Assertions.assertThat(result.getStatus()).isEqualTo(PaymentOrder.PaymentOrderStatus.SUCCESS);

    }
}