package edu.escuelaing.arcn.microservice.domain.model;

import org.springframework.data.annotation.Id;
import lombok.Data;
import java.util.Date;

@Data
public class PaymentMethod {
    @Id
    private String id;
    private String cardNumber;
    private Date expirationDate;
    private String cardHolderName;
    private String cvv;

    public PaymentMethod(String cardNumber, Date expirationDate, String cardHolderName, String cvv){
        this.cardNumber=cardNumber;
        this.expirationDate=expirationDate;
        this.cardHolderName= cardHolderName;
        this.cvv=cvv;
    }
}
