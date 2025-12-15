package com.tequila.ecommerce.vinoteca.controllers;

import com.tequila.ecommerce.vinoteca.models.Category;
import com.tequila.ecommerce.vinoteca.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//Este archivo actúa como intermediario entre el cliente (navegador o app) y 
//la lógica interna del sistema, gestionando las solicitudes relacionadas con categorías.

@RestController
@RequestMapping("/api/categoria") //Devuelve una lista de todas las categorías disponibles.

public class CategoryController {  //gestiona todas las operaciones relacionadas con las categorías de productos

    @Autowired //Inyección de dependencia automática
    private CategoryServices categoryServices; //Conecta con el sistema, delega la lógica de negocio

    @GetMapping
    public ResponseEntity<List<Category>> obtenerTodasLasCategorias() {
        List<Category> categorias = categoryServices.obtenerTodasLasCategorias();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/{id}") //Busca una categoría específica según su ID.
    public ResponseEntity<Category> obtenerCategoriaPorId(@PathVariable Long id) {
        Category categoria = categoryServices.obtenerCategoriaPorId(id);
        if (categoria != null) {
            return new ResponseEntity<>(categoria, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping // Recibe una categoría en formato JSON y la guarda en la base de datos.
    public ResponseEntity<Category> guardarCategoria(@RequestBody Category categoria) {
        Category nuevaCategoria = categoryServices.guardarCategorias(categoria);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")  //Elimina la categoría con el ID indicado.
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoryServices.eliminarCategorias(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
