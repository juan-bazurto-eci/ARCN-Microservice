package edu.escuelaing.arcn.microservice.mapper;

import edu.escuelaing.arcn.microservice.domain.model.Client;
import edu.escuelaing.arcn.microservice.dto.ClientRequestDTO;
import edu.escuelaing.arcn.microservice.dto.ClientResponseDTO;

public class ClientMapper {

    public static ClientResponseDTO toResponseDTO(Client client){
        return new ClientResponseDTO(
            client.getUsername(), 
            client.getFirstName(), 
            client.getLastName(), 
            client.getEmail(), 
            client.getCountry(), 
            client.getPhoneNumber(), 
            client.getBirthDate(), 
            client.getShippingAddress(), 
            client.getPaymentMethod()
            );
    }

    public static Client toEntity(ClientRequestDTO clientRequestDTO){
        return new Client(
            clientRequestDTO.getUsername(), 
            clientRequestDTO.getFirstName(), 
            clientRequestDTO.getLastName(), 
            clientRequestDTO.getEmail(), 
            clientRequestDTO.getPassword(),
            clientRequestDTO.getCountry(), 
            clientRequestDTO.getPhoneNumber(), 
            clientRequestDTO.getBirthDate(), 
            clientRequestDTO.getShippingAddress(), 
            clientRequestDTO.getPaymentMethod()
            );
    }
}
