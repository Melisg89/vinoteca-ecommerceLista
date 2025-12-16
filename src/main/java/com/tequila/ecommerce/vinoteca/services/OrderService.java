package com.tequila.ecommerce.vinoteca.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tequila.ecommerce.vinoteca.dto.OrderDTO;
import com.tequila.ecommerce.vinoteca.dto.OrderItemDTO;
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
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Order createOrderFromDTO(OrderDTO orderDTO) {
        // Validar que la orden tenga items
        if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            throw new IllegalArgumentException("La orden debe contener al menos un producto");
        }

        // Validar usuario
        if (orderDTO.getUser() == null || orderDTO.getUser().getId() == null) {
            throw new IllegalArgumentException("El usuario es requerido");
        }

        // Obtener el usuario de la base de datos
        User user = userRepository.findById(orderDTO.getUser().getId())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Validar detalles de entrega
        if (orderDTO.getFirstname() == null || orderDTO.getFirstname().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (orderDTO.getLastname() == null || orderDTO.getLastname().isEmpty()) {
            throw new IllegalArgumentException("El apellido es requerido");
        }
        if (orderDTO.getPhone() == null || orderDTO.getPhone().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es requerido");
        }
        if (orderDTO.getEmailaddress() == null || orderDTO.getEmailaddress().isEmpty()) {
            throw new IllegalArgumentException("El email es requerido");
        }
        if (orderDTO.getPaymentMethod() == null || orderDTO.getPaymentMethod().isEmpty()) {
            throw new IllegalArgumentException("El método de pago es requerido");
        }

        // Crear la orden
        Order order = new Order();
        order.setUser(user);
        order.setFirstname(orderDTO.getFirstname());
        order.setLastname(orderDTO.getLastname());
        order.setDepartment(orderDTO.getDepartment());
        order.setStreetaddress(orderDTO.getStreetaddress());
        order.setApartment(orderDTO.getApartment());
        order.setPostcodezip(orderDTO.getPostcodezip());
        order.setPhone(orderDTO.getPhone());
        order.setEmailaddress(orderDTO.getEmailaddress());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setFechaCreacion(LocalDateTime.now());
        order.setEstado("PENDIENTE");

        // Procesar los items de la orden
        List<OrderItem> processedItems = new ArrayList<>();
        
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            if (itemDTO.getProduct() == null || itemDTO.getProduct().getId() == null) {
                throw new IllegalArgumentException("Producto inválido en los items");
            }
            
            Product product = productRepository.findById(itemDTO.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + itemDTO.getProduct().getId()));
            
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity() != null && itemDTO.getQuantity() > 0 ? itemDTO.getQuantity() : 1);
            item.setPrice(itemDTO.getPrice() != null && itemDTO.getPrice() > 0 ? itemDTO.getPrice() : product.getPrice().doubleValue());
            
            processedItems.add(item);
        }
        
        order.setItems(processedItems);

        Order savedOrder = orderRepository.save(order);
        orderRepository.flush(); // Fuerza la sincronización con la BD
        
        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByEstado(String estado) {
        return orderRepository.findByEstado(estado);
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
        if (orderRepository.existsById(order.getId())) {
            return orderRepository.save(order);
        }
        return null;
    }

    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
