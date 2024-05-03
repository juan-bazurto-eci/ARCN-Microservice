package edu.escuelaing.arcn.microservice.dto;

import lombok.Data;

@Data
public class AuthorizationResponse {
    private String token;
    private ClientResponseDTO clientResponseDTO;

    public AuthorizationResponse(String token, ClientResponseDTO clientResponseDTO) {
        this.token = token;
        this.clientResponseDTO = clientResponseDTO;
    }

}
