package com.subprj.paymentv2.infrastructure.payment.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subprj.paymentv2.domain.payment.PaymentEventMessage;
import com.subprj.paymentv2.domain.payment.order.PaymentOrder;
import com.subprj.paymentv2.domain.payment.order.PaymentStatusUpdateCommand;
import com.subprj.paymentv2.domain.payment.oubbox.OutBox;
import com.subprj.paymentv2.domain.payment.oubbox.PaymentOutboxStore;
import com.subprj.paymentv2.stream.utils.PartitionKeyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class PaymentOutboxStoreImpl implements PaymentOutboxStore{
    private final PaymentOutboxRepository paymentOutboxRepository;
    private final ObjectMapper mapper;

    @Transactional
    @Override
    public PaymentEventMessage store(PaymentStatusUpdateCommand command) throws IOException {
        if (command.getStatus() != PaymentOrder.PaymentOrderStatus.SUCCESS) {
            throw new IllegalArgumentException();
        }

        PaymentEventMessage message = PaymentEventMessage.builder()
                .type(PaymentEventMessage.PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS)
                .payload(Map.of("orderId", command.getOrderId()))
                .metadata(Map.of("partitionKey", PartitionKeyUtil.createPartitionKey(command.getOrderId().hashCode())))
                .build();

        paymentOutboxRepository.save(OutBox.builder()
                        .idempotencyKey(command.getOrderId())
                        .partitionKey(message.getMetadata().get("partitionKey") != null ?
                                (Long) message.getMetadata().get("partitionKey") : 0)
                        .type(PaymentEventMessage.PaymentEventMessageType.PAYMENT_CONFIRMATION_SUCCESS.name())
                        .payload(mapper.writeValueAsString(message.getPayload()))
                        .metadata(mapper.writeValueAsString(message.getMetadata()))
                .build());
        return message;
    }
}
