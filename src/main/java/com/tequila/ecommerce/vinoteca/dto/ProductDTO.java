package com.tequila.ecommerce.vinoteca.dto;

public class ProductDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String tipoBebida;
    private Long categoryId;

    public ProductDTO() {}

    public ProductDTO(Long id, String nombre, String descripcion, Double precio, Integer stock, String tipoBebida, Long categoryId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.tipoBebida = tipoBebida;
        this.categoryId = categoryId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getTipoBebida() { return tipoBebida; }
    public void setTipoBebida(String tipoBebida) { this.tipoBebida = tipoBebida; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
