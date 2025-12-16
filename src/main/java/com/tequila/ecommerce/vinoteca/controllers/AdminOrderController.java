package com.tequila.ecommerce.vinoteca.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tequila.ecommerce.vinoteca.models.Order;
import com.tequila.ecommerce.vinoteca.services.OrderService;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*")
public class AdminOrderController {

    private static final Logger logger = LoggerFactory.getLogger(AdminOrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingOrders() {
        try {
            logger.info("📋 Obteniendo órdenes pendientes");
            List<Order> orders = orderService.getOrdersByEstado("PENDIENTE");
            logger.info("✅ Se obtuvieron {} órdenes pendientes", orders.size());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("❌ Error al obtener órdenes pendientes: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/completed")
    public ResponseEntity<?> getCompletedOrders() {
        try {
            logger.info("📋 Obteniendo órdenes completadas");
            List<Order> orders = orderService.getOrdersByEstado("COMPLETADA");
            logger.info("✅ Se obtuvieron {} órdenes completadas", orders.size());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("❌ Error al obtener órdenes completadas: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        try {
            logger.info("📦 Obteniendo detalles de la orden ID: {}", orderId);
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"Orden no encontrada\"}");
            }
            logger.info("✅ Detalles obtenidos para orden: {}", orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            logger.error("❌ Error al obtener detalles: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> request) {
        try {
            String newStatus = request.get("estado");
            logger.info("🔄 Actualizando estado de orden ID: {} a {}", orderId, newStatus);
            
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"Orden no encontrada\"}");
            }
            
            order.setEstado(newStatus);
            Order updatedOrder = orderService.updateOrder(order);
            
            logger.info("✅ Estado actualizado a: {}", newStatus);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            logger.error("❌ Error al actualizar estado: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        try {
            logger.info("🗑️ Eliminando orden ID: {}", orderId);
            boolean deleted = orderService.deleteOrder(orderId);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"Orden no encontrada\"}");
            }
            logger.info("✅ Orden eliminada");
            return ResponseEntity.ok("{\"message\": \"Orden eliminada exitosamente\"}");
        } catch (Exception e) {
            logger.error("❌ Error al eliminar orden: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }
}
