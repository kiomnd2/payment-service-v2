package com.subprj.paymentv2.domain.payment.recovery.config;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class BulkHeadConfig {

    @Bean
    public BulkheadConfig bulkheadConfig() {
        return BulkheadConfig.custom()
                .maxConcurrentCalls(2)
                .maxWaitDuration(Duration.ofMillis(5000))
                .build();
    }

    @Bean
    public Bulkhead bulkhead() {
        return Bulkhead.of("paymentRecoveryBulkhead", bulkheadConfig());
    }

}
