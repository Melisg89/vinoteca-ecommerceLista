package com.tequila.ecommerce.vinoteca.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tequila.ecommerce.vinoteca.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByEstado(String estado);
    List<Order> findByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Order> findByEstado(String estado, Pageable pageable);
}