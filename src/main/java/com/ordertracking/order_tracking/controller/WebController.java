package com.ordertracking.order_tracking.controller;

import com.ordertracking.order_tracking.model.*;
import com.ordertracking.order_tracking.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final OrderService orderService;

    // 🏠 Home: list all orders
    @GetMapping("/")
    public String viewAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    // ➕ New Order Page
    @GetMapping("/orders/new")
    public String newOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "new-order";
    }

    // 📨 Handle Order Submission
    @PostMapping("/orders/save")
    public String saveOrder(
            @RequestParam Long userId,
            @RequestParam List<String> productName,
            @RequestParam List<Integer> quantity,
            @RequestParam List<Double> price) {

        List<OrderItem> items = new ArrayList<>();
        for (int i = 0; i < productName.size(); i++) {
            items.add(OrderItem.builder()
                    .productName(productName.get(i))
                    .quantity(quantity.get(i))
                    .price(price.get(i))
                    .build());
        }
        orderService.createOrder(userId, items);
        return "redirect:/";
    }

    // 📦 View single order with its status logs
    @GetMapping("/orders/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        List<OrderStatusLog> logs = orderService.getStatusLogs(id);
        model.addAttribute("order", order);
        model.addAttribute("logs", logs);
        return "order-details";
    }

    // 🚚 Update order status
    @PostMapping("/orders/{id}/update-status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam Long updatedBy,
            @RequestParam String remarks) {

        orderService.updateOrderStatus(id, status, updatedBy, remarks);
        return "redirect:/orders/" + id;
    }
}
