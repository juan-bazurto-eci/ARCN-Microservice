package edu.escuelaing.arcn.microservice.application;

import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public  ClientService(ClientRepository clientRepository){
        this.clientRepository=clientRepository;
    }

    public Client createClient(String name, String email, String address, PaymentMethod paymentMethod){
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

    public Client updatePaymentMethod(String clientId, PaymentMethod paymentMethod){
        Client client = getClientById(clientId);
        client.setPaymentDetails(paymentMethod);
        return clientRepository.save(client);
    }

}
