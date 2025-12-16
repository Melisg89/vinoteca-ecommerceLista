package com.tequila.ecommerce.vinoteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tequila.ecommerce.vinoteca.models.User;
import com.tequila.ecommerce.vinoteca.services.UserService;

@Controller
public class RegistroController {

    @Autowired
    private UserService userService;

    // Mostrar formulario de registro (GET)
    @GetMapping("/registrarse")
    public String mostrarFormularioRegistro() {
        return "register"; // este es el nombre del HTML sin extensión (register.html en templates)
    }

    // Procesar registro (POST)
    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {

        User user = new User();
        user.setNombre(username);
        user.setEmail(email);
        // Encriptar la contraseña antes de guardar
        user.setPassword(userService.encodePassword(password));

        userService.guardarUsuario(user);

        return "redirect:/login.html"; // después de registrar, redirigir a login o donde quieras
    }
}