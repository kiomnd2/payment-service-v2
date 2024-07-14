package com.subprj.paymentv2.domain.payment.confirm;

import com.subprj.paymentv2.common.exception.PSPConfirmationException;
import com.subprj.paymentv2.common.exception.PaymentAlreadyProcessedException;
import com.subprj.paymentv2.common.exception.PaymentValidationException;
import com.subprj.paymentv2.domain.payment.*;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderStoreFactory;
import com.subprj.paymentv2.domain.payment.order.PaymentStatusUpdateCommand;
import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentConfirmService implements PaymentConfirmUseCase {
    private final PaymentOrderReader paymentReader;
    private final PaymentOrderStoreFactory paymentOrderStoreFactory;
    private final PaymentEventStoreFactory paymentEventStoreFactory;
    private final PaymentValidator paymentValidator;
    private final PaymentExecutor paymentExecutor;

    @Transactional
    @Override
    public PaymentConfirmationResult confirm(PaymentConfirmCommand command) {
        List<PaymentOrder> paymentOrders = paymentReader.readPaymentOrder(command.getOrderId());

        try {
            List<PaymentOrder> filteredOrder = paymentOrders.stream()
                    .filter(paymentOrder -> paymentValidator.isValid(paymentOrder.getOrderId(), command.getAmount()))
                    .toList();
            paymentOrderStoreFactory.store(filteredOrder, command);
            PaymentEvent event = paymentEventStoreFactory.store(command.getOrderId(), command.getPaymentKey());
            PaymentExecutionResult result = paymentExecutor.execute(command);
            paymentEventStoreFactory.updateOrderStatus(event, PaymentStatusUpdateCommand.builder()
                            .paymentKey(result.getPaymentKey())
                            .orderId(result.getOrderId())
                            .status(result.paymentOrderStatus())
                            .paymentExtraDetails(result.getPaymentExtraDetails())
                            .failure(result.getFailure())
                    .build());
            return PaymentConfirmationResult.builder()
                    .status(result.paymentOrderStatus())
                    .failure(result.getFailure())
                    .build();
        } catch (PSPConfirmationException e) {
            PaymentOrder.PaymentOrderStatus paymentOrderStatus = e.paymentStatus();
            PaymentExecutionResult.PaymentExecutionFailure t =
                    PaymentExecutionResult.PaymentExecutionFailure.builder()
                    .errorCode(e.getErrorCode())
                    .message(e.getErrorMessage()).build();

            PaymentStatusUpdateCommand statusUpdateCommand = PaymentStatusUpdateCommand.builder()
                    .paymentKey(command.getPaymentKey())
                    .orderId(command.getOrderId())
                    .status(paymentOrderStatus)
                    .failure(t)
                    .build();

            throw e;
        } catch (PaymentValidationException ve) {

            throw ve;
        } catch (PaymentAlreadyProcessedException ape) {

            throw ape;
        } catch (TimeoutException te) {

            throw te;
        }

    }
}
