package edu.escuelaing.arcn.microservice.domain.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getHolderName() {
        return cardHolderName;
    }

    public void setHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCcv() {
        return cvv;
    }

    public void setCcv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", expirationDate=" + expirationDate +
                ", holderName='" + cardHolderName + '\'' +
                ", ccv='" + cvv + '\'' +
                '}';
    }
}
