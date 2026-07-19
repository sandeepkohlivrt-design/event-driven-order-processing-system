package com.sandeep.orders.event;

import java.time.Instant;
import java.util.UUID;

public record OrderEvent(
        UUID eventId,
        UUID orderId,
        String eventType,
        int retryCount,
        Instant createdAt
) {
    public static OrderEvent created(UUID orderId) {
        return new OrderEvent(UUID.randomUUID(), orderId, "ORDER_CREATED", 0, Instant.now());
    }
}
