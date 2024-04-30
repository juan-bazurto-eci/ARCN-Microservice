package edu.escuelaing.arcn.microservice.infrastructure.inbound;

import edu.escuelaing.arcn.microservice.application.ClientService;
import edu.escuelaing.arcn.microservice.domain.exceptions.PaymentMethodException;
import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.model.ShippingAddress;
import edu.escuelaing.arcn.microservice.dto.ClientRequestDTO;
import edu.escuelaing.arcn.microservice.dto.ClientResponseDTO;
import edu.escuelaing.arcn.microservice.mapper.ClientMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> registerClient(@RequestBody ClientRequestDTO clientRequestDTO) throws PaymentMethodException {
        Client client = ClientMapper.toEntity(clientRequestDTO);
        ClientResponseDTO createdClient = clientService.registerClient(client);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody ClientRequestDTO clientRequestDTO) {
        String token = clientService.login(clientRequestDTO.getEmail(), clientRequestDTO.getPassword());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PutMapping("/{clientUsername}")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable String clientUsername, @RequestBody ClientRequestDTO clientRequestDTO) {
        Client client = ClientMapper.toEntity(clientRequestDTO);
        ClientResponseDTO updatedClient = clientService.updateClient(client);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    @PutMapping("/{clientUsername}/address")
    public ResponseEntity<Client> updateClientShippingAddress(@PathVariable String clientUsername, @RequestBody ShippingAddress shippingAddress) {
        Client updatedClient = clientService.updateShippingAddress(clientUsername, shippingAddress);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    @PutMapping("/{clientId}/payment-method")
    public ResponseEntity<Client> updateClientPaymentMethod(@PathVariable String clientId, @RequestBody PaymentMethod newPaymentMethod) throws PaymentMethodException {
        Client updatedClient = clientService.updatePaymentMethod(clientId, newPaymentMethod);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

}
