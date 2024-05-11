package edu.escuelaing.arcn.microservice.infrastructure.inbound;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class HealthCheckTest {

    @InjectMocks
    private HealthCheck healthCheck;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_Return_OkHTTPCode() {
        String response = healthCheck.check();
        assertEquals("Service Up", response);
    }
}
