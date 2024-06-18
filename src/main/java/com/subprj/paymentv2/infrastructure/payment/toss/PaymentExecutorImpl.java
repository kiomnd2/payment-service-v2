package com.subprj.paymentv2.infrastructure.payment.toss;

import com.subprj.paymentv2.domain.payment.PaymentCommand;
import com.subprj.paymentv2.domain.payment.PaymentExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Component
public class PaymentExecutorImpl implements PaymentExecutor {
    private final WebClient tossPaymentWebClient;
    private final String url = "/v1/payments/confirm";

    @Override
    public String execute(PaymentCommand command) {
        String bodyValue = String.format("""
                {
                    "paymentKey": "%s",
                    "orderId": "%s",
                    "amount": "%s"
                }
                """, command.getPaymentKey(),
                command.getOrderId(),
                command.getAmount())
                .trim();
        return tossPaymentWebClient.post()
                .uri(url)
                .bodyValue(bodyValue)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
