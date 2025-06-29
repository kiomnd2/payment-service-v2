package com.subprj.paymentv2.infrastructure.payment.toss;

import com.subprj.paymentv2.common.exception.PSPConfirmationException;
import com.subprj.paymentv2.domain.payment.PSPConfirmationStatus;
import com.subprj.paymentv2.domain.payment.PaymentEvent;
import com.subprj.paymentv2.domain.payment.PaymentExecutionResult;
import com.subprj.paymentv2.domain.payment.PaymentExecutor;
import com.subprj.paymentv2.domain.payment.confirm.PaymentConfirmCommand;
import com.subprj.paymentv2.infrastructure.payment.toss.response.TossPaymentConfirmationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RequiredArgsConstructor
@Component
public class PaymentExecutorImpl implements PaymentExecutor {
    private final WebClient tossPaymentWebClient;
    private String url = "/v1/payments/confirm";

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public PaymentExecutionResult execute(PaymentConfirmCommand command) {
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
        TossPaymentConfirmationResponse response = tossPaymentWebClient.post()
                .uri(url)
                .header("Idempotency-Key", command.getOrderId()) // 멱등성 키
                .bodyValue(bodyValue)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is5xxServerError() || httpStatusCode.is4xxClientError(),
                        clientResponse -> clientResponse.bodyToMono(TossPaymentConfirmationResponse.TossFailureResponse.class)
                                .flatMap(tossFailureResponse -> {
                                    TossPaymentError error = TossPaymentError.get(tossFailureResponse.getCode());
                                    return Mono.error(PSPConfirmationException.builder()
                                                    .errorCode(error.name())
                                                    .errorMessage(error.getDescription())
                                                    .isSuccess(error.isSuccess())
                                                    .isFailure(error.isFailure())
                                                    .isUnknown(error.isUnknown())
                                                    .isRetryableError(error.isRetryableError())
                                            .build());
                                }))
                .bodyToMono(TossPaymentConfirmationResponse.class)
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)).jitter(0.1)
                        .filter(throwable -> throwable instanceof PSPConfirmationException
                                && ((PSPConfirmationException) throwable).getIsRetryableError())
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure())
                ).block();
        return PaymentExecutionResult.builder()
                .paymentKey(response.getPaymentKey())
                .orderId(response.getOrderId())
                .paymentExtraDetails(PaymentExecutionResult.PaymentExtraDetails.builder()
                        .paymentType(PaymentEvent.PaymentType.get(response.getType()))
                        .paymentMethod(PaymentEvent.PaymentMethod.get(response.getMethod()))
                        .approvedAt(LocalDateTime.parse(response.getApprovedAt(), DateTimeFormatter.ISO_DATE_TIME))
                        .pspRawData(response.toString())
                        .orderName(response.getOrderName())
                        .pspConfirmationStatus(PSPConfirmationStatus.get(response.getStatus()))
                        .totalAmount((response.getTotalAmount()))
                        .build())
                .isSuccess(true)
                .isFailure(false)
                .isUnknown(false)
                .isRetryable(false)
                .build();
    }
}
