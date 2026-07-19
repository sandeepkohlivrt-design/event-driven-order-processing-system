package com.sandeep.orders.service;

import com.sandeep.orders.dto.CreateOrderRequest;
import com.sandeep.orders.dto.OrderResponse;
import com.sandeep.orders.event.OrderEvent;
import com.sandeep.orders.exception.OrderNotFoundException;
import com.sandeep.orders.model.OrderEntity;
import com.sandeep.orders.repository.OrderRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderEventPublisher publisher;

    public OrderService(OrderRepository orderRepository, OrderEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        OrderEntity order = new OrderEntity(UUID.randomUUID(), request.customerId(), request.amount());
        orderRepository.save(order);
        publisher.publish(OrderEvent.created(order.getId()));
        return OrderMapper.toResponse(order);
    }

    @Cacheable(cacheNames = "orders", key = "#id")
    @Transactional(readOnly = true)
    public OrderResponse get(UUID id) {
        return orderRepository.findById(id)
                .map(OrderMapper::toResponse)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @CacheEvict(cacheNames = "orders", key = "#id")
    public void evict(UUID id) {
        // Method exists so consumers can invalidate cached order state after updates.
    }
}
