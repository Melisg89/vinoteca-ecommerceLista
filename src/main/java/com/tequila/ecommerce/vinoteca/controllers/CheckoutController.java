package com.tequila.ecommerce.vinoteca.controllers;

import com.tequila.ecommerce.vinoteca.models.Order;
import com.tequila.ecommerce.vinoteca.models.User;
import com.tequila.ecommerce.vinoteca.models.Product;
import com.tequila.ecommerce.vinoteca.services.OrderService;
import com.tequila.ecommerce.vinoteca.services.UserService;
import com.tequila.ecommerce.vinoteca.services.ProductServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductServices productService;

    @PostMapping
    public String realizarCheckout(@RequestParam Long userId, @RequestParam List<Long> productIds) {
        User user = userService.obtenerUsuarioPorId(userId);
        if (user == null) {
            return "Usuario no encontrado";
        }

        List<Product> productos = productService.obtenerProductosPorIds(productIds);
        if (productos.isEmpty()) {
            return "No hay productos para comprar";
        }

        double total = productos.stream()
        	    .mapToDouble(product -> product.getPrice().doubleValue())
        	    .sum();

        Order order = new Order();
        order.setUser(user);
        order.setProducts(productos);
        order.setFechaCreacion(LocalDateTime.now());
        order.setEstado("Pendiente");
        order.setTotal(total);

        orderService.createOrder(order);

        return "Pedido realizado correctamente";
    }
}
