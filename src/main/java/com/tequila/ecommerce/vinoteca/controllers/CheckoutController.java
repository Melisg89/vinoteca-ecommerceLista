package com.tequila.ecommerce.vinoteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tequila.ecommerce.vinoteca.dto.OrderDTO;
import com.tequila.ecommerce.vinoteca.models.Order;
import com.tequila.ecommerce.vinoteca.services.OrderService;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> procesarCheckout(@RequestBody OrderDTO orderDTO) {
        try {
            Order newOrder = orderService.createOrderFromDTO(orderDTO);
            return ResponseEntity.ok(newOrder);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("{\"message\": \"Error interno: " + ex.getMessage() + "\"}");
        }
    }
}