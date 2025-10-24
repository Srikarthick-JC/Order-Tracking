package com.ordertracking.order_tracking.controller;

import com.ordertracking.order_tracking.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/reports")
    public String viewReports(Model model) {
        model.addAttribute("statusReport", reportService.getOrderStatusReport());
        model.addAttribute("deliveryReport", reportService.getDeliveryPerformanceReport());
        return "reports";
    }
}
