package com.ordertracking.order_tracking.repository;

import com.ordertracking.order_tracking.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
