package com.tequila.ecommerce.vinoteca.controllers;

import com.tequila.ecommerce.vinoteca.models.Order;
import com.tequila.ecommerce.vinoteca.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pedido") //Trae todos los pedidos.
public class OrderController {

    @Autowired
    private OrderService orderService; //manteniendo el código limpio y desacoplado.

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/estado/{estado}") //Trae pedidos filtrados por estado
    public ResponseEntity<List<Order>> getOrdersByEstado(@PathVariable String estado) {
        List<Order> orders = orderService.getOrdersByEstado(estado);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/usuario/{userId}") //Trae pedidos hechos por un usuario específico
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/fecha") //Trae pedidos entre dos fechas
    public ResponseEntity<List<Order>> getOrdersByFechaCreacionBetween(
            @RequestParam LocalDateTime fechaInicio, @RequestParam LocalDateTime fechaFin) {
        List<Order> orders = orderService.getOrdersByFechaCreacionBetween(fechaInicio, fechaFin);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/paginated") //Trae pedidos paginados
    public ResponseEntity<Page<Order>> getAllOrdersPaginated(Pageable pageable) {
        Page<Order> orders = orderService.getAllOrdersPaginated(pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/estado/paginated") //Igual que el anterior pero filtrado por estado
    public ResponseEntity<Page<Order>> getOrdersByEstadoPaginated(
            @RequestParam String estado, Pageable pageable) {
        Page<Order> orders = orderService.getOrdersByEstadoPaginated(estado, pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order newOrder = orderService.createOrder(order);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}") //Modifica un pedido existente
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long orderId, @RequestBody Order order) {
        order.setId(orderId);
        Order updatedOrder = orderService.updateOrder(order);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}") //Elimina un pedido.
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
