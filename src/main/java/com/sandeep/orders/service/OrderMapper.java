package com.sandeep.orders.service;

import com.sandeep.orders.dto.OrderResponse;
import com.sandeep.orders.model.OrderEntity;

public final class OrderMapper {
    private OrderMapper() {}

    public static OrderResponse toResponse(OrderEntity order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getAmount(),
                order.getStatus(),
                order.getFailureReason(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
