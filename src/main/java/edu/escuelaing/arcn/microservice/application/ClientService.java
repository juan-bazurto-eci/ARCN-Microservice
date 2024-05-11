package edu.escuelaing.arcn.microservice.application;

import edu.escuelaing.arcn.microservice.domain.exceptions.ClientServiceException;
import edu.escuelaing.arcn.microservice.domain.exceptions.PaymentMethodException;
import edu.escuelaing.arcn.microservice.domain.exceptions.ShippingAddressException;
import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.model.ShippingAddress;
import edu.escuelaing.arcn.microservice.domain.repository.ClientRepository;
import edu.escuelaing.arcn.microservice.dto.AuthorizationResponse;
import edu.escuelaing.arcn.microservice.dto.ClientResponseDTO;
import edu.escuelaing.arcn.microservice.mapper.ClientMapper;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientResponseDTO registerClient(Client client) {

        client = clientInformationIsValid(client.getUsername(), client.getFirstName(), client.getLastName(),
                client.getEmail(), client.getPasswordHash(), client.getCountry(), client.getPhoneNumber(),
                client.getBirthDate(), client.getShippingAddress(), client.getPaymentMethod());

        String password = BCrypt.hashpw(client.getPasswordHash(), BCrypt.gensalt(15));

        client.setPasswordHash(password);

        try {
            client.getPaymentMethod().setCardNumber(encryptCardNumber(client.getPaymentMethod().getCardNumber()));
            client.getPaymentMethod().setCvv(encrypt(client.getPaymentMethod().getCvv()));
            client.getPaymentMethod().setExpirationDate(encrypt(client.getPaymentMethod().getExpirationDate()));
        } catch (Exception e) {
            System.out.println("Failed here!");
            e.printStackTrace();
            throw new PaymentMethodException(PaymentMethodException.PAYMENT_INFORMATION_INVALID);
        }

        return ClientMapper.toResponseDTO(clientRepository.save(client));
    }

    public String encryptCardNumber(String cardNumber) throws Exception {
        String lastFourNumbers = cardNumber.substring(12, 16);
        String firstTwelveNumbers = cardNumber.substring(0, 12);

        String firstTwelveNumbersEncrypted = encrypt(firstTwelveNumbers);

        return firstTwelveNumbersEncrypted + lastFourNumbers;
    }

    public ClientResponseDTO updateClient(String clientUsername, Client client) {

        if (!isPaymentMethodValid(client.getPaymentMethod())) {
            throw new PaymentMethodException(PaymentMethodException.PAYMENT_INFORMATION_INVALID);
        }

        Optional<Client> previousClientOpt = clientRepository.findByUsername(clientUsername);

        if (previousClientOpt.isEmpty()) {
            throw new ClientServiceException(ClientServiceException.CLIENT_DOES_NOT_EXIST);
        }

        Client previousClient = previousClientOpt.get();

        client.set_id(previousClient.get_id());
        client.setUsername(clientUsername);

        return ClientMapper.toResponseDTO(clientRepository.save(client));


    }

    public AuthorizationResponse login(String email, String password) {
        Client client = getClientByEmail(email);
        if (BCrypt.checkpw(password, client.getPasswordHash())) {
            String token = getJwtToken(client.getUsername(), client.getEmail());
            ClientResponseDTO clientResponseDTO = ClientMapper.toResponseDTO(client);
            return new AuthorizationResponse(token, clientResponseDTO);
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

        if (paymentMethod.getCvv().length() != 3) {
            throw new PaymentMethodException(PaymentMethodException.CVV_NUMER_IS_INVALID);
        }

        try {
            paymentMethod.setCardNumber(encryptCardNumber(paymentMethod.getCardNumber()));
            paymentMethod.setCvv(encrypt(encrypt(paymentMethod.getCvv())));
            paymentMethod.setExpirationDate(encrypt(paymentMethod.getExpirationDate()));
        } catch (Exception e) {
            throw new PaymentMethodException(PaymentMethodException.PAYMENT_INFORMATION_INVALID);
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

    public boolean isPaymentMethodValid(PaymentMethod paymentMethod) {

        if (!isValidCardNumber(paymentMethod.getCardNumber())) {
            return false;
        }

        if (!isValidExpirationDate(paymentMethod.getExpirationDate())) {
            return false;
        }

        if (paymentMethod.getCvv() == null || paymentMethod.getCvv().length() != 3) {
            return false;
        }

        return true;
    }

    private boolean isValidCardNumber(String cardNumber) {

        if (cardNumber == null) {
            return false;
        }

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

    public boolean isValidExpirationDate(String dateString) {

        if (dateString == null) {
            return false;
        }

        if (dateString.length() != 5 || !dateString.contains("/")) {
            return false;
        }

        if (!Character.toString(dateString.charAt(2)).equals("/")) {
            return false;
        }

        String[] expirationDate = dateString.split("/");
        LocalDate currentDate = LocalDate.now();

        try {
            int currentMonth = Integer.valueOf(String.format("%02d", currentDate.getMonthValue()));
            int currentYear = Integer.valueOf(String.format("%02d", currentDate.getYear()).substring(2));
            int expirationYear = Integer.valueOf(expirationDate[1]);
            int expirationMonth = Integer.valueOf(expirationDate[0]);
            if (currentYear < expirationYear) {
                return true;
            } else if (expirationYear == currentYear && expirationMonth > currentMonth) {
                return true;
            } else {
                return false;
            }
        } catch (DateTimeParseException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
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

    public String encrypt(String plaintext) throws Exception {
        String key = System.getenv("AES_KEY");
        Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String ciphertext) throws Exception {
        String key = System.getenv("AES_KEY");
        Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(decryptedBytes);
    }
}
