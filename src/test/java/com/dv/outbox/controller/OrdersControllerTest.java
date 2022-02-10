package com.dv.outbox.controller;

import com.dv.outbox.AbstractIntegrationTest;
import com.dv.outbox.controller.dto.OrderDto;
import com.dv.outbox.model.Order;
import com.dv.outbox.model.OrderStatus;
import com.dv.outbox.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
class OrdersControllerTest extends AbstractIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Container
    public static DockerComposeContainer<?> environment = new DockerComposeContainer<>(new File("docker-compose.yaml"));

    @Test
    void getOrderTest() throws Exception {
        Order order = orderService.saveOrder(OrderDto.builder()
            .status(OrderStatus.NEW)
            .build());

        mockMvc.perform(get("/api/v1/orders/{id}", order.getId()))
            .andDo(print())
            .andExpect(status().isOk());
    }

}