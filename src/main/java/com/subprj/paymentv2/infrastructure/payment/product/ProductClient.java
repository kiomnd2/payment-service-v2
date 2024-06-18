package com.subprj.paymentv2.infrastructure.payment.product;

import com.subprj.paymentv2.domain.payment.product.Product;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ProductClient {
    List<Product> getProducts(Long cartId, List<Long> productIds);
}
