package edu.escuelaing.arcn.microservice.dto;

import java.time.LocalDate;
import lombok.Data;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.model.ShippingAddress;

@Data
public class ClientRequestDTO {
    private String _id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String country;
    private final String phoneNumber;
    private final LocalDate birthDate;
    private final ShippingAddress shippingAddress;
    private final PaymentMethod paymentMethod;

    public ClientRequestDTO(String username, String firstName, String lastName, String email, String password, String country, String phoneNumber, LocalDate birthDate, ShippingAddress shippingAddress, PaymentMethod paymentMethod){
        this.username = username;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.password = password;
        this.country = country;
        this.phoneNumber=phoneNumber;
        this.birthDate=birthDate;
        this.shippingAddress=shippingAddress;
        this.paymentMethod=paymentMethod;
    }

}

