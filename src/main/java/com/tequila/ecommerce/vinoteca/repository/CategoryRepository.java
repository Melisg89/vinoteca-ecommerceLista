package com.tequila.ecommerce.vinoteca.repository;

import com.tequila.ecommerce.vinoteca.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//Permite realizar operaciones de base de datos sobre las categorías sin tener que escribir SQL manualmente.

public interface CategoryRepository extends JpaRepository<Category, Long> {// Esto le da acceso a muchas operaciones CRUD
    List<Category> findByTipoBebida(String tipoBebida); //implementadas para la entidad Category, 
    List<Category> findByTipoProducto(String tipoProducto); //donde Long es el tipo de dato de la clave primaria.
}

//findByTipoBebida/product: Busca y devuelve una lista de categorías que tengan el tipo especificado.
