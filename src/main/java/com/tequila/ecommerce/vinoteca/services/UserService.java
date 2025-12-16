package com.tequila.ecommerce.vinoteca.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tequila.ecommerce.vinoteca.models.User;
import com.tequila.ecommerce.vinoteca.repository.UserRepository;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String encodePassword(String rawPassword) {
        logger.info("🔐 Codificando contraseña");
        return passwordEncoder.encode(rawPassword);
    }

    public User registerUser(String nombre, String email, String password, User.Role role) {
        logger.info("📝 Registrando usuario: {}", email);
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        User user = new User();
        user.setNombre(nombre);
        user.setEmail(email.trim().toLowerCase());
        // Asegura que la contraseña se encripte aquí SIEMPRE
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role != null ? role : User.Role.CLIENTE);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> obtenerTodosLosUsuarios() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User obtenerUsuarioPorId(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User guardarUsuario(User usuario) {
        return userRepository.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public User encontrarPorEmail(String email) {
        // Corregido: obtener el User del Optional o retornar null si no existe
        return userRepository.findByEmail(email).orElse(null);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        logger.info("✓ Validando contraseña");
        boolean isValid = passwordEncoder.matches(rawPassword, encodedPassword);
        logger.info("Resultado validación: {}", isValid ? "✅ Válida" : "❌ Inválida");
        return isValid;
    }
}
