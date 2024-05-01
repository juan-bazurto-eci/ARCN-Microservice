package edu.escuelaing.arcn.microservice.application;

import edu.escuelaing.arcn.microservice.domain.exceptions.ClientServiceException;
import edu.escuelaing.arcn.microservice.domain.exceptions.PaymentMethodException;
import edu.escuelaing.arcn.microservice.domain.exceptions.ShippingAddressException;
import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.model.ShippingAddress;
import edu.escuelaing.arcn.microservice.domain.repository.ClientRepository;
import edu.escuelaing.arcn.microservice.dto.ClientResponseDTO;
import edu.escuelaing.arcn.microservice.mapper.ClientMapper;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientResponseDTO registerClient(Client client) {

        client = clientInformationIsValid(client.getUsername(), client.getFirstName(), client.getLastName(), client.getEmail(), client.getCountry(), client.getCountry(), client.getPhoneNumber(), client.getBirthDate(), client.getShippingAddress(), client.getPaymentMethod());

        String password = BCrypt.hashpw(client.getPasswordHash(), BCrypt.gensalt(15));

        client.setPasswordHash(password);

        return ClientMapper.toResponseDTO(clientRepository.save(client));
    }

    public ClientResponseDTO updateClient(Client client) {

        Client updatedClient = clientInformationIsValid(client.getUsername(), client.getFirstName(),
                client.getLastName(), client.getEmail(), client.getPasswordHash(), client.getCountry(),
                client.getPhoneNumber(), client.getBirthDate(), client.getShippingAddress(), client.getPaymentMethod());

        return ClientMapper.toResponseDTO(clientRepository.save(updatedClient));
    }

    public String login(String email, String password) {
        Client client = getClientByEmail(email);
        System.out.println("CLIENT: " + client);
        if (BCrypt.checkpw(password, client.getPasswordHash())) {
            return getJwtToken(client.getUsername(), client.getEmail());
        } else {
            throw new ClientServiceException(ClientServiceException.INVALID_CREDENTIALS);
        }
    }

    public Client updateShippingAddress(String username, ShippingAddress shippingAddress) {
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
        Algorithm algorithm = Algorithm.HMAC256(System.getenv("SECRET_KEY"));
        String token = JWT.create()
                .withClaim("username", username)
                .withClaim("email", email)
                .sign(algorithm);
        return token;
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

    private Client clientInformationIsValid(String username, String firstName, String lastName, String email,
            String passwordHash,
            String country, String phoneNumber, LocalDate birthDate, ShippingAddress shippingAddress,
            PaymentMethod paymentMethod) {

        if (paymentMethod == null) {
            throw new PaymentMethodException(PaymentMethodException.MISSING_PAYMENT_METHOD);
        }

        if (!isValidCardNumber(paymentMethod.getCardNumber())) {
            throw new PaymentMethodException(PaymentMethodException.CARD_NUMBER_INVALID);
        }

        if (!isValidExpirationDate(paymentMethod.getExpirationDate())) {
            throw new PaymentMethodException(PaymentMethodException.EXPIRATION_DATE_INVALID);
        }

        if (username.equals("") || firstName.equals("") || lastName.equals("") || email.equals("")
                || passwordHash.equals("") || country.equals("") || phoneNumber.equals("")) {
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

        return new Client(username, firstName, lastName, email, passwordHash, country, phoneNumber, birthDate,
                shippingAddress, paymentMethod);
    }
}
