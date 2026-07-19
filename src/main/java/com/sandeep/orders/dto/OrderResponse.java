package com.sandeep.orders.dto;

import com.sandeep.orders.model.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String customerId,
        BigDecimal amount,
        OrderStatus status,
        String failureReason,
        Instant createdAt,
        Instant updatedAt
) {}
