package com.subprj.paymentv2.domain.payment.recovery;


import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import com.subprj.paymentv2.domain.payment.test.PaymentTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@SpringBootTest
@Import(PaymentTestConfiguration.class)
class PaymentRecoveryServiceTest {

    @Autowired
    PaymentRecoveryService paymentRecoveryService;

    @Test
    void test() {
        paymentRecoveryService.recovery();
    }
}
