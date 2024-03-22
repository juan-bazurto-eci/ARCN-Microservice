package edu.escuelaing.arcn.microservice.domain.model;

import java.util.Date;

public class PaymentMethod {
    private Long id;
    private String cardNumber;
    private Date expirationDate;
    private String holderName;
    private String ccv;

    public PaymentMethod(String cardNumber, Date expirationDate, String holderName, String ccv){
        this.cardNumber=cardNumber;
        this.expirationDate=expirationDate;
        this.holderName= holderName;
        this.ccv=ccv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", expirationDate=" + expirationDate +
                ", holderName='" + holderName + '\'' +
                ", ccv='" + ccv + '\'' +
                '}';
    }
}
