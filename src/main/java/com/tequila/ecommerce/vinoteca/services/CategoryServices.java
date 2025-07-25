package com.tequila.ecommerce.vinoteca.services;

import com.tequila.ecommerce.vinoteca.models.Category;
import com.tequila.ecommerce.vinoteca.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Esto indica que es un componente de servicio de Spring
		//responsable de contener la lógica de negocio y ser gestionado por el contenedor de Spring
public class CategoryServices {

    @Autowired //Inyección automática del CategoryRepository
    private CategoryRepository categoriaRepository;

    public List<Category> obtenerTodasLasCategorias(){ //Devuelve una lista con todas las categorías de la bd.
        return categoriaRepository.findAll();
    }
    public Category obtenerCategoriaPorId(Long id) {//Busca y retorna específica por su id. Si no la encuentra,  null
        return categoriaRepository.findById(id).orElse(null);
    }

    public Category guardarCategorias(Category categoria) {//Guarda una nueva categoría o actualiza una existente.


        return categoriaRepository.save(categoria);
    }

    public void eliminarCategorias(Long id) {//Elimina la categoría con el id especificado.

        categoriaRepository.deleteById(id);
    }

}
