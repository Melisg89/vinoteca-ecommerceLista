package com.tequila.ecommerce.vinoteca.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tequila.ecommerce.vinoteca.models.User;
import com.tequila.ecommerce.vinoteca.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public User registerUser(String nombre, String email, String password, User.Role role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        User user = new User();
        user.setNombre(nombre);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role != null ? role : User.Role.CLIENTE);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
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
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
