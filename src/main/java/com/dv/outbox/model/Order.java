package com.dv.outbox.model;

import com.dv.outbox.controller.dto.OrderEventType;
import com.dv.outbox.model.events.OrderEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "orders")
public class Order {

    private static ObjectMapper mapper = new ObjectMapper();

    @Id
    @SequenceGenerator(name = "pk_sequence", sequenceName = "orders_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @SneakyThrows
    public OrderEvent created(String aggregateType, String aggregateId) {
        return OrderEvent.builder()
            .aggregateType(aggregateType)
            .aggregateId(aggregateId)
            .type(OrderEventType.CREATE_ORDER.name())
            .payload(mapper.writeValueAsString(this))
            .build();
    }
}
