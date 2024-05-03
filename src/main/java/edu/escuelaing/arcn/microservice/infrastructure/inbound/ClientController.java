package edu.escuelaing.arcn.microservice.infrastructure.inbound;

import edu.escuelaing.arcn.microservice.application.ClientService;
import edu.escuelaing.arcn.microservice.domain.exceptions.ClientServiceException;
import edu.escuelaing.arcn.microservice.domain.exceptions.PaymentMethodException;
import edu.escuelaing.arcn.microservice.domain.exceptions.ShippingAddressException;
import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.model.ShippingAddress;
import edu.escuelaing.arcn.microservice.dto.AuthorizationResponse;
import edu.escuelaing.arcn.microservice.dto.ClientRequestDTO;
import edu.escuelaing.arcn.microservice.dto.ClientResponseDTO;
import edu.escuelaing.arcn.microservice.mapper.ClientMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @Operation(summary = "Register a client")
    public ResponseEntity<ClientResponseDTO> registerClient(@RequestBody ClientRequestDTO clientRequestDTO) {
        try {
            Client client = ClientMapper.toEntity(clientRequestDTO);
            ClientResponseDTO createdClient = clientService.registerClient(client);
            return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
        } catch (ClientServiceException e) {
            return new ResponseEntity<>(new ClientResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (PaymentMethodException e) {
            return new ResponseEntity<>(new ClientResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ShippingAddressException e) {
            return new ResponseEntity<>(new ClientResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Log In")
    @PostMapping("/login")
    public ResponseEntity<AuthorizationResponse> login(@RequestBody ClientRequestDTO clientRequestDTO) {
        try {
            AuthorizationResponse response = clientService.login(clientRequestDTO.getEmail(), clientRequestDTO.getPassword());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ClientServiceException e) {
            return new ResponseEntity<>(new AuthorizationResponse(null, null), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Update a client by username")
    @PutMapping("/{clientUsername}")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable String clientUsername,
            @RequestBody ClientRequestDTO clientRequestDTO) {
        Client client = ClientMapper.toEntity(clientRequestDTO);
        ClientResponseDTO updatedClient = clientService.updateClient(client);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    @Operation(summary = "Update a client's shipping address by username")
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

    @Operation(summary = "Update a client's payment method by username")
    @PutMapping("/{clientUsername}/payment-method")
    public ResponseEntity<ClientResponseDTO> updateClientPaymentMethod(@PathVariable String clientUsername,
            @RequestBody PaymentMethod newPaymentMethod) throws PaymentMethodException {

        try {
            ClientResponseDTO updatedClient = ClientMapper
                    .toResponseDTO(clientService.updatePaymentMethod(clientUsername, newPaymentMethod));
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ClientResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }   

}
