package com.ordertracking.order_tracking.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "order_status_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private String status; // Placed, Shipped, Delivered, Cancelled

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    private Instant timestamp = Instant.now();

    private String remarks;
}
