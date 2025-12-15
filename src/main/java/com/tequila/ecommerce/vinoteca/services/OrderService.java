package com.tequila.ecommerce.vinoteca.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tequila.ecommerce.vinoteca.models.Order;
import com.tequila.ecommerce.vinoteca.models.OrderItem;
import com.tequila.ecommerce.vinoteca.models.Product;
import com.tequila.ecommerce.vinoteca.models.User;
import com.tequila.ecommerce.vinoteca.repository.OrderRepository;
import com.tequila.ecommerce.vinoteca.repository.ProductRepository;
import com.tequila.ecommerce.vinoteca.repository.UserRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> getOrdersByFechaCreacion(LocalDateTime fechaCreacion) {
        return orderRepository.findByFechaCreacion(fechaCreacion);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByEstado(String estado) {
        return orderRepository.findByEstado(estado);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUser_Id(userId);
    }

    public List<Order> getOrdersByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return orderRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
    }

    public Page<Order> getAllOrdersPaginated(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> getOrdersByEstadoPaginated(String estado, Pageable pageable) {
        return orderRepository.findByEstado(estado, pageable);
    }

    public Order updateOrder(Order order) {
        if (order.getId() != null && orderRepository.existsById(order.getId())) {
            return orderRepository.save(order);
        }
        return null;
    }

    @Transactional
    public Order createOrder(Order order) {
        order.setId(null);

        // Validar usuario
        if (order.getUser() == null || order.getUser().getId() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio.");
        }
        User user = userRepository.findById(order.getUser().getId()).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }
        order.setUser(user);

        // Validar y asociar productos en items
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Debe haber al menos un producto en la orden.");
        }
        for (OrderItem item : order.getItems()) {
            if (item.getProduct() == null || item.getProduct().getId() == null) {
                throw new IllegalArgumentException("Producto inválido en el carrito.");
            }
            Product product = productRepository.findById(item.getProduct().getId()).orElse(null);
            if (product == null) {
                throw new IllegalArgumentException("Producto no encontrado (ID: " + item.getProduct().getId() + ")");
            }
            item.setProduct(product);
            item.setOrder(order);
            if (item.getQuantity() == null || item.getQuantity() < 1) {
                throw new IllegalArgumentException("Cantidad inválida para el producto " + product.getName());
            }
        }

        if (order.getFechaCreacion() == null) {
            order.setFechaCreacion(LocalDateTime.now());
        }
        if (order.getEstado() == null) {
            order.setEstado("pendiente");
        }

        // Calcula el total usando cantidad * precio
        double total = order.getItems().stream()
            .mapToDouble(item -> item.getProduct().getPrice().doubleValue() * item.getQuantity())
            .sum();
        order.setTotal(total);

        return orderRepository.save(order);
    }

    public boolean deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }
}
