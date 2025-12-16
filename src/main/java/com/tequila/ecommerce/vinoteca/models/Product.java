package com.tequila.ecommerce.vinoteca.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "tipo_bebida")
    private String tipoBebida;

    @Column(name = "precio", nullable = false)
    @NotNull(message = "El precio es obligatorio")
    @JsonProperty("precio")
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    @NotNull(message = "El stock es obligatorio")
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;

    @Transient
    private Integer quantity;

    public Product() {
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public Product setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTipoBebida() {
        return tipoBebida;
    }

    public Product setTipoBebida(String tipoBebida) {
        this.tipoBebida = tipoBebida;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Integer getStock() {
        return stock;
    }

    public Product setStock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Product setCategory(Category category) {
        this.category = category;
        return this;
    }

    public Integer getQuantity() {
        return quantity != null ? quantity : 1;
    }

    public Product setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
}

