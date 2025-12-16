package com.tequila.ecommerce.vinoteca.controllers;

import com.tequila.ecommerce.vinoteca.dto.OrderDTO;
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
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            Order newOrder = orderService.createOrderFromDTO(orderDTO);
            return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error interno: " + ex.getMessage() + "\"}");
        }
    }

    @PutMapping("/{orderId}") //Modifica un pedido existente
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long orderId, @RequestBody Order order) {
        order.setId(orderId);
        Order updatedOrder = orderService.updateOrder(order);
        if (updatedOrder != null) {
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{orderId}") //Elimina un pedido.
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        boolean deleted = orderService.deleteOrder(orderId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{orderId}") //Trae un pedido por su ID
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
