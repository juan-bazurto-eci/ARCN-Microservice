package edu.escuelaing.arcn.microservice.application;

import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createClientTest() {
        // Arrange
        PaymentMethod paymentMethod = new PaymentMethod("1234567890123456", null, "John Doe", "123");
        Client client = new Client("John Doe", "john@example.com", "Address", paymentMethod);
        when(clientRepository.save(client)).thenReturn(client);

        // Act
        Client createdClient = clientService.createClient("John Doe", "john@example.com", "Address", paymentMethod);

        // Assert
        assertEquals(client, createdClient);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void getClientByIdTest() {
        // Arrange
        String clientId = "123";
        Client client = new Client("John Doe", "john@example.com", "Address", null);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Act
        Client retrievedClient = clientService.getClientById(clientId);

        // Assert
        assertEquals(client, retrievedClient);
    }

    @Test
    void updateAddressTest() {
        // Arrange
        String clientId = "123";
        String newAddress = "New Address";
        Client client = new Client("John Doe", "john@example.com", "Address", null);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        // Act
        Client updatedClient = clientService.updateAddress(clientId, newAddress);

        // Assert
        assertEquals(newAddress, updatedClient.getAddress());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void updatePaymentMethodTest() {
        // Arrange
        String clientId = "123";
        PaymentMethod newPaymentMethod = new PaymentMethod("1234567890123456", null, "John Doe", "123");
        Client client = new Client("John Doe", "john@example.com", "Address", null);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        // Act
        Client updatedClient = clientService.updatePaymentMethod(clientId, newPaymentMethod);

        // Assert
        assertEquals(newPaymentMethod, updatedClient.getPaymentDetails());
        verify(clientRepository, times(1)).save(client);
    }
}
