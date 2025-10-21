package com.ordertracking.order_tracking.controller;

import com.ordertracking.order_tracking.model.*;
import com.ordertracking.order_tracking.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ✅ Create a new order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        List<Map<String, Object>> itemsData = (List<Map<String, Object>>) request.get("items");

        List<OrderItem> items = itemsData.stream()
                .map(i -> OrderItem.builder()
                        .productName(i.get("productName").toString())
                        .quantity(Integer.parseInt(i.get("quantity").toString()))
                        .price(Double.parseDouble(i.get("price").toString()))
                        .build())
                .toList();

        Order order = orderService.createOrder(userId, items);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    // ✅ Get all orders by a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    // ✅ Get order details
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(orderId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Get order status history
    @GetMapping("/{orderId}/status")
    public ResponseEntity<List<OrderStatusLog>> getStatusLogs(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getStatusLogs(orderId));
    }

    // ✅ Update order status
    @PostMapping("/{orderId}/status")
    public ResponseEntity<OrderStatusLog> updateStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, Object> body) {

        String status = body.get("status").toString();
        Long updatedBy = Long.valueOf(body.get("updatedBy").toString());
        String remarks = body.get("remarks").toString();

        OrderStatusLog log = orderService.updateOrderStatus(orderId, status, updatedBy, remarks);
        return ResponseEntity.ok(log);
    }

    // ✅ Admin: get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
