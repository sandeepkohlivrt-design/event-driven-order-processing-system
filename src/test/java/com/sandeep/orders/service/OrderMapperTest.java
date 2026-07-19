package com.sandeep.orders.service;

import com.sandeep.orders.model.OrderEntity;
import com.sandeep.orders.model.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderMapperTest {
    @Test
    void mapsOrderEntityToResponse() {
        OrderEntity order = new OrderEntity(UUID.randomUUID(), "customer-101", new BigDecimal("149.99"));

        var response = OrderMapper.toResponse(order);

        assertThat(response.customerId()).isEqualTo("customer-101");
        assertThat(response.amount()).isEqualByComparingTo("149.99");
        assertThat(response.status()).isEqualTo(OrderStatus.CREATED);
    }
}
