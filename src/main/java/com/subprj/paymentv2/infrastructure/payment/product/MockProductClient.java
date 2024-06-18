package com.subprj.paymentv2.infrastructure.payment.product;

import com.subprj.paymentv2.domain.payment.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MockProductClient implements ProductClient {
    @Override
    public List<Product> getProducts(Long cartId, List<Long> productIds) {
        return productIds.stream().map(
                v -> Product.builder()
                        .id(v)
                        .sellerId(2L)
                        .amount(BigDecimal.valueOf(10000))
                        .quantity(2)
                        .name("test product")
                        .build()
        ).toList();
    }
}
