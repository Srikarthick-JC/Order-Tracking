package com.ordertracking.order_tracking.repository;

import com.ordertracking.order_tracking.model.OrderStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderStatusLogRepository extends JpaRepository<OrderStatusLog, Long> {
    List<OrderStatusLog> findByOrder_OrderIdOrderByTimestampAsc(Long orderId);
}
