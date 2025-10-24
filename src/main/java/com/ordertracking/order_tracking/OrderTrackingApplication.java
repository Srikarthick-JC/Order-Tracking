package com.ordertracking.order_tracking;

import com.ordertracking.order_tracking.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
@RequiredArgsConstructor
public class OrderTrackingApplication implements CommandLineRunner {

	@Lazy
	private final CustomUserDetailsService userDetailsService;

	public static void main(String[] args) {
		SpringApplication.run(OrderTrackingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userDetailsService.createDefaultAdmin();
	}

}
