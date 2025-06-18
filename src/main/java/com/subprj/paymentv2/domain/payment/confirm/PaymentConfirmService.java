package com.subprj.paymentv2.domain.payment.confirm;

import com.subprj.paymentv2.common.exception.PSPConfirmationException;
import com.subprj.paymentv2.common.exception.PaymentAlreadyProcessedException;
import com.subprj.paymentv2.common.exception.PaymentValidationException;
import com.subprj.paymentv2.domain.payment.*;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderReader;
import com.subprj.paymentv2.domain.payment.order.PaymentOrderStoreFactory;
import com.subprj.paymentv2.domain.payment.order.PaymentStatusUpdateCommand;
import lombok.RequiredArgsConstructor;
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
        PaymentEvent event = paymentEventStoreFactory.store(command.getOrderId(), command.getPaymentKey());
        try {
            paymentValidator.isValid(command.getOrderId(), command.getAmount());
            paymentOrderStoreFactory.store(paymentOrders, command); // excuting 상태로 변경
            PaymentExecutionResult result = paymentExecutor.execute(command);
            paymentEventStoreFactory.updateOrderStatus(PaymentStatusUpdateCommand.builder()
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
            return processPSPConfirmation(command, event, e);
        } catch (PaymentValidationException ve) {
            return processPaymentValidation(command, ve);
        } catch (PaymentAlreadyProcessedException ape) {
            return processPaymentAlreadyProcesse(ape);
        } catch (Exception e) {
            return processEtc(command, event, e);
        }
    }

    private PaymentConfirmationResult processEtc(PaymentConfirmCommand command, PaymentEvent event, Exception e) {
        PaymentExecutionResult.PaymentExecutionFailure f =
                PaymentExecutionResult.PaymentExecutionFailure.builder()
                        .errorCode(e.getClass().getSimpleName())
                        .message(e.getMessage()).build();

        PaymentStatusUpdateCommand statusUpdateCommand = PaymentStatusUpdateCommand.builder()
                .paymentKey(command.getPaymentKey())
                .orderId(command.getOrderId())
                .status(PaymentOrder.PaymentOrderStatus.UNKNOWN)
                .failure(f)
                .build();

        paymentEventStoreFactory.updateOrderStatus(statusUpdateCommand);
        return PaymentConfirmationResult.builder()
                .status(PaymentOrder.PaymentOrderStatus.UNKNOWN)
                .failure(f)
                .build();
    }

    private static PaymentConfirmationResult processPaymentAlreadyProcesse(PaymentAlreadyProcessedException ape) {
        return PaymentConfirmationResult.builder()
                .status(PaymentOrder.PaymentOrderStatus.FAILURE)
                .failure(PaymentExecutionResult.PaymentExecutionFailure.builder()
                        .errorCode(ape.getClass().getSimpleName())
                        .message(ape.getMessage()).build())
                .build();
    }

    private static PaymentConfirmationResult processPaymentValidation(PaymentConfirmCommand command, PaymentValidationException ve) {
        PaymentExecutionResult.PaymentExecutionFailure f = PaymentExecutionResult.PaymentExecutionFailure.builder()
                .errorCode(ve.getClass().getSimpleName())
                .message(ve.getMessage()).build();
        PaymentStatusUpdateCommand.builder()
                .paymentKey(command.getPaymentKey())
                .orderId(command.getOrderId())
                .status(PaymentOrder.PaymentOrderStatus.FAILURE)
                .failure(f)
                .build();

        return PaymentConfirmationResult.builder()
                .status(PaymentOrder.PaymentOrderStatus.FAILURE)
                .failure(f)
                .build();
    }

    private PaymentConfirmationResult processPSPConfirmation(PaymentConfirmCommand command, PaymentEvent event, PSPConfirmationException e) {
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

        paymentEventStoreFactory.updateOrderStatus(statusUpdateCommand);
        return PaymentConfirmationResult.builder()
                .status(paymentOrderStatus)
                .failure(t)
                .build();
    }
}
