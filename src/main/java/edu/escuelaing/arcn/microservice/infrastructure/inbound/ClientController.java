package edu.escuelaing.arcn.microservice.infrastructure.inbound;

import edu.escuelaing.arcn.microservice.application.ClientService;
import edu.escuelaing.arcn.microservice.domain.exceptions.PaymentMethodException;
import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) throws PaymentMethodException {
        Client createdClient = clientService.createClient(client.getName(), client.getEmail(), client.getAddress(), client.getPaymentDetails());
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    @PutMapping("/{clientId}/address")
    public ResponseEntity<Client> updateClientAddress(@PathVariable String clientId, @RequestBody String newAddress) {
        Client updatedClient = clientService.updateAddress(clientId, newAddress);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    @PutMapping("/{clientId}/payment-method")
    public ResponseEntity<Client> updateClientPaymentMethod(@PathVariable String clientId, @RequestBody PaymentMethod newPaymentMethod) throws PaymentMethodException {
        Client updatedClient = clientService.updatePaymentMethod(clientId, newPaymentMethod);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

}
