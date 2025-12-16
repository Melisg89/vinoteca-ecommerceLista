package com.tequila.ecommerce.vinoteca.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tequila.ecommerce.vinoteca.models.Product;
import com.tequila.ecommerce.vinoteca.services.ProductService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;  // ✅ Changed from ProductServices to ProductService

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("📥 GET /api/products - Obteniendo todos los productos");
        List<Product> products = productService.getAllProducts();
        logger.info("📤 Retornando {} productos", products.size());
        products.forEach(p -> {
            logger.debug("   - {}: imageUrl = {}", p.getNombre(), p.getImageUrl());
        });
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        logger.info("📥 GET /api/products/{} - Obteniendo producto específico", id);
        try {
            Product product = productService.getProductById(id);
            logger.info("📤 Producto encontrado: {} | ImageUrl: {}", product.getNombre(), product.getImageUrl());
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("❌ Error obteniendo producto: {}", e.getMessage());
            throw new EntityNotFoundException("Product not found with id " + id);
        }
    }
}
