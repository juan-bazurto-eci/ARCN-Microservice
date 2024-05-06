package edu.escuelaing.arcn.microservice.dto;

import java.time.LocalDate;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.model.ShippingAddress;
import lombok.Data;

@Data
public class ClientResponseDTO {

    private String message;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String phoneNumber;
    private LocalDate birthDate;
    private ShippingAddress shippingAddress;
    private PaymentMethod paymentMethod;

    public ClientResponseDTO(String username, String firstName, String lastName, String email, String country, String phoneNumber, LocalDate birthDate, ShippingAddress shippingAddress, PaymentMethod paymentMethod){
        this.username = username;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.country = country;
        this.phoneNumber=phoneNumber;
        this.birthDate=birthDate;
        this.shippingAddress=shippingAddress;
        this.paymentMethod=paymentMethod;
    }

    public ClientResponseDTO(String message){
        this.message = message;
    }

}

