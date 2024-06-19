package com.subprj.paymentv2.domain.payment.checkout;

import com.subprj.paymentv2.domain.payment.PaymentEvent;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.test.PaymentDatabaseHelper;
import com.subprj.paymentv2.domain.payment.test.PaymentTestConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import(PaymentTestConfiguration.class)
@SpringBootTest
class CheckoutServiceTest {

    @Autowired
    private CheckoutUseCase checkoutUseCase;

    @Autowired
    private PaymentDatabaseHelper paymentDatabaseHelper;

    @BeforeEach
    void setup() {
        paymentDatabaseHelper.clear();
    }

    @Test
    void savePaymentAndPaymentOrder_success() {
        String orderId = UUID.randomUUID().toString();
        CheckoutCommand command = CheckoutCommand.builder()
                .cartId(1L)
                .buyerId(1L)
                .productIds(List.of(1L, 2L, 3L))
                .idempotencyKey(orderId)
                .build();

        CheckoutResult checkout = checkoutUseCase.checkout(command);

        assertThat(checkout.getOrderId()).isEqualTo(orderId);
        assertThat(checkout.getAmount()).isEqualTo(60000L);

        PaymentEvent paymentEvent = paymentDatabaseHelper.getPayment(orderId);

        assertThat(paymentEvent.getOrderId()).isEqualTo(orderId);
        assertThat(paymentEvent.getPaymentOrders().size()).isEqualTo(command.getProductIds().size());
        assertThat(paymentEvent.getIsPaymentDone()).isFalse();
        assertThat(paymentEvent.getPaymentOrders().stream().allMatch(PaymentOrder::getLedgerUpdated)).isFalse();
        assertThat(paymentEvent.getPaymentOrders().stream().allMatch(PaymentOrder::getWalletUpdated)).isFalse();
    }

    @Test
    void failToSavePaymentEventAndPaymentOrderWhenTryTwice_fail() {
        String orderId = UUID.randomUUID().toString();
        CheckoutCommand command = CheckoutCommand.builder()
                .cartId(1L)
                .buyerId(1L)
                .productIds(List.of(1L, 2L, 3L))
                .idempotencyKey(orderId)
                .build();

        checkoutUseCase.checkout(command);

        org.junit.jupiter.api.Assertions.assertThrows(DataIntegrityViolationException.class, () ->{
            checkoutUseCase.checkout(command);
        });
    }
}