package com.tequila.ecommerce.vinoteca.services;

import com.tequila.ecommerce.vinoteca.models.Product;
import com.tequila.ecommerce.vinoteca.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServices {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> obtenerTodosLosProductos() {
        return productRepository.findAll();
    }

    public Product obtenerProductoPorId(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> obtenerProductosPorIds(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

    public Product guardarProducto(Product producto) {
        return productRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        productRepository.deleteById(id);
    }
}
