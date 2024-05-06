package edu.escuelaing.arcn.microservice.domain.model;

import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
public class PaymentMethod {
    @Id
    private String id;
    private String cardNumber;
    private String expirationDate;
    private String cardHolderName;
    private String cvv;

    public PaymentMethod(String cardNumber, String expirationDate, String cardHolderName, String cvv){
        this.cardNumber=cardNumber;
        this.expirationDate=expirationDate;
        this.cardHolderName= cardHolderName;
        this.cvv=cvv;
    }
}
