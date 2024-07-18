package com.subprj.paymentv2.domain.payment.recovery;


import com.subprj.paymentv2.domain.payment.*;
import com.subprj.paymentv2.domain.payment.checkout.CheckoutCommand;
import com.subprj.paymentv2.domain.payment.checkout.CheckoutResult;
import com.subprj.paymentv2.domain.payment.checkout.CheckoutUseCase;
import com.subprj.paymentv2.domain.payment.confirm.PaymentConfirmCommand;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import com.subprj.paymentv2.domain.payment.order.PaymentStatusUpdateCommand;
import com.subprj.paymentv2.domain.payment.test.PaymentTestConfiguration;
import io.github.resilience4j.bulkhead.Bulkhead;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@Import(PaymentTestConfiguration.class)
class PaymentRecoveryServiceTest {

    @Autowired
    PaymentRecoveryService paymentRecoveryService;

    @Autowired
    private PaymentOrderReader paymentOrderReader;

    @Autowired
    private PaymentValidator paymentValidator;


    @Autowired
    private PaymentEventStoreFactory paymentEventStoreFactory;

    @Autowired
    private CheckoutUseCase checkoutUseCase;

    @Autowired
    Bulkhead bulkhead;

    private final PaymentExecutor paymentExecutor = Mockito.mock(PaymentExecutor.class);
    @Transactional
    @Test
    void should_recovery_payments() throws Exception {
        PaymentConfirmCommand unknownPayment = createUnknownPayment();
        createPaymentExecutionResult(unknownPayment);

        Mockito.when(paymentExecutor.execute(any())).thenReturn(createPaymentExecutionResult(unknownPayment));

        PaymentRecoveryService service =
                new PaymentRecoveryService(paymentOrderReader, paymentValidator, paymentExecutor, paymentEventStoreFactory, bulkhead);

        service.recovery();

        Thread.sleep(10000);
    }

    private static PaymentExecutionResult createPaymentExecutionResult(PaymentConfirmCommand unknownPayment) {
        return PaymentExecutionResult.builder()
                .paymentKey(unknownPayment.getPaymentKey())
                .orderId(unknownPayment.getOrderId())
                .paymentExtraDetails(PaymentExecutionResult.PaymentExtraDetails.builder()
                        .paymentType(PaymentEvent.PaymentType.NORMAL)
                        .paymentMethod(PaymentEvent.PaymentMethod.EASY_PAY)
                        .totalAmount(unknownPayment.getAmount())
                        .orderName("test_order")
                        .pspConfirmationStatus(PSPConfirmationStatus.DONE)
                        .approvedAt(LocalDateTime.now())
                        .pspRawData("{}")
                        .build())
                .isSuccess(true)
                .isFailure(false)
                .isRetryable(false)
                .isUnknown(false)
                .build();
    }

    @Transactional
    @Test
    PaymentConfirmCommand createUnknownPayment() {
        var orderId = UUID.randomUUID().toString();
        var paymentKey = UUID.randomUUID().toString();

        CheckoutCommand command = CheckoutCommand.builder()
                .cartId(1L)
                .buyerId(1L)
                .productIds(List.of(1L, 2L))
                .idempotencyKey(orderId)
                .build();

        var checkout = checkoutUseCase.checkout(command);

        PaymentConfirmCommand paymentConfirmCommand = PaymentConfirmCommand.builder()
                .paymentKey(paymentKey)
                .orderId(orderId)
                .amount(checkout.getAmount())
                .build();

        var event = paymentEventStoreFactory.store(paymentConfirmCommand.getOrderId(), paymentConfirmCommand.getPaymentKey());

        var paymentstatusupdatecommand = PaymentStatusUpdateCommand.builder()
                .paymentKey(paymentConfirmCommand.getPaymentKey())
                .orderId(paymentConfirmCommand.getOrderId())
                .status(PaymentOrder.PaymentOrderStatus.UNKNOWN)
                .failure(PaymentExecutionResult.PaymentExecutionFailure.builder().errorCode("UNKNOWN").message("UNKNOWN").build())
                .build();

        paymentEventStoreFactory.updateOrderStatus(paymentstatusupdatecommand);


        return paymentConfirmCommand;
    }
}
