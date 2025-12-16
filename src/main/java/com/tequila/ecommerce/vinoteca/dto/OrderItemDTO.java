package com.tequila.ecommerce.vinoteca.dto;

public class OrderItemDTO {
    private ProductDTO product;
    private Integer quantity;
    private Double price;

    public OrderItemDTO() {}

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
