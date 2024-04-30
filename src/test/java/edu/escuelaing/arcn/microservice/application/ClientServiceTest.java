package edu.escuelaing.arcn.microservice.application;

import edu.escuelaing.arcn.microservice.domain.exceptions.PaymentMethodException;
import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.model.ShippingAddress;
import edu.escuelaing.arcn.microservice.domain.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    void Should_registerClient() throws PaymentMethodException {
        
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431", Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), "John Doe", "123");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111", "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        Client expectedClient = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "johnspasswordhash", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);
        when(clientRepository.save(any(Client.class))).thenReturn(expectedClient);

        Client createdClient = clientService.registerClient("john_doe_arcn", "John", "Doe", "john@example.com", "johnspasswordhash", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        assertEquals(expectedClient, createdClient);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void Should_ThrowException_IfMandatoryFieldsAreMissing() {
        PaymentMethod paymentMethod = new PaymentMethod("371449398431", Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), "John Doe", "13");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111", "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        assertThrows(PaymentMethodException.class, () -> {
            clientService.registerClient("john_doe_arcn", "John", "Doe", "john@example.com", "johnspasswordhash", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);
        });
    }



    /*
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
    */

    /* 
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
    */

    /*
    @Test
    void updatePaymentMethodTest() throws PaymentMethodException {
        // Arrange
        String clientId = "123";
        PaymentMethod newPaymentMethod = new PaymentMethod("371449635398431", Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), "John Doe", "123");
        Client client = new Client("John Doe", "john@example.com", "Address", null);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        // Act
        Client updatedClient = clientService.updatePaymentMethod(clientId, newPaymentMethod);

        // Assert
        assertEquals(newPaymentMethod, updatedClient.getPaymentDetails());
        verify(clientRepository, times(1)).save(client);
    }
    */
}
