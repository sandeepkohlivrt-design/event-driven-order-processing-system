package com.sandeep.orders.service;

import com.sandeep.orders.event.OrderEvent;
import com.sandeep.orders.exception.OrderNotFoundException;
import com.sandeep.orders.model.OrderEntity;
import com.sandeep.orders.model.OrderStatus;
import com.sandeep.orders.model.ProcessedEvent;
import com.sandeep.orders.repository.OrderRepository;
import com.sandeep.orders.repository.ProcessedEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderEventConsumer {
    private final OrderRepository orderRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final OrderService orderService;

    public OrderEventConsumer(OrderRepository orderRepository,
                              ProcessedEventRepository processedEventRepository,
                              OrderService orderService) {
        this.orderRepository = orderRepository;
        this.processedEventRepository = processedEventRepository;
        this.orderService = orderService;
    }

    @KafkaListener(topics = "order-events", groupId = "order-processing-group")
    @Transactional
    public void process(OrderEvent event) {
        if (processedEventRepository.existsById(event.eventId())) {
            return;
        }

        OrderEntity order = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new OrderNotFoundException(event.orderId()));

        if (order.getAmount().signum() <= 0) {
            order.fail("Order amount must be greater than zero");
        } else {
            order.updateStatus(OrderStatus.VALIDATED);
            order.updateStatus(OrderStatus.PAYMENT_APPROVED);
            order.updateStatus(OrderStatus.FULFILLMENT_READY);
            order.updateStatus(OrderStatus.COMPLETED);
        }

        orderRepository.save(order);
        processedEventRepository.save(new ProcessedEvent(event.eventId()));
        orderService.evict(order.getId());
    }
}
