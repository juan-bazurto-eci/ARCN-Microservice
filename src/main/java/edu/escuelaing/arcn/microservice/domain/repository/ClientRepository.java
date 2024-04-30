package edu.escuelaing.arcn.microservice.domain.repository;

import edu.escuelaing.arcn.microservice.domain.model.Client;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findByEmail(String email);

    Optional<Client> findByUsername(String username);
}
