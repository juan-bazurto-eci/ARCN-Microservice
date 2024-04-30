package edu.escuelaing.arcn.microservice.domain.exceptions;

public class ShippingAddressException extends RuntimeException{
    
    public static final String MISSING_SHIPPING_ADDRESS = "the shipping address is missing";

    public ShippingAddressException(String message) {
        super(message);
    }
}
