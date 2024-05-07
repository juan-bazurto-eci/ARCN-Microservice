package edu.escuelaing.arcn.microservice.domain.model;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("clients")
public class Client {
    @Id
    private String _id;
    @Indexed(unique = true)
    private String username;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    private String passwordHash;
    private String country;
    private String phoneNumber;
    private LocalDate birthDate;
    private ShippingAddress shippingAddress;
    private PaymentMethod paymentMethod;

    public Client(String username, String firstName, String lastName, String email, String passwordHash, String country, String phoneNumber, LocalDate birthDate, ShippingAddress shippingAddress, PaymentMethod paymentMethod){
        this.username = username;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.passwordHash = passwordHash;
        this.country = country;
        this.phoneNumber=phoneNumber;
        this.birthDate=birthDate;
        this.shippingAddress=shippingAddress;
        this.paymentMethod=paymentMethod;
    }

}
