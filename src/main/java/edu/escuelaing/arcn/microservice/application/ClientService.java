package edu.escuelaing.arcn.microservice.application;

import edu.escuelaing.arcn.microservice.domain.exceptions.ClientServiceException;
import edu.escuelaing.arcn.microservice.domain.exceptions.PaymentMethodException;
import edu.escuelaing.arcn.microservice.domain.exceptions.ShippingAddressException;
import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.model.ShippingAddress;
import edu.escuelaing.arcn.microservice.domain.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client registerClient(String username, String firstName, String lastName, String email, String passwordHash,
            String country, String phoneNumber, LocalDate birthDate, ShippingAddress shippingAddress,
            PaymentMethod paymentMethod) {

        if (!isValidCardNumber(paymentMethod.getCardNumber())) {
            throw new PaymentMethodException(PaymentMethodException.CARD_NUMBER_INVALID);
        }

        if (!isValidExpirationDate(paymentMethod.getExpirationDate())) {
            throw new PaymentMethodException(PaymentMethodException.EXPIRATION_DATE_INVALID);
        }

        if (username == "" || firstName == "" || lastName == "" || email == "" || passwordHash == "" || country == ""
                || phoneNumber == "") {
            throw new ClientServiceException(ClientServiceException.BLANK_FIELDS);
        }

        if (shippingAddress == null) {
            throw new ShippingAddressException(ShippingAddressException.MISSING_SHIPPING_ADDRESS);
        }

        if (checkIfUserExists(username)) {
            throw new ClientServiceException(ClientServiceException.CLIENT_ALREADY_EXISTS);
        }

        if (checkIfEmailIsTaken(email)) {
            throw new ClientServiceException(ClientServiceException.EMAIL_ALREADY_TAKEN);
        }

        passwordHash = BCrypt.hashpw(passwordHash, BCrypt.gensalt(15));

        Client client = new Client(username, firstName, lastName, email, passwordHash, country, phoneNumber, birthDate,
                shippingAddress, paymentMethod);
        return clientRepository.save(client);
    }

    public Client updateClient(Client client) {

        if (!isValidCardNumber(client.getPaymentMethod().getCardNumber())) {
            throw new PaymentMethodException(PaymentMethodException.CARD_NUMBER_INVALID);
        }

        if (!isValidExpirationDate(client.getPaymentMethod().getExpirationDate())) {
            throw new PaymentMethodException(PaymentMethodException.EXPIRATION_DATE_INVALID);
        }

        if (client.getUsername() == "" || client.getFirstName() == "" || client.getLastName() == "" || client.getEmail() == "" || client.getPasswordHash() == "" || client.getCountry() == ""
                || client.getPhoneNumber() == "") {
            throw new ClientServiceException(ClientServiceException.BLANK_FIELDS);
        }

        if (client.getShippingAddress() == null) {
            throw new ShippingAddressException(ShippingAddressException.MISSING_SHIPPING_ADDRESS);
        }

        if (checkIfUserExists(client.getUsername())) {
            throw new ClientServiceException(ClientServiceException.CLIENT_ALREADY_EXISTS);
        }

        if (checkIfEmailIsTaken(client.getEmail())) {
            throw new ClientServiceException(ClientServiceException.EMAIL_ALREADY_TAKEN);
        }

        var passwordHash = BCrypt.hashpw(client.getPasswordHash(), BCrypt.gensalt(15));

        client.setPasswordHash(passwordHash);

        return clientRepository.save(client);
    }

    public String login(String email, String password) {
        Client client = getClientByEmail(email);
        if (BCrypt.checkpw(password, client.getPasswordHash())) {
            return getJwtToken(client.getUsername(), client.getEmail());
        } else {
            throw new ClientServiceException(ClientServiceException.INVALID_CREDENTIALS);
        }
    }

    public Client updateAddress(String username, ShippingAddress shippingAddress) {
        Client client = getClientByUsername(username);
        client.setShippingAddress(shippingAddress);
        return clientRepository.save(client);
    }

    public Client updatePaymentMethod(String username, PaymentMethod paymentMethod) {
        Client client = getClientByUsername(username);
        if (!isValidCardNumber(paymentMethod.getCardNumber())) {
            throw new PaymentMethodException(PaymentMethodException.CARD_NUMBER_INVALID);
        }

        if (!isValidExpirationDate(paymentMethod.getExpirationDate())) {
            throw new PaymentMethodException(PaymentMethodException.EXPIRATION_DATE_INVALID);
        }

        client.setPaymentMethod(paymentMethod);
        return clientRepository.save(client);
    }

    private boolean checkIfEmailIsTaken(String email) {
        Optional<Client> client = clientRepository.findByEmail(email);
        return client.isPresent();
    }

    public Client getClientById(String clientId) {
        return clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public Client getClientByUsername(String username) {
        return clientRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Client not found"));
    }

    public boolean checkIfUserExists(String username) {
        Optional<Client> client = clientRepository.findByUsername(username);
        return client.isPresent();
    }

    public String getJwtToken(String username, String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("hello");
            String token = JWT.create()
            .withClaim("username", username)
            .withClaim("email", email)
            .sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            System.err.println("JWT was not created");
            throw e;
        }
    }

    private boolean isValidCardNumber(String cardNumber) {
        int sum = 0;
        boolean isSecondDigit = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (isSecondDigit) {
                digit = digit * 2;
            }
            sum += digit % 10 + digit / 10;
            isSecondDigit = !isSecondDigit;
        }
        return (sum % 10 == 0) && cardNumber.length() >= 13; // Adjust minimum length as needed
    }

    private boolean isValidExpirationDate(Date expirationDate) {
        Date currentDate = new Date();
        return expirationDate.after(currentDate);
    }

}
