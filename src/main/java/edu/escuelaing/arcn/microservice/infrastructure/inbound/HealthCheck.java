package edu.escuelaing.arcn.microservice.infrastructure.inbound;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/health")
@CrossOrigin(origins = {"http://localhost:3000", "https://juan-bazurto-eci.github.io"})
public class HealthCheck {

    @GetMapping("")
    public String check() {
        return "Service Up";
    }
    
}
