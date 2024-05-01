package edu.escuelaing.arcn.microservice.infrastructure.inbound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import edu.escuelaing.arcn.microservice.application.ClientService;
import edu.escuelaing.arcn.microservice.domain.exceptions.PaymentMethodException;
import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.domain.model.PaymentMethod;
import edu.escuelaing.arcn.microservice.domain.model.ShippingAddress;
import edu.escuelaing.arcn.microservice.dto.ClientRequestDTO;
import edu.escuelaing.arcn.microservice.dto.ClientResponseDTO;
import edu.escuelaing.arcn.microservice.mapper.ClientMapper;

@CrossOrigin(origins = "*")
public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterClient() throws PaymentMethodException {

        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        ClientRequestDTO clientRequestDTO = new ClientRequestDTO("john_doe_arcn", "John", "Doe", "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        ClientResponseDTO expectedResponse = new ClientResponseDTO("john_doe_arcn", "John", "Doe", "john@example.com",
                "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientService.registerClient(any(Client.class))).thenReturn(expectedResponse);

        ResponseEntity<ClientResponseDTO> response = clientController.registerClient(clientRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testLogin() {
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        ClientRequestDTO clientRequestDTO = new ClientRequestDTO("john_doe_arcn", "John", "Doe", "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);
        String expectedToken = "mockedToken";

        when(clientService.login(clientRequestDTO.getEmail(), clientRequestDTO.getPassword()))
                .thenReturn(expectedToken);

        ResponseEntity<String> response = clientController.login(clientRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToken, response.getBody());
    }

    @Test
    public void testUpdateClient() {
        String clientUsername = "john_doe_arcn";

        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        ClientRequestDTO clientRequestDTO = new ClientRequestDTO("john_doe_arcn", "John", "Doe", "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        ClientResponseDTO expectedResponse = new ClientResponseDTO("john_doe_arcn", "John", "Doe", "john@example.com",
                "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientService.updateClient(any(Client.class))).thenReturn(expectedResponse);

        ResponseEntity<ClientResponseDTO> response = clientController.updateClient(clientUsername, clientRequestDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testUpdateClientShippingAddress() {
        // Create your test data and expectations
        String clientUsername = "john_doe_arcn";

        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        Client updatedClient = new Client("john_doe_arcn", "John", "Doe", "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientService.updateShippingAddress(anyString(), any(ShippingAddress.class))).thenReturn(updatedClient);

        ClientResponseDTO expectedClientResponseDTO = ClientMapper.toResponseDTO(updatedClient);

        ResponseEntity<ClientResponseDTO> response = clientController.updateClientShippingAddress(clientUsername, shippingAddress);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedClientResponseDTO, response.getBody());
    }

    @Test
    public void testUpdateClientPaymentMethod() throws PaymentMethodException {
        String clientId = "mockedClientId";
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148", "111111",
                "bogota");
        PaymentMethod newPaymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                "John Doe", "123");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client updatedClient = new Client("john_doe_arcn", "John", "Doe", "john@example.com",
        "password", "colombia", "3132105755", birthDate, shippingAddress, newPaymentMethod);

        when(clientService.updatePaymentMethod(anyString(), any(PaymentMethod.class))).thenReturn(updatedClient);

        ClientResponseDTO expectedClientResponseDTO = ClientMapper.toResponseDTO(updatedClient);

        ResponseEntity<ClientResponseDTO> response = clientController.updateClientPaymentMethod(clientId, newPaymentMethod);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedClientResponseDTO, response.getBody());
    }
}
