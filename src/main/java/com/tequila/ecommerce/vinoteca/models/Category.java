package com.tequila.ecommerce.vinoteca.models;

import jakarta.persistence.*; // ✅ Importación correcta para las anotaciones JPA
import java.util.List;

@Entity // Entidad Categoría
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "tipo_bebida")
    private String tipoBebida;

    @Column(name = "tipo_producto")
    private String tipoProducto;

    // Una categoría puede tener muchos productos asociados
    // Si una categoría se elimina, también se eliminan sus productos
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    public Category() {
    }

    public Category(Long id, String name, String tipoBebida, String tipoProducto, List<Product> products) {
        this.id = id;
        this.name = name;
        this.tipoBebida = tipoBebida;
        this.tipoProducto = tipoProducto;
        this.products = products;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTipoBebida() {
        return tipoBebida;
    }

    public void setTipoBebida(String tipoBebida) {
        this.tipoBebida = tipoBebida;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
