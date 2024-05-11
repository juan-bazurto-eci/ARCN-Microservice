package edu.escuelaing.arcn.microservice.domain.exceptions;

public class PaymentMethodException extends RuntimeException{

    public static final String EXPIRATION_DATE_INVALID = "expiration date is invalid";
    public static final String CARD_NUMBER_INVALID = "card number is invalid";
    public static final String MISSING_PAYMENT_METHOD = "the payment information is missing";
    public static final String PAYMENT_INFORMATION_INVALID = "the payment information is invalid";
    public static final String CVV_NUMER_IS_INVALID = "cvv is invalid";

    public PaymentMethodException(String message) {
        super(message);
    }

}
