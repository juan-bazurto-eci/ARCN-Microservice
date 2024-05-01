package edu.escuelaing.arcn.microservice.infrastructure.inbound;

import edu.escuelaing.arcn.microservice.application.ClientService;
import edu.escuelaing.arcn.microservice.domain.exceptions.ClientServiceException;
import edu.escuelaing.arcn.microservice.domain.exceptions.PaymentMethodException;
import edu.escuelaing.arcn.microservice.domain.exceptions.ShippingAddressException;
import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.model.ShippingAddress;
import edu.escuelaing.arcn.microservice.dto.ClientRequestDTO;
import edu.escuelaing.arcn.microservice.dto.ClientResponseDTO;
import edu.escuelaing.arcn.microservice.mapper.ClientMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> registerClient(@RequestBody ClientRequestDTO clientRequestDTO) {
        Client client = ClientMapper.toEntity(clientRequestDTO);
        try {
            ClientResponseDTO createdClient = clientService.registerClient(client);
            return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
        } catch (PaymentMethodException e) {
            return new ResponseEntity<>(new ClientResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ShippingAddressException e) {
            return new ResponseEntity<>(new ClientResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ClientServiceException e) {
            return new ResponseEntity<>(new ClientResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody ClientRequestDTO clientRequestDTO) {
        try {
            String token = clientService.login(clientRequestDTO.getEmail(), clientRequestDTO.getPassword());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (ClientServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{clientUsername}")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable String clientUsername,
            @RequestBody ClientRequestDTO clientRequestDTO) {
        Client client = ClientMapper.toEntity(clientRequestDTO);
        try {
            ClientResponseDTO updatedClient = clientService.updateClient(client);
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } catch (PaymentMethodException e) {
            return new ResponseEntity<>(new ClientResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ShippingAddressException e) {
            return new ResponseEntity<>(new ClientResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ClientServiceException e) {
            return new ResponseEntity<>(new ClientResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{clientUsername}/address")
    public ResponseEntity<ClientResponseDTO> updateClientShippingAddress(@PathVariable String clientUsername,
            @RequestBody ShippingAddress shippingAddress) {
        try {
            ClientResponseDTO updatedClient = ClientMapper
                    .toResponseDTO(clientService.updateShippingAddress(clientUsername, shippingAddress));
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ClientResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{clientId}/payment-method")
    public ResponseEntity<ClientResponseDTO> updateClientPaymentMethod(@PathVariable String clientId,
            @RequestBody PaymentMethod newPaymentMethod) throws PaymentMethodException {

        try {
            ClientResponseDTO updatedClient = ClientMapper
                    .toResponseDTO(clientService.updatePaymentMethod(clientId, newPaymentMethod));
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ClientResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
