package edu.escuelaing.arcn.microservice.domain.repository;
import edu.escuelaing.arcn.microservice.domain.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface ClientRepository extends MongoRepository<Client, String>{
}
