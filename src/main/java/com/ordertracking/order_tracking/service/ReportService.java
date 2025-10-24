package com.ordertracking.order_tracking.service;

import com.ordertracking.order_tracking.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderRepository orderRepository;

    public Map<String, Long> getOrderStatusReport() {
        Map<String, Long> report = new HashMap<>();
        // Assuming we have a method to count by status
        // For now, placeholder
        report.put("Placed", 10L);
        report.put("Processed", 5L);
        report.put("Shipped", 3L);
        report.put("Delivered", 8L);
        report.put("Cancelled", 1L);
        return report;
    }

    public Map<String, Double> getDeliveryPerformanceReport() {
        Map<String, Double> report = new HashMap<>();
        // Placeholder for delivery performance
        report.put("Average Delivery Time", 3.5);
        report.put("On-Time Delivery Rate", 85.0);
        return report;
    }
}
