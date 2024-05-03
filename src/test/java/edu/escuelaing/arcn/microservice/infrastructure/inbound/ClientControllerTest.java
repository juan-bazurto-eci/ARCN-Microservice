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
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault())
                        .toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        ClientRequestDTO clientRequestDTO = new ClientRequestDTO("john_doe_arcn", "John", "Doe",
                "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        ClientResponseDTO expectedResponse = new ClientResponseDTO("john_doe_arcn", "John", "Doe",
                "john@example.com",
                "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientService.registerClient(any(Client.class))).thenReturn(expectedResponse);

        ResponseEntity<ClientResponseDTO> response = clientController.registerClient(clientRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void Should_RespondWithBadRequestStatus_IfPaymentMethodIsInvalid_When_RegisteringClient() {
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault())
                        .toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        ClientRequestDTO clientRequestDTO = new ClientRequestDTO("john_doe_arcn", "John", "Doe",
                "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientService.registerClient(any(Client.class)))
                .thenThrow(new PaymentMethodException(PaymentMethodException.CARD_NUMBER_INVALID));

        ResponseEntity<ClientResponseDTO> response = clientController.registerClient(clientRequestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void Should_RespondWithBadRequestStatus_IfShippingAddressIsInvalid_When_RegisteringClient() {
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault())
                        .toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        ClientRequestDTO clientRequestDTO = new ClientRequestDTO("john_doe_arcn", "John", "Doe",
                "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientService.registerClient(any(Client.class)))
                .thenThrow(new ShippingAddressException(ShippingAddressException.MISSING_SHIPPING_ADDRESS));

        ResponseEntity<ClientResponseDTO> response = clientController.registerClient(clientRequestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void Should_RespondWithBadRequestStatus_IfUsernameIsTaken_When_RegisteringClient() {
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault())
                        .toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        ClientRequestDTO clientRequestDTO = new ClientRequestDTO("john_doe_arcn", "John", "Doe",
                "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientService.registerClient(any(Client.class)))
                .thenThrow(new ClientServiceException(ClientServiceException.CLIENT_ALREADY_EXISTS));

        ResponseEntity<ClientResponseDTO> response = clientController.registerClient(clientRequestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    @Test
    public void testLogin() {
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
        Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault())
        .toInstant()),
        "John Doe", "123");
        
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
        "111111",
        "bogota");
        
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO("john_doe_arcn", "John", "Doe",
        "john@example.com",
        "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);
        AuthorizationResponse expectedToken = new AuthorizationResponse("mockToken", new ClientResponseDTO(null));
        
        when(clientService.login(clientRequestDTO.getEmail(), clientRequestDTO.getPassword()))
        .thenReturn(expectedToken);
        
        ResponseEntity<AuthorizationResponse> response = clientController.login(clientRequestDTO);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToken, response.getBody());
    }
    
    @Test
    public void Should_RespondWithUnauthorizedStatus_IfCredentialsAreIncorrect_When_LoggingIn() {
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault())
                        .toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        ClientRequestDTO clientRequestDTO = new ClientRequestDTO("john_doe_arcn", "John", "Doe",
                "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientService.login(any(String.class),any(String.class))).thenThrow(new ClientServiceException(ClientServiceException.INVALID_CREDENTIALS));

        ResponseEntity<AuthorizationResponse> response = clientController.login(clientRequestDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void Should_RespondWithBadRequestStatus_IfPaymentMethodIsInvalid_When_UpdatingClient() {
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault())
                        .toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        ClientRequestDTO clientRequestDTO = new ClientRequestDTO("john_doe_arcn", "John", "Doe",
                "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientService.updatePaymentMethod(any(String.class), any(PaymentMethod.class))).thenThrow(new PaymentMethodException(PaymentMethodException.CARD_NUMBER_INVALID));

        ResponseEntity<ClientResponseDTO> response = clientController.updateClientPaymentMethod(clientRequestDTO.getUsername(), paymentMethod);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void Should_RespondWithBadRequestStatus_IfClientIsNotFound_When_UpdatingClientsShippingAddress() {
        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault())
                        .toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        ClientRequestDTO clientRequestDTO = new ClientRequestDTO("john_doe_arcn", "John", "Doe",
                "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientService.updatePaymentMethod(any(String.class), any(PaymentMethod.class))).thenThrow(new RuntimeException());

        ResponseEntity<ClientResponseDTO> response = clientController.updateClientShippingAddress(clientRequestDTO.getUsername(), shippingAddress);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testUpdateClient() {
        String clientUsername = "john_doe_arcn";

        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault())
                        .toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        ClientRequestDTO clientRequestDTO = new ClientRequestDTO("john_doe_arcn", "John", "Doe",
                "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        ClientResponseDTO expectedResponse = new ClientResponseDTO("john_doe_arcn", "John", "Doe",
                "john@example.com",
                "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientService.updateClient(any(Client.class))).thenReturn(expectedResponse);

        ResponseEntity<ClientResponseDTO> response = clientController.updateClient(clientUsername,
                clientRequestDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testUpdateClientShippingAddress() {
        // Create your test data and expectations
        String clientUsername = "john_doe_arcn";

        PaymentMethod paymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault())
                        .toInstant()),
                "John Doe", "123");

        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");

        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);

        Client updatedClient = new Client("john_doe_arcn", "John", "Doe", "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, paymentMethod);

        when(clientService.updateShippingAddress(anyString(), any(ShippingAddress.class)))
                .thenReturn(updatedClient);

        ClientResponseDTO expectedClientResponseDTO = ClientMapper.toResponseDTO(updatedClient);

        ResponseEntity<ClientResponseDTO> response = clientController
                .updateClientShippingAddress(clientUsername, shippingAddress);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedClientResponseDTO, response.getBody());
    }

    @Test
    public void testUpdateClientPaymentMethod() throws PaymentMethodException {
        String clientId = "mockedClientId";
        ShippingAddress shippingAddress = new ShippingAddress("John Doe", "3132105755", "cr 104 cll 148",
                "111111",
                "bogota");
        PaymentMethod newPaymentMethod = new PaymentMethod("371449635398431",
                Date.from(LocalDate.now().plusDays(120).atStartOfDay().atZone(ZoneId.systemDefault())
                        .toInstant()),
                "John Doe", "123");
        LocalDate birthDate = LocalDate.of(2003, Month.JULY, 8);
        Client updatedClient = new Client("john_doe_arcn", "John", "Doe", "john@example.com",
                "password", "colombia", "3132105755", birthDate, shippingAddress, newPaymentMethod);

        when(clientService.updatePaymentMethod(anyString(), any(PaymentMethod.class)))
                .thenReturn(updatedClient);

        ClientResponseDTO expectedClientResponseDTO = ClientMapper.toResponseDTO(updatedClient);

        ResponseEntity<ClientResponseDTO> response = clientController.updateClientPaymentMethod(clientId,
                newPaymentMethod);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedClientResponseDTO, response.getBody());
    }
}
