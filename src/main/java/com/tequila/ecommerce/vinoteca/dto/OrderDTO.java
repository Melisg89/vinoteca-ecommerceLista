package com.tequila.ecommerce.vinoteca.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderDTO {

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("emailaddress")
    private String emailaddress;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("streetaddress")
    private String streetaddress;

    @JsonProperty("apartment")
    private String apartment;

    @JsonProperty("department")
    private String department;

    @JsonProperty("postcodezip")
    private String postcodezip;

    @JsonProperty("paymentMethod")
    private String paymentMethod;

    @JsonProperty("items")
    private List<OrderItemDTO> items;

    // Constructor sin argumentos (REQUERIDO para deserialización JSON)
    public OrderDTO() {
    }

    // Getters y Setters
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreetaddress() {
        return streetaddress;
    }

    public void setStreetaddress(String streetaddress) {
        this.streetaddress = streetaddress;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPostcodezip() {
        return postcodezip;
    }

    public void setPostcodezip(String postcodezip) {
        this.postcodezip = postcodezip;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
