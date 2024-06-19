package com.subprj.paymentv2.domain.payment.checkout;

import com.subprj.paymentv2.domain.payment.PaymentEvent;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.product.Product;
import com.subprj.paymentv2.domain.payment.product.ProductReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .buyerId(command.getBuyerId())
                .orderId(command.getIdempotencyKey())
                .orderName(products.stream().map(Product::getName).collect(Collectors.joining(",")))
                .build();

        for (Product product : products) {
            paymentEvent.addPaymentOrder(PaymentOrder.builder()
                    .sellerId(product.getSellerId())
                    .orderId(command.getIdempotencyKey())
                    .productId(product.getId())
                    .amount(product.getAmount().multiply(BigDecimal.valueOf(product.getQuantity())))
                    .paymentOrderStatus(PaymentOrder.PaymentOrderStatus.NOT_STARTED)
                    .build());
        }
        return paymentEvent;
    }
}
