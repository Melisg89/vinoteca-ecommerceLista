package com.tequila.ecommerce.vinoteca.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tequila.ecommerce.vinoteca.dto.AuthResponseDTO;
import com.tequila.ecommerce.vinoteca.dto.LoginDTO;
import com.tequila.ecommerce.vinoteca.models.User;
import com.tequila.ecommerce.vinoteca.security.JwtUtil;
import com.tequila.ecommerce.vinoteca.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String nombre = request.get("nombre");
            String email = request.get("email");
            String password = request.get("password");
            String roleStr = request.getOrDefault("role", "CLIENTE");

            User.Role role = User.Role.valueOf(roleStr.toUpperCase());
            User user = userService.registerUser(nombre, email, password, role);

            String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().toString());
            return ResponseEntity.ok(new AuthResponseDTO(token, user.getId(), user.getEmail(), user.getRole().toString()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            String email = loginDTO.getEmail().trim().toLowerCase();
            logger.info("Intentando login para email: '{}'", email);

            // Busca el usuario por email normalizado
            User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            logger.info("Usuario encontrado: {}", user.getEmail());

            // Verifica la contraseña usando el encoder
            if (!userService.validatePassword(loginDTO.getPassword(), user.getPassword())) {
                logger.warn("Contraseña incorrecta para usuario: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\": \"Contraseña incorrecta\"}");
            }

            String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().toString());
            logger.info("Login exitoso para usuario: {}", email);
            return ResponseEntity.ok(new AuthResponseDTO(token, user.getId(), user.getEmail(), user.getRole().toString()));
        } catch (Exception ex) {
            logger.error("Error en login: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\": \"Token inválido\"}");
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);
            User user = userService.getUserById(userId);

            return ResponseEntity.ok(new AuthResponseDTO(token, user.getId(), user.getEmail(), role));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{\"message\": \"" + ex.getMessage() + "\"}");
        }
    }
}
