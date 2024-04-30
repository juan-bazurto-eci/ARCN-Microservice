package edu.escuelaing.arcn.microservice.domain.model;

import lombok.Data;

@Data
public class ShippingAddress {

    private String name;
    private String phoneNumber;
    private String address;
    private String postalCode;
    private String city;

    public ShippingAddress(String name, String phoneNumber, String address, String postalCode, String city) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
    }

}
