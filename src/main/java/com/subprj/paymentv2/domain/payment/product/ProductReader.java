package com.subprj.paymentv2.domain.payment.product;

import java.util.List;

public interface ProductReader {
    List<Product> readProduct(Long cartId, List<Long> productIds);
}
