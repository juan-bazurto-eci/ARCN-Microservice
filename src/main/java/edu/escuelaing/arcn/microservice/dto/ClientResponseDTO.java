package edu.escuelaing.arcn.microservice.dto;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.model.ShippingAddress;
import lombok.Data;

@Data
public class ClientResponseDTO {

    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String country;
    private final String phoneNumber;
    private final LocalDate birthDate;
    private final ShippingAddress shippingAddress;
    private final PaymentMethod paymentMethod;

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

}

