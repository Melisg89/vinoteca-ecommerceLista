package com.tequila.ecommerce.vinoteca.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users") // nombre de la tabla en la base de datos
public class User {

    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El valor de este campo se generará automáticamente
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false) // nombre de la columna en la base de datos
    private String name;

    @Column(name = "email", nullable = false, unique = true) // nombre de la columna en la base de datos
    private String email;

    @Column(name = "password", nullable = false) // nombre de la columna en la base de datos
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.CLIENTE;

    @Column(name = "enabled")
    private Boolean enabled = true;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore // Evita el error de lazy initialization al serializar User
    private List<Order> orders; // almacena todas las compras realizadas por el usuario.

    // Constructor por defecto
    public User() {}

    // Getters estándar JavaBean
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public List<Order> getPedidos() {
        return orders;
    }

    // Setters con retorno para encadenar
    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public User setNombre(String nombre) {
        this.name = nombre;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public User setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public User setPedidos(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    public enum Role {
        ADMIN, CLIENTE
    }
}
