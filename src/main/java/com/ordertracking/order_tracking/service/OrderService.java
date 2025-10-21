package com.ordertracking.order_tracking.service;

import com.ordertracking.order_tracking.model.*;
import com.ordertracking.order_tracking.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusLogRepository statusLogRepository;

    // ✅ Create new order
    public Order createOrder(Long userId, List<OrderItem> items) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        double total = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .totalAmount(total)
                .currentStatus("Placed")
                .items(new ArrayList<>())
                .build();

        // Save order first
        Order savedOrder = orderRepository.save(order);

        // Attach items to this order
        items.forEach(i -> i.setOrder(savedOrder));
        orderItemRepository.saveAll(items);

        // Add initial status log
        OrderStatusLog log = OrderStatusLog.builder()
                .order(savedOrder)
                .status("Placed")
                .updatedBy(user)
                .remarks("Order placed successfully")
                .build();
        statusLogRepository.save(log);

        return savedOrder;
    }

    // ✅ Update order status (e.g., Shipped, Delivered, Cancelled)
    public OrderStatusLog updateOrderStatus(Long orderId, String status, Long updatedById, String remarks) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        User updater = userRepository.findById(updatedById)
                .orElseThrow(() -> new EntityNotFoundException("Updater user not found"));

        order.setCurrentStatus(status);
        orderRepository.save(order);

        OrderStatusLog log = OrderStatusLog.builder()
                .order(order)
                .status(status)
                .updatedBy(updater)
                .remarks(remarks)
                .build();
        return statusLogRepository.save(log);
    }

    // ✅ Get all orders by user
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUser_UserId(userId);
    }

    // ✅ Get order by ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    // ✅ Get status logs for an order
    public List<OrderStatusLog> getStatusLogs(Long orderId) {
        return statusLogRepository.findByOrder_OrderIdOrderByTimestampAsc(orderId);
    }

    // ✅ Get all orders (for admin)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
