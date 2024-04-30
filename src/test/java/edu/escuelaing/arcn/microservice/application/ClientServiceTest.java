package edu.escuelaing.arcn.microservice.application;

import edu.escuelaing.arcn.microservice.domain.exceptions.ClientServiceException;
import edu.escuelaing.arcn.microservice.domain.exceptions.PaymentMethodException;
import edu.escuelaing.arcn.microservice.domain.exceptions.ShippingAddressException;
import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.model.ShippingAddress;
import edu.escuelaing.arcn.microservice.domain.repository.ClientRepository;
import edu.escuelaing.arcn.microservice.dto.ClientRequestDTO;
import edu.escuelaing.arcn.microservice.dto.ClientResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        ClientResponseDTO expectedClient = new ClientResponseDTO("john_doe_arcn", "John", "Doe", "john@example.com", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientResponseDTO createdClient = clientService.registerClient(client);

        assertEquals(expectedClient, createdClient);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void Should_ThrowException_IfUsernameAlreadyExist() {
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431", Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), "John Doe", "123");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111", "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);
        when(clientRepository.save(any(Client.class))).thenThrow(new ClientServiceException(ClientServiceException.CLIENT_ALREADY_EXISTS));

        assertThrows(ClientServiceException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test
    void Should_ThrowException_IfEmailIsAlreadyTaken() {
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431", Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), "John Doe", "123");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111", "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        when(clientRepository.save(any(Client.class))).thenThrow(new ClientServiceException(ClientServiceException.EMAIL_ALREADY_TAKEN));
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        assertThrows(ClientServiceException.class, () -> {
            clientService.registerClient(client);
        });
    }
    

    @Test
    void Should_ThrowException_IfPaymentMethodIsWrong() {
        PaymentMethod paymentMethod = new PaymentMethod("371449398431", Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), "John Doe", "13");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111", "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        assertThrows(PaymentMethodException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test
    void Should_ThrowException_IfExpirationDateIsWrong() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-27");
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431", date, "John Doe", "13");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111", "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        assertThrows(PaymentMethodException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test
    void Should_ThrowException_IfThereAreBlankFields() throws ParseException {
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431", Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), "John Doe", "123");
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111", "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client client = new Client("", "John", "Doe", "john@example.com", "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        assertThrows(ClientServiceException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test
    void Should_ThrowException_IfShippingAddressIsNull() throws ParseException {
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431", Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), "John Doe", "123");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia", "3132105755", birthDate,null, paymentMethod);

        assertThrows(ShippingAddressException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test
    void Should_ThrowException_IfPaymentMethodIsNull() throws ParseException {
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111", "bogota");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client client = new Client("john_doe_arcn", "John", "Doe", "john@example.com", "password", "colombia", "3132105755", birthDate, shippingAddress, null);

        assertThrows(PaymentMethodException.class, () -> {
            clientService.registerClient(client);
        });
    }

    @Test 
    void Should_GetCorrectToken() {
        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im1pZ3VlbHMwMDciLCJlbWFpbCI6Im1pZ3VlbEBleGFtcGxlLmNvbSJ9.W-9Vw4Ef04WPEQU_hRMBGO5FqYXjWBeclxM_0-GkYPs";
        assertEquals(expectedToken, clientService.getJwtToken("miguels007", "miguel@example.com"));
    }

}
