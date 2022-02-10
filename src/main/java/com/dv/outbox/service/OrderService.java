package com.dv.outbox.service;

import com.dv.outbox.controller.dto.OrderDto;
import com.dv.outbox.model.Order;
import com.dv.outbox.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public OrderDto getOrder(Long id) {
        return orderRepository.findById(id)
            .map(order -> mapper.map(order, OrderDto.class))
            .orElseThrow();
    }

    @Transactional
    public Order saveOrder(OrderDto orderDto) {
        Order order = mapper.map(orderDto, Order.class);
        return orderRepository.save(order);
    }
}
