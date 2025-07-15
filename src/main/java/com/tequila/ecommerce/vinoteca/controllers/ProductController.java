package com.tequila.ecommerce.vinoteca.controllers;

import com.tequila.ecommerce.vinoteca.models.Product;
import com.tequila.ecommerce.vinoteca.services.ProductServices;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    @Autowired
    private ProductServices productServices;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productServices.obtenerTodosLosProductos();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productServices.obtenerProductoPorId(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            throw new EntityNotFoundException("Product not found with id " + id);
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productServices.guardarProducto(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product existingProduct = productServices.obtenerProductoPorId(id);
        if (existingProduct != null) {
            existingProduct.setName(productDetails.getName())
                    .setDescription(productDetails.getDescription())
                    .setTipoBebida(productDetails.getTipoBebida())
                    .setPrice(productDetails.getPrice())
                    .setStock(productDetails.getStock())
                    .setCategory(productDetails.getCategory());

            Product updatedProduct = productServices.guardarProducto(existingProduct);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } else {
            throw new EntityNotFoundException("Product not found with id " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product existingProduct = productServices.obtenerProductoPorId(id);
        if (existingProduct != null) {
            productServices.eliminarProducto(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new EntityNotFoundException("Product not found with id " + id);
        }
    }
}
