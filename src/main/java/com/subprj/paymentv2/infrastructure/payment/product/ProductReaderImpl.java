package com.subprj.paymentv2.infrastructure.payment.product;

import com.subprj.paymentv2.domain.payment.product.Product;
import com.subprj.paymentv2.domain.payment.product.ProductReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductReaderImpl implements ProductReader {
    private final ProductClient productClient;

    @Override
    public List<Product> readProduct(Long cartId, List<Long> productIds) {
        return productClient.getProducts(cartId, productIds);
    }
}
