package com.subprj.paymentv2.payment.infrastructure.toss;

import com.subprj.paymentv2.payment.domain.PaymentCommand;
import com.subprj.paymentv2.payment.domain.PaymentExecutor;
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
