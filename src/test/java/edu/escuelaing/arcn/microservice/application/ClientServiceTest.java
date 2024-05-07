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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
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

        PaymentMethod paymentMethod = new PaymentMethod("5429264884032453",
                "11/32",
                "John Doe", "123");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        ClientResponseDTO expectedClient = new ClientResponseDTO("a", "john_doe_arcn", "John", "Doe",
                "john@example.com",
                "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia",
                "3132105755", birthDate, shippingAddress, paymentMethod);
        client.set_id("a");
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientResponseDTO createdClient = clientService.registerClient(client);

        assertEquals(expectedClient, createdClient);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void Should_ThrowException_IfUsernameAlreadyExist() {
        PaymentMethod paymentMethod = new PaymentMethod("5429264884032453",
                "11/32",
                "John Doe", "123");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia",
                "3132105755", birthDate, shippingAddress, paymentMethod);
        when(clientRepository.save(any(Client.class)))
                .thenThrow(new ClientServiceException(ClientServiceException.CLIENT_ALREADY_EXISTS));

        assertThrows(ClientServiceException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test
    void Should_ThrowException_IfEmailIsAlreadyTaken() {
        PaymentMethod paymentMethod = new PaymentMethod("5429264884032453",
                "11/32",
                "John Doe", "123");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        when(clientRepository.save(any(Client.class)))
                .thenThrow(new ClientServiceException(ClientServiceException.EMAIL_ALREADY_TAKEN));
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia",
                "3132105755", birthDate, shippingAddress, paymentMethod);

        assertThrows(ClientServiceException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test
    void Should_ThrowException_IfPaymentMethodIsWrong() {
        PaymentMethod paymentMethod = new PaymentMethod("542926488032453",
                "11/32",
                "John Doe", "13");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia",
                "3132105755", birthDate, shippingAddress, paymentMethod);

        assertThrows(PaymentMethodException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test
    void Should_ThrowException_IfExpirationDateIsWrong() throws ParseException {
        PaymentMethod paymentMethod = new PaymentMethod("5429264884032453", "11/22", "John Doe", "123");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia",
                "3132105755", birthDate, shippingAddress, paymentMethod);

        assertThrows(PaymentMethodException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test
    void Should_ThrowException_IfThereAreBlankFields() throws ParseException {
        PaymentMethod paymentMethod = new PaymentMethod("5429264884032453",
                "11/32",
                "John Doe", "123");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client client = new Client("", "John", "Doe", "john@example.com", "password", "colombia", "3132105755",
                birthDate, shippingAddress, paymentMethod);

        assertThrows(ClientServiceException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test
    void Should_ThrowException_IfShippingAddressIsNull() throws ParseException {
        PaymentMethod paymentMethod = new PaymentMethod("5429264884032453",
                "11/32",
                "John Doe", "123");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia",
                "3132105755", birthDate, null, paymentMethod);

        assertThrows(ShippingAddressException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test
    void Should_ThrowException_IfPaymentMethodIsNull() throws ParseException {
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia",
                "3132105755", birthDate, shippingAddress, null);

        assertThrows(PaymentMethodException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test
    void Should_GetCorrectToken() {
        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im1pZ3VlbHMwMDciLCJlbWFpbCI6Im1pZ3VlbEBleGFtcGxlLmNvbSJ9.W-9Vw4Ef04WPEQU_hRMBGO5FqYXjWBeclxM_0-GkYPs";
        assertEquals(expectedToken, clientService.getJwtToken("miguels007", "miguel@example.com"));
    }

    @Test
    public void testLogin() {
        PaymentMethod paymentMethod = new PaymentMethod("5429264884032453",
                "11/32",
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        AuthorizationResponse expectedResponse = new AuthorizationResponse(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im1pZ3VlbHMwMDciLCJlbWFpbCI6Im1pZ3VlbEBleGFtcGxlLmNvbSJ9.W-9Vw4Ef04WPEQU_hRMBGO5FqYXjWBeclxM_0-GkYPs",
                new ClientResponseDTO("a","miguels007", "John", "Doe", "miguel@example.com", "colombia",
                        "3132105755", birthDate, shippingAddress, paymentMethod));

        Client expectedClient = new Client("miguels007", "John", "Doe", "miguel@example.com",
                "$2a$15$zcCXfD7wDHB6WcHOJzmYteEY1VjkOCb8TX30W.i0KNg2dU0EAc0pe", "colombia",
                "3132105755", birthDate, shippingAddress, paymentMethod);
        expectedClient.set_id("a");

        Client client = new Client("miguels007", "John", "Doe", "miguel@example.com", "password", "colombia",
                "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientRepository.findByEmail("miguel@example.com")).thenReturn(Optional.of(expectedClient));

        assertEquals(expectedResponse, clientService.login(client.getEmail(), client.getPasswordHash()));
    }

    @Test
    void Should_ThrowException_IfCredentialsAreIncorrect() {

        PaymentMethod paymentMethod = new PaymentMethod("5429264884032453",
                "11/32",
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        Client expectedClient = new Client("miguels007", "John", "Doe", "miguel@example.com",
                "$2a$15$zcCXfD7wDHB6WcHOJzmYteEY1VjkOCb8TX30W.i0KNg2dU0EAc0pe", "colombia",
                "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientRepository.findByEmail("miguel@example.com")).thenReturn(Optional.of(expectedClient));

        assertThrows(ClientServiceException.class, () -> {
            clientService.login("miguel@example.com", "passwo");
        });
    }

    @Test
    void Should_UpdatePaymentMethod() {
        PaymentMethod paymentMethod = new PaymentMethod("5429264884032453",
                "11/32",
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        Client expectedClient = new Client("john_doe_arcn", "John", "Doe", "miguel@example.com",
                BCrypt.hashpw("password", BCrypt.gensalt(15)), "colombia",
                "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientRepository.findByUsername(expectedClient.getUsername()))
                .thenReturn(Optional.of(expectedClient));
        when(clientRepository.save(any(Client.class))).thenReturn(expectedClient);

        Client client = clientService.updatePaymentMethod(expectedClient.getUsername(), paymentMethod);

        assertEquals(expectedClient, client);
    }

    
    @Test
    void Should_ReturnFalse_IfExpirationDateIsWrong() {
            assertFalse(clientService.isValidExpirationDate("07/23"));
            assertFalse(clientService.isValidExpirationDate("0723"));
            assertFalse(clientService.isValidExpirationDate("07263"));
            assertFalse(clientService.isValidExpirationDate("abcdd"));
            assertFalse(clientService.isValidExpirationDate("0/123"));
            assertFalse(clientService.isValidExpirationDate("ab/cd"));
            assertFalse(clientService.isValidExpirationDate("01/cd"));
            assertFalse(clientService.isValidExpirationDate("ab/26"));
        }
        
        
    @Test
    void Should_ReturnTrue_IfExpirationDateIsCorrect() {
        assertTrue(clientService.isValidExpirationDate("09/26"));
        assertTrue(clientService.isValidExpirationDate("07/24"));
    }

}
