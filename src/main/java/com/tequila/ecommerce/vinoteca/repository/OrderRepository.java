package com.tequila.ecommerce.vinoteca.repository;

import com.tequila.ecommerce.vinoteca.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_Id(Long userId);
    List<Order> findByFechaCreacion(LocalDateTime fechaCreacion);
    List<Order> findByEstado(String estado);
    List<Order> findByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByEstado(String estado, Pageable pageable);
}