package com.dv.outbox.listener;

import com.dv.outbox.controller.dto.OrderDto;
import com.dv.outbox.controller.dto.OrderEventType;
import com.dv.outbox.model.InboxMessage;
import com.dv.outbox.model.Order;
import com.dv.outbox.model.events.OrderEvent;
import com.dv.outbox.service.DeduplicationService;
import com.dv.outbox.service.OrderEventService;
import com.dv.outbox.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaOrderListener {

    private final ObjectMapper mapper;
    private final DeduplicationService deduplicationService;
    private final OrderService orderService;
    private final OrderEventService orderEventService;

    @KafkaListener(topics = "orders-in")
    @Transactional
    public void onOrderEvent(@Payload String message,
                             @Headers Map<String, Object> headers) {
        log.info(String.format("#### -> Consumed message -> %s", message));
        try {
            String uniqueMessageId = new String((byte[]) headers.get("id"), StandardCharsets.UTF_8);
            String eventType = new String((byte[]) headers.get("type"), StandardCharsets.UTF_8);

            // deduplication
            if (!deduplicationService.exists(uniqueMessageId)) {
                deduplicationService.saveInboxMessage(InboxMessage.builder()
                    .id(uniqueMessageId)
                    .build());

                // processing
                if (eventType.equalsIgnoreCase(OrderEventType.CREATE_ORDER.name())) {
                    OrderDto orderDto = mapper.readValue(message, OrderDto.class);
                    Order order = orderService.saveOrder(orderDto);
                    OrderEvent orderCreated = order.created("aggregateId");
                    orderEventService.save(orderCreated);
                } else {
                    throw new RuntimeException("Event type not supported: " + eventType);
                }
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", message);
            throw new RuntimeException("Error processing message", e);
        }

    }

}
