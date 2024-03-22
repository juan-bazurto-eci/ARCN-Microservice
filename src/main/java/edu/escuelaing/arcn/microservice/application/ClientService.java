package edu.escuelaing.arcn.microservice.application;

import edu.escuelaing.arcn.microservice.domain.exceptions.PaymentMethodException;
import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public  ClientService(ClientRepository clientRepository){
        this.clientRepository=clientRepository;
    }

    public Client createClient(String name, String email, String address, PaymentMethod paymentMethod) throws PaymentMethodException{
        if (!isValidCardNumber(paymentMethod.getCardNumber())) {
            throw new PaymentMethodException("card number is not valid");
        }

        if (!isValidExpirationDate(paymentMethod.getExpirationDate())) {
            throw new PaymentMethodException("expiration date is not valid");
        }

        Client client = new Client(name, email, address, paymentMethod);
        return clientRepository.save(client);
    }

    public Client getClientById(String clientId){
        return clientRepository.findById(clientId).orElseThrow(()-> new RuntimeException("Client not found"));
    }

    public Client updateAddress(String clientId, String newAddress){
        Client client = getClientById(clientId);
        client.setAddress(newAddress);
        return clientRepository.save(client);
    }

    public Client updatePaymentMethod(String clientId, PaymentMethod paymentMethod) throws PaymentMethodException{
        Client client = getClientById(clientId); 
        if (!isValidCardNumber(paymentMethod.getCardNumber())) {
            throw new PaymentMethodException("card number is not valid");
        }

        if (!isValidExpirationDate(paymentMethod.getExpirationDate())) {
            throw new PaymentMethodException("expiration date is not valid");
        }

        client.setPaymentDetails(paymentMethod);
        return clientRepository.save(client);
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
