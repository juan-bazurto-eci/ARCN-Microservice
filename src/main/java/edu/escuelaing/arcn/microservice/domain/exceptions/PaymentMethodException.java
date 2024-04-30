package edu.escuelaing.arcn.microservice.domain.exceptions;

public class PaymentMethodException extends RuntimeException{

    public static final String EXPIRATION_DATE_INVALID = "expiration date iS invalid";
    public static final String CARD_NUMBER_INVALID = "card number is invalid";

    public PaymentMethodException(String message) {
        super(message);
    }

}
