package com.ordertracking.order_tracking.controller;

import com.ordertracking.order_tracking.model.*;
import com.ordertracking.order_tracking.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final OrderService orderService;

    // 🏠 Admin Dashboard: list all orders with history
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin-dashboard";
    }

    // 👤 User Dashboard: list user's orders
    @GetMapping("/user/dashboard")
    @PreAuthorize("hasRole('USER')")
    public String userDashboard(Authentication authentication, Model model) {
        // Get current user ID from authentication
        String email = authentication.getName();
        // For simplicity, assume userId is 1 for now; in real app, get from UserDetails
        // TODO: Get userId properly
        List<Order> orders = orderService.getOrdersByUser(1L); // Placeholder
        model.addAttribute("orders", orders);
        model.addAttribute("userEmail", email);
        return "user-dashboard";
    }

    // ➕ New Order Page
    @GetMapping("/orders/new")
    @PreAuthorize("hasRole('USER')")
    public String newOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "new-order";
    }

    // 📨 Handle Order Submission
    @PostMapping("/orders/save")
    @PreAuthorize("hasRole('USER')")
    public String saveOrder(
            Authentication authentication,
            @RequestParam List<String> productName,
            @RequestParam List<Integer> quantity,
            @RequestParam List<Double> price) {

        // Get current user ID
        // TODO: Get userId from authentication properly
        Long userId = 1L; // Placeholder

        List<OrderItem> items = new ArrayList<>();
        for (int i = 0; i < productName.size(); i++) {
            items.add(OrderItem.builder()
                    .productName(productName.get(i))
                    .quantity(quantity.get(i))
                    .price(price.get(i))
                    .build());
        }
        orderService.createOrder(userId, items);
        return "redirect:/user/dashboard";
    }

    // 📦 View single order with its status logs
    @GetMapping("/orders/{id}")
    public String viewOrder(@PathVariable Long id, Authentication authentication, Model model) {
        Order order = orderService.getOrderById(id);
        List<OrderStatusLog> logs = orderService.getStatusLogs(id);
        model.addAttribute("order", order);
        model.addAttribute("logs", logs);

        // Check if user can view this order
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !order.getUser().getUserId().equals(1L)) { // TODO: Get current user ID
            return "redirect:/access-denied";
        }

        return "order-details";
    }

    // 🚚 Update order status
    @PostMapping("/orders/{id}/update-status")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam Long updatedBy,
            @RequestParam String remarks) {

        orderService.updateOrderStatus(id, status, updatedBy, remarks);
        return "redirect:/admin/dashboard";
    }

    // 🗑️ Delete Order
    @PostMapping("/orders/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/admin/dashboard";
    }

}
