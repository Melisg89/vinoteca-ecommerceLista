package com.tequila.ecommerce.vinoteca.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users") // nombre de la tabla en la base de datos
public class User {

    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El valor de este campo se generará automáticamente
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre") // nombre de la columna en la base de datos
    private String name;

    @Column(name = "email") // nombre de la columna en la base de datos
    private String email;

    @Column(name = "password") // nombre de la columna en la base de datos
    private String password;

    @OneToMany(mappedBy = "user") // Define una relación de uno a muchos con la entidad Order
    private List<Order> orders;

    public Long id() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String nombre() {
        return name;
    }

    public User setNombre(String nombre) {
        this.name = nombre;
        return this;
    }

    public String email() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String password() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<Order> pedidos() {
        return orders;
    }

    public User setPedidos(List<Order> orders) {
        this.orders = orders;
        return this;
    }
}
