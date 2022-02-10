package com.dv.outbox.service;

import com.dv.outbox.model.events.OrderEvent;
import com.dv.outbox.repository.OrderEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderEventService {

    private final OrderEventRepository orderEventRepository;

    @Transactional
    public OrderEvent save(OrderEvent orderCreated) {
        return orderEventRepository.save(orderCreated);
    }
}
