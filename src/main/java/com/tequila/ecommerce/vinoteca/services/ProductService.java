// ...existing imports...
package com.tequila.ecommerce.vinoteca.services;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tequila.ecommerce.vinoteca.dto.ProductDTO;
import com.tequila.ecommerce.vinoteca.models.Category;
import com.tequila.ecommerce.vinoteca.models.Product;
import com.tequila.ecommerce.vinoteca.repository.CategoryRepository;
import com.tequila.ecommerce.vinoteca.repository.ProductRepository;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Product createProduct(ProductDTO productDTO) {
        logger.info("📝 Creando producto: {}", productDTO.getNombre());
        
        Product product = new Product();
        product.setNombre(productDTO.getNombre());
        product.setDescripcion(productDTO.getDescripcion());
        product.setPrecio(BigDecimal.valueOf(productDTO.getPrecio()));
        product.setStock(productDTO.getStock());
        
        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            product.setCategory(category);
        }
        
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, ProductDTO productDTO) {
        logger.info("📝 Actualizando producto ID: {}", productId);
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        product.setNombre(productDTO.getNombre());
        product.setDescripcion(productDTO.getDescripcion());
        product.setPrecio(BigDecimal.valueOf(productDTO.getPrecio()));
        product.setStock(productDTO.getStock());
        
        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            product.setCategory(category);
        }
        
        return productRepository.save(product);
    }

    public Product updateProductPrice(Long productId, Double newPrice) {
        logger.info("💰 Actualizando precio de producto ID: {} a ${}", productId, newPrice);
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        product.setPrecio(BigDecimal.valueOf(newPrice));
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(Long productId) {
        logger.info("🗑️ Eliminando producto ID: {}", productId);
        productRepository.deleteById(productId);
    }
}
