package com.subprj.paymentv2.domain.payment.checkout;

import com.subprj.paymentv2.domain.payment.PaymentEvent;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.product.Product;
import com.subprj.paymentv2.domain.payment.product.ProductReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CheckoutService implements CheckoutUseCase {
    private final ProductReader productReader;
    private final PaymentStore paymentStore;

    @Transactional
    @Override
    public CheckoutResult checkout(CheckoutCommand command) {
        List<Product> products = productReader.readProduct(command.getCartId(), command.getProductIds());
        PaymentEvent paymentEvent = createPaymentEvent(command, products);
        paymentStore.store(paymentEvent);

        return CheckoutResult.builder()
                .amount(paymentEvent.totalAmount())
                .orderName(paymentEvent.getOrderName())
                .orderId(paymentEvent.getOrderId())
                .build();
    }

    private PaymentEvent createPaymentEvent(CheckoutCommand command, List<Product> products) {
        return PaymentEvent.builder()
                .buyerId(command.getBuyerId())
                .orderId(command.getIdempotencyKey())
                .orderName(products.stream().map(Product::getName).collect(Collectors.joining(",")))
                .paymentOrders(products.stream()
                        .map(product -> PaymentOrder.builder()
                                .sellerId(product.getSellerId())
                                .orderId(command.getIdempotencyKey())
                                .productId(product.getId())
                                .amount(product.getAmount())
                                .paymentOrderStatus(PaymentOrder.PaymentOrderStatus.NOT_STARTED)
                                .build()).toList())
                .build();
    }
}
