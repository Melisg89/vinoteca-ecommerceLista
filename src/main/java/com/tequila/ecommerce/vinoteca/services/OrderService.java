package com.tequila.ecommerce.vinoteca.services;

import com.tequila.ecommerce.vinoteca.models.Order;
import com.tequila.ecommerce.vinoteca.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service // encargado de la lógica de negocio sobre las órdenes.

public class OrderService {

    @Autowired // Esto permite acceder a la capa de persistencia (base de datos)
    private OrderRepository orderRepository;

    public List<Order> getOrdersByFechaCreacion(LocalDateTime fechaCreacion) {//Obtiene órdenes creadas en una fecha específica
        return orderRepository.findByFechaCreacion(fechaCreacion);
    }

    public List<Order> getAllOrders() { //Obtiene todas las órdenes.
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByEstado(String estado) {//Obtiene órdenes filtradas por su estado (ej. pendiente).
        return orderRepository.findByEstado(estado);
    }

    public List<Order> getOrdersByUserId(Long userId) {//Obtiene órdenes asociadas a un usuario específico.
        return orderRepository.findByUser_Id(userId);
    }

    public List<Order> getOrdersByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return orderRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
    }//Obtiene órdenes creadas en un rango de fechas.

    //Paginacion:
    public Page<Order> getAllOrdersPaginated(Pageable pageable) {
        return orderRepository.findAll(pageable);
    } //Obtiene todas las órdenes, pero paginadas para optimizar consultas con muchos datos.

    public Page<Order> getOrdersByEstadoPaginated(String estado, Pageable pageable) {
        return orderRepository.findByEstado(estado, pageable);
    }//Obtiene órdenes por estado, paginadas.

    //Operaciones de escritura:
    public Order updateOrder(Order order) {
        return orderRepository.save(order); //Crea una nueva orden en la base de datos.
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);// Actualiza una orden existente.
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);//Elimina una orden por su ID
    }
}
