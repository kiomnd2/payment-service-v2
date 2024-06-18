package com.subprj.paymentv2.interfaces.payment.api;

import com.subprj.paymentv2.common.response.CommonResponse;
import com.subprj.paymentv2.domain.payment.PaymentCommand;
import com.subprj.paymentv2.domain.payment.PaymentExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/toss")
@RestController
public class TossPaymentsController {
    private final PaymentExecutor tossPaymentExecutor;

    @PostMapping("/confirm")
    public ResponseEntity<CommonResponse<String>> confirm(
            @RequestBody PaymentsDto.TossPaymentConfirmRequest request) {
        PaymentCommand command = PaymentCommand.builder()
                .paymentKey(request.getPaymentKey())
                .orderId(request.getOrderId())
                .amount(Long.valueOf(request.getAmount()))
                .build();
        String result = tossPaymentExecutor.execute(command);
        return ResponseEntity.ok().body(CommonResponse.with(HttpStatus.OK, "처리되었습니다", result));
    }
}
