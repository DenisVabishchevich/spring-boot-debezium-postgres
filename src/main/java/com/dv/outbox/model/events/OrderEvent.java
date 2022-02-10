package com.dv.outbox.model.events;

import jakarta.persistence.Entity;
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

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "orders_outbox")
public class OrderEvent {

    @Id
    @SequenceGenerator(name = "pk_orders_outbox_seq", sequenceName = "orders_outbox_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_orders_outbox_seq")
    private Long id;

    private String aggregateId;
    private String aggregateType;
    private String type;
    private String payload;
}
