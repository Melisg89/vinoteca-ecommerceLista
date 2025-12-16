package com.tequila.ecommerce.vinoteca.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tequila.ecommerce.vinoteca.dto.OrderDTO;
import com.tequila.ecommerce.vinoteca.models.Order;
import com.tequila.ecommerce.vinoteca.models.User;
import com.tequila.ecommerce.vinoteca.services.OrderService;
import com.tequila.ecommerce.vinoteca.services.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    @Autowired
    private OrderService orderService; //manteniendo el código limpio y desacoplado.

    @Autowired
    private UserService userService;
    
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

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody OrderDTO orderDTO, Authentication authentication) {
        try {
            logger.info("📍 Recibido checkout request");
            
            // Validar que la orden no sea nula
            if (orderDTO == null || orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
                logger.warn("⚠️ Carrito vacío");
                return ResponseEntity.badRequest().body("{\"message\": \"Carrito vacío\"}");
            }
            
            // Obtener el usuario autenticado
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.error("❌ Usuario no autenticado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\": \"Usuario no autenticado\"}");
            }
            
            String email = authentication.getName();
            User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            logger.info("✅ Usuario encontrado: {}", email);
            logger.info("✅ Validación pasada, creando orden...");
            
            // Crear la orden
            Order result = orderService.createOrder(orderDTO, user);
            
            logger.info("✅ Orden creada exitosamente");
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            logger.error("❌ Error en checkout: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error al procesar el pedido: " + e.getMessage() + "\"}");
        }
    }
}
