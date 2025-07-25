package com.tequila.ecommerce.vinoteca.repository;

import com.tequila.ecommerce.vinoteca.models.Category;
import com.tequila.ecommerce.vinoteca.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {//Hereda métodos para realizar operaciones CRUD Product, 
																		////donde el ID es de tipo Long.
    List<Product> findByTipoBebida(String tipoBebida);
//findByTipoBebida Permite obtener una lista de productos filtrados por el tipo de bebida
}
