package edu.escuelaing.arcn.microservice.domain.exceptions;

public class ClientServiceException extends RuntimeException{

    public static final String BLANK_FIELDS = "there are fields left blank";
    public static final String CLIENT_ALREADY_EXISTS = "client already exists";
    public static final String EMAIL_ALREADY_TAKEN = "email is already taken";
    public static final String INVALID_CREDENTIALS = "invalid credentials";

    public ClientServiceException(String message) {
        super(message);
    }
}
