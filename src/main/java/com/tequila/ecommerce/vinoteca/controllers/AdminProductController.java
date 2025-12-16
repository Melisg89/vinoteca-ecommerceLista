package com.tequila.ecommerce.vinoteca.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tequila.ecommerce.vinoteca.dto.ProductDTO;
import com.tequila.ecommerce.vinoteca.models.Product;
import com.tequila.ecommerce.vinoteca.models.User;
import com.tequila.ecommerce.vinoteca.services.ProductService;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "*")
public class AdminProductController {

    private static final Logger logger = LoggerFactory.getLogger(AdminProductController.class);

    @Autowired
    private ProductService productService;

    private ResponseEntity<?> verificarAdmin(Authentication authentication) {
        logger.info("🔐 VERIFICANDO PERMISOS ADMIN");
        
        if (authentication == null) {
            logger.error("❌ No hay autenticación");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{\"message\": \"No autenticado\"}");
        }

        if (!authentication.isAuthenticated()) {
            logger.error("❌ Usuario no autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{\"message\": \"Usuario no autenticado\"}");
        }

        User user = (User) authentication.getPrincipal();
        logger.info("👤 Usuario: {}", user.getEmail());
        logger.info("🛡️  Rol: {}", user.getRole());

        if (user.getRole() != User.Role.ADMIN) {
            logger.warn("⛔ ACCESO DENEGADO - Usuario no es ADMIN");
            logger.warn("   Rol actual: {}", user.getRole());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("{\"message\": \"Acceso denegado. Solo admins pueden acceder.\"}");
        }

        logger.info("✅ ACCESO PERMITIDO - Usuario es ADMIN");
        return null;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO, Authentication authentication) {
        ResponseEntity<?> adminCheck = verificarAdmin(authentication);
        if (adminCheck != null) return adminCheck;

        try {
            logger.info("📝 Creando nuevo producto: {}", productDTO.getNombre());
            Product product = productService.createProduct(productDTO);
            logger.info("✅ Producto creado exitosamente con ID: {}", product.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (Exception e) {
            logger.error("❌ Error al crear producto: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error al crear producto: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO, Authentication authentication) {
        ResponseEntity<?> adminCheck = verificarAdmin(authentication);
        if (adminCheck != null) return adminCheck;

        try {
            logger.info("📝 Actualizando producto ID: {}", productId);
            Product product = productService.updateProduct(productId, productDTO);
            logger.info("✅ Producto actualizado exitosamente");
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error("❌ Error al actualizar producto: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error al actualizar producto: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{productId}/price")
    public ResponseEntity<?> updateProductPrice(@PathVariable Long productId, @RequestBody java.util.Map<String, Double> request, Authentication authentication) {
        ResponseEntity<?> adminCheck = verificarAdmin(authentication);
        if (adminCheck != null) return adminCheck;

        try {
            Double newPrice = request.get("precio");
            logger.info("💰 Actualizando precio del producto ID: {} a ${}", productId, newPrice);
            Product product = productService.updateProductPrice(productId, newPrice);
            logger.info("✅ Precio actualizado exitosamente");
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error("❌ Error al actualizar precio: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error al actualizar precio: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        // GET sin autenticación requerida
        try {
            logger.info("📦 Obteniendo todos los productos");
            List<Product> products = productService.getAllProducts();
            logger.info("✅ Se obtuvieron {} productos", products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("❌ Error al obtener productos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error al obtener productos: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            logger.info("📦 Obteniendo producto ID: {}", id);
            Product product = productService.getAllProducts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
            
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"Producto no encontrado\"}");
            }
            logger.info("✅ Producto obtenido");
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error("❌ Error al obtener producto: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId, Authentication authentication) {
        ResponseEntity<?> adminCheck = verificarAdmin(authentication);
        if (adminCheck != null) return adminCheck;

        try {
            logger.info("🗑️ Eliminando producto ID: {}", productId);
            productService.deleteProduct(productId);
            logger.info("✅ Producto eliminado exitosamente");
            return ResponseEntity.ok("{\"message\": \"Producto eliminado\"}");
        } catch (Exception e) {
            logger.error("❌ Error al eliminar producto: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"Error al eliminar producto: " + e.getMessage() + "\"}");
        }
    }
}
