package com.tequila.ecommerce.vinoteca.repository;

import com.tequila.ecommerce.vinoteca.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
