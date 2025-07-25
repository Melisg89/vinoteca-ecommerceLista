package com.tequila.ecommerce.vinoteca.repository;

import com.tequila.ecommerce.vinoteca.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> { //Proporciona operaciones CRUD  para la entidad Order, 
																		//con Long como tipo de la clave primaria.
        
	List<Order> findByUser_Id(Long userId); //Obtiene todas las órdenes asociadas a un usuario específico según su ID.

    List<Order> findByFechaCreacion(LocalDateTime fechaCreacion);//Obtiene órdenes con una fecha exacta de creación.

    List<Order> findByEstado(String estado);// Obtiene órdenes según su estado

    List<Order> findByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    	// Obtiene órdenes cuyo rango de fecha de creación está entre dos fechas específicas.
    //Paginación
        Page<Order> findAll(Pageable pageable);// Devuelve todas las órdenes paginadas, útil para manejar grandes cantidades de datos.

        Page<Order> findByEstado(String estado, Pageable pageable);// Devuelve las órdenes filtradas por estado, pero con paginación.
}